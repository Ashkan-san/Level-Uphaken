package com.haw.se1lab.common.api.datatype;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@SuppressWarnings("JpaAttributeMemberSignatureInspection")
public class TaskId implements Serializable {

    private static final String TASK_ID_PATTERN = "(\\w{2})(\\d{1})(\\d{2})";


    /* ---- Member Fields ---- */

    private String taskId;

    public TaskId(String taskId) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile(TASK_ID_PATTERN);
        Matcher matcher = pattern.matcher(taskId);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid TaskId: " + taskId);
        }

        this.taskId = taskId;
    }

    public TaskId(String subjectAcronym, String subjectNumber, String taskNumber) {
        this.taskId = subjectAcronym + subjectNumber + taskNumber;
    }

    public TaskId() {}

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        Pattern pattern = Pattern.compile(TASK_ID_PATTERN);
        Matcher matcher = pattern.matcher(taskId);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid TaskId: " + taskId);
        }

        this.taskId = taskId;
    }

    @JsonIgnore
    public String getSubjectAcronym() {
        Pattern pattern = Pattern.compile(TASK_ID_PATTERN);
        Matcher matcher = pattern.matcher(this.taskId);
        return matcher.group(1);
    }

    @JsonIgnore
    public String getSubjectNumber() {
        Pattern pattern = Pattern.compile(TASK_ID_PATTERN);
        Matcher matcher = pattern.matcher(this.taskId);
        return matcher.group(2);
    }

    @JsonIgnore
    public String getTaskNumber() {
        Pattern pattern = Pattern.compile(TASK_ID_PATTERN);
        Matcher matcher = pattern.matcher(this.taskId);
        return matcher.group(3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskId taskId1 = (TaskId) o;
        return Objects.equals(taskId, taskId1.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

}

