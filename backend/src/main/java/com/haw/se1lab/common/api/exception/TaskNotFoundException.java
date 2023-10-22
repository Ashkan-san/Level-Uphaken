package com.haw.se1lab.common.api.exception;

import com.haw.se1lab.common.api.datatype.TaskId;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents the exception when a task could not be found by the given search
 * criteria.
 *
 * @author Isabell Schettler
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends Exception {
    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String TASK_WITH_ID_NOT_FOUND_MESSAGE = "Could not find task with ID %s.";

    public static final String TASK_WITH_TITLE_NOT_FOUND_MESSAGE = "Could not find task with title %s.";

    /* ---- Member Fields ---- */

    private final TaskId id;

    private final String title;

    /* ---- Constructors ---- */

    public TaskNotFoundException(TaskId id) {
        super(String.format(TASK_WITH_ID_NOT_FOUND_MESSAGE, id.toString()));
        this.id = id;
        this.title = null;
    }

    public TaskNotFoundException(String title) {
        super(String.format(TASK_WITH_TITLE_NOT_FOUND_MESSAGE, title));
        this.id = null;
        this.title = title;
    }

    /* ---- Getters/Setters ---- */

    public TaskId getId() {
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
