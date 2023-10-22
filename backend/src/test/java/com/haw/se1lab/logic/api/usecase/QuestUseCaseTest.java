package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.Application;
import com.haw.se1lab.dataaccess.api.entity.Quest;
import com.haw.se1lab.dataaccess.api.repo.QuestRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.haw.se1lab.common.api.datatype.QuestType.TAGE_3;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link QuestUseCase}.
 *
 * @author Kathleen Neitzel
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
public class QuestUseCaseTest {
    @Autowired
    private QuestUseCase questUseCase;

    @Autowired
    private QuestRepository questRepository;

    private Quest quest;

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

        quest = new Quest("3 Tage hintereinander anmelden", TAGE_3, 30);
        questRepository.save(quest);

    }

    @AfterEach
    public void tearDown() {
        // clean up test data after each test method execution

        questRepository.deleteAll();
    }

    @Test
    public void findAllQuests_Success() {
        // [GIVEN]
        String title = quest.getTitle();

        // [WHEN]
        List<Quest> loadedQuests = questUseCase.findAllQuests();

        // [THEN]
        assertThat(loadedQuests).hasSize(1);
        assertThat(loadedQuests).extracting(Quest::getTitle).containsOnlyOnce(title);
    }

}
