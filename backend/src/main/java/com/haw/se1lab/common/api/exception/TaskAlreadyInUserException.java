package com.haw.se1lab.common.api.exception;

import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.TaskId;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TaskAlreadyInUserException extends Exception {
    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String TASK_WITH_ID_ALREADY_EXISTS = "Task mit Id %s bereits in Liste enthalten.";

    /* ---- Member Fields ---- */

    private final TaskId id;

    /* ---- Constructors ---- */

    public TaskAlreadyInUserException(TaskId id) {
        super(String.format(TASK_WITH_ID_ALREADY_EXISTS, id.toString()));
        this.id = id;
    }

    /* ---- Getters/Setters ---- */

    public TaskId getId() {
        return id;
    }

    /* ---- Overridden Methods ---- */

    // overridden to improve object representation in logging and debugging
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
