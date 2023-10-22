package com.haw.se1lab.dataaccess.api.entity;

import com.fasterxml.jackson.annotation.*;
import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.InsufficientPermissionsException;
import com.haw.se1lab.common.api.exception.TaskAlreadyInUserException;
import com.haw.se1lab.common.api.exception.TaskNotFoundException;
import com.haw.se1lab.common.api.exception.UserAlreadyInSubjectException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasse für Entität Benutzer
 *
 * @author Kjell May
 */
@Entity
public class User {

    @Id
    @GeneratedValue // lets Hibernate take care of assigning an ID to new database entries
    private Long id;

    @Embedded
    @NotNull
    @Column(unique = true)
    @JsonUnwrapped
    private EmailType email;

    private String firstName;

    private String lastName;

    private RoleType role;

    @NotNull
    @Column(unique = true)
    @JsonUnwrapped
    private AKennungType aKennung;

    @JsonUnwrapped
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Password password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate lastLogin;

    // TODO eigener Datentyp ?
    @Column(unique = true, length = 300)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String session;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties({ "users" })
    private AcademicCourse academicCourse;

    // All Tasks the Users finished
    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.LAZY)
    @JoinTable(name = "user_finished_tasks", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "task_id"))
    @JsonIgnoreProperties({ "memo" })
    private final List<Task> finishedTasks = new ArrayList<>();

    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH },fetch = FetchType.LAZY)
    @JoinTable(name = "user_subjects", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @JsonIgnoreProperties({ "users" })
    private final List<Subject> subjects = new ArrayList<>();

    @Fetch(FetchMode.SELECT)
    @OneToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "prof_id")
    @JsonIgnoreProperties({ "users" })
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Subject> profSubjects = new ArrayList<>();

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "user" })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LevelSystem levelsystem;

    public User(EmailType email, String firstName, String lastName, RoleType role, AKennungType aKennung,
                Password password, AcademicCourse academicCourse, LevelSystem levelSystem) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.aKennung = aKennung;
        this.password = password;
        if (!role.equals(RoleType.PROFESSOR)) setAcademicCourse(academicCourse);
        if (!role.equals(RoleType.PROFESSOR)) setLevelsystem(levelSystem);
    }

    public User() {

    }

    //Getter für alle Attribute

    public Long getId() {
        return id;
    }

    public EmailType getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public RoleType getRole() {
        return role;
    }

    public AKennungType getaKennung() {
        return aKennung;
    }

    public Password getPassword() {
        return password;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public String getSession() {
        return session;
    }

    public AcademicCourse getAcademicCourse() {
        return academicCourse;
    }

    public List<Task> getFinishedTasks() {
        return finishedTasks;
    }

    @JsonIgnore
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Subject subject : getSubjects()) {
            for (Memo memo : subject.getMemos()) {
                tasks.addAll(memo.getTasks());
            }
        }
        return tasks;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public List<Subject> getProfSubjects() {
        return profSubjects;
    }

    public LevelSystem getLevelsystem() {
        return levelsystem;
    }

    //Setter für ausgewählte Attribute

    public void setEmail(EmailType email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public void setaKennung(AKennungType aKennung) {
        this.aKennung = aKennung;
    }

    public void setAcademicCourse(AcademicCourse academicCourse) {
        if (academicCourse != null && getAcademicCourse() == null) {
            this.academicCourse = academicCourse;
            academicCourse.addUser(this);
        }
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setSession(String session) {
        this.session = session;
    }

    /* ---- Custom Methods ---- */

    public void addSubject(Subject subject) throws UserAlreadyInSubjectException {
        if (subjects.contains(subject)) throw new UserAlreadyInSubjectException(getEmail());
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        if (!subjects.contains(subject)) return;
        subjects.remove(subject);
    }

    public void addProfSubject(Subject subject) throws InsufficientPermissionsException {
        if (!role.equals(RoleType.PROFESSOR)) throw new InsufficientPermissionsException(role);
        if (profSubjects.contains(subject)) return;
         profSubjects.add(subject);
         subject.setProf(this);
    }

    public void removeProfSubjects(Subject subject) {
        profSubjects.remove(subject);
    }

    public void addTask(Task task) throws TaskAlreadyInUserException {
        if (finishedTasks.contains(task)) throw new TaskAlreadyInUserException(task.getTaskId());

        finishedTasks.add(task);
    }

    public void removeTask(Task task) throws TaskNotFoundException {
        if (!finishedTasks.contains(task)) throw new TaskNotFoundException(task.getTaskId());

        finishedTasks.remove(task);
    }

    public void setLevelsystem(LevelSystem levelSystem) {
        if (levelSystem != null && getLevelsystem() == null) {
            this.levelsystem = levelSystem;
            levelSystem.setUser(this);
        }
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(aKennung, user.aKennung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, aKennung);
    }
}
