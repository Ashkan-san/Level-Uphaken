package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.dataaccess.api.entity.Memo;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.Task;
import com.haw.se1lab.dataaccess.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Represents a repository for the management of {@link Memo} entities in a
 * database.
 *
 * @author Isabell Schettler
 */
@Repository
public interface MemoRepository extends JpaRepository<Memo, Integer> {

    /**
     * Returns the {@link Memo} entity with the given id.
     *
     * @param id is the id of the memo
     * @return an {@link Optional} containing the found memo
     */
    @Query("select m from Memo m where m.id = :id")
    Optional<Memo> findById(@Param("id") int id);

    /**
     * Returns the {@link Memo} entity with the given title.
     *
     * @param title is the title of the memo
     * @return an {@link Optional} containing the found memo
     */
    @Query("select m from Memo m where m.title = :title")
    Optional<Memo> findByTitle(@Param("title") String title);

    /**
     * Returns the {@link Memo} entities with the given subject.
     *
     * @param subject the subject to search for
     * @return the found memo
     */
    @Query("select m from Memo m where m.subject = :subject")
    List<Task> findBySubject(@Param("subject") Subject subject);

    /**
     * Löscht die {@link Memo} Entität mit dem gegebenen Titel
     *
     * @param title der Titel
     */
    @Transactional
    void deleteByTitle(String title);

    /**
     * Löscht die {@link Memo} Entität mit dem gegebenen Titel
     *
     * @param id die ID
     */
    @Transactional
    void deleteById(int id);
}
