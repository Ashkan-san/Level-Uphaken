package com.haw.se1lab.facade.api;

import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.exception.SubjectAlreadyExistingException;
import com.haw.se1lab.common.api.exception.SubjectNotFoundException;
import com.haw.se1lab.common.api.exception.UserNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.User;


import java.util.List;

/**
 * Represents a facade for the system operations for subjects which are
 * available from outside the system.
 *
 * @author Isabell Schettler
 */

public interface SubjectFacade {

    /**
     * Returns all available subjects.
     *
     * @return the found subject or an empty list if none were found
     */
    List<Subject> getSubjects();

    /**
     * Returns the subject with the given ID.
     *
     * @param id the subjects's technical ID
     * @return the found subject
     * @throws SubjectNotFoundException in case the Subject could not be found
     */
    Subject getSubject(long id) throws SubjectNotFoundException;

    /**
     * Returns the subjects with the given user.
     *
     * @param email is the user who is enrolled in Subjects
     * @return the found subjects
     */
    List<Subject> getSubjectsByUserEmail(EmailType email) throws UserNotFoundException;

    /**
     * Creates a subject with the given data.
     *
     * @param subject the subject to be created; must not be <code>null</code>
     * @return the created subject
     * @throws SubjectAlreadyExistingException in case a subject with the given data already exists
     */
    Subject createSubject(Subject subject) throws SubjectAlreadyExistingException;

    /**
     * Updates a subject with the given data.
     *
     * @param id the id of the subject to be updated; must not be <code>null</code>
     * @param newSubject the updated subject ; must not be <code>null</code>
     * @return the updated subject
     * @throws SubjectNotFoundException in case the subject could not be found
     */
    Subject updateSubject(long id, Subject newSubject) throws SubjectNotFoundException;

    /**
     * Deletes the subject with the given ID.
     *
     * @param id the subject's technical ID
     * @throws SubjectNotFoundException in case the subject could not be found
     */
    void deleteSubject(long id) throws SubjectNotFoundException;

}
