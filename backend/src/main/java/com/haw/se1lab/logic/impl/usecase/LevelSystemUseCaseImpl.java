package com.haw.se1lab.logic.impl.usecase;

import com.haw.se1lab.common.api.exception.LevelSystemAlreadyExistingException;
import com.haw.se1lab.common.api.exception.LevelSystemNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.repo.LevelSystemRepository;
import com.haw.se1lab.logic.api.usecase.LevelSystemUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Default implementation for {@link LevelSystemUseCase}.
 *
 * @author Kathleen Neitzel
 */
@Service // causes Spring to automatically create a Spring bean for this class which can then be used using @Autowired
public class LevelSystemUseCaseImpl implements LevelSystemUseCase {

    @Autowired // automatically initializes the field with a Spring bean of a matching type
    private LevelSystemRepository levelSystemRepository;

    @Override
    public List<LevelSystem> findAllLevelSystems() {
        // load entities from DB
        return levelSystemRepository.findAll();
    }

    @Override
    public LevelSystem findLevelSystemById(int id) throws LevelSystemNotFoundException {
        // load entity from DB
        return levelSystemRepository.findById(id).orElseThrow(() -> new LevelSystemNotFoundException(id));
    }

    @Override
    public LevelSystem createLevelSystem(int id)
            throws LevelSystemAlreadyExistingException {
        // check preconditions
        Assert.notNull(id, "Parameter 'id' must not be null!");

        if (levelSystemRepository.findById(id).isPresent()) {
            throw new LevelSystemAlreadyExistingException(id);
        }

        // create a new level system as plain old Java object
        LevelSystem levelSystem = new LevelSystem();

        // store entity in DB (from then on: entity object is observed by Hibernate within current transaction)
        return levelSystemRepository.save(levelSystem);
    }

    @Override
    public LevelSystem updateLevelSystem(LevelSystem levelSystem) throws LevelSystemNotFoundException {
        // check preconditions
        Assert.notNull(levelSystem, "Parameter 'levelSystem' must not be null!");

        // make sure the level system to be updated exists (throw exception if not)
        findLevelSystemById(levelSystem.getId());

        // store entity in DB (from then on: entity object is observed by Hibernate within current transaction)
        return levelSystemRepository.save(levelSystem);
    }

    @Override
    public void deleteLevelSystem(int id) throws LevelSystemNotFoundException {
        // check preconditions
        // make sure the level system to be deleted exists (throw exception if not) and also load the level system
        LevelSystem levelSystem = findLevelSystemById(id);

        // delete entity in DB
        levelSystemRepository.delete(levelSystem);
    }

}
