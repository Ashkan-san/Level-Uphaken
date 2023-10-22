package com.haw.se1lab.facade.impl;

import com.haw.se1lab.common.api.exception.MemoAlreadyExistingException;
import com.haw.se1lab.common.api.exception.MemoNotFoundException;
import com.haw.se1lab.common.api.exception.SubjectNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.Memo;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.repo.SubjectRepository;
import com.haw.se1lab.facade.api.MemoFacade;
import com.haw.se1lab.logic.api.usecase.MemoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Facade Implementierung für Memo
 *
 * @author Kjell May
 */
@RestController
@RequestMapping(path = "/memos")
public class MemoFacadeImpl implements MemoFacade {

    @Autowired
    private MemoUseCase memoUseCase;

    @Autowired
    private SubjectRepository subjectRepository;

    /**
     * Returns all available memos.
     *
     * @return the found memos or an empty list if none were found
     */
    @GetMapping
    @Override
    public List<Memo> getMemos() {
        return memoUseCase.findAllMemos();
    }

    /**
     * Returns the memo with the given ID.
     *
     * @param id the memo's technical ID
     * @return the found memo
     * @throws MemoNotFoundException in case the customer could not be found
     */
    @GetMapping(value = "/{id:[\\d]+}")
    @Override
    public Memo getMemo(@PathVariable(value = "id") int id) throws MemoNotFoundException {
        return memoUseCase.findMemoById(id);
    }

    /**
     * Creates a memo with the given data.
     *
     * @param memo the memo to be created; must not be <code>null</code>
     * @return the created memo
     * @throws MemoAlreadyExistingException in case a memo with the given data already exists
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Override
    public Memo createMemo(@RequestBody Memo memo) throws MemoAlreadyExistingException, SubjectNotFoundException {
        Assert.notNull(memo, "Memo darf nicht null sein");
        Subject subject = subjectRepository.findById(memo.getSubject().getId()).orElseThrow(() -> new SubjectNotFoundException(memo.getSubject().getId()));
        return memoUseCase.createMemo(subject, memo.getTitle(), memo.getRewardXP(), memo.getDescription());
    }

    /**
     * Updates a memo with the given data.
     *
     * @param memo the memo to be updated; must not be <code>null</code>
     * @return the updated memo
     * @throws MemoNotFoundException in case the memo could not be found
     */
    @PutMapping
    @Transactional
    @Override
    public Memo updateMemo(@RequestBody Memo memo) throws MemoNotFoundException {
        return memoUseCase.updateMemo(memo);
    }

    /**
     * Deletes the memo with the given ID.
     *
     * @param id the memo's technical ID
     * @throws MemoNotFoundException in case the memo could not be found
     */
    @DeleteMapping("{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Override
    public void deleteMemo(@PathVariable("id") int id) throws MemoNotFoundException {
        memoUseCase.deleteMemo(id);
    }

    @ExceptionHandler(MemoNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Memo not found.")
    private void handleMemoNotFoundException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler(MemoAlreadyExistingException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Memo already exists.")
    private void handleMemoAlreadyExistingException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }
}
