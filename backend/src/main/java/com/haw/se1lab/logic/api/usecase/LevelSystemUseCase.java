package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.common.api.exception.LevelSystemAlreadyExistingException;
import com.haw.se1lab.common.api.exception.LevelSystemNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;

import java.util.List;

/**
 * Defines use case functionality for {@link LevelSystem} entities.
 *
 * @author Kathleen Neitzel
 */
public interface LevelSystemUseCase {
    /**
     * Returns all available level systems.
     *
     * @return the found level systems or an empty list if none were found
     */
    List<LevelSystem> findAllLevelSystems();

    /**
     * Returns the level system with the given ID.
     *
     * @param id the level system's technical ID
     * @return the found level system
     * @throws LevelSystemNotFoundException in case the level system could not be found
     */
    LevelSystem findLevelSystemById(int id) throws LevelSystemNotFoundException;


    //TODO createLevelSystem(){} mit autogenerated id (immer level = 1 und xp = 0?)
    /**
     * Creates a customer with the given data.
     *
     * @param id the level system's id; must not be <code>null</code>
     * @return the created level system
     * @throws LevelSystemAlreadyExistingException in case a level system with the given id already exists
     */
    LevelSystem createLevelSystem(int id)
            throws LevelSystemAlreadyExistingException;

    /**
     * Updates a level system with the given data.
     *
     * @param levelsystem the level system data to be updated; must not be <code>null</code>
     * @return the updated level system
     * @throws LevelSystemNotFoundException in case the customer could not be found
     */
    LevelSystem updateLevelSystem(LevelSystem levelsystem) throws LevelSystemNotFoundException;

    /**
     * Deletes the level system with the given ID.
     *
     * @param id the level system's technical ID; must be the ID of an existing level system
     * @throws LevelSystemNotFoundException in case the level system could not be found
     */
    void deleteLevelSystem(int id) throws LevelSystemNotFoundException;

}
