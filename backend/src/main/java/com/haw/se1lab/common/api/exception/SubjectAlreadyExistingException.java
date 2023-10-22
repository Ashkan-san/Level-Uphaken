package com.haw.se1lab.common.api.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents the exception when a subject could not be created because it
 * already exists.
 *
 * @author Isabell Schettler
 *
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubjectAlreadyExistingException extends Exception{


    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String SUBJECT_WITH_STRING_CRITERIA_ALREADY_EXISTS = "Subject with String criteria %s already exists.";

    /* ---- Member Fields ---- */

    private final String string;

    /* ---- Constructors ---- */

    public SubjectAlreadyExistingException(String string) {
        super(String.format(SUBJECT_WITH_STRING_CRITERIA_ALREADY_EXISTS, string));
        this.string = string;
    }


    /* ---- Getters/Setters ---- */

    public String getString() {
        return this.string;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /* ---- Custom Methods ---- */

}
