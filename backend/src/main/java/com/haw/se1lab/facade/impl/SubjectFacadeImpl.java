package com.haw.se1lab.facade.impl;

import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.exception.SubjectAlreadyExistingException;
import com.haw.se1lab.common.api.exception.SubjectNotFoundException;
import com.haw.se1lab.common.api.exception.UserAlreadyExistingException;
import com.haw.se1lab.common.api.exception.UserNotFoundException;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.facade.api.SubjectFacade;
import com.haw.se1lab.logic.api.usecase.SubjectUseCase;
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
 * Facade Implementierung für Fach
 *
 * @author Isabell Schettler
 */
@RestController
@RequestMapping(path = "/subjects")
public class SubjectFacadeImpl implements SubjectFacade {

    @Autowired
    private SubjectUseCase subjectUseCase;
    @Autowired
    private UserUseCase userUseCase;


    @GetMapping
    @Override
    public List<Subject> getSubjects() {
        return subjectUseCase.findAllSubjects();
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @Override
    public Subject getSubject(@PathVariable(value = "id")long id) throws SubjectNotFoundException {
        return subjectUseCase.findSubjectById(id);
    }


    @GetMapping(value = "/{email:^[a-zA-Z0-9_+-.]+@haw-hamburg\\.de$}")
    @Override
    public List<Subject> getSubjectsByUserEmail(@PathVariable(value = "email") EmailType email) throws UserNotFoundException {
        return userUseCase.findUserByEmail(email).getSubjects();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @Override
    public Subject createSubject(@RequestBody Subject subject) throws SubjectAlreadyExistingException {
        Assert.notNull(subject, "Fach darf nicht null sein");
        return subjectUseCase.createSubject(subject.getSubjectName(), subject.getAcronym(), subject.getMinimumSemester(), subject.getEnrollmentKey(), subject.getDescription() ,subject.getProf());
    }

    @PutMapping(value = "/{id:[\\d]+}")
    @Transactional
    @Override
    public Subject updateSubject(@PathVariable(value = "id") long id, @RequestBody Subject newSubject) throws SubjectNotFoundException {
        Subject currentSubject = subjectUseCase.findSubjectById(id);
        return subjectUseCase.updateSubject(currentSubject, newSubject);
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    @Override
    public void deleteSubject(@PathVariable(value = "id") long id) throws SubjectNotFoundException {
        subjectUseCase.deleteSubject(id);
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Fach konnte nicht gefunden werden.")
    private void handleSubjectNotFoundException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler(SubjectAlreadyExistingException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Fach existiert bereits")
    private void handleSubjectAlreadyExistingException() {
        // nothing to do -> just set the HTTP response status as defined in @ResponseStatus
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private void handleIllegalArgumentException(IllegalArgumentException exception, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

}
