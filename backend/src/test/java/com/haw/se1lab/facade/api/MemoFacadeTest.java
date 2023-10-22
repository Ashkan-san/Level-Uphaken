package com.haw.se1lab.facade.api;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.UserNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.*;
import com.haw.se1lab.dataaccess.api.repo.*;
import com.haw.se1lab.logic.api.usecase.UserUseCase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Testklasse für die MemoFacade
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // environment
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class MemoFacadeTest {

    private final Log log = LogFactory.getLog(getClass());

    @LocalServerPort
    private int port;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private SubjectRepository subjectRepository;

    private User prof;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

    private LevelSystem profLevelSystem;

    private Subject subject;

    private Memo memo;

    private User user;

    private String token;

    @BeforeEach
    public void setUp() throws UserNotFoundException {
        // set up fresh test data before each test method execution
        academicCourse = new AcademicCourse("Angewandte Informatik");
        academicCourseRepository.save(academicCourse);

        profLevelSystem = new LevelSystem();
        levelSystemRepository.save(profLevelSystem);

        prof = new User(new EmailType("prof.musterman@haw-hamburg.de"), "Max", "Musterman",
                RoleType.PROFESSOR, new AKennungType("aaa111"), new Password("Password!1"), academicCourse, profLevelSystem);
        userRepository.save(prof);

        subject = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123!"), "", prof);
        subjectRepository.save(subject);

        memo = new Memo(subject, "memoTest", 100, "");
        memoRepository.save(memo);

        levelSystem = new LevelSystem();
        levelSystemRepository.save(levelSystem);

        user = new User(new EmailType("max.musterman@haw-hamburg.de"), "Max", "Musterman", RoleType.STUDENT,
                new AKennungType("aaa123"), new Password("Password!1"), academicCourse, levelSystem);
        userRepository.save(user);

        user = userUseCase.logIn(user.getEmail(), user.getPassword());
        token = user.getSession();

        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @AfterEach
    public void tearDown() {
        // clean up test data after each test method execution

        memoRepository.deleteAll();
        subjectRepository.deleteAll();
        userRepository.deleteAll();
        levelSystemRepository.deleteAll();
        academicCourseRepository.deleteAll();
    }

    @Test
    public void getMemos_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/memos")

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rewardXP", hasItems(memo.getRewardXP()));
        // @formatter:on
    }

    @Test
    public void getMemoSuccess() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/memos/{id}", memo.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rewardXP", equalTo(memo.getRewardXP()));
        // @formatter:on
    }

    @Test
    public void getMemo_Fail_MemoNotFound() {
        // @formatter:off
        int id = 6;
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/memos/{id}", id)

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

    @Test
    public void createMemo_Success() {
        // @formatter:off
        // [GIVEN]
        Memo memo2 = new Memo(subject, "memoTest2", 50, "Description");

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(memo2)

                // [WHEN]
                .when()
                .post("/memos")

                // [THEN]
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(notNullValue()));
        // @formatter:on
    }

    @Test
    public void createMemo_Fail_MemoAlreadyExisting() {
        // @formatter:off
        // [GIVEN]
        Memo memoDuplicate = new Memo(subject, "memoTest", 100, "Description");
        memoDuplicate.setId(memo.getId());

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(memoDuplicate)

                // [WHEN]
                .when()
                .post("/memos")

                // [THEN]
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        // @formatter:on
    }

    @Test
    public void updateMemo_Success() {
        // @formatter:off
        // [GIVEN]
        int newRewardXP = 99;
        memo.setRewardXP(newRewardXP);

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(memo)

                // [WHEN]
                .when()
                .put("/memos")

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/memos/{id}", memo.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rewardXP", is(equalTo(99)));
        // @formatter:on
    }

    @Test
    public void deleteMemo_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .delete("/memos/{id}", memo.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/memos/{id}", memo.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

}
