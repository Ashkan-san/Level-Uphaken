package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.dataaccess.api.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Represents a repository for the management of {@link Quest} entities in a
 * database.
 *
 * @author Kathleen Neitzel
 */

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {

        /* ---- Custom Query Methods ---- */

        // For documentation about how query methods work and how to declare them see "Spring Data - Query Methods":
        // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods

        /**
         * Returns the {@link Quest} entity with the given id.
         *
         * @param id the id
         * @return an {@link Optional} containing the found course
         */
        // custom query method using JPQL query string
        @Query("select q from Quest q where q.id = :id")
        Optional<Quest> findById(@Param("id") long id);

        /**
         * Returns the {@link Quest} entities with the given title.
         *
         * @param title the name to search for
         * @return the found quest
         */
        // custom query method using JPQL query string
        @Query("select q from Quest q where q.title = :title")
        Optional<Quest> findByTitle(@Param("title") String title);

        /**
         * Deletes the {@link Quest} entity with the given id.
         *
         * @param id the id
         */
        // custom query method with query automatically derived from method name (e.g. "<action>By<attribute name>")
        // equivalent SQL query: delete from QUEST where ID = [id.code]
        @Transactional
        // causes the method to be executed in a database transaction (required for write operations)
        void deleteById(long id);

        /**
         * Deletes the {@link Quest} entity with the given title.
         *
         * @param title the title
         */
        // custom query method with query automatically derived from method name (e.g. "<action>By<attribute name>")
        // equivalent SQL query: delete from QUEST where TITLE = [title]
        @Transactional
        // causes the method to be executed in a database transaction (required for write operations)
        void deleteByTitle(String title);

    }
