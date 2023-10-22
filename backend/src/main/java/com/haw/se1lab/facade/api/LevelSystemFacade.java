package com.haw.se1lab.facade.api;

import com.haw.se1lab.common.api.exception.LevelSystemAlreadyExistingException;
import com.haw.se1lab.common.api.exception.LevelSystemNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;

import java.util.List;

/**
 * Represents a facade for the system operations for level systems which are
 * available from outside the system.
 *
 * @author Kathleen Neitzel
 */

public interface LevelSystemFacade {
    /**
     * Returns all level systems.
     *
     * @return the found level systems or an empty list if none were found
     */
    List<LevelSystem> getLevelSystems();

    /**
     * Returns the level system with the given ID.
     *
     * @param id the level system's technical ID
     * @return the found level system
     * @throws LevelSystemNotFoundException in case the level system could not be found
     */
    LevelSystem getLevelSystem(int id) throws LevelSystemNotFoundException;

    /**
     * Creates a level system with the given data.
     *
     * @param levelSystem the level system to be created; must not be <code>null</code>
     * @return the created level system
     * @throws LevelSystemAlreadyExistingException in case a quest with the given
     *                                          data already exists
     */
    LevelSystem createLevelSystem(LevelSystem levelSystem) throws LevelSystemAlreadyExistingException;

    /**
     * Updates a level system with the given data.
     *
     * @param levelsystem the level system to be updated; must not be <code>null</code>
     * @return the updated level system
     * @throws LevelSystemNotFoundException in case the level system could not be found
     */
    LevelSystem updateLevelSystem(LevelSystem levelsystem) throws LevelSystemNotFoundException;

    /**
     * Deletes the level system with the given ID.
     *
     * @param id the level system's technical ID
     * @throws LevelSystemNotFoundException in case the level system could not be found
     */
    void deleteLevelSystem(int id) throws LevelSystemNotFoundException;

}
