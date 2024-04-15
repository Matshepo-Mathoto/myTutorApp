package com.mytutor.demo.DatabaseControllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mytutor.DBConnection;
import com.mytutor.demo.object_files.Activity;
import com.mytutor.demo.object_files.ActivitySession;
import com.mytutor.demo.object_files.Administrator;
import com.mytutor.demo.object_files.Application;
import com.mytutor.demo.object_files.ApplicationPost;
import com.mytutor.demo.object_files.Course;
import com.mytutor.demo.object_files.Lecturer;
import com.mytutor.demo.object_files.Person;
import com.mytutor.demo.object_files.Student;
import com.mytutor.demo.object_files.UserLogin;

@Component
public class DatabaseCreateController {
    private DatabaseQueryController dbQueryController = new DatabaseQueryController();
    private DatabaseUpdateController dbUpdateController;

    // Start Person
    /**
     * This method add a person object to the database.
     * 
     * @param person
     * @return
     * @throws SQLException
     */

    public int addProfile(Person person) throws SQLException {
        if (dbQueryController.profileExists(person.getUsername())) {
            return 1;
        }

        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO UserProfiles "
                + "(FirstName, username, LastName, EmailAddress, ContactNumber, Title)"
                + " VALUES (?,?,?,?,?,?) ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getUsername());
        preparedStatement.setString(3, person.getLastName());
        preparedStatement.setString(4, person.getEmailAddress());
        preparedStatement.setString(5, person.getContactNumber());
        preparedStatement.setString(6, person.getTitle());

        int res = preparedStatement.executeUpdate();

        connection.close();
        preparedStatement.close();
        return res;
    }
    // End Person

    // Start LoginDetails
    /**
     * Given the username, password, and rold this method adds login details to the
     * database.
     * 
     * @param username
     * @param password
     * @param role
     * @return
     * @throws SQLException
     */

    public int addLoginDetails(String username, String password, String role)
            throws SQLException {

        if (dbQueryController.detailsExists(username)) {
            return dbUpdateController.updateRole(username, role);
        }

        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO LoginDetails " +
                "(Username, LoginPassword,Role)" +
                "VALUES (?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, role);

        int res = preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
        return res;
    }

    /**
     * Adds login details into the database using the UserLogin object
     * 
     * @param userLogin
     * @return
     * @throws SQLException
     */
    public int addLoginDetails(UserLogin userLogin) throws SQLException {
        return addLoginDetails(userLogin.getUsername(), userLogin.getPassword(), userLogin.getRole());
    }
    // End LoginDetails

    // StartStudent
    /**
     * Adds student details into the database using the Student object
     * 
     * @param student
     * @return
     * @throws SQLException
     */

    public int addStudent(Student student) throws SQLException {
        Connection connection = DBConnection.getConnection();
        try {
            String query = "INSERT INTO Students " +
                    "(StudentNumber, StudyLevel, YearOfStudy, Role, Majors, Faculty) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, student.getUsername());
            preparedStatement.setString(2, student.getStudyLevel());
            preparedStatement.setInt(3, student.getYearOfStudy());
            preparedStatement.setString(4, student.getRole());
            preparedStatement.setString(5, student.getMajors());
            preparedStatement.setString(6, student.getFaculty());

            int res = preparedStatement.executeUpdate();
            connection.close();
            preparedStatement.close();
            return res;
        } catch (Exception e) {
            return 0;
        }
    }
    // EndStudent

    // StartAdmin
    /**
     * Adds admin details into the database using the Administrator object
     * 
     * @param admin
     * @return
     * @throws SQLException
     */
    public int addAdmin(Administrator admin) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO administrators (username, department, faculty, adminRole) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, admin.getUsername());
        preparedStatement.setString(2, admin.getDepartment());
        preparedStatement.setString(3, admin.getFaculty());
        preparedStatement.setString(4, admin.getAdminRole());

        int res = preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
        return res;
    }

    /**
     * Adds an administrator username into the database. This method is used in the
     * case where not all the Admin details are known
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public int addAdmin(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO administrators (username) VALUES (?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        int res = preparedStatement.executeUpdate();

        connection.close();
        preparedStatement.close();
        return res;
    }

    // EndAdmin

    // Start Lecturer
    /**
     * Given a Lecturer object, this method adds the lecturer details into the
     * database.
     * 
     * @param lecturer
     * @return
     * @throws SQLException
     */
    public int addLecturer(Lecturer lecturer) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO lecturers (username, department, lecturerRole) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, lecturer.getUsername());
        preparedStatement.setString(2, lecturer.getDepartment());
        preparedStatement.setString(3, lecturer.getRole());

        int res = preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
        return res;

    }
    // End Lecturer

    // Start Course
    /**
     * Given a course object, this method adds course details into te database
     * 
     * @param course
     * @return
     * @throws SQLException
     */
    public int addCourse(Course course) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO courses (CourseCode, CourseYear, CourseDescription, CreatorID, Department, NumberOfStudents)"
                + "VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, course.getCourseCode());
        preparedStatement.setInt(2, course.getCourseYear());
        preparedStatement.setString(3, course.getCourseDescription());
        preparedStatement.setString(4, course.getCreatorID());
        preparedStatement.setString(5, course.getDepartment());
        preparedStatement.setInt(6, course.getNumberOfStudents());

        int res = preparedStatement.executeUpdate();
        if (res != 0) {
            connection.close();
            preparedStatement.close();
            return dbQueryController.getCourseID(course.getCourseCode(), course.getCourseYear());
        }
        connection.close();
        preparedStatement.close();
        return res;

    }

    /**
     * This method adds a course convenor to a course
     * 
     * @param course
     * @param courseID
     * @return
     * @throws SQLException
     */
    public int addCourseConvenor(Course course, int courseID) throws SQLException {
        return addIntoCourseDetails(courseID, course.getConvenor(), "ROLE_CONVENOR");
    }

    /**
     * This method adds a Teaching Assistant to a course
     * 
     * @param course
     * @param courseID
     * @return
     * @throws SQLException
     */
    public int addCourseTeachingAssistant(Course course, int courseID) throws SQLException {
        return addIntoCourseDetails(courseID, course.getTeachingAssistant(), "ROLE_TA");
    }

    /**
     * This method adds a Tutor to a course
     * 
     * @param course
     * @param courseID
     * @param index
     * @return
     * @throws SQLException
     */
    public int addCourseTutor(Course course, int courseID, int index) throws SQLException {
        return addIntoCourseDetails(courseID, course.getTutorID(index), "ROLE_TUTOR");
    }

    /**
     * Adds a list of tutors for a course into a database. This method is used when
     * a list of tutor usernames are provided.
     * 
     * @param course
     * @param courseID
     * @return
     * @throws SQLException
     */
    public int addTutorsToCourse(Course course, int courseID) throws SQLException {
        int numTutors = course.getNumTutors();
        int recordsAdded = 0;
        for (int i = 0; i < numTutors; i++) {
            String tutorUsername = course.getTutorID(i);
            tutorUsername = tutorUsername.strip();
            if (!dbQueryController.courseParticipantExists(courseID, tutorUsername, "ROLE_TUTOR")) {
                recordsAdded += addIntoCourseDetails(courseID, tutorUsername, "ROLE_TUTOR");
            }
        }
        return recordsAdded;
    }

    /**
     * This method adds a lecturer to a course
     * @param courseID
     * @param username
     * @return
     * @throws SQLException
     */
    public int addCourseLecturer(int courseID, String username) throws SQLException {
        return addIntoCourseDetails(courseID, username, "ROLE_LECTURER");
    }

    /**
     * Adds a list of lecturers for a course into a database. This method is used
     * when
     * a list of lecturer usernames are provided.
     * 
     * @param course
     * @param courseID
     * @return
     * @throws SQLException
     */
    public int addLecturersToCourse(Course course, int courseID) throws SQLException {
        List<String> lecturers = course.getLecturers();
        int recordsAdded = 0;
        for (String lecturer : lecturers) {
            lecturer = lecturer.strip();
            if (!dbQueryController.courseParticipantExists(courseID, lecturer, "ROLE_LECTURER")) {
                recordsAdded += addIntoCourseDetails(courseID, lecturer, "ROLE_LECTURER");
            }
        }
        return recordsAdded;
    }

    /**
     * This method adds a participant to a course.
     * 
     * @param courseID
     * @param username
     * @param role
     * @return
     * @throws SQLException
     */
    protected int addIntoCourseDetails(int courseID, String username, String role) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT IGNORE INTO courseDetails (courseID, username, role) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, role);

        int res = preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
        return res;
    }
    // End Course

    /**
     * Add activities of a course into the database using an activity object.
     * 
     * @param activity
     * @return
     * @throws SQLException
     */
    public int addActivity(Activity activity) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO activities " +
                "(ActivityName, ActivityDescription, ActivityType, CourseID)" +
                "VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, activity.getActivityName());
        preparedStatement.setString(2, activity.getActivityDescription());
        preparedStatement.setString(3, activity.getActivityType());
        preparedStatement.setInt(4, activity.getCourseID());

        int res = preparedStatement.executeUpdate();

        //System.out.println("Activity details successfully added");
        connection.close();
        preparedStatement.close();
        return res;
    }

    /**
     * This method is called given an activity object and a courseID
     * 
     * @param activity
     * @param courseID
     * @return
     * @throws SQLException
     */
    public int addActivity(Activity activity, int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO activities " +
                "(ActivityName, ActivityDescription, ActivityType, CourseID)" +
                "VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, activity.getActivityName());
        preparedStatement.setString(2, activity.getActivityDescription());
        preparedStatement.setString(3, activity.getActivityType());
        preparedStatement.setInt(4, courseID);

        int res = preparedStatement.executeUpdate();

        //System.out.println("Activity details successfully added");
        connection.close();
        preparedStatement.close();
        return res;
    }
    // End Activity

    // Start Activity Sessions
    /**
     * This method adds activity sign up slot belonging to a existing activity into
     * the database.
     * 
     * @param activitySession
     * @param activityID
     * @param startTime
     * @param endtime
     * @param slot
     * @return
     * @throws SQLException
     */
    public int AddSlots(ActivitySession activitySession, int activityID, String startTime, String endtime, String slot)
            throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO activitysessions" + " (ActivityID, StartTime, EndTime," +
                " Day, Venue, NumberOfTutors, ExpectedStudents, Recurring, RecurFrom, RecurUntil," +
                " OpeningDate, ClosingDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, activityID);
        preparedStatement.setString(2, startTime);
        preparedStatement.setString(3, endtime);
        preparedStatement.setString(4, activitySession.getDay());
        preparedStatement.setString(5, activitySession.getVenue());
        preparedStatement.setInt(6, Integer.parseInt(slot));
        preparedStatement.setInt(7, activitySession.getExpectedStudents());
        preparedStatement.setBoolean(8, activitySession.isRecurring());
        preparedStatement.setString(9, activitySession.getRecurringFrom());
        if (activitySession.isRecurring()) {
            preparedStatement.setString(10, activitySession.getRecurringTo());
        } else {
            preparedStatement.setString(10, activitySession.getRecurringFrom());
        }
        preparedStatement.setString(11, activitySession.getOpeningDate());
        preparedStatement.setString(12, activitySession.getClosingDate());
        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        return res;
    }

    /**
     * This methods adds activity sessions into the database.
     * 
     * @param activitySession
     * @param activityID
     * @return
     * @throws SQLException
     */
    public String addSessions(ActivitySession activitySession, int activityID) throws SQLException {
        for (int i = 0; i < activitySession.getSlotArray().length; i++) {
            String array[] = activitySession.getSlotArray()[i].split("-");
            AddSlots(activitySession, activityID, array[0], array[1], array[2]);
        }
        return "Done";
    }

    // End Activity Sessions

    // Start Session signups
    /**
     * This methods adds a record into the database of which tutor signed up for a
     * slot
     * 
     * @param sessionID
     * @param tutorUsername
     * @return
     * @throws SQLException
     */
    public int addSessionSignup(int sessionID, String tutorUsername) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO sessionsignups (tutorUsername, sessionID) VALUES (?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, tutorUsername);
        preparedStatement.setInt(2, sessionID);

        int res = preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
        return res;
    }

    /**
     * This method adds the location of a tutor for a particular session during the
     * session slot.
     * 
     * @param tutorUsername
     * @param startCoordinates
     * @param sessionID
     * @return
     * @throws SQLException
     */
    public int addSessionlog(String tutorUsername, String startCoordinates, int sessionID) throws SQLException {
        String query = "INSERT INTO sessionlogs (tutorUsername, startingCoordinates, startTime, sessionID) VALUES (?, ?, now(), ?)";
        Connection connection = DBConnection.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, tutorUsername);
        preparedStatement.setString(2, startCoordinates);
        preparedStatement.setInt(3, sessionID);
        int res = preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
        return res;
    }
    // End Session signups

    // Start Post
    /**
     * This methods adds an application post into the database.
     * 
     * @param applicationPost
     * @return
     * @throws SQLException
     */
    public int addPost(ApplicationPost applicationPost) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO applicationposts " +
                "(PostIdentifier, PostDescription, CreatorID, opendate, closingDate,Role, Department)" +
                " VALUES (?,?,?,?,?,?,?) ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, applicationPost.getApplicationPostId());
        preparedStatement.setString(2, applicationPost.getDescription());
        preparedStatement.setString(3, applicationPost.getAdminId());
        preparedStatement.setString(4, applicationPost.getOpenDate());
        preparedStatement.setString(5, applicationPost.getClosingDate());
        preparedStatement.setString(6, applicationPost.getRole());
        preparedStatement.setString(7, applicationPost.getDepartment());

        int result = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        return result;
    }
    // End Post

    // Start Applicant
    /**
     * This methods adds a user application into the database. The application
     * belongs to a existing application post.
     * 
     * @param application
     * @return
     * @throws SQLException
     */
    public int addApplication(Application application) throws SQLException {
        dbQueryController = new DatabaseQueryController();
        Connection connection = DBConnection.getConnection();

        PreparedStatement preparedStatement;
        connection.setAutoCommit(false);

        int applicantID = 0;

        try {
            String query = "INSERT INTO applicants "
                    + "(applicantname, applicantlastname, studentnumber, applicantemail,"
                    + "contactnumber, postidentifier, qualifications, yearofstudy, studyLevel, applicationStatus)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, application.getFirstName());
            preparedStatement.setString(2, application.getLastName());
            preparedStatement.setString(3, application.getUsername());
            preparedStatement.setString(4, application.getEmailAddress());
            preparedStatement.setString(5, application.getContactNumber());
            preparedStatement.setString(6, application.getPostIdentifier());
            preparedStatement.setString(7, application.getQualifications());
            preparedStatement.setInt(8, application.getYearOfStudy());
            preparedStatement.setString(9, application.getStudyLevel());
            preparedStatement.setString(10, application.getApplicationStatus());

            preparedStatement.executeUpdate();

            applicantID = dbQueryController.getApplicantID(application.getPostIdentifier(),
                    application.getUsername());

            query = "INSERT INTO applicantsmarks " +
                    "(ApplicantID, CourseCode, CourseMark)" +
                    "VALUES (?,?,?)";
            List<String> applicantMarks = application.getApplicantMarks();
            String separator = application.getSEPARATOR();

            for (String mark : applicantMarks) {
                String[] values = mark.split(separator);
                String course = values[0];
                int grade = Integer.parseInt(values[0]);
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, applicantID);
                preparedStatement.setString(2, course);
                preparedStatement.setInt(3, grade);

                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
        }

        connection.close();
        return applicantID;
    }

    /**
     * This method adds the details of the applicant to the database.
     * 
     * @param applicant
     * @return
     * @throws SQLException
     */
    public int addApplicant(Application applicant) throws SQLException {
        dbQueryController = new DatabaseQueryController();
        Connection connection = DBConnection.getConnection();

        String query = "INSERT INTO applicants "
                + "(applicantname, applicantlastname, studentnumber, applicantemail,"
                + "contactnumber, postidentifier, qualifications, yearofstudy, studyLevel, applicationStatus)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, applicant.getFirstName());
        preparedStatement.setString(2, applicant.getLastName());
        preparedStatement.setString(3, applicant.getUsername());
        preparedStatement.setString(4, applicant.getEmailAddress());
        preparedStatement.setString(5, applicant.getContactNumber());
        preparedStatement.setString(6, applicant.getPostIdentifier());
        preparedStatement.setString(7, applicant.getQualifications());
        preparedStatement.setInt(8, applicant.getYearOfStudy());
        preparedStatement.setString(9, applicant.getStudyLevel());
        preparedStatement.setString(10, applicant.getApplicationStatus());

        preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
        return dbQueryController.getApplicantID(applicant.getPostIdentifier(), applicant.getUsername());
    }

    /**
     * Adds the marks of the applicant into the database.
     * 
     * @param applicantID
     * @param applicant
     * @return
     * @throws SQLException
     */
    public int addApplicantMarks(int applicantID, Application applicant) throws SQLException {
        List<String> applicantMarks = applicant.getApplicantMarks();

        int recordCount = 0;
        String separator = applicant.getSEPARATOR();
        for (String mark : applicantMarks) {
            String[] values = mark.split(separator);
            recordCount += addApplicantMarks(applicantID, values[0], Integer.parseInt(values[1]));
        }
        return recordCount;

    }

    /**
     * Adds applicant marks intot he database given that only the applicantID (from
     * the database), the course and the mark are known.
     * 
     * @param applicantID
     * @param course
     * @param mark
     * @return
     * @throws SQLException
     */
    public int addApplicantMarks(int applicantID, String course, int mark) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO applicantsmarks " +
                "(ApplicantID, CourseCode, CourseMark)" +
                "VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, applicantID);
        preparedStatement.setString(2, course);
        preparedStatement.setInt(3, mark);
        int res = preparedStatement.executeUpdate();
        connection.close();
        preparedStatement.close();
        return res;
    }

    /**
     * @param applicantID
     * @param docName
     * @param docType
     * @param storagePath
     * @return
     * @throws SQLException
     */
    public int addApplicantDocs(int applicantID, String docName, String docType, String storagePath)
            throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO applicantsdocuments " +
                "(ApplicantID, DocumentName,DocumentType, StoragePath)" +
                "VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, applicantID);
        preparedStatement.setString(2, docName);
        preparedStatement.setString(3, docType);
        preparedStatement.setString(4, storagePath);

        int res = preparedStatement.executeUpdate();

        connection.close();
        preparedStatement.close();
        return res;
    }
    // End Application

    // Start Lecturer & Convenor
    /**
     * Adds a lecturer username into the database. This method is mainly used when
     * the administrator adds a lecturer into the system.
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public int addLecturer(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO lecturers (username) VALUES (?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        int res = preparedStatement.executeUpdate();

        connection.close();
        preparedStatement.close();
        return res;
    }
    // End Lecturer & Convenor
}
