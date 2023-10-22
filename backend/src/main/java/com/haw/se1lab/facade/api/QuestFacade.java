package com.haw.se1lab.facade.api;

import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Quest;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Represents a facade for the system operations for quests which are
 * available from outside the system.
 *
 * @author Kathleen Neitzel
 */
public interface QuestFacade {

    /**
     * Returns all available quests.
     *
     * @return the found quests or an empty list if none were found
     */
    List<Quest> getQuests();

    /**
     * Returns the quest with the given ID.
     *
     * @param id the quest's technical ID
     * @return the found quest
     * @throws QuestNotFoundException in case the quest could not be found
     */
    Quest getQuest(long id) throws QuestNotFoundException;

    /**
     * Creates a quest with the given data.
     *
     * @param quest the quest to be created; must not be <code>null</code>
     * @return the created quest
     * @throws QuestAlreadyExistingException in case a quest with the given
     *                                          data already exists
     */
    Quest createQuest(Quest quest) throws QuestAlreadyExistingException;

    /**
     * Updates a quest with the given data.
     *
     * @param quest the quest to be updated; must not be <code>null</code>
     * @return the updated quest
     * @throws QuestNotFoundException in case the quest could not be found
     */
    Quest updateQuest(Quest quest) throws QuestNotFoundException;

    /**
     * Deletes the quest with the given ID.
     *
     * @param id the quest's technical ID
     * @throws QuestNotFoundException in case the quest could not be found
     */
    void deleteQuest(long id) throws QuestNotFoundException;

    /**
     * Deletes the quest with the given TITLE.
     *
     * @param title the quest's TITLE
     * @throws QuestNotFoundException in case the quest could not be found
     */
    void deleteQuestByTitle(String title) throws QuestNotFoundException;

    /**
     * Completes a Quest
     *
     * @param id
     * @return
     * @throws QuestNotFoundException
     * @throws UserNotFoundException
     */
    LevelSystem completeQuest(long id) throws QuestNotFoundException, UserNotFoundException, LevelSystemAlreadyInQuestException, QuestAlreadyInLevelSystemException;
}
