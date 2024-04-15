package com.mytutor.demo.DatabaseControllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.mytutor.DBConnection;
import com.mytutor.demo.object_files.Administrator;
import com.mytutor.demo.object_files.Course;
import com.mytutor.demo.object_files.Lecturer;
import com.mytutor.demo.object_files.Person;
import com.mytutor.demo.object_files.Student;
import com.mytutor.demo.object_files.UserLogin;

@Component
public class DatabaseUpdateController {
    private DatabaseCreateController dbCreateController = new DatabaseCreateController();
    private DatabaseQueryController dbQueryController = new DatabaseQueryController();

    // Start Profile/Person
    /**
     * This method is used to update person details
     * @param person
     * @return
     * @throws SQLException
     */
    public int updateProfile(Person person) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "Update UserProfiles SET "
                + "FirstName = ?, LastName = ?, EmailAddress = ?, ContactNumber = ?, Title = ?"
                + " WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, person.getFirstName());
        preparedStatement.setString(2, person.getLastName());
        preparedStatement.setString(3, person.getEmailAddress());
        preparedStatement.setString(4, person.getContactNumber());
        preparedStatement.setString(5, person.getTitle());
        preparedStatement.setString(6, person.getUsername());

        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();

        return res;
    }

    /**
     * Updates the first name of a person given their username
     * @param firstName
     * @param username
     * @throws SQLException
     */
    public void UpdateFirstName(String firstName, String username) throws SQLException {
        //System.out.println("update first name.");
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE userprofiles set FirstName= ? where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    /**
     * Updates the last name of a person given their username
     * @param lastName
     * @param username
     * @throws SQLException
     */
    public void updateLastName(String lastName, String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE userprofiles set LastName= ? where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, lastName);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    /**
     * Updates the email address of a person given their username
     * @param emailAddress
     * @param username
     * @throws SQLException
     */
    public void updateEmailAddress(String emailAddress, String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE userprofiles set EmailAddress= ? where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, emailAddress);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    /**
     * Updates the cellphone number of a person given their username
     * @param contactNo
     * @param username
     * @throws SQLException
     */
    public void UpdateContactNo(String contactNo, String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE userprofiles set ContactNumber= ? where username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, contactNo);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }

    // End Profile/Person

    // Start LoginDetails
    /**
     * Updates the role of a person given their username in the login details
     * @param username
     * @param role
     * @return
     * @throws SQLException
     */
    public int updateRole(String username, String role) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE loginDetails SET role = ? WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, role);
        preparedStatement.setString(2, username);
        int res = preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
        return res;
    }

    /**
     * Updates the password of a person given their userlogin object
     * @param user
     * @return
     * @throws SQLException
     */
    public int updatePassword(UserLogin user) throws SQLException {
        return updatePassword(user.getUsername(), user.getPassword());
    }

    /**
     * 
     * Updates the password of a person given their username
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public int updatePassword(String username, String password) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE loginDetails SET loginPassword = ? WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, password);
        preparedStatement.setString(2, username);
        int res = preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
        return res;
    }

    /**
     * Updates the login time of a person given their username
     * @param username
     * @param loginDate
     * @return
     * @throws SQLException
     */
    public int addActive(String username, String loginDate) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE loginDetails SET lastLogin = ?, LoggedOut = NULL WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, loginDate);
        preparedStatement.setString(2, username);
        int res = preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
        return res;
    }

    /**
     * Updates the logout time of a person given their username
     * @param username
     * @param logoutDate
     * @return
     * @throws SQLException
     */
    public int loggingOut(String username, String logoutDate) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE loginDetails SET LoggedOut = ? WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, logoutDate);
        preparedStatement.setString(2, username);
        int res = preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
        return res;
    }
    // End LoginDetails

    // Start Admin
    /**
     * Updates the details of an administrator
     * @param admin
     * @return
     * @throws SQLException
     */
    public int updateAdmin(Administrator admin) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE administrators SET " +
                " Department = ?, Faculty = ?, AdminRole = ? "
                + "WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, admin.getDepartment());
        preparedStatement.setString(2, admin.getFaculty());
        preparedStatement.setString(3, admin.getAdminRole());
        preparedStatement.setString(4, admin.getUsername());

        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        //System.out.println(res + " record were successfully updated to Administrators");
        return res;
    }
    // End Admin

    // Start Student
    /**
     * Updates the details of a student
     * @param student
     * @return
     * @throws SQLException
     */
    public int updateStudent(Student student) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE students SET " +
                " StudyLevel = ?, YearOfStudy = ?, Role = ?, Majors = ?, Faculty = ? "
                + "WHERE studentNumber = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, student.getStudyLevel());
        preparedStatement.setInt(2, student.getYearOfStudy());
        preparedStatement.setString(3, student.getRole());
        preparedStatement.setString(4, student.getMajors());
        preparedStatement.setString(5, student.getFaculty());
        preparedStatement.setString(6, student.getUsername());

        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        // System.out.println(res + " record were successfully updated to Students");
        return res;
    }

    // End Student

    // Start Lecturer
    /**
     * Updates the details of a lecturer
     * @param lecturer
     * @return
     * @throws SQLException
     */
    public int updateLecturer(Lecturer lecturer) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE lecturers SET " +
                " Department = ?, lecturerRole = ?"
                + "WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, lecturer.getDepartment());
        preparedStatement.setString(2, lecturer.getRole());
        preparedStatement.setString(3, lecturer.getUsername());

        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        
        return res;
    }
    // End Lecturer

    // Start Course
    /**
     * Updates the details of a course
     * @param course
     * @return
     * @throws SQLException
     */
    public int updateCourse(Course course) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE Courses SET " +
                "CourseDescription = ?, CreatorID = ?, Department = ?, NumberOfStudents = ?" +
                " WHERE CourseCode = ? AND CourseYear = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, course.getCourseDescription());
        preparedStatement.setString(2, course.getCreatorID());
        preparedStatement.setString(3, course.getDepartment());
        preparedStatement.setInt(4, course.getNumberOfStudents());
        preparedStatement.setString(5, course.getCourseCode());
        preparedStatement.setInt(6, course.getCourseYear());

        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        if (res != 0) {
            return dbQueryController.getCourseID(course.getCourseCode(), course.getCourseYear());
        }
        return 0;
    }

    /**
     * Updates the participants in a course
     * @param courseID
     * @param username
     * @param role
     * @return
     * @throws SQLException
     */
    private int updateCourseDetails(int courseID, String username, String role) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE courseDetails SET username = ? WHERE courseID = ? AND role = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, courseID);
        preparedStatement.setString(3, role);

        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        return res;
    }

    /**
     * Updates the convenor of a course
     * @param convenor
     * @param courseID
     * @throws SQLException
     */
    public void updateConvenor(String convenor, int courseID) throws SQLException {
        String convenorName = dbQueryController.getConvenor(courseID);

        if (convenorName.equals("None")) {
            dbCreateController.addIntoCourseDetails(courseID, convenor, "ROLE_CONVENOR");
        } else {
            updateCourseDetails(courseID, convenor, "ROLE_CONVENOR");
        }

    }

    /**
     * Updates the teaching assistant of a course
     * @param ta
     * @param courseID
     * @throws SQLException
     */
    public void updateTeachingAssistant(String ta, int courseID) throws SQLException {
        String taName = dbQueryController.getTA(courseID);

        if (taName.equals("None")) {
            dbCreateController.addIntoCourseDetails(courseID, ta, "ROLE_TA");
        } else {
            updateCourseDetails(courseID, ta, "ROLE_TA");
        }

    }

    /**
     * Updates the session logs of a tutor. Sets the location of the tutor at the start and end of a session.
     * @param tutorUsername
     * @param endCoordinates
     * @param sessionID
     * @param feedback
     * @return
     * @throws SQLException
     */
    public int updateSessionlog(String tutorUsername, String endCoordinates, int sessionID, String feedback)
            throws SQLException {
        String query = "UPDATE sessionlogs SET finishingCoordinates = ?, endTime = now(), sessionfeedback = ?"
                + " WHERE DATE(startTime) = CURDATE() AND tutorUsername = ? AND sessionID = ?";
        Connection connection = DBConnection.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, endCoordinates);
        preparedStatement.setString(2, feedback);
        preparedStatement.setString(3, tutorUsername);
        preparedStatement.setInt(4, sessionID);
        int res = preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
        return res;
    }
    // End Course

    // Start Application
    /**
     * Updates the status of a applicant's application
     * @param lstUsernames
     * @param postIdentifier
     * @param newStatus
     * @return
     * @throws SQLException
     */
    public int updateMultiStatuses(String lstUsernames, String postIdentifier, String newStatus) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE applicants SET applicationStatus = ? WHERE postIdentifier = ? AND  studentNumber IN ("
                + lstUsernames + ")";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newStatus);
        preparedStatement.setString(2, postIdentifier);
        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        return res;
    }

    /**
     * @param postIdentifier
     * @param username
     * @param newStatus
     * @return
     * @throws SQLException
     */
    public int updateStatus(String postIdentifier, String username, String newStatus) throws SQLException {
        int applicantID = dbQueryController.getApplicantID(postIdentifier, username);

        return updateStatus(applicantID, newStatus);
    }

    /**
     * @param applicantID
     * @param newStatus
     * @return
     * @throws SQLException
     */
    public int updateStatus(int applicantID, String newStatus) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE applicants SET applicationStatus = ? WHERE applicantID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newStatus);
        preparedStatement.setInt(2, applicantID);

        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        return res;
    }

    /**
     * Updates documents submitted by the applicant
     * @param applicantID
     * @param docName
     * @param docType
     * @param storagePath
     * @return
     * @throws SQLException
     */
    public int updateDocs(int applicantID, String docName, String docType, String storagePath) throws SQLException {
        String query = "UPDATE applicantsdocuments SET" +
                " DocumentName = ? , DocumentType = ?, StoragePath = ? WHERE ApplicantID = ?";
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, docName);
        preparedStatement.setString(2, docType);
        preparedStatement.setString(3, storagePath);
        preparedStatement.setInt(4, applicantID);

        int res = preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
        return res;
    }
    // End Application

}
