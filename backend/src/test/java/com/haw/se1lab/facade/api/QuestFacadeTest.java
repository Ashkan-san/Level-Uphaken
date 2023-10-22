package com.haw.se1lab.facade.api;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.UserNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Quest;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.dataaccess.api.repo.AcademicCourseRepository;
import com.haw.se1lab.dataaccess.api.repo.LevelSystemRepository;
import com.haw.se1lab.dataaccess.api.repo.QuestRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.haw.se1lab.common.api.datatype.QuestType.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Test class for {@link QuestFacade}.
 *
 * @author Kathleen Neitzel
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // environment
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class) // Disable WebSecurity
public class QuestFacadeTest {
    private final Log log = LogFactory.getLog(getClass());

    @LocalServerPort
    private int port;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    @Autowired
    private UserUseCase userUseCase;

    private Quest quest;

    private User user;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

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

        levelSystem = new LevelSystem();
        levelSystemRepository.save(levelSystem);

        quest = new Quest("3 Tage hintereinander anmelden", TAGE_3, 30);
        questRepository.save(quest);

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

        questRepository.deleteAll();
        userRepository.deleteAll();
        levelSystemRepository.deleteAll();
        academicCourseRepository.deleteAll();
    }

    @Test
    public void getQuests_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/quests")

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", hasItems(quest.getTitle()));
        // @formatter:on
    }

    @Test
    public void getQuestSuccess() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/quests/{id}", quest.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(quest.getTitle()));
        // @formatter:on
    }

    @Test
    public void getQuest_Fail_QuestNotFound() {
        // @formatter:off
        int wrongId = 0;
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/quests/{id}", wrongId)

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

    @Test
    public void createQuest_Success() {
        // @formatter:off
        // [GIVEN]
        Quest quest2 = new Quest("5 Tage lang 1 Aufgabe pro Tag erledigen.", TAGE_5_AUFGABE_1, 70);


        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(quest2)

                // [WHEN]
                .when()
                .post("/quests")

                // [THEN]
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("title", is(notNullValue()));
        // @formatter:on
    }

//    @Test
//    public void createQuest_Fail_QuestAlreadyExisting() {
//        // @formatter:off
//        // [GIVEN]
//        Quest questDuplicate = new Quest("3 Aufgaben erledigen.", AUFGABEN_3, 30);
//        //questDuplicate.setId(quest.getId());
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(questDuplicate)
//
//                // [WHEN]
//                .when()
//                .post("/quests")
//
//                // [THEN]
//                .then()
//                .statusCode(HttpStatus.BAD_REQUEST.value());
//        // @formatter:on
//    }

    @Test
    public void updateQuest_Success() {
        // @formatter:off
        // [GIVEN]
        int newXP = 50;
        quest.setRewardXP(newXP);

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(quest)

                // [WHEN]
                .when()
                .put("/quests")

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/quests/{id}", quest.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rewardXP", is(equalTo(50)));
        // @formatter:on
    }

    @Test
    public void deleteQuest_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .delete("/quests/{id}", quest.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/quests/{id}", quest.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

}