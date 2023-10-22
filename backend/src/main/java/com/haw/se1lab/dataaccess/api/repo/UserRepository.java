package com.haw.se1lab.dataaccess.api.repo;

import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Repository für Entität Benutzer
 *
 * @author Kjell May
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // TODO: 05.05.2021 Nach Vor- UND Nachnamen suchen können
    /**
     * Gibt die {@link User} Entität mit der passenden Email zurück
     *
     * @param email die email
     * @return ein {@link Optional} mit dem gefundenen Benutzer
     */
    // custom query method using JPQL query string
    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") EmailType email);

    // TODO: 30.04.2021 kann man nochmal drüber diskutieren
    /**
     * Gibt die {@link User} Entitäten mit dem passenden Vornamen zurück
     *
     * @param firstName der Vorname
     * @return ein {@link List<User>} mit den gefundenen Benutzern
     */
    // custom query method using JPQL query string
    @Query("select u from User u where u.firstName = :firstName")
    List<User> findByFirstName(@Param("firstName") String firstName);

    /**
     * Gibt die {@link User} Entitäten mit dem passenden Nachnamen zurück
     *
     * @param lastName der Nachname
     * @return ein {@link List<User>} mit den gefundenen Benutzern
     */
    // custom query method using JPQL query string
    @Query("select u from User u where u.lastName = :lastName")
    List<User> findByLastName(@Param("lastName") String lastName);

    /**
     * Gibt die {@link User} Entitäten mit der passenden Rolle zurück
     *
     * @param role die Rolle
     * @return ein {@link List<User>} mit den gefundenen Benutzern
     */
    // custom query method using JPQL query string
    @Query("select u from User u where u.role = :role")
    List<User> findByRole(@Param("role") RoleType role);

    /**
     * Gibt die {@link User} Entitäten mit der passenden A-Kennung zurück
     *
     * @param aKennung die A-Kennung
     * @return ein {@link List<User>} mit den gefundenen Benutzern
     */
    // custom query method using JPQL query string
    @Query("select u from User u where u.aKennung = :aKennung")
    Optional<User> findByAKennung(@Param("aKennung") AKennungType aKennung);

    /**
     * Gibt die {@link User} Entitäten mit dem passenden Studiengang zurück
     *
     * @param academicCourse der Studiengang
     * @return ein {@link Optional} mit dem gefundenen Benutzer
     */
    // custom query method using JPQL query string
    @Query("select u from User u where u.academicCourse = :academicCourse")
    List<User> findByAcademicCourse(@Param("academicCourse") AcademicCourse academicCourse);

    /**
     * Löscht die {@link User} Entität mit der passenden Email
     *
     * @param email die Email
     */
    // custom query method with query automatically derived from method name (e.g. "<action>By<attribute name>")
    // equivalent SQL query: delete from COURSE where COURSE_NUMBER = [courseNumber.code]
    @Transactional
    // causes the method to be executed in a database transaction (required for write operations)
    void deleteByEmail(EmailType email);

}
