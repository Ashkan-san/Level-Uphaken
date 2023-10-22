package com.haw.se1lab.facade.impl;

import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.facade.api.UserFacade;
import com.haw.se1lab.logic.api.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Facade Implementierung für Benutzer
 *
 * @author Kjell May
 */
@RestController
@RequestMapping(path = "/users")
public class UserFacadeImpl implements UserFacade {

    @Autowired
    private UserUseCase userUseCase;

    @PostMapping("/login")
    @Override
    public User logIn(@RequestBody User user) throws UserNotFoundException {
        return userUseCase.logIn(user.getEmail(), user.getPassword());
    }

    @GetMapping("/info")
    @Override
    public User getCurrentUser() throws UserNotFoundException {
        return userUseCase.getCurrentUser();
    }

    @GetMapping
    @Override
    public List<User> getUsers() {
        return userUseCase.findAllUsers();
    }

    @GetMapping(value = "/{email:^[a-zA-Z0-9_+-.]+@haw-hamburg\\.de$}")
    @Override
    public User getUser(@PathVariable(value = "email") String email) throws UserNotFoundException {
        return userUseCase.findUserByEmail(new EmailType(email));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Override
    public User createUser(@RequestBody User user) throws UserAlreadyExistingException {
        Assert.notNull(user, "Benutzer darf nicht null sein");

        return userUseCase.createUser(user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getaKennung(), user.getPassword(), user.getAcademicCourse());
    }

    @PutMapping
    @Transactional
    @Override
    public User updateUser(@RequestBody User newUser) throws UserNotFoundException {
        User currentUser = userUseCase.getCurrentUser();
        return userUseCase.updateUser(currentUser, newUser);
    }

    @DeleteMapping("/{email:^[a-zA-Z0-9_+-.]+@haw-hamburg\\.de$}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Override
    public void deleteUser(@PathVariable("email") EmailType email) throws UserNotFoundException {
        userUseCase.deleteUser(email);
    }

    @PutMapping(path = "/enroll/{acronym}/{enrollmentKey}")
    @Transactional
    @Override
    public User enrollInSubject(@PathVariable("acronym") String acronym, @PathVariable("enrollmentKey") String enrollmentKey)
            throws UserNotFoundException, SubjectNotFoundException, InvalidEnrollmentKeyException, InsufficientPermissionsException, UserAlreadyInSubjectException {
        Assert.notNull(acronym, "Akronym darf nicht null sein");
        Assert.notNull(enrollmentKey, "Einschreibeschlüssel darf nicht null sein");

        return userUseCase.enrollInSubject(getCurrentUser(), acronym, new Password(enrollmentKey));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found.")
    private void handleUserNotFoundException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler(UserAlreadyExistingException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User already exists.")
    private void handleUserAlreadyExistingException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private void handleIllegalArgumentException(IllegalArgumentException exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

}
