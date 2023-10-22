package com.haw.se1lab.common.api.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents the exception when a memo could not be created because it
 * already exists.
 *
 * @author Isabell Schettler
 *
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemoAlreadyExistingException extends Exception {

    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String MEMO_WITH_TITLE_ALREADY_EXISTS = "Memo with Title %s already exists.";

    /* ---- Member Fields ---- */

    private final String title;

    /* ---- Constructors ---- */

    public MemoAlreadyExistingException(String title) {
        super(String.format(MEMO_WITH_TITLE_ALREADY_EXISTS, title));
        this.title = title;
    }

    /* ---- Getters/Setters ---- */

    public String getTitle() {
        return this.title;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /* ---- Custom Methods ---- */

}
