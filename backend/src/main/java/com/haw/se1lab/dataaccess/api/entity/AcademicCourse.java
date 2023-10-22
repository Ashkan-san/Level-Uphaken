package com.haw.se1lab.dataaccess.api.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Studiengang für Benutzer
 *
 * @author Kjell May
 */
@Entity
public class AcademicCourse {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String name;

    @Fetch(FetchMode.SELECT)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_course_id")
    private final List<User> users = new ArrayList<>();

    public AcademicCourse(String name) {
        this.name = name;
    }

    public AcademicCourse() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            user.setAcademicCourse(this);
        }
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setAcademicCourse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcademicCourse that = (AcademicCourse) o;
        return getId() == that.getId() && getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
