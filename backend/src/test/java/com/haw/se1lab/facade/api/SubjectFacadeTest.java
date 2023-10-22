package com.haw.se1lab.facade.api;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.UserNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.*;
import com.haw.se1lab.dataaccess.api.repo.AcademicCourseRepository;
import com.haw.se1lab.dataaccess.api.repo.LevelSystemRepository;
import com.haw.se1lab.dataaccess.api.repo.SubjectRepository;
import com.haw.se1lab.dataaccess.api.repo.UserRepository;
import com.haw.se1lab.logic.api.usecase.UserUseCase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * Testklasse für die SubjectFacade
 *
 * @author Isabell Schettler
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // environment
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class) // Disable WebSecurity
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class SubjectFacadeTest {

    private final Log log = LogFactory.getLog(getClass());

    @LocalServerPort
    private int port;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    @Autowired
    private UserUseCase userUseCase;

    private Subject subject;

    private User user;

    private User prof;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

    private LevelSystem profLevelSystem;

    private String token;

    @BeforeAll
    public static void setUpAll() {
        // actions to be performed once before execution of first test method

    }

    @AfterAll
    public static void tearDownAll() {
        // actions to be performed once after execution of last test method

    }

    @BeforeEach
    public void setUp() throws UserNotFoundException {
        // set up fresh test data before each test method execution
        academicCourse = new AcademicCourse("Angewandte Informatik");
        academicCourseRepository.save(academicCourse);

        profLevelSystem = new LevelSystem();
        levelSystemRepository.save(profLevelSystem);

        prof = new User(new EmailType("Michael.Neitzke@haw-hamburg.de"), "Michael", "Neitzke",
                RoleType.PROFESSOR, new AKennungType("abc123"), new Password("ABCde567!"), academicCourse, profLevelSystem);
        userRepository.save(prof);

        subject = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123*"), "", prof);
        subjectRepository.save(subject);

        levelSystem = new LevelSystem();
        levelSystemRepository.save(levelSystem);

        user = new User(new EmailType("max.musterman@haw-hamburg.de"), "Max", "Musterman",
                RoleType.STUDENT, new AKennungType("aaa123"), new Password("Password!1"), academicCourse, levelSystem);
        userRepository.save(user);

        user = userUseCase.logIn(user.getEmail(), user.getPassword());
        token = user.getSession();

        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @AfterEach
    public void tearDown() {
        // clean up test data after each test method execution

        subjectRepository.deleteAll();
        userRepository.deleteAll();
        academicCourseRepository.deleteAll();
        levelSystemRepository.deleteAll();
    }

    @Test
    public void getSubjects_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/subjects")

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("subjectName", hasItems(subject.getSubjectName()));
        // @formatter:on
    }

    @Test
    public void getSubjectSuccess() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/subjects/{id}", subject.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("subjectName", equalTo(subject.getSubjectName()));
        // @formatter:on
    }

    @Test
    public void getSubject_Fail_SubjectNotFound() {
        // @formatter:off
        long id = subject.getId() + 8;
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/subjects/{id}", id)

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

    @Test
    public void createSubject_Success() {
        // @formatter:off
        // [GIVEN]
        Subject subject2 = new Subject("Software Engineering 2", "SE2", 4, new Password("Test123*"), "", prof);

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(subject2)

                // [WHEN]
                .when()
                .post("/subjects")

                // [THEN]
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(notNullValue()));
        // @formatter:on
    }

    @Test
    public void createSubject_Fail_SubjectAlreadyExisting() {
        // @formatter:off
        // [GIVEN]
        Subject subjectDuplicate = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123*"), "", prof);
        subjectDuplicate.setId(subject.getId());

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(subjectDuplicate)

                // [WHEN]
                .when()
                .post("/subjects")

                // [THEN]
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        // @formatter:on
    }

    @Test
    public void updateSubject_Success() {
        // @formatter:off
        // [GIVEN]
        String newSubjectName = "Intelligente Systeme 1";

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body("{\"subjectName\": \"" + newSubjectName + "\"}")

                // [WHEN]
                .when()
                .put("/subjects/{id}", subject.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/subjects/{id}", subject.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("subjectName", is(equalTo(newSubjectName)));
        // @formatter:on

    }

    @Test
    public void deleteSubject_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .delete("/subjects/{id}", subject.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/subjects/{id}", subject.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

}

