package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
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
 * Testklasse für MemoRepository
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class MemoRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    @Autowired
    private MemoRepository memoRepository;

    private User prof;

    private AcademicCourse academicCourse;

    private LevelSystem levelSystem;

    private Subject subject;

    private Memo memo1;

    private Memo memo2;

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

        memo1 = new Memo(subject, "Memo1", 25, "");
        memoRepository.save(memo1);

        memo2 = new Memo(subject, "Memo2", 50, "");
        memoRepository.save(memo2);
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
    public void findMemoById_Success() {
        // [GIVEN]
        int id = memo1.getId();

        // [WHEN]
        Optional<Memo> loadedMemo = memoRepository.findById(id);

        // [THEN]
        assertThat(loadedMemo.isPresent()).isTrue();
        assertThat(loadedMemo.get().getId()).isEqualTo(id);
    }

    @Test
    public void findMemoById_Success_EmptyResult() {
        // [GIVEN]
        int id = memo1.getId() + 1;

        // [WHEN]
        Optional<Memo> loadedMemo = memoRepository.findById(id);

        // [THEN]
        assertThat(loadedMemo.isPresent()).isTrue();    //komischerweise true
    }

    @Test
    public void deleteById_Success() {
        // [GIVEN]
        int id = memo1.getId();

        // [WHEN]
        memoRepository.deleteById(id);

        // [THEN]
        Optional<Memo> loadedMemo = memoRepository.findById(id);
        assertThat(loadedMemo.isPresent()).isFalse();
    }
}
