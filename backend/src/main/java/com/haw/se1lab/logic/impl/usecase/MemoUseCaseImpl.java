package com.haw.se1lab.logic.impl.usecase;

import com.haw.se1lab.common.api.exception.MemoAlreadyExistingException;
import com.haw.se1lab.common.api.exception.MemoNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.Memo;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.repo.MemoRepository;
import com.haw.se1lab.logic.api.usecase.MemoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Implementierende UseCase-Klasse für Memo
 *
 * @author Kjell May
 */
@Service
public class MemoUseCaseImpl implements MemoUseCase {

    @Autowired
    private MemoRepository memoRepository;

    /**
     * Gibt alle Memos zurück
     *
     * @return List<Memo> Eine Liste der Memos
     */
    @Override
    public List<Memo> findAllMemos() {
        return memoRepository.findAll();
    }

    /**
     * GIbt eine Memo nach ID zurück
     *
     * @param id Die Id der Memo
     * @return die gesuchte Memo
     * @throws MemoNotFoundException wenn Memo nicht gefunden wurde
     */
    @Override
    public Memo findMemoById(int id) throws MemoNotFoundException {
        return memoRepository.findById(id).orElseThrow(() -> new MemoNotFoundException(id));
    }

    /**
     * Lässt eine Memo erstellen
     *
     * @param title   Gewünschter Titel
     * @param rewardXP Zugehöriges Fach
     * @return Neu erstellte Memo
     * @throws MemoAlreadyExistingException wenn es bereits solch eine Memo gibt
     */
    @Override
    public Memo createMemo(Subject subject, String title, int rewardXP, String description) throws MemoAlreadyExistingException {
        Assert.notNull(title, "Titel darf nicht null sein");
        Assert.hasText(title, "Titel darf nicht leer sein");
        Assert.notNull(description, "Description darf nicht null sein");
        Assert.hasText(description, "Description darf nicht leer sein");
        Assert.isTrue(rewardXP >= 0, "RewardXP muss positiv sein");

        if (memoRepository.findByTitle(title).isPresent()) throw new MemoAlreadyExistingException(title);

        Memo memo = new Memo(subject, title, rewardXP, description);

        return memoRepository.save(memo);
    }

    /**
     * Lässt eine Memo erstellen
     *
     * @param memo Die zu aktualisierende Memo
     * @return die aktualisierte Memo
     * @throws MemoNotFoundException Wenn es die Memo nicht gibt
     */
    @Override
    public Memo updateMemo(Memo memo) throws MemoNotFoundException {
        Assert.notNull(memo, "Memo darf nicht null sein");

        findMemoById(memo.getId());

        return memoRepository.save(memo);
    }

    /**
     * Lässt eine Memo löschen
     *
     * @param id Id der zu löschenden Memo
     * @throws MemoNotFoundException Wenn es die Memo nicht gibt
     */
    @Override
    public void deleteMemo(int id) throws MemoNotFoundException {
        Memo memo = findMemoById(id);

        memoRepository.delete(memo);
    }
}
