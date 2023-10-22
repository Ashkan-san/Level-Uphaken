package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.*;
import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.User;
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
 * Testklasse für das UserRepository
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    private User user1;

    private User user2;

    private User user3;

    private AcademicCourse academicCourse1;

    private AcademicCourse academicCourse2;

    private AcademicCourse academicCourse3;

    private LevelSystem levelSystem1;

    private LevelSystem levelSystem2;

    private LevelSystem levelSystem3;

    @BeforeEach
    public void setUp() {
        // set up fresh test data before each test method execution

        academicCourse1 = new AcademicCourse("Angewandte Informatik");
        academicCourseRepository.save(academicCourse1);

        academicCourse2 = new AcademicCourse("Wirtschaftsinformatik");
        academicCourseRepository.save(academicCourse2);

        academicCourse3 = new AcademicCourse("Technische Informatik");
        academicCourseRepository.save(academicCourse3);

        levelSystem1 = new LevelSystem();
        levelSystemRepository.save(levelSystem1);

        levelSystem2 = new LevelSystem();
        levelSystemRepository.save(levelSystem2);

        levelSystem3 = new LevelSystem();
        levelSystemRepository.save(levelSystem3);

        user1 = new User(new EmailType("max.musterman@haw-hamburg.de"), "Max", "Musterman", RoleType.STUDENT,
                new AKennungType("aaa123"), new Password("Password!1"), academicCourse1, levelSystem1);
        userRepository.save(user1);

        user2 = new User(new EmailType("bieter.dohlen@haw-hamburg.de"), "Bieter", "Dohlen", RoleType.PROFESSOR,
                new AKennungType("azy444"), new Password("pASSWORD1!"), academicCourse2, levelSystem2);
        userRepository.save(user2);

        user3 = new User(new EmailType("navier.xaidoo@haw-hamburg.de"), "Navier", "Xaidoo", RoleType.ADMIN,
                new AKennungType("all999"), new Password("1234ABC?a"), academicCourse3, levelSystem3);
        userRepository.save(user3);
    }

    @AfterEach
    public void tearDown() {
        // clean up test data after each test method execution

        userRepository.deleteAll();
        levelSystemRepository.deleteAll();
        academicCourseRepository.deleteAll();
    }

    @Test
    public void findUserByEmail_Success() {
        // [GIVEN]
        EmailType email = user1.getEmail();

        // [WHEN]
        Optional<User> loadedUser = userRepository.findByEmail(email);

        // [THEN]
        assertThat(loadedUser.isPresent()).isTrue();
        assertThat(loadedUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void findUserByEmail_Success_EmptyResult() {
        // [GIVEN]
        EmailType email = new EmailType("aaa.bbb@haw-hamburg.de");

        // [WHEN]
        Optional<User> loadedUser = userRepository.findByEmail(email);

        // [THEN]
        assertThat(loadedUser.isPresent()).isFalse();
    }

    @Test
    public void deleteByEmail_Success() {
        // [GIVEN]
        EmailType email = user1.getEmail();

        // [WHEN]
        userRepository.deleteByEmail(email);

        // [THEN]
        Optional<User> loadedUser = userRepository.findByEmail(email);
        assertThat(loadedUser.isPresent()).isFalse();
    }
}
