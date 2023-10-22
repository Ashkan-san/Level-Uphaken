package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.*;
import com.haw.se1lab.dataaccess.api.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testklasse für TaskRepository
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskRepositoryTest {

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
    private TaskRepository taskRepository;

    private User prof;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

    private Subject subject;

    private Memo memo;

    private Task task1;

    private Task task2;

    @BeforeEach
    public void setUp() {
        // set up fresh test data before each test method execution
        academicCourse = new AcademicCourse("Angewandte Informatik");
        academicCourseRepository.save(academicCourse);

        levelSystem = new LevelSystem();
        levelSystemRepository.save(levelSystem);

        prof = new User(new EmailType("prof.musterman@haw-hamburg.de"), "Max", "Musterman",
                RoleType.PROFESSOR, new AKennungType("aaa111"), new Password("Password!1"), academicCourse, levelSystem);
        userRepository.save(prof);

        subject = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123!"), "", prof);
        subjectRepository.save(subject);

        memo = new Memo(subject, "memoTest", 100, "");
        memoRepository.save(memo);

        task1 = new Task(memo, "SE2-Task", new TaskId("SE201"), 75, TaskType.MANDATORY, "Schreibe Tests");
        taskRepository.save(task1);

        task2 = new Task(memo, "BW2-Task", new TaskId("BW201"), 75, TaskType.OPTIONAL, "Aufgabe 7");
        taskRepository.save(task2);
    }

    @Test
    public void findTaskById_Success() {
        // [GIVEN]
        TaskId taskId = task1.getTaskId();

        // [WHEN]
        Optional<Task> loadedTask = taskRepository.findByTaskId(taskId);

        // [THEN]
        assertThat(loadedTask).isPresent();
        assertThat(loadedTask.get().getTaskId()).isEqualTo(taskId);
    }

    @Test
    public void findTaskById_Success_EmptyResult() {
        // [GIVEN]
        TaskId taskId = new TaskId("SE203");

        // [WHEN]
        Optional<Task> loadedTask = taskRepository.findByTaskId(taskId);

        // [THEN]
        assertThat(loadedTask).isNotPresent();
    }

    @Test
    public void deleteById_Success() {
        // [GIVEN]
        long id = task1.getId();

        // [WHEN]
        taskRepository.deleteById(id);

        // [THEN]
        Optional<Task> loadedTask = taskRepository.findById(id);
        assertThat(loadedTask).isNotPresent();
    }
}
