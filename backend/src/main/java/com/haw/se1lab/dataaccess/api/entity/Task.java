package com.haw.se1lab.dataaccess.api.entity;


import com.fasterxml.jackson.annotation.*;
import com.haw.se1lab.common.api.datatype.TaskId;
import com.haw.se1lab.common.api.datatype.TaskType;
import com.haw.se1lab.common.api.exception.UserAlreadyInTaskException;
import com.haw.se1lab.common.api.exception.UserNotFoundException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Repräsentiert eine Aufgabe in der App.
 *
 * @author Isabell Schettler
 *
 */
@Entity
public class Task {

    /* ---- Member Fields ---- */

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @Column(unique = true)
    @JsonUnwrapped
    private TaskId taskId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    //@Column(unique = true)
    @NotNull
    private String title;

    @NotNull
    private int rewardXP;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskType type;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "tasks" })
    private Memo memo;

    // All Users who finished this Task
    @ManyToMany(mappedBy = "finishedTasks", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> finishedUsers = new ArrayList<>();

    /* ---- Constructors ---- */

    public Task(){}

    public Task(Memo memo, String title, TaskId taskId, int rewardXP, TaskType type, String content){
        this.memo = memo;
        this.rewardXP = rewardXP;
        this.taskId = taskId;
        this.title = title;
        this.type = type;
        this.content = content;
        memo.addTask(this);
    }

    /* ---- Getters/Setters ---- */

    public boolean isChecked(User user) {
        return finishedUsers.contains(user);
    }

    public Long getId() {
        return id;
    }

    public TaskId getTaskId() {
        return taskId;
    }

    public void setTaskId(TaskId id) {
        this.taskId = id;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type){
        this.type = type;
    }

    public int getRewardXP(){
        return rewardXP;
    }

    public void setRewardXP(int rewardXP){
        this.rewardXP = rewardXP;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Memo getMemo() {
        return memo;
    }

    public void setMemo(Memo memo) {
        this.memo = memo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<User> getFinishedUsers() {
        return finishedUsers;
    }

    /* ---- Custom Methods ---- */

    public void addUser(User user) throws UserAlreadyInTaskException {
        //prevent loops
        if(finishedUsers.contains(user)) throw new UserAlreadyInTaskException(user.getEmail());

        finishedUsers.add(user);
    }

    public void removeUser(User user) throws UserNotFoundException {
        if(!finishedUsers.contains(user)) throw new UserNotFoundException(user.getEmail());

        finishedUsers.remove(user);
    }

    /* ---- Overridden Methods ---- */

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
