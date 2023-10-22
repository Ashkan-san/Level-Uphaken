package com.haw.se1lab.facade.impl;

import com.haw.se1lab.common.api.exception.LevelSystemAlreadyExistingException;
import com.haw.se1lab.common.api.exception.LevelSystemNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.facade.api.LevelSystemFacade;
import com.haw.se1lab.logic.api.usecase.LevelSystemUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Default implementation for {@link com.haw.se1lab.facade.api.LevelSystemFacade}.
 * This implementation uses REST to provide the defined functionality.
 *
 * @author Kathleen Neitzel
 */

@RestController
@RequestMapping(path = "/levelSystems")
public class LevelSystemFacadeImpl implements LevelSystemFacade {


    @Autowired
    private LevelSystemUseCase levelSystemUseCase;

    @GetMapping
    @Override
    public List<LevelSystem> getLevelSystems() {
    return levelSystemUseCase.findAllLevelSystems();
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @Override
    public LevelSystem getLevelSystem(@PathVariable("id") int id) throws LevelSystemNotFoundException {
        return levelSystemUseCase.findLevelSystemById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Override
    public LevelSystem createLevelSystem(@RequestBody LevelSystem levelSystem) throws LevelSystemAlreadyExistingException {
        // check preconditions
        Assert.notNull(levelSystem, "Parameter 'levelSystem' must not be null!");

        return levelSystemUseCase.createLevelSystem(levelSystem.getId());
    }

    @PutMapping
    @Transactional
    @Override
    public LevelSystem updateLevelSystem(@RequestBody LevelSystem levelsystem) throws LevelSystemNotFoundException {
        return levelSystemUseCase.updateLevelSystem(levelsystem);
    }

    @DeleteMapping("/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Override
    public void deleteLevelSystem(@PathVariable("id") int id) throws LevelSystemNotFoundException {
        levelSystemUseCase.deleteLevelSystem(id);
    }


    @ExceptionHandler(LevelSystemNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Level system not found.")
    private void handleLevelSystemNotFoundException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

}
