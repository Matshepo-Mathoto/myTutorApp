package com.mytutor.demo;

import static org.junit.Assert.assertEquals;


import java.sql.SQLException;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.DatabaseControllers.DatabaseDeleteController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.object_files.Activity;
import com.mytutor.demo.object_files.Administrator;
import com.mytutor.demo.object_files.Course;
import com.mytutor.demo.object_files.Person;
import com.mytutor.demo.object_files.UserLogin;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	static DatabaseQueryController dbQueryController;
	@Autowired
	static DatabaseCreateController dbCreateController;

	@Autowired
	static DatabaseDeleteController dbDeleteController;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	static Person person = new Person();
	static UserLogin userLogin = new UserLogin();
	static int randomID = 0;
	static Course course = new Course();
	static int courseID = 0;
	static int activityID = 0;
	static String postIdentifier = "";

	@BeforeAll
	static void setValues() {
		dbCreateController = new DatabaseCreateController();
		dbQueryController = new DatabaseQueryController();
		randomID = (new Random()).nextInt(9000) + 1000;
		String username = "Ntokozo" + randomID;
		person = new Person("Ntokozo", username, "Ndlovu", "27710797880", "ntokozomdurh1@gmail.com", "Mr");

		String password = "Mduduzi01";
		String role = "ROLE_ADMIN";
		userLogin = new UserLogin(username, password, role);

		String courseCode = "TST" + randomID + "Z";
		course = new Course(courseCode, "Testing", "Testing Course", username, 2023, 100);
		course.setConvenor("Convenor1");
		course.setTeachingAssistant("TeachingAssistant1");
		course.addTutor("Tutor1");
		course.addTutor("Tutor2");
		course.addTutor("TutorZ");
		course.addLecturer("Lecturer1");
		course.addLecturer("Lecturer2");
		course.addLecturer("Lecturer2");

		Administrator admin = new Administrator(person, "Test Department", "Test faculty", role);
		int updateCount = 0;
		try {
			updateCount = dbCreateController.addProfile(person);
			dbCreateController.addAdmin(admin);

			dbCreateController.addCourse(course);
			courseID = dbQueryController.getCourseID(course.getCourseCode(), course.getCourseYear());
			dbCreateController.addCourseConvenor(course, courseID);
			dbCreateController.addCourseTeachingAssistant(course, courseID);
			dbCreateController.addTutorsToCourse(course, courseID);
			dbCreateController.addLecturersToCourse(course, courseID);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertEquals("Test Adding a Profile", 1, updateCount);
	}

	/**
	 * 
	 */
	@Test
	void addLoginDetails() {
		UserLogin addingLogin = new UserLogin(userLogin.getUsername(), passwordEncoder.encode(userLogin.getPassword()),
				userLogin.getRole());

		int updateCount = 0;
		try {
			updateCount = dbCreateController.addLoginDetails(addingLogin);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertEquals("Test Adding Login Detais", 1, updateCount);
	}

	/**
	 * 
	 */
	@Test
	void userLoginTest() {
		UserLogin fromDB = new UserLogin();
		UserLogin invalid = new UserLogin("SHADDY","MDUDUZI01", "ROLE_ADMIN");
		try {
			fromDB = dbQueryController.getUserLogin(person.getUsername());
		} catch (SQLException e) {
			System.err.println(e);
		}
		boolean passwordMatches = passwordEncoder.matches(userLogin.getPassword(), fromDB.getPassword());
		assertEquals(fromDB.getRole(), userLogin.getRole());
		assertEquals(true, passwordMatches);

		boolean matches = passwordEncoder.matches(invalid.getPassword(), fromDB.getPassword());
		assertEquals(fromDB.getRole(), invalid.getRole());
		assertEquals(false, matches);
		

	}

	/**
	 * 
	 */
	@Test
	void personTest() {
		Person fromDB = new Person();
		try {
			fromDB = dbQueryController.getPerson(person.getUsername());
		} catch (SQLException e) {
			System.err.println(e);
		}
		assertEquals(person, fromDB);
	}

	@Test
	void addCourseTest() {
		Course fromDB = null;
		try {
			fromDB = dbQueryController.getCourse(course.getCourseCode(), course.getCourseYear());
		} catch (SQLException e) {
			System.err.println(e);
		}
		assertEquals(course, fromDB);
	}

	/**
	 * 
	 */
	@Test
	public void activityTest() {
		Activity activity = new Activity("Workshop Tuesday",
				"Workshop activity for INF2009F and INF2011S", "HYBRID", courseID);

		Activity fromDB = new Activity();
		try {
			dbCreateController.addActivity(activity);
			int activityID = dbQueryController.getActivityID(activity.getActivityName(),
					activity.getCourseID());
			fromDB = dbQueryController.getActivity(activityID);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertEquals("Activity OK", activity, fromDB);

	}

	@AfterAll
	static void cleanup() {
		dbDeleteController = new DatabaseDeleteController();
		try {
			dbDeleteController.deleteCourse(course.getDisplayCourse());
			dbDeleteController.deleteAdmin(person.getUsername());
			dbDeleteController.deletePerson(person.getUsername());
		} catch (SQLException e) {
			System.err.println(e);
		}
	}

}
