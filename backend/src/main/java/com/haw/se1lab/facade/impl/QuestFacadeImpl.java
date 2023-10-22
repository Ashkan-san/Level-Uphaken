package com.haw.se1lab.facade.impl;

import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Quest;
import com.haw.se1lab.facade.api.QuestFacade;
import com.haw.se1lab.logic.api.usecase.QuestUseCase;
import com.haw.se1lab.logic.api.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Default implementation for {@link QuestFacade}. This implementation uses REST to provide the defined
 * functionality.
 *
 * @author Kathleen Neitzel
 */
@RestController
@RequestMapping(path = "/quests")
public class QuestFacadeImpl implements QuestFacade {

    @Autowired
    private QuestUseCase questUseCase;

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping
    @Override
    public List<Quest> getQuests() {
        return questUseCase.findAllQuests();
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @Override
    public Quest getQuest(@PathVariable("id") long id) throws QuestNotFoundException {
        return questUseCase.findQuestById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Override
    public Quest createQuest(@RequestBody Quest quest) throws QuestAlreadyExistingException {
        // check preconditions
        Assert.notNull(quest, "Parameter 'quest' must not be null!");

        return questUseCase.createQuest(quest.getTitle(), quest.getContent(),
                quest.getRewardXP());
    }

    @PutMapping
    @Transactional
    @Override
    public Quest updateQuest(@RequestBody Quest quest) throws QuestNotFoundException {
        return questUseCase.updateQuest(quest);
    }

    @DeleteMapping("/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Override
    public void deleteQuest(@PathVariable("id") long id) throws QuestNotFoundException {
        questUseCase.deleteQuest(id);
    }

    @DeleteMapping("/{title:[A-Za-z]+}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Override
    public void deleteQuestByTitle(@PathVariable("title") String title) throws QuestNotFoundException {
        questUseCase.deleteQuestByTitle(title);
    }

    @PutMapping("/complete/{id:[\\d]+}")
    @Transactional
    @Override
    public LevelSystem completeQuest(@PathVariable("id") long id) throws QuestNotFoundException, UserNotFoundException, LevelSystemAlreadyInQuestException, QuestAlreadyInLevelSystemException {
        Quest quest = questUseCase.findQuestById(id);
        return questUseCase.completeQuest(quest, userUseCase.getCurrentUser());
    }

    @ExceptionHandler(QuestNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Quest not found.")
    private void handleQuestNotFoundException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler(QuestAlreadyExistingException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Quest already exists.")
    private void handleQuestAlreadyExistingException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler({LevelSystemAlreadyInQuestException.class, QuestAlreadyInLevelSystemException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Quest already completed.")
    private void handleQuestAlreadyCompleted() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private void handleIllegalArgumentException(IllegalArgumentException exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

}
