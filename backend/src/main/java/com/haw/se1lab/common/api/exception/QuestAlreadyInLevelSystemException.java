package com.haw.se1lab.common.api.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class QuestAlreadyInLevelSystemException extends Exception {
    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String QUEST_WITH_TITLE_ALREADY_EXISTS = "Quest with title %d already exists.";

    /* ---- Member Fields ---- */

    private final String title;

    /* ---- Constructors ---- */

    public QuestAlreadyInLevelSystemException (String title) {
        super(String.format(QUEST_WITH_TITLE_ALREADY_EXISTS, title));
        this.title = title;
    }

    /* ---- Getters/Setters ---- */

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
