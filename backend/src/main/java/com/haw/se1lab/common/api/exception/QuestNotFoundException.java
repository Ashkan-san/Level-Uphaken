package com.haw.se1lab.common.api.exception;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents the exception when a quest could not be found by the given search criteria.
 *
 * @author Kathleen Neitzel
 */
public class QuestNotFoundException extends Exception{
    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String QUEST_WITH_ID_NOT_FOUND_MESSAGE = "Could not find quest with ID %d.";

    public static final String QUEST_WITH_TITLE_NOT_FOUND_MESSAGE = "Could not find quest with title %s.";

    /* ---- Member Fields ---- */

    private final long id;

    private final String title;

    /* ---- Constructors ---- */

    public QuestNotFoundException(long id) {
        super(String.format(QUEST_WITH_ID_NOT_FOUND_MESSAGE, id));
        this.id = id;
        this.title = null;
    }

    public QuestNotFoundException(String title) {
        super(String.format(QUEST_WITH_TITLE_NOT_FOUND_MESSAGE, title));
        this.id = 0;
        this.title = title;
    }

    /* ---- Getters/Setters ---- */

    public long getId() {
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
