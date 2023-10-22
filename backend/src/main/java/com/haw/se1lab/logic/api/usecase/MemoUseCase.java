package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.common.api.exception.MemoAlreadyExistingException;
import com.haw.se1lab.common.api.exception.MemoNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.Memo;
import com.haw.se1lab.dataaccess.api.entity.Subject;

import java.util.List;

/**
 * Use Case Interface für Memo
 *
 * @author Kjell May
 */
public interface MemoUseCase {

    /**
     * Gibt alle Memos zurück
     *
     * @return List<Memo> Eine Liste der Memos
     */
    List<Memo> findAllMemos();

    /**
     * Gibt eine Memo nach ID zurück
     *
     * @param id Die Id der Memo
     * @return die gesuchte Memo
     * @throws MemoNotFoundException wenn Memo nicht gefunden wurde
     */
    Memo findMemoById(int id) throws MemoNotFoundException;

    /**
     * Lässt eine Memo erstellen
     *
     * @param subject Das subject
     * @param title Gewünschter Titel
     * @param rewardXP Die Belohnungspunkte
     * @return Neu erstellte Memo
     * @throws MemoAlreadyExistingException wenn es bereits solch eine Memo gibt
     */
    Memo createMemo(Subject subject, String title, int rewardXP, String description) throws MemoAlreadyExistingException;

    /**
     * Lässt eine Memo aktualisieren
     *
     * @param memo Die zu aktualisierende Memo
     * @return die aktualisierte Memo
     * @throws MemoNotFoundException Wenn es die Memo nicht gibt
     */
    Memo updateMemo(Memo memo) throws MemoNotFoundException;

    /**
     * Lässt eine Memo löschen
     *
     * @param id Id der zu löschenden Memo
     * @throws MemoNotFoundException Wenn es die Memo nicht gibt
     */
    void deleteMemo(int id) throws MemoNotFoundException;
}
