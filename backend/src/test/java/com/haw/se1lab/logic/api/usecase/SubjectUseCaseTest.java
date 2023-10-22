package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.dataaccess.api.repo.AcademicCourseRepository;
import com.haw.se1lab.dataaccess.api.repo.LevelSystemRepository;
import com.haw.se1lab.dataaccess.api.repo.SubjectRepository;
import com.haw.se1lab.dataaccess.api.repo.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testklasse für den SubjectUseCase
 *
 * @author Isabell Schettler
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
public class SubjectUseCaseTest {

    @Autowired
    private SubjectUseCase subjectUseCase;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    private User prof;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

    private Subject subject;

    @BeforeAll
    public static void setUpAll() {
        // actions to be performed once before execution of first test method

    }

    @AfterAll
    public static void tearDownAll() {
        // actions to be performed once after execution of last test method

    }

    @BeforeEach
    public void setUp() {
        // set up fresh test data before each test method execution
        academicCourse = new AcademicCourse("Angewandte Informatik");
        academicCourseRepository.save(academicCourse);

        levelSystem = new LevelSystem();
        levelSystemRepository.save(levelSystem);

        prof = new User(new EmailType("Michael.Neitzke@haw-hamburg.de"), "Michael", "Neitzke",
                RoleType.PROFESSOR, new AKennungType("abc123"), new Password("ABCde567!"), academicCourse, levelSystem);
        userRepository.save(prof);

        subject = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123*"), "", prof);
        subjectRepository.save(subject);
    }

    @AfterEach
    public void tearDown() {

        subjectRepository.deleteAll();
        userRepository.deleteAll();
        academicCourseRepository.deleteAll();
        levelSystemRepository.deleteAll();
    }

    @Test
    public void findAllMemos_Success() {
        // [GIVEN]
        long id = subject.getId();

        // [WHEN]
        List<Subject> loadedSubjects = subjectUseCase.findAllSubjects();

        // [THEN]
        assertThat(loadedSubjects).hasSize(1);
        assertThat(loadedSubjects).extracting(Subject::getId).containsOnlyOnce(id);

    }
}
