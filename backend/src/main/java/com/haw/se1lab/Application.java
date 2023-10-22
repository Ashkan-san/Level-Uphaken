package com.haw.se1lab;

import com.haw.se1lab.common.api.datatype.*;
import com.haw.se1lab.dataaccess.api.entity.*;
import com.haw.se1lab.dataaccess.api.repo.*;
import com.haw.se1lab.dataaccess.api.repo.QuestRepository;
import com.haw.se1lab.dataaccess.api.repo.UserRepository;
import com.haw.se1lab.logic.api.usecase.TaskUseCase;
import com.haw.se1lab.logic.api.usecase.UserUseCase;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

import static com.haw.se1lab.common.api.datatype.QuestType.*;

/**
 * Main application class used for running the application.
 * 
 * @author Arne Busch
 */
@SpringBootApplication()
public class Application {

	/**
	 * Starts the application.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Value("${application.jwtSecret}")
		private String jwtSecret;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			if (jwtSecret == null) {
				throw new IllegalArgumentException("jwtSecret is not set in Properties");
			}
			JWTAuthorizationFilter jwtAuthorizationFilter = new JWTAuthorizationFilter();
			jwtAuthorizationFilter.setJwtSecret(jwtSecret);

			http
							.csrf().disable()
							.cors().and()
							.addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
							.authorizeRequests()
							.antMatchers("/h2-console/**").permitAll()
							.antMatchers(HttpMethod.POST, "/users").permitAll()
							.antMatchers(HttpMethod.POST, "/users/login").permitAll()
							.anyRequest().authenticated();

			http.headers().frameOptions().disable();

			http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().write(new JSONObject()
								.put("timestamp", LocalDateTime.now())
								.put("status", 403)
								.put("error", "Access denied")
								.put("message", "Not Authenticated")
								.put("path", request.getContextPath())
								.toString());
			});
		}

		@Bean
		CorsConfigurationSource corsConfigurationSource() {
			CorsConfiguration configuration = new CorsConfiguration();
			//noinspection ArraysAsListWithZeroOrOneArgument
			configuration.setAllowedOrigins(Arrays.asList("*"));
			configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type"));
			configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT", "DELETE"));

			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", configuration);
			return source;
		}
	}

}

/**
 * Inserts some initial data into the database at startup.
 * 
 * @author Arne Busch
 */
@Profile("!test") // execute the method "run" only in mode "production" (i.e. when @ActiveProfiles != "test")
@Component
@Transactional
class InitialDataInsertionRunner implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AcademicCourseRepository academicCourseRepository;

	@Autowired
	private LevelSystemRepository levelSystemRepository;

	@Autowired
	private UserUseCase userUseCase;

	@Autowired
	private MemoRepository memoRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskUseCase taskUseCase;

	@Autowired
	private QuestRepository questRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Override
	public void run(String... args) {

		//create users
		String academicCourseName = "Angewandte Informatik";
		if (!academicCourseRepository.findByName(academicCourseName).isPresent()) {
			AcademicCourse academicCourse = new AcademicCourse(academicCourseName);
			academicCourseRepository.save(academicCourse);
		}

		EmailType userEmail = new EmailType("max.musterman@haw-hamburg.de");
		if (!userRepository.findByEmail(userEmail).isPresent()) {
			AcademicCourse academicCourse = academicCourseRepository.findByName(academicCourseName).get();
			LevelSystem levelSystem = new LevelSystem();
			levelSystemRepository.save(levelSystem);

			User user = new User(userEmail, "Max", "Musterman", RoleType.STUDENT,
							new AKennungType("aaa123"), new Password("Password!1"), academicCourse, levelSystem);
			userRepository.save(user);
		}

		EmailType profEmail = new EmailType("prof.musterman@haw-hamburg.de");
		if (!userRepository.findByEmail(profEmail).isPresent()) {
			User prof = new User(profEmail, "Prof", "Musterman", RoleType.PROFESSOR, new AKennungType("aaa111"), new Password("Password!1"), null, null);
			userRepository.save(prof);
		}

		//create quests
		String questTitle = "3 Tage hintereinander anmelden";
		if (!questRepository.findByTitle(questTitle).isPresent()) {
			Quest quest = new Quest(questTitle, TAGE_3, 30);
			questRepository.save(quest);
		}
		String questTitle2 = "Memo abhaken";
		if (!questRepository.findByTitle(questTitle2).isPresent()) {
			Quest quest = new Quest(questTitle2, MEMO_ABSCHLIESSEN, 150);
			questRepository.save(quest);
		}

		//create subjects
		String subjectNameIS = "Intelligente Systeme";
		if (!subjectRepository.findBySubjectName(subjectNameIS).isPresent()) {
			User subjectProf = userRepository.findByEmail(profEmail).get();
			Subject subject = new Subject(subjectNameIS, "IS", 4, new Password("Test123!"), "Im Modul \"Intelligente Systeme\" (IS) von Michael Neitzke lernt ihr eine Menge spannender Sachen über künstliche Intelligenz.<br /> Wie Programme lernen, suchen und viele weitere, theoretische Themen werdet ihr in den Praktika empirisch erforschen. Interessante Coding Praktika warten hier auf euch.", subjectProf);
			subjectRepository.save(subject);

			try {
				User subjectStudent = userRepository.findByEmail(userEmail).get();
				userUseCase.enrollInSubject(subjectStudent, subject.getAcronym(), subject.getEnrollmentKey());
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		String subjectNameSE2 = "Software Engineering 2";
		if (!subjectRepository.findBySubjectName(subjectNameSE2).isPresent()) {
			User subjectProf = userRepository.findByEmail(profEmail).get();
			Subject subject = new Subject(subjectNameSE2, "SE2", 4, new Password("Test123!"), "Im besten Modul des 4. Semesters \"Software Engineering 2\" (SE2) von Sven Heitsch verbessert ihr eure bereits in SE1 gelernten Skills und stellt diese nun in einem Teamprojekt auf die Probe, indem ihr zusammen an einem gemeinsamen Webprojekt arbeitet. Ja nicht verpassen ;)", subjectProf);
			subjectRepository.save(subject);
		}

		// damit es voller aussieht
		String subjectName3 = "Algorithmen und Datenstrukturen";
        if (!subjectRepository.findBySubjectName(subjectName3).isPresent()) {
            User subjectProf = userRepository.findByEmail(profEmail).get();
            Subject subject = new Subject(subjectName3, "AD", 3, new Password("Test123!"), "Im besten Modul des 3. Semesters \"Algorithmen und Datenstrukturen\" (AD) von ... verbessert ihr eure bereits in irgendeinem Semester gelernten Skills und stellt euch vor super langen Praktika und lebt keine Sekunde. Wie mit jedem Modul eigentlich. Ja nicht verpassen ;)", subjectProf);
            subjectRepository.save(subject);
        }

		String subjectName5 = "Verteilte Systeme";
        if (!subjectRepository.findBySubjectName(subjectName5).isPresent()) {
            User subjectProf = userRepository.findByEmail(profEmail).get();
            Subject subject = new Subject(subjectName5, "VS", 5, new Password("Test123!"), "Im besten Modul des 5. Semesters \"Verteilte Systeme\" (VS) von ... verbessert ihr eure bereits in irgendeinem Semester gelernten Skills und stellt euch vor super langen Praktika und lebt keine Sekunde. Wie mit jedem Modul eigentlich. Ja nicht verpassen ;)", subjectProf);
            subjectRepository.save(subject);
        }
		String subjectName6 = "Logik und Berechenbarkeit";
        if (!subjectRepository.findBySubjectName(subjectName6).isPresent()) {
            User subjectProf = userRepository.findByEmail(profEmail).get();
            Subject subject = new Subject(subjectName6, "LB", 2, new Password("Test123!"), "Im besten Modul des 2. Semesters \"Logik und Berechenbarkeit\" (LB) von ... verbessert ihr eure bereits in irgendeinem Semester gelernten Skills und stellt euch vor super langen Praktika und lebt keine Sekunde. Wie mit jedem Modul eigentlich. Ja nicht verpassen ;)", subjectProf);
            subjectRepository.save(subject);
        }
		String subjectName7 = "Betriebssysteme";
        if (!subjectRepository.findBySubjectName(subjectName7).isPresent()) {
            User subjectProf = userRepository.findByEmail(profEmail).get();
            Subject subject = new Subject(subjectName7, "BS", 3, new Password("Test123!"), "Im besten Modul des 3. Semesters \"Betriebssysteme\" (BS) von ... verbessert ihr eure bereits in irgendeinem Semester gelernten Skills und stellt euch vor super langen Praktika und lebt keine Sekunde. Wie mit jedem Modul eigentlich. Ja nicht verpassen ;)", subjectProf);
            subjectRepository.save(subject);
        }

		//create memos
		// IS
		String memoTitleIS = "Vorlesung";
		if (!memoRepository.findByTitle(memoTitleIS).isPresent()) {
			Subject subject = subjectRepository.findBySubjectName(subjectNameIS).get();
			Memo memo = new Memo(subject, memoTitleIS, 150, "Die Memo zur IS Vorlesung. Die zur Verfügung gestellten Videos sind die Grundlage für eure Diskussionen in den Lerngruppen.");
			memoRepository.save(memo);
		}

		String memoTitleISP1 = "Suchen";
        if (!memoRepository.findByTitle(memoTitleISP1).isPresent()) {
            Subject subject = subjectRepository.findBySubjectName(subjectNameIS).get();
            Memo memo = new Memo(subject, memoTitleISP1, 150, "Sucht euch ein Themenfeld im Bereich \"Suchen\" aus und erstellt ein Projekt in Python, in welchem ihr eure gelernten Erkenntnisse praktisch umsetzt.<br />  <object data=\"/documents/pdfViewerTest.pdf\" width=\"100%\" height=\"600\" type=\"application/pdf\" />");
            memoRepository.save(memo);
        }

		String memoTitleISP2 = "Lernen";
        if (!memoRepository.findByTitle(memoTitleISP2).isPresent()) {
            Subject subject = subjectRepository.findBySubjectName(subjectNameIS).get();
            Memo memo = new Memo(subject, memoTitleISP2, 150, "Sucht euch ein Themenfeld im Bereich \"Lernen\" aus und erstellt ein Projekt in Python, in welchem ihr eure gelernten Erkenntnisse praktisch umsetzt.<br />  <object data=\"/documents/pdfViewerTest.pdf\" width=\"100%\" height=\"600\" type=\"application/pdf\" />");
            memoRepository.save(memo);
        }
        String memoTitleISP3 = "Sequentielle Daten";
        if (!memoRepository.findByTitle(memoTitleISP3).isPresent()) {
            Subject subject = subjectRepository.findBySubjectName(subjectNameIS).get();
            Memo memo = new Memo(subject, memoTitleISP3, 150, "Sucht euch ein Themenfeld im Bereich \"Verarbeitung sequentieller Daten\" aus und erstellt ein Projekt in Python, in welchem ihr eure gelernten Erkenntnisse praktisch umsetzt.<br />  <object data=\"/documents/pdfViewerTest.pdf\" width=\"100%\" height=\"600\" type=\"application/pdf\" />");
            memoRepository.save(memo);
        }
		// SE2
		String memoTitleSE2Ch = "Challenges";
        if (!memoRepository.findByTitle(memoTitleSE2Ch).isPresent()) {
            Subject subject = subjectRepository.findBySubjectName(subjectNameSE2).get();
            Memo memo = new Memo(subject, memoTitleSE2Ch, 150, "Im Rahmen von 'Blended Learning' stellt ihr euch Challenges, die ihr euren Kommiliton*innen vorstellt.");
            memoRepository.save(memo);
        }
        String memoTitleSE2P = "Praktikum";
        if (!memoRepository.findByTitle(memoTitleSE2P).isPresent()) {
            Subject subject = subjectRepository.findBySubjectName(subjectNameSE2).get();
            Memo memo = new Memo(subject, memoTitleSE2P, 150, "In diesem Praktikum erstellt ihr in 3 Meilensteinen eine Applikation, die auf der Semesterabschlussmesse vorgestellt wird.");
            memoRepository.save(memo);
        }

		//create tasks

		// IS Vorlesung
		// Tut euch zu Lerngruppen zusammen
		TaskId taskIdIS = new TaskId("IS001");
        if (!taskRepository.findByTaskId(taskIdIS).isPresent()) {
            Memo memo = memoRepository.findByTitle(memoTitleIS).get();
            Task task = new Task(memo, "Lerngruppe finden", taskIdIS, 75, TaskType.MANDATORY, "Findet euch zu Lerngruppen zusammen.");
            taskRepository.save(task);
            try {
                User subjectStudent = userRepository.findByEmail(userEmail).get();
                taskUseCase.checkTask(subjectStudent, task);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        TaskId taskIdIS2 = new TaskId("IS002");
        if (!taskRepository.findByTaskId(taskIdIS2).isPresent()) {
            Memo memo = memoRepository.findByTitle(memoTitleIS).get();
            Task task = new Task(memo, "Lerntagebuch führen", taskIdIS2, 75, TaskType.OPTIONAL, "Halte deinen Lernfortschritt im Lerntagebuch fest.");
            taskRepository.save(task);
        }

		// IS Praktikum
		// Sucht euch ein Thema
		// Erstellt ein Projekt und experimentiert
		// Bereitet das Praktikum vor
		// Überlegt euch ein Prüfungsthema

		List<String> memoTitlesISP = new ArrayList<>();
		memoTitlesISP.add(memoTitleISP1);
		memoTitlesISP.add(memoTitleISP2);
		memoTitlesISP.add(memoTitleISP3);

		for(int i=0; i<memoTitlesISP.size(); i++) {
		    String memoTitle = memoTitlesISP.get(i);

		    TaskId taskId1 = new TaskId("IS"+(i+1)+"01");
            if (!taskRepository.findByTaskId(taskId1).isPresent()) {
                Memo memo = memoRepository.findByTitle(memoTitle).get();
                Task task = new Task(memo, memoTitle+": Thema suchen", taskId1, 75, TaskType.MANDATORY, "Sucht euch ein Themenfeld im Bereich \"Suchen\" aus und erstellt ein Projekt in Python, in welchem ihr eure gelernten Erkenntnisse praktisch umsetzt.");
                taskRepository.save(task);
                try {
                    User subjectStudent = userRepository.findByEmail(userEmail).get();
                    taskUseCase.checkTask(subjectStudent, task);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            TaskId taskId2 = new TaskId("IS"+(i+1)+"02");
            if (!taskRepository.findByTaskId(taskId2).isPresent()) {
                Memo memo = memoRepository.findByTitle(memoTitle).get();
                Task task = new Task(memo, memoTitle+": Experimentieren", taskId2, 150, TaskType.MANDATORY, "Sucht euch ein Themenfeld im Bereich \"Lernen\" aus und erstellt ein Projekt in Python, in welchem ihr eure gelernten Erkenntnisse praktisch umsetzt.");
                taskRepository.save(task);
            }

            TaskId taskId3 = new TaskId("IS"+(i+1)+"03");
            if (!taskRepository.findByTaskId(taskId3).isPresent()) {
                Memo memo = memoRepository.findByTitle(memoTitle).get();
                Task task = new Task(memo, memoTitle+": Praktikum vorbereiten", taskId3, 50, TaskType.OPTIONAL, "Sucht euch ein Themenfeld im Bereich \"Verarbeitung sequentieller Daten\" aus und erstellt ein Projekt in Python, in welchem ihr eure gelernten Erkenntnisse praktisch umsetzt.");
                taskRepository.save(task);
            }

            TaskId taskId4 = new TaskId("IS"+(i+1)+"04");
            if (!taskRepository.findByTaskId(taskId4).isPresent()) {
                Memo memo = memoRepository.findByTitle(memoTitle).get();
                Task task = new Task(memo, memoTitle+": Prüfungsthema suchen", taskId4, 10, TaskType.OPTIONAL, "Sucht euch Prüfungsthemen, über die wir im Praktikum sprechen.");
                taskRepository.save(task);
            }
		}

        // SE2
        // Challenges -> aufteilen, machen
        // Praktikum -> Meilensteine LiveDemo
        TaskId taskIdSe2Ch = new TaskId("SE201");
        if (!taskRepository.findByTaskId(taskIdSe2Ch).isPresent()) {
            Memo memo = memoRepository.findByTitle(memoTitleSE2Ch).get();
            Task task = new Task(memo, "Challenges aufteilen", taskIdIS, 75, TaskType.MANDATORY, "Teilt die Challenges in eurer Gruppe auf.");
            taskRepository.save(task);
            try {
                User subjectStudent = userRepository.findByEmail(userEmail).get();
                // taskUseCase.checkTask(subjectStudent, task);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        TaskId taskIdSe2Ch2 = new TaskId("SE202");
        if (!taskRepository.findByTaskId(taskIdSe2Ch2).isPresent()) {
            Memo memo = memoRepository.findByTitle(memoTitleSE2Ch).get();
            Task task = new Task(memo, "Challenges bearbeiten", taskIdIS, 75, TaskType.MANDATORY, "Bearbeitet die Challenges und fügt sie in eure Dokumentation ein.");
            taskRepository.save(task);
            try {
                User subjectStudent = userRepository.findByEmail(userEmail).get();
                // taskUseCase.checkTask(subjectStudent, task);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        TaskId taskIdSe2P1 = new TaskId("SE211");
        if (!taskRepository.findByTaskId(taskIdSe2P1).isPresent()) {
            Memo memo = memoRepository.findByTitle(memoTitleSE2P).get();
            Task task = new Task(memo, "1. Meilenstein", taskIdIS, 75, TaskType.MANDATORY, "Plant den 1. Meilenstein und setzt ihn durch!");
            taskRepository.save(task);
            try {
                User subjectStudent = userRepository.findByEmail(userEmail).get();
                // taskUseCase.checkTask(subjectStudent, task);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        TaskId taskIdSe2P2 = new TaskId("SE212");
        if (!taskRepository.findByTaskId(taskIdSe2P2).isPresent()) {
            Memo memo = memoRepository.findByTitle(memoTitleSE2P).get();
            Task task = new Task(memo, "2. Meilenstein", taskIdIS, 75, TaskType.MANDATORY, "Plant den 2. Meilenstein und setzt ihn durch!");
            taskRepository.save(task);
            try {
                User subjectStudent = userRepository.findByEmail(userEmail).get();
                // taskUseCase.checkTask(subjectStudent, task);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        TaskId taskIdSe2P3 = new TaskId("SE213");
        if (!taskRepository.findByTaskId(taskIdSe2P3).isPresent()) {
            Memo memo = memoRepository.findByTitle(memoTitleSE2P).get();
            Task task = new Task(memo, "3. Meilenstein", taskIdIS, 75, TaskType.MANDATORY, "Plant den 3. Meilenstein und setzt ihn durch! Endspurt!");
            taskRepository.save(task);
            try {
                User subjectStudent = userRepository.findByEmail(userEmail).get();
                // taskUseCase.checkTask(subjectStudent, task);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        TaskId taskIdSe2PDemo = new TaskId("SE214");
        if (!taskRepository.findByTaskId(taskIdSe2PDemo).isPresent()) {
            Memo memo = memoRepository.findByTitle(memoTitleSE2P).get();
            Task task = new Task(memo, "Live Demo", taskIdIS, 75, TaskType.MANDATORY, "Stellt euer fertiges Projekt der HAW im Rahmen der Semesterabschlussmesse vor.");
            taskRepository.save(task);
            try {
                User subjectStudent = userRepository.findByEmail(userEmail).get();
                // taskUseCase.checkTask(subjectStudent, task);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

	}

}
