package com.haw.se1lab.logic.impl.usecase;

import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.InsufficientPermissionsException;
import com.haw.se1lab.common.api.exception.SubjectAlreadyExistingException;
import com.haw.se1lab.common.api.exception.SubjectNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.dataaccess.api.repo.SubjectRepository;
import com.haw.se1lab.dataaccess.api.repo.UserRepository;
import com.haw.se1lab.logic.api.usecase.SubjectUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * UseCase Implementierung für Fach
 *
 * @author Isabell Schettler
 */
@Service
public class SubjectUseCaseImpl implements SubjectUseCase {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Gibt alle Fächer zurück
     *
     * @return List<Subject> Eine Liste der Fächer
     */
    @Override
    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    /**
     * Gibt ein Fache mit dem gegebenem Namen zurück
     *
     * @param subjectName der Name des Fachs
     * @return gesuchtes fach
     * @throws SubjectNotFoundException wenn kein Fach für den gegebenen Namen gefunden wird
     */
    @Override
    public Subject findSubjectBySubjectName(String subjectName) throws SubjectNotFoundException {
        return subjectRepository.findBySubjectName(subjectName).orElseThrow(() -> new SubjectNotFoundException(subjectName));
    }

    /**
     * Gibt ein Fache mit dem gegebenem Kürzel zurück
     *
     * @param acronym das Kürzel des Fachs
     * @return gesuchtes fach
     * @throws SubjectNotFoundException wenn kein Fach für das gegebene Kürzel gefunden wird
     */
    @Override
    public Subject findSubjectByAcronym(String acronym) throws SubjectNotFoundException {
        return subjectRepository.findByAcronym(acronym).orElseThrow(() -> new SubjectNotFoundException(acronym));
    }

    @Override
    public Subject findSubjectById(long id) throws SubjectNotFoundException {
        return subjectRepository.findById(id).orElseThrow(() -> new SubjectNotFoundException(id));
    }

    /**
     * Gibt eine Liste mit Fächern mit dem gegebenem Prof zurück
     *
     * @param prof der Prof der Fächer leitet
     * @return Liste aller Fächer die von dem angegebenem Prof geleitet werden
     */
    @Override
    public List<Subject> findSubjectsByProf(User prof) {
        return subjectRepository.findByProf(prof);
    }

    /**
     * Gibt ein Liste aller Fächer mit dem gegebenem mindest Semester zurück
     *
     * @param minimumSemester das mindest Semester der Fächer
     * @return Liste aller Fächer die das angegebene mindest Semester haben
     */
    @Override
    public List<Subject> findSubjectsByMinimumSemester(int minimumSemester) {
        return subjectRepository.findByMinimumSemester(minimumSemester);
    }

    /**
     * Lässt ein Fach erstellen
     *
     * @param subjectName     Gewünschter Name
     * @param acronym         das Kürzel
     * @param minimumSemester das mindest Semester
     * @param enrollmentKey   der Einschreibeschlüssel
     * @param prof            der Professor, der das Fach leitet
     * @return Neu erstelltes Fach
     * @throws SubjectAlreadyExistingException Wenn es bereits ein Fach mit diesen Daten gibt
     */
    @Override
    public Subject createSubject(String subjectName, String acronym, int minimumSemester, Password enrollmentKey, String description, User prof) throws SubjectAlreadyExistingException {
        Assert.notNull(subjectName, "Fachname darf nicht null sein");
        Assert.notNull(acronym, "Kuerzel darf nicht null sein");
        Assert.notNull(enrollmentKey, "Einschreibeschlüssel darf nicht null sein");
        Assert.notNull(description, "Einschreibeschlüssel darf nicht null sein");
        Assert.notNull(prof, "Professor darf nicht null sein");

        if (subjectRepository.findBySubjectName(subjectName).isPresent())
            throw new SubjectAlreadyExistingException(subjectName);
        if (subjectRepository.findByAcronym(acronym).isPresent()) throw new SubjectAlreadyExistingException(acronym);


        Subject subject = new Subject(subjectName, acronym, minimumSemester, enrollmentKey, description, prof);

        return subjectRepository.save(subject);
    }

    /**
     * Lässt ein Fach aktualisieren
     *
     * @param currentSubject Das zu aktualisierende Fach
     * @return das aktualisierte Fach
     * @throws SubjectNotFoundException Wenn es das Fach nicht gibt
     */
    @Override
    public Subject updateSubject(Subject currentSubject, Subject newSubject) throws SubjectNotFoundException {
        Assert.notNull(currentSubject, "altes Fach darf nicht null sein");
        Assert.notNull(newSubject, "neues Fach darf nicht null sein");

        if (newSubject.getSubjectName() != null) {
            currentSubject.setSubjectName(newSubject.getSubjectName());
        }

        if (newSubject.getAcronym() != null) {
            currentSubject.setAcronym(newSubject.getAcronym());
        }

        if (newSubject.getMinimumSemester() != 0) {
            currentSubject.setMinimumSemester(newSubject.getMinimumSemester());
        }

        if (newSubject.getEnrollmentKey() != null && newSubject.getEnrollmentKey().getPassword() != null) {
            Password newPassword;
            try {
                newPassword = new Password(newSubject.getEnrollmentKey().getPassword());
            } catch (Exception exception) {
                throw new IllegalArgumentException("Ungültiger neuer Einschreibeschlüssel!");
            }
            currentSubject.setEnrollmentKey(newPassword);
        }

        if (newSubject.getProf() != null) {
            User newProf;
            try {
                newProf = userRepository.findByEmail(newSubject.getProf().getEmail()).get();
            } catch (Exception exception) {
                throw new IllegalArgumentException("Ungültiger neuer Professor!");
            }
            try {
                currentSubject.setProf(newProf);
            } catch (InsufficientPermissionsException e) {
                throw new IllegalArgumentException("Neuer User muss ein Professor sein");
            }
        }


        return subjectRepository.save(currentSubject);
    }

    /**
     * Lässt ein Fach löschen
     *
     * @param id id des zu löschenden Fachs
     * @throws SubjectNotFoundException Wenn es kein Fach mit der Id gibt
     */
    @Override
    public void deleteSubject(long id) throws SubjectNotFoundException {
        Subject subject = findSubjectById(id);

        subjectRepository.delete(subject);

    }
}
