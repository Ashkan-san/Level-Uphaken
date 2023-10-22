package com.haw.se1lab.facade.api;

import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Facade Interface für den Benutzer
 *
 * @author Kjell May
 */
public interface UserFacade {

    User logIn(@RequestBody User user) throws UserNotFoundException;

    User getCurrentUser() throws UserNotFoundException;

    List<User> getUsers();

    User getUser(String email) throws UserNotFoundException;

    User createUser(User user) throws UserAlreadyExistingException;

    User updateUser(User user) throws UserNotFoundException;

    void deleteUser(EmailType email) throws UserNotFoundException;

    User enrollInSubject(String subjectName, String enrollmentKey) throws UserNotFoundException, SubjectNotFoundException, InvalidEnrollmentKeyException, InsufficientPermissionsException, UserAlreadyInSubjectException;
}
