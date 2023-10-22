package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.*;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.*;
import com.haw.se1lab.dataaccess.api.repo.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testklasse für TaskUseCase
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskUseCaseTest {

    @Autowired
    private TaskUseCase taskUseCase;

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    private Task task;

    private Task task2;

    private Memo memo;

    private User user;

    private User prof;

    private Subject subject;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

    private LevelSystem profLevelSystem;

    @BeforeEach
    @Transactional
    public void setUp() throws TaskNotFoundException, UserAlreadyInTaskException, TaskAlreadyInUserException, TaskCheckingException, UserNotFoundException, SubjectNotFoundException, InsufficientPermissionsException, InvalidEnrollmentKeyException, UserAlreadyInSubjectException {
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

        subject = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123!"), "", prof);
        subjectRepository.save(subject);
        userUseCase.enrollInSubject(user, subject.getAcronym(), subject.getEnrollmentKey());

        memo = new Memo(subject, "memoTest", 100, "");
        memoRepository.save(memo);

        task = new Task(memo, "SE2-Task", new TaskId("SE201"), 75, TaskType.MANDATORY, "Schreibe Tests");
        taskRepository.save(task);

        task2 = new Task(memo, "IS-Task", new TaskId("IS101"), 50, TaskType.OPTIONAL, "Denke Aufgabe aus");
        taskRepository.save(task2);
        taskUseCase.checkTask(user, task2);

    }

    @Test
    @Transactional
    public void findAllTasks_Success() {
        // [GIVEN]
        TaskId id = task.getTaskId();

        // [WHEN]
        List<Task> loadedTasks = taskUseCase.findAllTasks();

        // [THEN]
        assertThat(loadedTasks).hasSize(2);
        assertThat(loadedTasks).extracting(Task::getTaskId).containsOnlyOnce(id);
    }

    @Test
    @Transactional
    public void checkTask_Success() throws TaskNotFoundException, TaskCheckingException, TaskAlreadyInUserException, UserAlreadyInTaskException {
        // [GIVEN]
        TaskId id = task.getTaskId();
        double prevProgress = memo.getProgress(user);
        long prevCheckedTasks = memo.getTasks().stream().filter(task -> task.isChecked(user)).count();

        // [WHEN]
        taskUseCase.checkTask(user, task);

        // [THEN]
        assertThat(task.getTaskId()).isEqualTo(id);
        assertThat(task.getMemo()).isEqualTo(memo);
        assertThat(task.getFinishedUsers()).contains(user);
        assertThat(task.isChecked(user)).isTrue();
        assertThat(memo.getProgress(user)).isGreaterThan(prevProgress);
        assertThat(memo.getTasks().stream().filter(task -> task.isChecked(user)).count()).isGreaterThan(prevCheckedTasks);
    }

    @Test
    @Transactional
    public void uncheckTask_Success() throws TaskNotFoundException, TaskCheckingException, UserNotFoundException {
        // [GIVEN]
        TaskId id = task2.getTaskId();
        long prevCheckedTasks = memo.getTasks().stream().filter(task -> task.isChecked(user)).count();

        // [WHEN]
        taskUseCase.uncheckTask(user, task2);

        // [THEN]
        assertThat(task2.getTaskId()).isEqualTo(id);
        assertThat(task2.getMemo()).isEqualTo(memo);
        assertThat(task2.getFinishedUsers()).doesNotContain(user);
        assertThat(task2.isChecked(user)).isFalse();
        assertThat(memo.getTasks().stream().filter(task -> task.isChecked(user)).count()).isLessThan(prevCheckedTasks);
    }

}
