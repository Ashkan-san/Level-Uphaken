package com.haw.se1lab.facade.api;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.*;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.*;
import com.haw.se1lab.dataaccess.api.repo.*;
import com.haw.se1lab.logic.api.usecase.TaskUseCase;
import com.haw.se1lab.logic.api.usecase.UserUseCase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * Testklasse für die TaskFacade
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // environment
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskFacadeTest {

    private final Log log = LogFactory.getLog(getClass());

    @LocalServerPort
    private int port;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private TaskUseCase taskUseCase;

    private User prof;

    private Subject subject;

    private Task task;

    private Task task2;

    private User user;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

    private LevelSystem profLevelSystem;

    private Memo memo;

    private String token;

    @Autowired
    private TransactionTemplate txTemplate;

    @BeforeEach
    public void setUp() throws UserNotFoundException, TaskNotFoundException, TaskCheckingException, TaskAlreadyInUserException, UserAlreadyInTaskException, SubjectNotFoundException, InsufficientPermissionsException, InvalidEnrollmentKeyException, UserAlreadyInSubjectException {
        // set up fresh test data before each test method execution

        txTemplate.execute(new TransactionCallbackWithoutResult() {

            @SneakyThrows
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
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

                subject = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123!"), "", prof);
                subjectRepository.save(subject);
                userUseCase.enrollInSubject(user, subject.getAcronym(), subject.getEnrollmentKey());

                memo = new Memo(subject, "memoTest", 100, "");
                memoRepository.save(memo);

                task = new Task(memo, "SE2-Task", new TaskId("SE201"), 75, TaskType.MANDATORY, "Schreibe Tests");
                taskRepository.save(task);

                task2 = new Task(memo, "IS-Task", new TaskId("IS101"), 50, TaskType.OPTIONAL, "Denke Aufgaben aus");
                taskRepository.save(task2);
                taskUseCase.checkTask(user, task2);

                user = userUseCase.logIn(user.getEmail(), user.getPassword());
                token = user.getSession();
            }
        });

        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @Test
    public void getTasks_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/tasks")

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(2));
        // @formatter:on
    }

    @Test
    public void getTask_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/tasks/{id}", task.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(task.getTitle()));
        // @formatter:on
    }

    @Test
    public void getTask_Fail_TaskNotFound() {
        // @formatter:off

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/tasks/{id}",666)

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

    @Test
    public void createTask_Success() {
        // @formatter:off
        // [GIVEN]
        Task task2 = new Task(memo, "SE2-Task", new TaskId("SE101"), 75, TaskType.MANDATORY, "Schreibe Tests");

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(task2)

                // [WHEN]
                .when()
                .post("/tasks")

                // [THEN]
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(notNullValue()));
        // @formatter:on
    }

    @Test
    public void createTask_Fail_TaskAlreadyExisting() {
        // @formatter:off
        // [GIVEN]
        Task taskDuplicate = new Task(memo, "SE2-Task", new TaskId("SE201"), 75, TaskType.MANDATORY, "Schreibe Tests");

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body(taskDuplicate)

                // [WHEN]
                .when()
                .post("/tasks")

                // [THEN]
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        // @formatter:on
    }

    @Test
    public void updateTask_Success() {
        // @formatter:off
        // [GIVEN]
        int rewardXP = 70;
        TaskType newType = TaskType.OPTIONAL;

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .body("{\"rewardXP\": " + rewardXP + ", \"type\": \"" + newType + "\"}")

                // [WHEN]
                .when()
                .put("/tasks/{id}", task.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/tasks/{id}", task.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rewardXP", is(equalTo(70)), "type", equalTo(TaskType.OPTIONAL.toString()));
        // @formatter:on
    }

    @Test
    public void deleteTask_Success() {
        // @formatter:off
        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .delete("/tasks/{id}", task.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());

        // [GIVEN]
        given()
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .get("/tasks/{id}", task.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
        // @formatter:on
    }

    @Test
    public void checkTask_Success() {
        // @formatter:off
        // [GIVEN]

        // [WHEN]
        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .put("tasks/check/{id}", task.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    @Test
    public void uncheckTask_Success() {
        // @formatter:off
        // [GIVEN]

        // [WHEN]
        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)

                // [WHEN]
                .when()
                .put("tasks/uncheck/{id}", task2.getId())

                // [THEN]
                .then()
                .statusCode(HttpStatus.OK.value());
        // @formatter:on
    }
}
