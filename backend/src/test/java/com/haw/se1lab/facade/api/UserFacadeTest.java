package com.haw.se1lab.facade.api;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.UserNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.dataaccess.api.repo.AcademicCourseRepository;
import com.haw.se1lab.dataaccess.api.repo.LevelSystemRepository;
import com.haw.se1lab.dataaccess.api.repo.SubjectRepository;
import com.haw.se1lab.dataaccess.api.repo.UserRepository;

import com.haw.se1lab.logic.api.usecase.UserUseCase;
import io.restassured.RestAssured;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.springframework.http.HttpStatus;
import io.restassured.http.ContentType;

/**
 * Testklasse für die UserFacade
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // environment
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class) // Disable WebSecurity
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserFacadeTest {

    private final Log log = LogFactory.getLog(getClass());

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserUseCase userUseCase;

    private User user;

    private User prof;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

    private LevelSystem profLevelSystem;

    private Subject subject;

    private String token;

    @BeforeEach
    public void setUp() throws UserNotFoundException {
        // set up fresh test data before each test method execution
        academicCourse = new AcademicCourse("Angewandte Informatik");
        academicCourseRepository.save(academicCourse);

        levelSystem = new LevelSystem();
        levelSystemRepository.save(levelSystem);

        user = new User(new EmailType("max.musterman@haw-hamburg.de"), "Max", "Musterman", RoleType.STUDENT,
                new AKennungType("aaa123"), new Password("Password!1"), academicCourse, levelSystem);
        userRepository.save(user);

        profLevelSystem = new LevelSystem();
        levelSystemRepository.save(profLevelSystem);

        prof = new User(new EmailType("prof.musterman@haw-hamburg.de"), "Max", "Musterman",
                RoleType.PROFESSOR, new AKennungType("aaa111"), new Password("Password!1"), academicCourse, profLevelSystem);
        userRepository.save(prof);

        subject = new Subject("Software Engineering 2", "SE2", 3, new Password("SoSe21!!!"), "", prof);
        subjectRepository.save(subject);

        user = userUseCase.logIn(user.getEmail(), user.getPassword());
        token = user.getSession();

        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @Test
    public void getUsers_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/users")

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("lastName", hasItems(user.getLastName()));
        // @formatter:on
    }

    @Test
    public void getUserSuccess() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/users/{email}", user.getEmail().getEmailAddress())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("lastName", equalTo(user.getLastName()));
        // @formatter:on
    }

    @Test
    public void getUser_Fail_UserNotFound() {
        // @formatter:off
        EmailType email = new EmailType("nonexisting@haw-hamburg.de");
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/users/{email}", email.getEmailAddress())

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

    @Test
    public void createUser_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body("{\n" +
                              "\"emailAddress\": \"bieter.dohlen@haw-hamburg.de\",\n" +
                              "\"firstName\": \"Bieter\",\n" +
                              "\"lastName\": \"Dohlen\",\n" +
                              "\"aKennung\": \"azy444\",\n" +
                              "\"password\": \"Test123!\",\n" +
                              "\"academicCourse\": \"Wirtschaftsinformatik\"\n" +
                              "}")

                // [WHEN]
                .when()
                .post("/users")

                // [THEN]
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(notNullValue()));
        // @formatter:on
    }

    @Test
    public void createUser_Fail_UserAlreadyExisting() {
        // @formatter:off
        // [GIVEN]
        AcademicCourse duplicateAcademicCourse = new AcademicCourse("Angewandte Informatik");
        LevelSystem duplicateLevelSystem = new LevelSystem();
        User userDuplicate = new User(user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole(),
                user.getaKennung(), user.getPassword(),duplicateAcademicCourse, levelSystem);

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(userDuplicate)

                // [WHEN]
                .when()
                .post("/users")

                // [THEN]
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        // @formatter:on
    }

    @Test
    public void updateUser_Success() {
        // @formatter:off
        // [GIVEN]
        String newFirstName = "Dieter";

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body("{\"firstName\": \"" + newFirstName + "\"}")

                // [WHEN]
                .when()
                .put("/users")

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/users/{email}", user.getEmail().getEmailAddress())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("firstName", is(equalTo(newFirstName)));
        // @formatter:on
    }

    @Test
    public void deleteUser_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .delete("/users/{email}", user.getEmail().getEmailAddress())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/users/{email}", user.getEmail().getEmailAddress())

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

    @Test
    public void enrollInSubject_Success() {
        // @formatter:off
        // [GIVEN]
        String acronym = subject.getAcronym();
        Password enrollmentKey = subject.getEnrollmentKey();

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .put("/users/enroll/{acronym}/{enrollmentKey}", acronym, enrollmentKey.getPassword())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/users/{email}", user.getEmail().getEmailAddress())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("subjects[0].subjectName", equalTo(subject.getSubjectName()));
        // @formatter:on
    }
}
