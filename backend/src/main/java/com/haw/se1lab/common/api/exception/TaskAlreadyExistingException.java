package com.haw.se1lab.common.api.exception;

import com.haw.se1lab.common.api.datatype.TaskId;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents the exception when a task could not be created because it
 * already exists.
 *
 * @author Isabell Schettler
 *
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskAlreadyExistingException extends Exception {

    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String TASK_WITH_TITLE_ALREADY_EXISTS = "Task with Title %s already exists.";
    public static final String TASK_WITH_TASK_ID_ALREADY_EXISTS = "Task with id %b already exists.";

    /* ---- Member Fields ---- */

    private final String title;
    private TaskId id;

    /* ---- Constructors ---- */

    public TaskAlreadyExistingException(String title) {
        super(String.format(TASK_WITH_TITLE_ALREADY_EXISTS, title));
        this.title = title;
        this.id = null;
    }

    public TaskAlreadyExistingException(TaskId id) {
        super(String.format(TASK_WITH_TASK_ID_ALREADY_EXISTS, id));
        this.title = null;
        this.id = id;
    }


    /* ---- Getters/Setters ---- */

    public String getTitle() {
        return this.title;
    }

    public TaskId getId() {
        return id;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /* ---- Custom Methods ---- */

}
