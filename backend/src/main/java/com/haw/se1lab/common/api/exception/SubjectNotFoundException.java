package com.haw.se1lab.common.api.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents the exception when a subject could not be found by the given search
 * criteria.
 *
 * @author Isabell Schettler
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubjectNotFoundException extends Exception {

    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String SUBJECT_WITH_ID_NOT_FOUND_MESSAGE = "Could not find subject with ID %d.";

    public static final String SUBJECT_WITH_TITLE_NOT_FOUND_MESSAGE = "Could not find subject with Title %s.";

    /* ---- Member Fields ---- */

    private final Long id;

    private final String name;

    /* ---- Constructors ---- */

    public SubjectNotFoundException(Long id) {
        super(String.format(SUBJECT_WITH_ID_NOT_FOUND_MESSAGE, id));
        this.id = id;
        this.name = null;
    }

    public SubjectNotFoundException(String title) {
        super(String.format(SUBJECT_WITH_TITLE_NOT_FOUND_MESSAGE, title));
        this.id = null;
        this.name = title;
    }

    /* ---- Getters/Setters ---- */

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /* ---- Custom Methods ---- */

    }
