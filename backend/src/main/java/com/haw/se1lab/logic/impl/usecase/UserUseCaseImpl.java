package com.haw.se1lab.logic.impl.usecase;

import com.haw.se1lab.common.api.datatype.AKennungType;
import com.haw.se1lab.common.api.datatype.EmailType;
import com.haw.se1lab.common.api.datatype.Password;
import com.haw.se1lab.common.api.datatype.RoleType;
import com.haw.se1lab.common.api.exception.*;
import com.haw.se1lab.dataaccess.api.entity.AcademicCourse;
import com.haw.se1lab.dataaccess.api.entity.LevelSystem;
import com.haw.se1lab.dataaccess.api.entity.Subject;
import com.haw.se1lab.dataaccess.api.entity.User;
import com.haw.se1lab.dataaccess.api.repo.AcademicCourseRepository;
import com.haw.se1lab.dataaccess.api.repo.LevelSystemRepository;
import com.haw.se1lab.dataaccess.api.repo.SubjectRepository;
import com.haw.se1lab.dataaccess.api.repo.UserRepository;
import com.haw.se1lab.logic.api.usecase.UserUseCase;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import io.jsonwebtoken.Jwts;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementierende UseCase-Klasse für Benutzer
 *
 * @author Kjell May
 */
@Service
public class UserUseCaseImpl implements UserUseCase {

    // TODO: 05.05.2021 Zugriffsrechte durch Rolle (-> Exceptions)
    // TODO: 10.05.2021 Forgot Password Funktion

    @Value("${application.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AcademicCourseRepository academicCourseRepository;

    @Autowired
    private LevelSystemRepository levelSystemRepository;

    // TODO: 30.04.2021 log in LOGIK implementieren

    /**
     * Benutzer kann sich anmelden
     *
     * @param email    Email des Benutzers
     * @param password Passwort des Benutzers
     * @return den Benutzer
     * @throws UserNotFoundException wenn es keinen Benutzer mit diesen Daten gibt
     */
    @Override
    public User logIn(EmailType email, Password password) throws UserNotFoundException {
        Assert.notNull(email, "Email darf nicht null sein!");
        Assert.notNull(password, "Passwort darf nicht null sein!");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        if (!user.getPassword().equals(password)) {
            throw new UserNotFoundException(email);
        }

        // Update unseren User
        user.setLastLogin(LocalDate.now());
        String token = getJWTToken(user.getEmail().getEmailAddress());
        user.setSession(token);
        userRepository.save(user);

        return user;
    }

    private String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        return Jwts
                .builder()
                .setId("level-uphaken")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))) // 24 Stunden
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).compact();
    }

    /**
     * @return User Aktueller Benutzer
     * @throws UserNotFoundException Fehlerhafter JWT Token
     */
    @Override
    public User getCurrentUser() throws UserNotFoundException {
        return this.findUserByEmail(new EmailType(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    /**
     * Gibt alle Benutzer zurück
     *
     * @return List<User> Eine Liste der Benutzer
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Gibt einen Benutzer nach Email zurück
     *
     * @param email Die Email des Benutzers
     * @return Benutzer den gesuchten Benutzer
     * @throws UserNotFoundException wenn kein Benutzer mit dieser Mail gefunden wurde
     */
    @Override
    public User findUserByEmail(EmailType email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    /**
     * Lässt einen Benutzer erstellen
     *
     * @param email     Gewünschte Email
     * @param firstName den Vornamen
     * @param lastName  den Nachnamen
     * @param aKennung  die A-Kennung
     * @param password  Gewünschtes Passwort
     * @return Neu erstellten Benutzer
     * @throws UserAlreadyExistingException Wenn es bereits einen Benutzer mit diesen Daten gibt
     */
    @Override
    public User createUser(EmailType email, String firstName, String lastName, AKennungType aKennung, Password password, AcademicCourse academicCourse) throws UserAlreadyExistingException {
        Assert.notNull(email.getEmailAddress(), "EMail darf nicht null sein");
        Assert.hasText(email.getEmailAddress(), "EMail darf nicht leer sein");
        Assert.notNull(firstName, "Vorname darf nicht null sein");
        Assert.hasText(firstName, "Vorname darf nicht leer sein");
        Assert.notNull(lastName, "Nachname darf nicht null sein");
        Assert.hasText(lastName, "Nachname darf nicht leer sein");
        Assert.notNull(aKennung.getaKennung(), "A-Kennung darf nicht null sein");
        Assert.hasText(aKennung.getaKennung(), "A-Kennung darf nicht leer sein");
        Assert.notNull(password.getPassword(), "Passwort darf nicht null sein");
        Assert.hasText(password.getPassword(), "Passwort darf nicht leer sein");
        Assert.notNull(academicCourse, "AcademicCourse darf nicht null sein");
        Assert.hasText(academicCourse.getName(), "AcademicCourse darf nicht leer sein");

        if (userRepository.findByEmail(email).isPresent()) throw new UserAlreadyExistingException(email);

        AcademicCourse validAcademicCourse = academicCourseRepository.findByName(academicCourse.getName()).orElseGet(() ->
            academicCourseRepository.save(new AcademicCourse(academicCourse.getName()))
        );
        LevelSystem levelSystem = new LevelSystem();
        levelSystemRepository.save(levelSystem);

        User user = new User(new EmailType(email.getEmailAddress()), firstName, lastName, RoleType.STUDENT, new AKennungType(aKennung.getaKennung()), new Password(password.getPassword()), validAcademicCourse, levelSystem);

        return userRepository.save(user);
    }

    /**
     * Lässt einen Benutzer aktualisieren
     *
     * @param currentUser Der zu aktualisierende Benutzer
     * @param newUser Der Benutzer mit den neuen Daten
     * @return den aktualisierten Benutzer
     */
    @Override
    public User updateUser(User currentUser, User newUser) throws IllegalArgumentException {
        Assert.notNull(newUser, "Benutzer darf nicht null sein");

        if (newUser.getFirstName() != null) {
            currentUser.setFirstName(newUser.getFirstName());
        }
        if (newUser.getLastName() != null) {
            currentUser.setLastName(newUser.getLastName());
        }
        if (newUser.getEmail().getEmailAddress() != null) {
            EmailType newEmail;
            try {
                newEmail = new EmailType(newUser.getEmail().getEmailAddress());
            } catch (Exception exception) {
                throw new IllegalArgumentException("Ungültige neue EMail Adresse!");
            }
            currentUser.setEmail(newEmail);
            String token = getJWTToken(currentUser.getEmail().getEmailAddress());
            currentUser.setSession(token);
        }
        if (newUser.getaKennung().getaKennung() != null) {
            AKennungType newaKennung;
            try {
                newaKennung = new AKennungType(newUser.getaKennung().getaKennung());
            } catch (Exception exception) {
                throw new IllegalArgumentException("Ungültige neue AKennung!");
            }
            currentUser.setaKennung(newaKennung);
        }
        if (newUser.getPassword().getPassword() != null) {
            Password newPassword;
            try {
                newPassword = new Password(newUser.getPassword().getPassword());
            } catch (Exception exception) {
                throw new IllegalArgumentException("Ungültiges neues Passwort!");
            }
            currentUser.setPassword(newPassword);
        }

        return userRepository.save(currentUser);
    }

    /**
     * Lässt einen Benutzer löschen
     *
     * @param email Email des zu löschenden Benutzers
     * @throws UserNotFoundException Wenn es keinen Benutzer mit der Email gibt
     */
    @Override
    public void deleteUser(EmailType email) throws UserNotFoundException {
        User user = findUserByEmail(email);

        userRepository.delete(user);
    }

    /**
     * Lässt in Fach einschreiben
     *
     * @param user         Email des Benutzers
     * @param acronym       Akronym des Fachs
     * @param enrollmentKey Einschreibeschlüssel des Fachs
     * @return den aktualisierten Benutzer
     * @throws SubjectNotFoundException      Wenn das Fach mit dem Namen nicht gefunden wurde
     * @throws InvalidEnrollmentKeyException Wenn der Einschreibeschlüsse ungültig war
     * @throws UserAlreadyInSubjectException Wenn der Benutzer bereits in der Liste des Fachs ist
     */
    @Override
    public User enrollInSubject(User user, String acronym, Password enrollmentKey) throws SubjectNotFoundException, InvalidEnrollmentKeyException, UserAlreadyInSubjectException {
        Subject subject = subjectRepository.findByAcronym(acronym).orElseThrow(() -> new SubjectNotFoundException(acronym));

        if (!subject.getEnrollmentKey().equals(enrollmentKey)) throw new InvalidEnrollmentKeyException(enrollmentKey);

        user.addSubject(subject);
        subject.addUser(user);

        return userRepository.save(user);
    }

}
