package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.common.api.datatype.QuestType;
import com.haw.se1lab.common.api.exception.LevelSystemAlreadyInQuestException;
import com.haw.se1lab.common.api.exception.QuestAlreadyExistingException;
import com.haw.se1lab.common.api.exception.QuestAlreadyInLevelSystemException;
import com.haw.se1lab.common.api.exception.QuestNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Quest;
import com.haw.se1lab.dataaccess.api.entity.User;

import java.util.List;

/**
 * Defines use case functionality for {@link Quest} entities.
 *
 * @author Kathleen Neitzel
 */
public interface QuestUseCase {

    /**
     * Returns all available quests.
     *
     * @return the found quests or an empty list if none were found
     */
    List<Quest> findAllQuests();

    /**
     * Returns the quest with the given ID.
     *
     * @param id the quest's technical ID
     * @return the found quest
     * @throws QuestNotFoundException in case the quest could not be found
     */
    Quest findQuestById(long id) throws QuestNotFoundException;

    /**
     * Returns the quest with the given title.
     *
     * @param title the quest's title; must not be <code>null</code>
     * @return the found quest
     * @throws QuestNotFoundException in case the quest could not be found
     */
    Quest findQuestByTitle(String title) throws QuestNotFoundException;

    /**
     * Creates a quest with the given data.
     *
     * @param title the quest's title; must not be <code>null</code>
     * @param content      the quest's content; //TODO must contain text or
     * @param rewardXP         the quest's reward; must not be <code>null</code>
     * @return the created quest
     * @throws QuestAlreadyExistingException in case a customer with the given customer number already exists
     */
    Quest createQuest(String title, QuestType content, int rewardXP)
            throws QuestAlreadyExistingException;

    /**
     * Updates a quest with the given data.
     *
     * @param quest the quest data to be updated; must not be <code>null</code>
     * @return the updated quest
     * @throws QuestNotFoundException in case the quest could not be found
     */
    Quest updateQuest(Quest quest) throws QuestNotFoundException;

    /**
     * Deletes the quest with the given ID.
     *
     * @param id the quest's technical ID; must be the ID of an existing quest
     * @throws QuestNotFoundException in case the quest could not be found
     */
    void deleteQuest(long id) throws QuestNotFoundException;

    /**
     * Deletes the quest with the given TITLE.
     *
     * @param title the quest's title; must be the TITLE of an existing quest
     * @throws QuestNotFoundException in case the quest could not be found
     */
    void deleteQuestByTitle(String title) throws QuestNotFoundException;

    /**
     * Completes a Quest for a User
     *
     * @param quest
     * @param user
     * @return
     */
    LevelSystem completeQuest(Quest quest, User user) throws LevelSystemAlreadyInQuestException, QuestAlreadyInLevelSystemException;
}
