package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testklasse für SubjectRepository
 *
 * @author Isabell Schettler
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
public class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    private Subject subject1;

    private User prof;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

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

        subject1 = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123*"), "", prof);
        subjectRepository.save(subject1);

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
    public void findSubjectById_Success() {
        // [GIVEN]
        long id = subject1.getId();

        // [WHEN]
        Optional<Subject> loadedSubject = subjectRepository.findById(id);

        // [THEN]
        assertThat(loadedSubject.isPresent()).isTrue();
        assertThat(loadedSubject.get().getId()).isEqualTo(id);
    }

    @Test
    public void findSubjectById_Success_EmptyResult() {
        // [GIVEN]
        long id = subject1.getId() + 8;

        // [WHEN]
        Optional<Subject> loadedSubject = subjectRepository.findById(id);

        // [THEN]
        assertThat(loadedSubject.isPresent()).isFalse();    //komischerweise true
    }

    @Test
    public void deleteSubjectById_Success() {
        // [GIVEN]
        long id = subject1.getId();

        // [WHEN]
        subjectRepository.deleteById(id);

        // [THEN]
        Optional<Subject> loadedsubject = subjectRepository.findById(id);
        assertThat(loadedsubject.isPresent()).isFalse();
    }
}
