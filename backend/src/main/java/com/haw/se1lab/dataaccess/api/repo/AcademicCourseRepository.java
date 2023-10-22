package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface AcademicCourseRepository extends JpaRepository<AcademicCourse, Long> {

    /**
     * Returns the {@link AcademicCourse} entity with the given id.
     *
     * @param id is the id of the Academic course
     * @return an {@link Optional} containing the found Academic course
     */
    @Query("select a from AcademicCourse a where a.id = :id")
    Optional<AcademicCourse> findById(@Param("id") long id);

    /**
     * Returns the {@link AcademicCourse} entity with the given title.
     *
     * @param name is the title of the Academic course
     * @return an {@link Optional} containing the found Academic course
     */
    @Query("select a from AcademicCourse a where a.name = :name")
    Optional<AcademicCourse> findByName(@Param("name") String name);


    /**
     * Deletes the {@link AcademicCourse} entity with the given id
     *
     * @param id is the id of the Academic course
     */
    @Transactional
    void deleteById(long id);

}
