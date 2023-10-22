package com.haw.se1lab.dataaccess.api.entity;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Repräsentiert eine Memo in der App.
 *
 * @author Isabell Schettler
 *
 */
@Entity
public class Memo {

    /* ---- Member Fields ---- */

    @Id
    @GeneratedValue
    private int id;

    //@Column(unique = true)
    @NotNull
    private String title;

    @NotNull
    private int rewardXP;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "memos" })
    private Subject subject;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @Fetch(FetchMode.SELECT)
    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    @JsonIgnoreProperties({ "memo" })
    private final List<Task> tasks = new ArrayList<>();

    /* ---- Constructors ---- */

    public Memo(){}

    public Memo(Subject subject, String title, int rewardXP, String description){
        this.subject = subject;
        this.title = title;
        this.rewardXP = rewardXP;
        this.description = description;
        subject.addMemo(this);
    }

    /* ---- Getters/Setters ---- */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getProgress(User user) {
        return user.getFinishedTasks().stream().filter(task -> task.getMemo().equals(this)).count() / (double) tasks.size();
    }

    public Subject getSubject() {
        return subject;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    /* ---- Custom Methods ---- */

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    // TODO Throw Exception
    public void addTask(Task task) {
        if (!tasks.contains(task)) tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
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
        Memo memo = (Memo) o;
        return id == memo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
