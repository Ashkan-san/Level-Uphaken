package com.haw.se1lab.logic.impl.usecase;

import com.haw.se1lab.common.api.datatype.QuestType;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Quest;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.dataaccess.api.repo.QuestRepository;
import com.haw.se1lab.dataaccess.api.repo.UserRepository;
import com.haw.se1lab.logic.api.usecase.QuestUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Default implementation for {@link QuestUseCase}.
 *
 * @author Kathleen Neitzel
 */
@Service // causes Spring to automatically create a Spring bean for this class which can then be used using @Autowired
public class QuestUseCaseImpl implements QuestUseCase {

    @Autowired // automatically initializes the field with a Spring bean of a matching type
    private QuestRepository questRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Quest> findAllQuests() {
        // load entities from DB
        return questRepository.findAll();
    }

    @Override
    public Quest findQuestById(long id) throws QuestNotFoundException {
        // load entity from DB
        return questRepository.findById(id).orElseThrow(() -> new QuestNotFoundException(id));
    }

    @Override
    public Quest findQuestByTitle(String title) throws QuestNotFoundException {
        // check preconditions
        Assert.notNull(title, "Parameter 'title' must not be null!");

        // load entity from DB
        return questRepository.findByTitle(title)
                .orElseThrow(() -> new QuestNotFoundException(title));
    }

    @Override
    public Quest createQuest(String title, QuestType content, int rewardXP)
            throws QuestAlreadyExistingException {
        // check preconditions
        Assert.notNull(title, "Parameter 'title' must not be null!");
        //Assert.hasText(content, "Parameter 'content' ");
        Assert.notNull(rewardXP, "Parameter 'rewardXP' must not be null!");

        if (questRepository.findByTitle(title).isPresent()) {
            throw new QuestAlreadyExistingException(title);
        }

        // create a new customer as plain old Java object
        Quest quest = new Quest(title, content, rewardXP);

        // store entity in DB (from then on: entity object is observed by Hibernate within current transaction)
        return questRepository.save(quest);
    }

    @Override
    public Quest updateQuest(Quest quest) throws QuestNotFoundException {
        // check preconditions
        Assert.notNull(quest, "Parameter 'quest' must not be null!");

        // make sure the quest to be updated exists (throw exception if not)
        findQuestById(quest.getId());
        // store entity in DB (from then on: entity object is observed by Hibernate within current transaction)
        return questRepository.save(quest);
    }

    @Override
    public void deleteQuest(long id) throws QuestNotFoundException {
        // check preconditions
        // make sure the quest to be deleted exists (throw exception if not) and also load the quest
        Quest quest = findQuestById(id);

        // delete entity in DB
        questRepository.delete(quest);
    }

    @Override
    public void deleteQuestByTitle(String title) throws QuestNotFoundException {
        // check preconditions
        // make sure the quest to be deleted exists (throw exception if not) and also load the quest
        Quest quest = findQuestByTitle(title);

        // delete entity in DB
        questRepository.delete(quest);
    }

    @Override
    public LevelSystem completeQuest(Quest quest, User user) throws LevelSystemAlreadyInQuestException, QuestAlreadyInLevelSystemException {
        if (!user.getRole().equals(RoleType.STUDENT)) throw new IllegalArgumentException("User must be a Student");
        user.getLevelsystem().addQuest(quest);
        userRepository.save(user);
        return user.getLevelsystem();
    }

}
