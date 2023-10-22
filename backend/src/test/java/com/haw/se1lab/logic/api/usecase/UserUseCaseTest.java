package com.haw.se1lab.logic.api.usecase;

import java.io.IOException;
import java.util.List;

import com.haw.se1lab.Application;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für den UserUseCase
 *
 * @author Kjell May
 */
@ActiveProfiles("test") // causes exclusive creation of general and test-specific beans (marked by @Profile("test"))
@ExtendWith(SpringExtension.class) // required to use Spring TestContext Framework in JUnit 5
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE) // test environment
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
public class UserUseCaseTest {

	@Autowired
	private UserUseCase userUseCase;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AcademicCourseRepository academicCourseRepository;

	@Autowired
	private LevelSystemRepository levelSystemRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private JacksonTester<User> json;

	private User user;

	private User user2;

	private Subject subject;

	private User updateUser;

	private AcademicCourse academicCourse;

	private AcademicCourse academicCourse2;

	private AcademicCourse updateAcademicCourse;

	private LevelSystem levelSystem;

	private LevelSystem levelSystem2;

	private LevelSystem updateLevelSystem;

	@BeforeEach
	public void setUp() {
		// set up fresh test data before each test method execution
		academicCourse = new AcademicCourse("Angewandte Informatik");
		academicCourseRepository.save(academicCourse);

		levelSystem = new LevelSystem();
		levelSystemRepository.save(levelSystem);

		user = new User(new EmailType("max.musterman@haw-hamburg.de"), "Max", "Musterman", RoleType.STUDENT,
                new AKennungType("aaa123"), new Password("Password!1"), academicCourse, levelSystem);
		userRepository.save(user);

		academicCourse2 = new AcademicCourse("Wirtschaftsinformatik");
		academicCourseRepository.save(academicCourse2);

		levelSystem2 = new LevelSystem();
		levelSystemRepository.save(levelSystem2);

		user2 = new User(new EmailType("prof.musterman@haw-hamburg.de"), "Max", "Musterman",
				RoleType.PROFESSOR, new AKennungType("aaa111"), new Password("Password!1"), academicCourse2, levelSystem2);
		userRepository.save(user2);

		subject = new Subject("Software Engineering 2", "SE2", 3,new Password("SoSe21!!!"), "", user2);
		subjectRepository.save(subject);

		updateAcademicCourse = new AcademicCourse("Technische Informatik");
		academicCourseRepository.save(updateAcademicCourse);

		updateLevelSystem = new LevelSystem();
		levelSystemRepository.save(updateLevelSystem);

		updateUser = new User(new EmailType("max.update@haw-hamburg.de"), "Max", "Update", RoleType.STUDENT,
						new AKennungType("abc123"), new Password("Password!1"), updateAcademicCourse, updateLevelSystem);
		userRepository.save(updateUser);
	}

	@Test
	public void findAllUsers_Success() {
		// [GIVEN]
		EmailType email = user.getEmail();

		// [WHEN]
		List<User> loadedUsers = userUseCase.findAllUsers();

		// [THEN]
		assertThat(loadedUsers).hasSize(3);
		assertThat(loadedUsers).extracting(User::getEmail).containsOnlyOnce(email);
	}

	@Test
	public void logIn_Success() throws UserNotFoundException {
		// [GIVEN]
		EmailType email = user.getEmail();
		Password password = user.getPassword();
		// [WHEN]
		User loggedInUser = userUseCase.logIn(email, password);

		// [THEN]
		assertThat(loggedInUser.getEmail()).isEqualTo(user.getEmail());
		assertThat(loggedInUser.getPassword()).isEqualTo(user.getPassword());
	}

	@Test
	public void logIn_Fail_WrongEmail() throws UserNotFoundException {
		// [GIVEN]
		EmailType wrongEmail = new EmailType("max1.mustermann@haw-hamburg.de");
		Password password = user.getPassword();
		// [WHEN]

		// [THEN]
		assertThatThrownBy(() -> userUseCase.logIn(wrongEmail, password));
	}

	@Test
	public void logIn_Fail_WrongPassword() throws UserNotFoundException {
		// [GIVEN]
		EmailType email = user.getEmail();
		Password wrongPassword = new Password("PAssword!1");
		// [WHEN]

		// [THEN]
		assertThatThrownBy(() -> userUseCase.logIn(email, wrongPassword));
	}

	@Test
	@Transactional
	public void enrollInSubject_Success() throws UserNotFoundException, SubjectNotFoundException, InvalidEnrollmentKeyException, InsufficientPermissionsException, UserAlreadyInSubjectException {
		// [GIVEN]
		EmailType email = user.getEmail();
		String acronym = subject.getAcronym();
		Password enrollmentKey = subject.getEnrollmentKey();
		// [WHEN]
		User enrolledUser = userUseCase.enrollInSubject(user, acronym, enrollmentKey);
		Subject updatedSubject = enrolledUser.getSubjects().get(enrolledUser.getSubjects().size() - 1);
		// [THEN]
		assertThat(user.getEmail()).isEqualTo(email);
		assertThat(updatedSubject.getAcronym()).isEqualTo(acronym);
		assertThat(enrolledUser.getSubjects()).contains(updatedSubject);
		assertThat(updatedSubject.getUsers()).contains(enrolledUser);
	}

	@Test
	public void enrollInSubject_Fail() throws InvalidEnrollmentKeyException {
		// [GIVEN]
		String subjectName = subject.getSubjectName();
		Password enrollmentKey = new Password("Aaa1234!");
		// [WHEN]

		// [THEN]
		assertThatThrownBy(() -> userUseCase.enrollInSubject(user, subjectName, enrollmentKey));
	}

	@Test
	public void updateUser_Success() throws UserNotFoundException {
		// [GIVEN]
		AcademicCourse academicCourse = new AcademicCourse("Maschinenbau");
		LevelSystem levelSystem = new LevelSystem();
		User newUser = new User(new EmailType("tom.updated@haw-hamburg.de"), "Tom", "Updated", RoleType.STUDENT,
						new AKennungType("ade456"), new Password("Test123!"), academicCourse, levelSystem);

		// [WHEN]
		updateUser = userUseCase.updateUser(updateUser, newUser);

		// [THEN]
		assertThat(updateUser.getFirstName()).isEqualTo(newUser.getFirstName());
		assertThat(updateUser.getLastName()).isEqualTo(newUser.getLastName());
		assertThat(updateUser.getEmail().getEmailAddress()).isEqualTo(newUser.getEmail().getEmailAddress());
		assertThat(updateUser.getaKennung().getaKennung()).isEqualTo(newUser.getaKennung().getaKennung());
		assertThat(updateUser.getPassword().getPassword()).isEqualTo(newUser.getPassword().getPassword());
	}

	@Test
	public void updateUser_EMail_Fail() throws IOException {
		// [GIVEN]
		String jsonContent = "{\"emailAddress\": \"tom.updated@gmail.com\"}";
		User illegalUser = this.json.parse(jsonContent).getObject();

		// [WHEN]

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			updateUser = userUseCase.updateUser(updateUser, illegalUser);
		});

		// [THEN]
		assertEquals("Ungültige neue EMail Adresse!", exception.getMessage());
	}

	@Test
	public void updateUser_aKennung_Fail() throws IOException {
		// [GIVEN]
		String jsonContent = "{\"aKennung\": \"def444\"}";
		User illegalUser = this.json.parse(jsonContent).getObject();

		// [WHEN]

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			updateUser = userUseCase.updateUser(updateUser, illegalUser);
		});

		// [THEN]
		assertEquals("Ungültige neue AKennung!", exception.getMessage());
	}

	@Test
	public void updateUser_Password_Fail() throws IOException {
		// [GIVEN]
		String jsonContent = "{\"password\": \"hunter2\"}";
		User illegalUser = this.json.parse(jsonContent).getObject();

		// [WHEN]

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			updateUser = userUseCase.updateUser(updateUser, illegalUser);
		});

		// [THEN]
		assertEquals("Ungültiges neues Passwort!", exception.getMessage());
	}
}
