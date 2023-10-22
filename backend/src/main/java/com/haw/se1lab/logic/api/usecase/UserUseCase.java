package com.haw.se1lab.logic.api.usecase;

import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Task;
import com.haw.se1lab.dataaccess.api.entity.User;

import java.util.List;

/**
 * Use Case Interface für Benutzer
 *
 * @author Kjell May
 */
public interface UserUseCase {

    /**
     * Benutzer kann sich anmelden
     *
     * @param email    Email des Benutzers
     * @param password Passwort des Benutzers
     * @return den Benutzer
     * @throws UserNotFoundException wenn es keinen Benutzer mit diesen Daten gibt
     */
    User logIn(EmailType email, Password password) throws UserNotFoundException;

    /**
     * Gibt aktuell angemeldeten User zurück
     */
    User getCurrentUser() throws UserNotFoundException;

    /**
     * Gibt alle Benutzer zurück
     *
     * @return List<User> Eine Liste der Benutzer
     */
    List<User> findAllUsers();

    /**
     * Gibt einen Benutzer nach Email zurück
     *
     * @param email Die Email des Benutzers
     * @return den gesuchten Benutzer
     * @throws UserNotFoundException wenn kein Benutzer mit dieser Mail gefunden wurde
     */
    User findUserByEmail(EmailType email) throws UserNotFoundException;

    /**
     * Lässt einen Benutzer erstellen
     *
     * @param email     Gewünschte Email
     * @param firstName den Vornamen
     * @param lastName  den Nachnamen
     * @param aKennung  die A-Kennung
     * @param password  Gewünschtes Passwort
     * @return Neu erstellten Benutzer
     * @throws UserAlreadyExistingException Wenn es bereits einen Benutzer mit diesen Daten gibt
     */
    User createUser(EmailType email, String firstName, String lastName, AKennungType aKennung,
                    Password password, AcademicCourse academicCourse) throws UserAlreadyExistingException;

    /**
     * Lässt einen Benutzer aktualisieren
     *
     * @param currentUser Der zu aktualisierende Benutzer
     * @param newUser Der Benutzer mit den neuen Daten
     * @return den aktualisierten Benutzer
     * @throws UserNotFoundException Wenn es den Benutzer nicht gibt
     */
    User updateUser(User currentUser, User newUser) throws UserNotFoundException;

    /**
     * Lässt einen Benutzer löschen
     *
     * @param email Email des zu löschenden Benutzers
     * @throws UserNotFoundException Wenn es keinen Benutzer mit der Email gibt
     */
    void deleteUser(EmailType email) throws UserNotFoundException;

    /**
     * Lässt in Fach einschreiben
     *
     * @param user        der Benutzer
     * @param acronym   Name des Fachs
     * @param enrollmentKey Einschreibeschlüssel des Fachs
     * @return den aktualisierten Benutzer
     * @throws UserNotFoundException         Wenn der Benutzer mit der Email nicht gefunden wurde
     * @throws SubjectNotFoundException      Wenn das Fach mit dem Namen nicht gefunden wurde
     * @throws InvalidEnrollmentKeyException Wenn der Einschreibeschlüsse ungültig war
     */
    User enrollInSubject(User user, String acronym, Password enrollmentKey) throws UserNotFoundException, SubjectNotFoundException, InvalidEnrollmentKeyException, UserAlreadyInSubjectException, InsufficientPermissionsException;

}
