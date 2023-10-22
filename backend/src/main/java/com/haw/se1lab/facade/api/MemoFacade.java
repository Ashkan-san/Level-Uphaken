package com.haw.se1lab.facade.api;

import com.haw.se1lab.common.api.exception.MemoAlreadyExistingException;
import com.haw.se1lab.common.api.exception.MemoNotFoundException;
import com.haw.se1lab.common.api.exception.SubjectNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.Memo;

import java.util.List;

/**
 * Represents a facade for the system operations for memos which are
 * available from outside the system.
 *
 * @author Isabell Schettler
 */
public interface MemoFacade {
    /**
     * Returns all available memos.
     *
     * @return the found memos or an empty list if none were found
     */
    List<Memo> getMemos();

    /**
     * Returns the memo with the given ID.
     *
     * @param id the memo's technical ID
     * @return the found memo
     * @throws MemoNotFoundException in case the customer could not be found
     */
    Memo getMemo(int id) throws MemoNotFoundException;

    /**
     * Creates a memo with the given data.
     *
     * @param memo the memo to be created; must not be <code>null</code>
     * @return the created memo
     * @throws MemoAlreadyExistingException in case a memo with the given data already exists
     */
    Memo createMemo(Memo memo) throws MemoAlreadyExistingException, SubjectNotFoundException;

    /**
     * Updates a memo with the given data.
     *
     * @param memo the memo to be updated; must not be <code>null</code>
     * @return the updated memo
     * @throws MemoNotFoundException in case the memo could not be found
     */
    Memo updateMemo(Memo memo) throws MemoNotFoundException;

    /**
     * Deletes the memo with the given ID.
     *
     * @param id the memo's technical ID
     * @throws MemoNotFoundException in case the memo could not be found
     */
    void deleteMemo(int id) throws MemoNotFoundException;
}
