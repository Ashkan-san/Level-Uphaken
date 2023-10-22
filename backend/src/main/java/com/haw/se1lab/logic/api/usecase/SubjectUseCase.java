package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.common.api.datatype.*;
import com.haw.se1lab.common.api.exception.UserNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.common.api.exception.SubjectAlreadyExistingException;
import com.haw.se1lab.common.api.exception.SubjectNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Use Case Interface für Fach
 *
 * @author Isabell Schettler
 */
public interface SubjectUseCase {

    /**
     * Gibt alle Fächer zurück
     *
     * @return List<Subject> Eine Liste der Fächer
     */
    List<Subject> findAllSubjects();

    /**
     * Gibt ein Fach nach Name zurück
     *
     * @param subjectName der Name des gesuchten Fachs
     * @return das gesuchte fach
     * @throws SubjectNotFoundException wenn kein fach für den Namen gefunden wird
     */
    Subject findSubjectBySubjectName(String subjectName) throws SubjectNotFoundException;

    /**
     * Gibt ein Fach nach Name zurück
     *
     * @param acronym das Kürzel des gesuchten Fachs
     * @return das gesuchte fach
     * @throws SubjectNotFoundException wenn kein fach für das kürzel gefunden wird
     */
    Subject findSubjectByAcronym(String acronym) throws SubjectNotFoundException;

    /**
     * Gibt ein Fach nach Id zurück
     *
     * @param id ist die Id des Faches
     * @return das gesuchte Fach
     * @throws SubjectNotFoundException wenn kein fach für das kürzel gefunden wird
     */
    Subject findSubjectById(long id) throws SubjectNotFoundException;

    /**
     * Gibt ein Fach nach Name zurück
     *
     * @param prof der prof der ein Fach leitet
     * @return die gesuchten Fächer oder leere Liste
     */
    List<Subject> findSubjectsByProf(User prof);

    /**
     * Gibt ein Fach nach Name zurück
     *
     * @param minimumSemester das mindest Semester für ein Fach
     * @return die gesuchten Fächer oder leere Liste
     */
    List<Subject> findSubjectsByMinimumSemester(int minimumSemester);

    /**
     * Lässt ein Fach erstellen
     *
     * @param subjectName Gewünschter Name
     * @param acronym  das Kürzel
     * @param minimumSemester das mindest Semester
     * @param enrollmentKey  der Einschreibeschlüssel
     * @param prof der Professor, der das Fach leitet
     * @return Neu erstelltes Fach
     * @throws SubjectAlreadyExistingException Wenn es bereits ein Fach mit diesen Daten gibt
     */
    Subject createSubject(String subjectName, String acronym, int minimumSemester, Password enrollmentKey, String description, User prof) throws SubjectAlreadyExistingException;

    /**
     * Lässt einen Benutzer aktualisieren
     *
     * @param currentSubject Das zu aktualisierende Fach
     * @param newSubject das aktualisierte Fach
     * @return das aktualisierte Fach
     * @throws SubjectNotFoundException Wenn es das Fach nicht gibt
     */
    Subject updateSubject(Subject currentSubject, Subject newSubject) throws SubjectNotFoundException;

    /**
     * Lässt ein Fach löschen
     *
     * @param id id des zu löschenden Fachs
     * @throws SubjectNotFoundException Wenn es kein Fach mit der id gibt
     */
    void deleteSubject(long id) throws SubjectNotFoundException;

}
