package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.TaskId;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.Task;
import com.haw.se1lab.dataaccess.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Represents a repository for the management of {@link Subject} entities in a
 * database.
 *
 * @author Isabell Schettler
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    /**
     * Returns the {@link Subject} entity with the given subjectName.
     *
     * @param subjectName is the name of the subject
     * @return an {@link Subject} containing the found subject
     */
    Optional<Subject> findBySubjectName(@Param(("subjectName")) String subjectName);

    /**
     * Returns the {@link Subject} entity with the given acronym.
     *
     * @param acronym is the acronym of the subject
     * @return an {@link Optional} containing the found subject
     */
    Optional<Subject> findByAcronym(@Param(("acronym")) String acronym);

    /**
     * Returns the {@link Subject} entity with the given id.
     *
     * @param id is the id of the subject
     * @return an {@link Optional} containing the found subject
     */
    Optional<Subject> findById(@Param(("id")) long id);

    /**
     * Returns the {@link Subject} entity with the given prof.
     *
     * @param prof is the prof of the subject
     * @return the found subjects
     */
    List<Subject> findByProf(@Param(("prof")) User prof);

    /**
     * Returns the {@link Subject} entities with the given minimumSemester.
     *
     * @param minimumSemester the minimumSemester to search for
     * @return the found Subjects
     */
    List<Subject> findByMinimumSemester(@Param("minimumSemester") int minimumSemester);

    /**
     * Löscht die {@link Subject} Entität mit der passenden Id
     *
     * @param id die Id
     */
    @Transactional
    void deleteById(long id);


    //TODO queries?

}
