package com.haw.se1lab.common.api.exception;

import com.haw.se1lab.dataaccess.api.entity.Task;
import com.haw.se1lab.dataaccess.api.entity.User;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TaskCheckingException extends Exception {

    /* ---- Class Fields ---- */

    private static final long serialVersionUID = 1L;

    public static final String TASK_WITH_ID_ALREADY_CHECKED = "Task with id %s already checked.";
    public static final String TASK_WITH_ID_NOT_YET_CHECKED = "Can't uncheck, Task with id %s is not checked.";

    /* ---- Member Fields ---- */

    private final String title;

    /* ---- Constructors ---- */

    public TaskCheckingException(Task task, User user) {
        super(String.format(task.isChecked(user) ? TASK_WITH_ID_ALREADY_CHECKED : TASK_WITH_ID_NOT_YET_CHECKED, task.getTitle()));
        this.title = task.getTitle();
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
