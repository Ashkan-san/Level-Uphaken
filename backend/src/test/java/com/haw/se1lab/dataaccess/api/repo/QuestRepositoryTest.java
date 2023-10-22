package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.Application;
import com.haw.se1lab.dataaccess.api.entity.Quest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.haw.se1lab.common.api.datatype.QuestType.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link QuestRepository}.
 *
 * @author Kathleen Neitzel
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
public class QuestRepositoryTest {


        @Autowired
        private QuestRepository questRepository;

        private Quest quest1;

        private Quest quest2;

        private Quest quest3;

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

            quest1 = new Quest("3 Tage hintereinander anmelden", TAGE_3, 30);
            questRepository.save(quest1);

            quest2 = new Quest("5 Tage lang 1 Aufgabe pro Tag erledigen.", TAGE_5_AUFGABE_1, 70);
            questRepository.save(quest2);

            quest3 = new Quest("Alle Aufgaben einer Memo erledigen.", MEMO_ABSCHLIESSEN, 10);
            questRepository.save(quest3);
        }

        @AfterEach
        public void tearDown() {
            // clean up test data after each test method execution

            questRepository.deleteAll();
        }

        @Test
        public void findQuestByTitle_Success() {
            // [GIVEN]
            String title = quest1.getTitle();

            // [WHEN]
            Optional<Quest> loadedQuest = questRepository.findByTitle(title);

            // [THEN]
            assertThat(loadedQuest.isPresent()).isTrue();
            assertThat(loadedQuest.get().getTitle()).isEqualTo(title);
        }

        @Test
        public void findQuestByTitle_Success_EmptyResult() {
            // [GIVEN]
            String title = "xxx";

            // [WHEN]
            Optional<Quest> loadedQuest = questRepository.findByTitle(title);

            // [THEN]
            assertThat(loadedQuest.isPresent()).isFalse();
        }

        @Test
        public void deleteByTitle_Success() {
            // [GIVEN]
            String title = quest1.getTitle();

            // [WHEN]
            questRepository.deleteByTitle(title);

            // [THEN]
            Optional<Quest> loadedQuest = questRepository.findByTitle(title);
            assertThat(loadedQuest.isPresent()).isFalse();
        }
}
