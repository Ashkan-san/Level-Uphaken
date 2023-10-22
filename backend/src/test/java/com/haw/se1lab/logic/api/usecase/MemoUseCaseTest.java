package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.Application;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.dataaccess.api.entity.*;
import com.haw.se1lab.dataaccess.api.repo.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testklasse für den MemoUseCase
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class MemoUseCaseTest {

    @Autowired
    private MemoUseCase memoUseCase;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemoRepository memoRepository;

    private Memo memo;

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
        User prof = new User(new EmailType("prof.musterman@haw-hamburg.de"), "Max", "Musterman",
                RoleType.PROFESSOR, new AKennungType("aaa111"), new Password("Password!1"), null, null);
        userRepository.save(prof);

        Subject subject = new Subject("Intelligente Systeme", "IS", 4, new Password("Test123!"), "Description", prof);
        subjectRepository.save(subject);

        memo = new Memo(subject, "memoTest", 100, "");
        memoRepository.save(memo);
    }

    @AfterEach
    public void tearDown() {

        memoRepository.deleteAll();
        subjectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllMemos_Success() {
        // [GIVEN]
        int id = memo.getId();

        // [WHEN]
        List<Memo> loadedMemos = memoUseCase.findAllMemos();

        // [THEN]
        assertThat(loadedMemos).hasSize(1);
        assertThat(loadedMemos).extracting(Memo::getId).containsOnlyOnce(id);

    }
}
