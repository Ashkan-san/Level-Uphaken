package com.haw.se1lab.common.api.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents the exception when a memo could not be found by the given search
 * criteria.
 *
 * @author Isabell Schettler
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemoNotFoundException extends Exception {

    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String MEMO_WITH_ID_NOT_FOUND_MESSAGE = "Could not find memo with ID %d.";

    public static final String MEMO_WITH_TITLE_NOT_FOUND_MESSAGE = "Could not find memo with title %s.";

    /* ---- Member Fields ---- */

    private final Integer id;

    private final String title;

    /* ---- Constructors ---- */

    public MemoNotFoundException(Integer id) {
        super(String.format(MEMO_WITH_ID_NOT_FOUND_MESSAGE, id));
        this.id = id;
        this.title = null;
    }

    public MemoNotFoundException(String title) {
        super(String.format(MEMO_WITH_TITLE_NOT_FOUND_MESSAGE, title));
        this.id = null;
        this.title = title;
    }

    /* ---- Getters/Setters ---- */

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /* ---- Custom Methods ---- */

}
