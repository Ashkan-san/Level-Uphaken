package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Represents a repository for the management of {@link LevelSystem} entities in a
 * database.
 *
 * @author Kathleen Neitzel
 */

@Repository
public interface LevelSystemRepository extends JpaRepository<LevelSystem, Integer> {

        /* ---- Custom Query Methods ---- */

        // For documentation about how query methods work and how to declare them see "Spring Data - Query Methods":
        // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods

        /**
         * Returns the {@link LevelSystem} entity with the given id.
         *
         * @param id the id
         * @return an {@link Optional} containing the found level system
         */
        // custom query method using JPQL query string
        @Query("select l from LevelSystem l where l.id = :id")
        Optional<LevelSystem> findById(@Param("id") int id);


        /**
         * Deletes the {@link LevelSystem} entity with the given id.
         *
         * @param id the id
         */
        // custom query method with query automatically derived from method name (e.g. "<action>By<attribute name>")
        // equivalent SQL query: delete from LEVELSYSTEM where ID = [id.code]
        @Transactional
        // causes the method to be executed in a database transaction (required for write operations)
        void deleteById(int id);

    }
