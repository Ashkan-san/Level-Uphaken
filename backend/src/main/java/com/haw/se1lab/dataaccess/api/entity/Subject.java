package com.haw.se1lab.dataaccess.api.entity;

import com.fasterxml.jackson.annotation.*;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.InsufficientPermissionsException;
import com.haw.se1lab.common.api.exception.UserAlreadyInSubjectException;
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
 * Repräsentiert ein Fach in der App.
 *
 * @author Isabell Schettler
 *
 */
@Entity
public class Subject {

    /* ---- Member Fields ---- */

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    @NotNull
    private String subjectName;

    @Column(unique = true)
    @NotNull
    private String acronym;

    @NotNull
    private int minimumSemester;

    @NotNull
    @JsonUnwrapped
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Password enrollmentKey;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @Fetch(FetchMode.SELECT)
    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    @JsonIgnoreProperties({ "subject" })
    private final List<Memo> memos = new ArrayList<>();

    @Fetch(FetchMode.SELECT)
    @ManyToMany(mappedBy = "subjects")
    @JsonIgnoreProperties({ "subjects", "profSubjects", "tasks", "finishedTasks" })
    private final List<User> users = new ArrayList<>();

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @NotNull
    @JsonIgnoreProperties({ "subjects", "profSubjects", "tasks", "finishedTasks" })
    private User prof;

    /* ---- Constructors ---- */

    public Subject(){}

    public Subject(String subjectName, String acronym, int minimumSemester, Password enrollmentKey, String description, User prof){
        this.subjectName = subjectName;
        this.acronym = acronym;
        this.minimumSemester = minimumSemester;
        this.enrollmentKey = enrollmentKey;
        this.description = description;
        try {
            setProf(prof);
        } catch (InsufficientPermissionsException e) {
            e.printStackTrace();
        }
    }

    /* ---- Getters/Setters ---- */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public int getMinimumSemester() {
        return minimumSemester;
    }

    public void setMinimumSemester(int mindestSemester) {
        this.minimumSemester = mindestSemester;
    }

    public Password getEnrollmentKey() {
        return enrollmentKey;
    }

    public void setEnrollmentKey(Password enrollmentKey) {
        this.enrollmentKey = enrollmentKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getProf() {
        return prof;
    }

    public void setProf(User prof) throws InsufficientPermissionsException {
        if (!prof.getRole().equals(RoleType.PROFESSOR)) throw new InsufficientPermissionsException(prof.getRole());
        this.prof = prof;
        prof.addProfSubject(this);
    }

    public List<Memo> getMemos() {
        return memos;
    }

    public List<User> getUsers() {
        return users;
    }

    /* ---- Custom Methods ---- */

    // TODO Exceptions
    public void addMemo(Memo memo) {
        if (!memos.contains(memo)) memos.add(memo);
    }

    public void removeMemo(Memo memo) {
        memos.remove(memo);
    }

    public void addUser(User user) throws UserAlreadyInSubjectException {
        if (users.contains(user)) throw new UserAlreadyInSubjectException(user.getEmail());
        users.add(user);
    }

    public void removeUser(User user) {
        if (!users.contains(user)) return;
        users.remove(user);
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
        Subject subject = (Subject) o;
        return id == subject.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
