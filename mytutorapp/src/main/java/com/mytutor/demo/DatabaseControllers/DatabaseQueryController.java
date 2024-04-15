package com.mytutor.demo.DatabaseControllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mytutor.DBConnection;
import com.mytutor.demo.object_files.Activity;
import com.mytutor.demo.object_files.ActivitySession;
import com.mytutor.demo.object_files.Administrator;
import com.mytutor.demo.object_files.Application;
import com.mytutor.demo.object_files.Course;
import com.mytutor.demo.object_files.Lecturer;
import com.mytutor.demo.object_files.Person;
import com.mytutor.demo.object_files.SessionLog;
import com.mytutor.demo.object_files.Student;
import com.mytutor.demo.object_files.UserLogin;

@Component
public class DatabaseQueryController {

    public DatabaseQueryController() {
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<String> showTables() throws SQLException {

        Connection connection = DBConnection.getConnection();

        String query = "SHOW TABLES";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        List<String> arrList = new ArrayList<String>();
        while (rs.next()) {

            arrList.add(rs.getString(1));
        }
        rs.close();

        statement.close();
        connection.close();
        return arrList;
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<List<String>> getParticipantsWithoutAccounts() throws SQLException {
        String query = "SELECT coursedetails.Username, coursedetails.role, coursedetails.CourseID, courses.Course_Name"
                + " FROM coursedetails JOIN courses ON courses.CourseID = coursedetails.CourseID"
                + " LEFT JOIN userprofiles ON coursedetails.Username = userprofiles.Username"
                + " WHERE userID IS NULL ORDER BY coursedetails.CourseID;";
        return getDataWithColumns(query);
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<List<String>> getParticipantsPerCourse() throws SQLException {
        String query = "SELECT Course_name, role , COUNT(role) as 'Course participants per role'"
                + " FROM courses JOIN coursedetails ON courses.CourseID = coursedetails.CourseID"
                + " GROUP BY role, courses.CourseID ORDER BY course_Name";
        return getDataWithColumns(query);
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<List<String>> getRecordCounts() throws SQLException {
        String query = "SELECT upper(table_name) AS 'Table', table_rows AS 'Record Count'"
                + " FROM information_schema.tables WHERE table_schema = 'mytutordb'";
        return getDataWithColumns(query);
    }

    /**
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<List<String>> getTableDataWithColumns(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;

        return getDataWithColumns(query);
    }

    /**
     * @param query
     * @return
     * @throws SQLException
     */
    public List<List<String>> getDataWithColumns(String query) throws SQLException {
        List<List<String>> result = new ArrayList<>();
        try {
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            // Get column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            result.add(columnNames);

            // Process the result set and add data to the list
            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                result.add(row);
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Start Person
    /**
     * Returns the person object given the username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public Person getPerson(String username) throws SQLException {
        int userID = getUserID(username);
        return getPerson(userID);
    }

    /**
     * Gets the person object given the database userID
     * 
     * @param userID
     * @return
     * @throws SQLException
     */
    public Person getPerson(int userID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM userProfiles WHERE userID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userID);
        ResultSet rs = preparedStatement.executeQuery();
        // ResultSet rs = statement.executeQuery(query);

        Person p = null;
        if (rs.next()) {
            p = new Person(rs.getString("FirstName"), rs.getString("username"), rs.getString("LastName"),
                    rs.getString("ContactNumber"), rs.getString("EmailAddress"), rs.getString("Title"));
            preparedStatement.close();
            connection.close();
            return p;
        }

        preparedStatement.close();
        connection.close();
        return p;
    }

    /**
     * Gets the username of a particular user given their email address
     * 
     * @param emailAddress
     * @return
     * @throws SQLException
     */
    public String getUsername(String emailAddress) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT username FROM userprofiles WHERE emailAddress = '" + emailAddress + "'";
        connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        String username = "";
        if (rs.next()) {
            username = rs.getString("username");
        }
        preparedStatement.close();
        connection.close();
        return username;
    }

    /**
     * Gets the email address of a particular user given the username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public String getEmailAddress(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT emailAddress FROM userprofiles WHERE username = '" + username + "'";
        connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        String emailAddress = "";
        if (rs.next()) {
            emailAddress = rs.getString("emailAddress");
        }
        preparedStatement.close();
        connection.close();
        return emailAddress;
    }

    /**
     * Gets the userID given a username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public int getUserID(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT userid FROM UserProfiles WHERE Username = '" + username + "'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        int pID = 0;
        if (rs.next()) {
            pID = rs.getInt("userid");
        }

        rs.close();
        // System.out.println("UserID: " + pID);
        preparedStatement.close();
        connection.close();
        return pID;
    }

    /**
     * Checks whether a particular user has a profile or not
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public boolean profileExists(String username) throws SQLException {
        int userID = getUserID(username);

        if (userID <= 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns a list of all userprofiles in the database
     * 
     * @return
     * @throws SQLException
     */
    public List<String> getUsernames() throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT username FROM userprofiles";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> usernames = new ArrayList<String>();
        while (rs.next()) {
            usernames.add(rs.getString("username"));
        }
        preparedStatement.close();
        connection.close();
        return usernames;
    }

    /**
     * @return
     * @throws SQLException
     */
    public List<String> getEmailingList() throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT username, emailAddress FROM userprofiles";

        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet rs = preparedStatement.executeQuery();

        List<String> emailingList = new ArrayList<String>();
        while (rs.next()) {
            String emailing = rs.getString("username") + " - " + rs.getString("emailAddress");
            emailingList.add(emailing);
        }
        preparedStatement.close();
        connection.close();
        return emailingList;
    }
    // End Person

    // Start LoginDetails
    /**
     * Verifies login details
     * 
     * @param log
     * @return
     * @throws SQLException
     */
    public String verifyUserLogin(UserLogin log) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT logindetails.username, role "
                + " FROM userprofiles JOIN logindetails ON userprofiles.username = logindetails.Username "
                + " WHERE (userprofiles.username = ? OR userprofiles.emailAddress = ?) AND loginPassword = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, log.getUsername());
        preparedStatement.setString(2, log.getUsername());
        preparedStatement.setString(3, log.getPassword());
        ResultSet rs = preparedStatement.executeQuery();
        String result = "Error404";

        if (rs.next()) {
            result = rs.getString(1) + "##" + rs.getString(2);
        }
        rs.close();
        preparedStatement.close();
        connection.close();
        return result;
    }

    /**
     * Returns the user login object for a particular user
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public UserLogin getUserLogin(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM loginDetails WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        UserLogin userLogin = null;
        if (rs.next()) {
            userLogin = new UserLogin();
            userLogin.setUsername(username);
            userLogin.setPassword(rs.getString("loginPassword"));
            userLogin.setRole(rs.getString("role"));
        }

        preparedStatement.close();
        connection.close();
        return userLogin;
    }

    /**
     * Get the role of a particular user
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public String getUserRole(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT role FROM loginDetails WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        String userRole = "";
        if (rs.next()) {
            userRole = rs.getString("role");
        }

        preparedStatement.close();
        connection.close();
        return userRole;
    }

    /**
     * Checks whether a particular user had login details
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public boolean detailsExists(String username) throws SQLException {
        UserLogin userLogin = getUserLogin(username);

        if (userLogin == null) {
            return false;
        } else {
            return true;
        }
    }
    // End LoginDetails

    // Start Admin
    /**
     * Returns the administrator object given a username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public Administrator getAdministrator(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM administrators WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        Administrator admin = null;
        if (rs.next()) {
            Person p = getPerson(username);
            admin = new Administrator(p, rs.getString("department"), rs.getString("faculty"),
                    rs.getString("adminRole"));
            List<String> createdCourses = getCreatorCourses(username);
            admin.setCreatedCourses(createdCourses);
            List<String> createdPosts = getCreatorPosts(username);
            admin.setCreatedPosts(createdPosts);

        }
        preparedStatement.close();
        connection.close();
        return admin;
    }

    /**
     * Get the database AdminID given a username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public int getAdminID(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT adminID FROM Administrators WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        int adminID = 0;
        if (rs.next()) {
            adminID = rs.getInt("adminID");
        }

        preparedStatement.close();
        connection.close();
        return adminID;
    }

    /**
     * Checks whether a admin exists given a username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public boolean adminExists(String username) throws SQLException {
        int adminID = getAdminID(username);

        if (adminID <= 0) {
            return false;
        } else {
            return true;
        }
    }
    // End Admin

    // Start Student
    /**
     * returns the student object given a username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public Student getStudent(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM students WHERE studentNumber = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        Student student = null;
        if (rs.next()) {
            Person p = getPerson(username);
            student = new Student(p, rs.getString("studyLevel"), rs.getInt("yearOfStudy"), rs.getString("role"),
                    rs.getString("majors"), rs.getString("faculty"));
            List<String> listOfCourses = getCourseNameList(username, student.getRole());
            student.setListOfCourses(listOfCourses);
        }
        preparedStatement.close();
        connection.close();
        return student;
    }
    // End Student

    // Start Lecturer & Convenor
    /**
     * Return the lecturer object given a username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public Lecturer getLecturer(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM lecturers WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        Lecturer lecturer = null;
        if (rs.next()) {
            Person p = getPerson(username);
            lecturer = new Lecturer(p, rs.getString("lecturerRole"), rs.getString("department"));
            List<String> courses = getCourseNameList(username, lecturer.getRole());
            lecturer.setListOfCourses(courses);
        }
        preparedStatement.close();
        connection.close();
        return lecturer;
    }

    /**
     * Gets a lectureID of a certain username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public int getLectureID(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT lecturerID FROM lecturers WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        int lecturerID = 0;
        if (rs.next()) {
            lecturerID = rs.getInt("lecturerID");
        }
        // System.out.println("lecturerID " + lecturerID);
        preparedStatement.close();
        connection.close();
        return lecturerID;
    }

    /**
     * Gets the database convenorID for a specific course given a username and
     * courseID
     * 
     * @param username
     * @param courseID
     * @return
     * @throws SQLException
     */
    public int getConvenorID(String username, int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT detailsID FROM courseDetails WHERE username = ? AND courseID = ? AND role = 'ROLE_CONVENOR'";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        int convenorID = 0;
        if (rs.next()) {
            convenorID = rs.getInt("detailsID");
        }
        // System.out.println("ConvenorID: " + convenorID);
        preparedStatement.close();
        connection.close();
        return convenorID;
    }

    /**
     * Checks whether a lecturer of a certain username exists
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public boolean lecturerExists(String username) throws SQLException {
        int lecturerID = getLectureID(username);

        if (lecturerID <= 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks whether a convenor of a certain username exists
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public boolean convenorExists(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT detailsID FROM courseDetails WHERE username = ? AND role = 'ROLE_CONVENOR'";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        int convenorID = 0;
        if (rs.next()) {
            convenorID = rs.getInt("detailsID");
        }
        // System.out.println("ConvenorID: " + convenorID);
        preparedStatement.close();
        connection.close();
        if (convenorID <= 0) {
            return false;
        } else {

            return true;
        }
    }
    // End Lecturer & Convenor

    // Start Course
    /**
     * Returns the courseID of a particular course given the course code and year.
     * 
     * @param courseCode
     * @param courseYear
     * @return
     * @throws SQLException
     */
    public int getCourseID(String courseCode, int courseYear) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT courseID FROM Courses WHERE courseCode = ? AND courseYear = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, courseCode);
        preparedStatement.setInt(2, courseYear);
        ResultSet rs = preparedStatement.executeQuery();

        int courseID = 0;
        if (rs.next()) {
            courseID = rs.getInt("courseID");
        }
        preparedStatement.close();
        connection.close();
        return courseID;
    }

    /**
     * Returns a list of course names created by a particular user/admin
     * 
     * @param creatorID
     * @return
     * @throws SQLException
     */
    public List<String> getCreatorCourses(String creatorID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT course_name FROM Courses WHERE creatorID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, creatorID);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> courses = new ArrayList<String>();
        while (rs.next()) {
            courses.add(rs.getString(1));
        }
        preparedStatement.close();
        connection.close();
        return courses;
    }

    /**
     * Gets a list of email addresses for participant with a particular role from a
     * specific course.
     * 
     * @param courseID
     * @param roles
     * @return
     * @throws SQLException
     */
    public List<String> getCourseEmailingList(int courseID, String roles) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT userprofiles.Username, EmailAddress "
                + " FROM userprofiles JOIN coursedetails ON userprofiles.username = coursedetails.Username WHERE role = '"
                + roles + "' AND CourseID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> emailingList = new ArrayList<String>();
        while (rs.next()) {
            String emailing = rs.getString("username") + " - " + rs.getString("emailAddress");
            emailingList.add(emailing);
        }
        preparedStatement.close();
        connection.close();
        return emailingList;

    }

    /**
     * Gets the course object given a course code and a year
     * 
     * @param courseCode
     * @param courseYear
     * @return
     * @throws SQLException
     */
    public Course getCourse(String courseCode, int courseYear) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT courseID FROM Courses WHERE courseCode = ? AND courseYear = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, courseCode);
        preparedStatement.setInt(2, courseYear);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            int courseID = rs.getInt("courseID");
            preparedStatement.close();
            connection.close();
            return getCourse(courseID);
        }

        preparedStatement.close();
        connection.close();
        return null;
    }

    /**
     * Gets the course object given a courseID
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public Course getCourse(int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM Courses WHERE courseID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            Course course = new Course();
            course.setCourseCode(rs.getString("CourseCode"));
            course.setCourseYear(rs.getInt("CourseYear"));
            course.setCourseDescription(rs.getString("CourseDescription"));
            course.setCreatorID(rs.getString("CreatorID"));
            course.setDepartment(rs.getString("Department"));
            course.setNumberOfStudents(rs.getInt("NumberOfStudents"));
            course.setConvenor(getConvenor(courseID));
            course.setTeachingAssistant(getTA(courseID));
            List<String> lecturers = getCourseLecturers(courseID);
            course.setLecturers(lecturers);
            List<String> tutors = getCourseTutors(courseID);
            course.setTutors(tutors);
            course.setNumTutors(tutors.size());
            preparedStatement.close();
            connection.close();
            return course;
        }
        preparedStatement.close();
        connection.close();
        return null;
    }

    /**
     * Get the username of the TA for a particular course
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public String getTA(int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT username FROM CourseDetails WHERE courseID = ? and role LIKE 'ROLE_T%A%'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            String TA = rs.getString("username");
            preparedStatement.close();
            connection.close();
            return TA;
        }
        preparedStatement.close();
        connection.close();
        return "None";

    }

    /**
     * Gets the username of the course convenor for a particular course
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public String getConvenor(int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT username FROM CourseDetails WHERE courseID = ? and role LIKE '%CONVENOR%'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            String convenor = rs.getString("username");
            preparedStatement.close();
            connection.close();
            return convenor;
        }
        preparedStatement.close();
        connection.close();
        return "None";
    }

    /**
     * Gets a list of lecturers for a particular course
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public List<String> getCourseLecturers(int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT DISTINCT username FROM CourseDetails WHERE courseID = ? and role LIKE '%LECTURE%'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> tutors = new ArrayList<String>();
        while (rs.next()) {
            tutors.add(rs.getString("username"));
        }

        preparedStatement.close();
        connection.close();
        return tutors;

    }

    /**
     * Checks whether a tutor is a part of a course
     * 
     * @param courseID
     * @param username
     * @param role
     * @return
     * @throws SQLException
     */
    public boolean courseParticipantExists(int courseID, String username, String role) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM coursedetails WHERE courseID = ? AND username = ? AND role = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, role);

        ResultSet rs = preparedStatement.executeQuery();
        boolean exists = rs.next();
        preparedStatement.close();
        connection.close();
        return exists;
    }

    /**
     * Returns a list of tutor usernames for a particular course
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public List<String> getCourseTutors(int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT DISTINCT username FROM CourseDetails WHERE courseID = ? and role LIKE '%Tutor%'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> tutors = new ArrayList<String>();
        while (rs.next()) {
            tutors.add(rs.getString("username"));
        }

        preparedStatement.close();
        connection.close();
        return tutors;

    }

    /**
     * Checks whether a particular course exists
     * 
     * @param courseCode
     * @param courseYear
     * @return
     * @throws SQLException
     */
    public Boolean courseExists(String courseCode, int courseYear) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT CourseID FROM courses WHERE CourseCode = ? AND CourseYear = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, courseCode);
        preparedStatement.setInt(2, courseYear);
        ResultSet rs = preparedStatement.executeQuery();

        int courseID = 0;
        if (rs.next()) {
            courseID = rs.getInt("CourseID");

        }

        preparedStatement.close();
        connection.close();

        if (courseID > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Returns a list of course names for a particular user
     * 
     * @param username
     * @param role
     * @return
     * @throws SQLException
     */
    public List<String> getCourseNameList(String username, String role) throws SQLException {

        List<String> courseNameList = new ArrayList<>();
        Connection connection = DBConnection.getConnection();

        String query = "SELECT Course_name FROM courses JOIN coursedetails ON courses.courseID = coursedetails.courseID "
                + " WHERE Username = ? AND role = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username.toUpperCase());
        preparedStatement.setString(2, role);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            courseNameList.add(rs.getString("Course_Name"));
        }

        preparedStatement.close();
        connection.close();
        return courseNameList;
    }

    /**
     * Returns a list of courseIDs for a particular user
     * 
     * @param username
     * @param role
     * @return
     * @throws SQLException
     */
    public List<Integer> getCourseIDList(String username, String role) throws SQLException {

        List<Integer> courseIDList = new ArrayList<>();
        Connection connection = DBConnection.getConnection();

        String query = "SELECT CourseID FROM coursedetails WHERE Username = ? AND role = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username.toUpperCase());
        preparedStatement.setString(2, role);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            courseIDList.add(rs.getInt("CourseID"));
        }

        preparedStatement.close();
        connection.close();
        return courseIDList;
    }

    /**
     * Get a list of course names given a list of database courseIDs
     * 
     * @param array
     * @return
     * @throws SQLException
     */
    public List<String> getCourseNames(List<Integer> array) throws SQLException {
        List<String> coursesNames = new ArrayList<String>();
        Connection connection = DBConnection.getConnection();
        for (int i = 0; i < array.size(); i++) {
            String query = "SELECT course_name FROM courses WHERE CourseID = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, array.get(i));
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                coursesNames.add(result.getString(1));
            }
            preparedStatement.close();
        }

        connection.close();
        return coursesNames;
    }

    // End Course

    // Start Activities
    /**
     * Get a specific activity given the database activityID.
     * 
     * @param activityID
     * @return
     * @throws SQLException
     */
    public Activity getActivityObj(int activityID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM activities WHERE activityID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, activityID);
        ResultSet rs = preparedStatement.executeQuery();

        Activity activity = null;
        if (rs.next()) {
            activity = new Activity(rs.getString("activityName"), rs.getString("activityDescription"),
                    rs.getString("activityType"), rs.getInt("courseID"));
        }
        preparedStatement.close();
        connection.close();
        return activity;
    }

    /**
     * Gets a list of activity names for a particular course
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public List<String> getActivities(int courseID) throws SQLException {
        List<String> activities = new ArrayList<>();
        Connection connection = DBConnection.getConnection();

        String query = "SELECT activityName FROM activities WHERE CourseID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet result = preparedStatement.executeQuery();
        while (result.next()) {
            activities.add(result.getString("activityName"));
        }
        // System.out.println("activities: " + activities.size());
        preparedStatement.close();
        connection.close();
        return activities;
    }

    /**
     * @param activity
     * @return
     * @throws SQLException
     */
    public int getActivityID(Activity activity) throws SQLException {
        return getActivityID(activity.getActivityName(), activity.getCourseID());
    }

    /**
     * Get the database activityID given the name of the activity and the courseID
     * to which it belongs too
     * 
     * @param activityName
     * @param courseID
     * @return
     * @throws SQLException
     */
    public int getActivityID(String activityName, int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT ActivityID FROM activities WHERE CourseID = ? and activityName= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        preparedStatement.setString(2, activityName);
        ResultSet result = preparedStatement.executeQuery();
        int activityId = 0;
        if (result.next()) {
            activityId = result.getInt("ActivityID");
        }
        preparedStatement.close();
        connection.close();
        return activityId;
    }

    /**
     * Get Activity and activity sessions related to that activity
     * 
     * @param courseCode
     * @param year
     * @return
     * @throws SQLException
     */
    public List<Integer> getCourseActivities(String courseCode, int year) throws SQLException {
        int courseID = getCourseID(courseCode, year);
        return getCourseActivities(courseID);
    }

    /**
     * Returns a list of activites for a particular course given a courseID
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public List<Activity> getActivitiesForCourse(int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM activities WHERE CourseID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        List<Activity> lstActivities = new ArrayList<>();
        while (rs.next()) {
            Activity activity = new Activity(rs.getString("ActivityName"), rs.getString("ActivityDescription"),
                    rs.getString("ActivityType"), courseID);
            lstActivities.add(activity);
        }
        preparedStatement.close();
        connection.close();
        return lstActivities;
    }

    /**
     * Returns a list of activityIDs for a particular course given the courseID
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public List<Integer> getCourseActivities(int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT ActivityID FROM activities WHERE CourseID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        List<Integer> lstActivityIDs = new ArrayList<>();
        while (rs.next()) {
            lstActivityIDs.add(rs.getInt("ActivityID"));
        }
        preparedStatement.close();
        connection.close();
        return lstActivityIDs;
    }

    /**
     * Gets a specific activity given the activityID
     * 
     * @param activityID
     * @return
     * @throws SQLException
     */
    public Activity getActivity(int activityID) throws SQLException {
        Activity activity = new Activity();
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM activities WHERE activityID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, activityID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            activity.setActivityName(rs.getString("activityName"));
            activity.setActivityDescription(rs.getString("ActivityDescription"));
            activity.setActivityType(rs.getString("ActivityType"));
            activity.setCourseID(rs.getInt("CourseID"));
            preparedStatement.close();
            connection.close();
            return activity;
        }
        preparedStatement.close();
        connection.close();
        return null;
    }

    // End Activities

    // Start Activity-Sessions
    /**
     * Returns a specific session given the sessionID
     * 
     * @param sessionID
     * @return
     * @throws SQLException
     */
    public ActivitySession getActivitySession(int sessionID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM activitysessions WHERE SessionID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, sessionID);
        ResultSet rs = preparedStatement.executeQuery();

        ActivitySession session = null;
        if (rs.next()) {
            session = new ActivitySession(rs.getInt("activityID"), rs.getString("startTime"), rs.getString("endTime"),
                    rs.getString("day"), rs.getString("venue"), rs.getInt("numberOfTutors"),
                    rs.getInt("expectedStudents"), rs.getBoolean("recurring"), rs.getString("RecurFrom"),
                    rs.getString("RecurUntil"), rs.getString("openingDate"), rs.getString("closingDate"));
        }
        preparedStatement.close();
        connection.close();
        return session;
    }

    /**
     * Returns all the sessions for a particular course
     * 
     * @param courseID
     * @return
     * @throws SQLException
     */
    public List<Integer> getSessionsIDList(int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT sessionID"
                + " FROM activitysessions JOIN activities ON activities.activityID = activitySessions.activityID "
                + " WHERE activities.courseID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        List<Integer> lstSessions = new ArrayList<Integer>();
        while (rs.next()) {
            lstSessions.add(rs.getInt("sessionID"));
        }

        preparedStatement.close();
        connection.close();
        return lstSessions;
    }

    /**
     * Returns all the sessions for a particular activity
     * 
     * @param activityID
     * @return
     * @throws SQLException
     */
    public List<Integer> getSessionsForActivity(int activityID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT sessionID"
                + " FROM activitysessions"
                + " WHERE activityID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, activityID);
        ResultSet rs = preparedStatement.executeQuery();

        List<Integer> lstSessions = new ArrayList<Integer>();
        while (rs.next()) {
            lstSessions.add(rs.getInt("sessionID"));
        }

        preparedStatement.close();
        connection.close();
        return lstSessions;
    }

    /**
     * Gets the number of signed ups for a particular session
     * 
     * @param sessionID
     * @return
     * @throws SQLException
     */
    public int getNumSignups(int sessionID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT SessionID, count(sessionID) as numSessions FROM sessionsignups WHERE sessionID = ? GROUP BY sessionID";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, sessionID);

        ResultSet rs = preparedStatement.executeQuery();
        int numSignups = 0;
        if (rs.next()) {
            numSignups = rs.getInt("numSessions");
        }
        preparedStatement.close();
        connection.close();
        return numSignups;
    }

    /**
     * Checks if a particular tutor has signed up for a particular activity
     * 
     * @param activityID
     * @param username
     * @return
     * @throws SQLException
     */
    public boolean checkSignup(int activityID, String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT sessionsignups.sessionID FROM"
                + " activitysessions JOIN sessionsignups ON activitySessions.sessionID = sessionsignups.sessionID"
                + " WHERE activitysessions.activityID = ? AND tutorUsername = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, activityID);
        preparedStatement.setString(2, username);
        ResultSet rs = preparedStatement.executeQuery();

        boolean hasSigned = rs.next();
        preparedStatement.close();
        connection.close();
        return hasSigned;
    }

    /**
     * Gets all the sessions that are signed up for
     * 
     * @param sessionID
     * @return
     * @throws SQLException
     */
    public List<String> getTutorSignup(int sessionID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT TutorUsername FROM sessionsignups WHERE sessionID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, sessionID);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> lstTutors = new ArrayList<>();
        while (rs.next()) {
            lstTutors.add(rs.getString("TutorUsername"));
        }
        preparedStatement.close();
        connection.close();
        return lstTutors;
    }

    /**
     * Gets all the tutor sessions for a particular tutor regardless of the course
     * 
     * @param tutorUsername
     * @return
     * @throws SQLException
     */
    public List<Integer> getTutorSessions(String tutorUsername) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT sessionID"
                + " FROM sessionsignups"
                + " WHERE tutorUsername = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, tutorUsername);
        ResultSet rs = preparedStatement.executeQuery();

        List<Integer> lstSessions = new ArrayList<Integer>();
        while (rs.next()) {
            lstSessions.add(rs.getInt("sessionID"));
        }

        preparedStatement.close();
        connection.close();
        return lstSessions;
    }

    /**
     * Gets all the sessions for a particular tutor for a specific course
     * 
     * @param tutorUsername
     * @param courseID
     * @return
     * @throws SQLException
     */
    public List<Integer> getTutorSessions(String tutorUsername, int courseID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT sessionsignups.sessionID"
                + " FROM sessionsignups JOIN activities JOIN activitysessions"
                + " ON sessionsignups.sessionID = activitysessions.sessionID"
                + " AND activities.activityID = activitysessions.activityID"
                + " WHERE tutorUsername = ? AND courseID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, tutorUsername);
        preparedStatement.setInt(2, courseID);
        ResultSet rs = preparedStatement.executeQuery();

        List<Integer> lstSessions = new ArrayList<Integer>();
        while (rs.next()) {
            lstSessions.add(rs.getInt("sessionID"));
        }

        preparedStatement.close();
        connection.close();
        return lstSessions;
    }

    /**
     * Checks whether a session is open for sign ups
     * 
     * @param sessionID
     * @return
     * @throws SQLException
     */
    public boolean isSessionOpen(int sessionID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT sessionID FROM activitysessions WHERE (NOW() BETWEEN openingDate AND closingDate) AND (sessionID = ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, sessionID);

        ResultSet rs = preparedStatement.executeQuery();
        boolean isOpen = rs.next();
        preparedStatement.close();
        connection.close();
        return isOpen;

    }

    /**
     * Returns a session log to monitor tutor sessions
     * 
     * @param courseID
     * @param dayWhen
     * @return
     * @throws SQLException
     */
    public List<SessionLog> getSessionLogs(int courseID, String dayWhen) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM sessionlogs JOIN activitysessions JOIN activities "
                + " ON sessionlogs.sessionID = activitysessions.sessionID "
                + " AND activities.activityID = activitysessions.activityID "
                + " WHERE activities.courseID = ?  AND DATE(sessionlogs.startTime) = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, courseID);
        preparedStatement.setString(2, dayWhen);
        ResultSet rs = preparedStatement.executeQuery();

        List<SessionLog> lstLogs = new ArrayList<>();
        while (rs.next()) {
            SessionLog log = new SessionLog(rs.getInt("logID"), rs.getString("tutorUsername"),
                    rs.getString("sessionlogs.startTime"), rs.getString("sessionlogs.endTime"),
                    rs.getString("sessionFeedback"), rs.getInt("sessionLogs.sessionID"),
                    rs.getString("startingCoordinates"), rs.getString("finishingCoordinates"));
            lstLogs.add(log);
        }
        return lstLogs;

    }

    // End Activity-Sessions

    // Start Post
    /**
     * Gets the application post description.
     * 
     * @param postIndentifier
     * @return
     * @throws SQLException
     */
    public String getPostDecription(String postIndentifier) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT postDescription FROM applicationposts WHERE postIdentifier = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, postIndentifier);
        ResultSet rs = preparedStatement.executeQuery();

        String postDescription = "";
        if (rs.next()) {
            postDescription = rs.getString("postDescription");
        }
        preparedStatement.close();
        connection.close();
        return postDescription;
    }

    /**
     * Using the post identifier, this method checks if the application is still
     * open for new applications
     * 
     * @param postIdentifier
     * @return
     * @throws SQLException
     */
    public boolean isApplicationOpen(String postIdentifier) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM applicationposts WHERE (NOW() BETWEEN openDate AND closingDate) AND (postIdentifier = ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, postIdentifier);

        ResultSet rs = preparedStatement.executeQuery();
        boolean isOpen = rs.next();
        preparedStatement.close();
        connection.close();
        return isOpen;
    }

    /**
     * Returns the role which the application post is for.
     * 
     * @param postIndentifier
     * @return
     * @throws SQLException
     */
    public String getPostRole(String postIndentifier) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT role FROM applicationposts WHERE postIdentifier = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, postIndentifier);
        ResultSet rs = preparedStatement.executeQuery();

        String role = "";
        if (rs.next()) {
            role = rs.getString("role");
        }
        preparedStatement.close();
        connection.close();
        return role;
    }

    /**
     * Returns all the application post identifiers from the database using the
     * username of the post's creator as a filter.
     * 
     * @param creatorUsername
     * @return
     * @throws SQLException
     */

    public List<String> getCreatorPosts(String creatorUsername) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT postIdentifier FROM applicationposts WHERE creatorID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, creatorUsername);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> postNames = new ArrayList<String>();
        while (rs.next()) {
            postNames.add(rs.getString("postIdentifier"));
        }
        preparedStatement.close();
        connection.close();
        return postNames;
    }

    /**
     * Returns all the application post identifiers from the database
     * 
     * @return
     * @throws SQLException
     */
    public List<String> getPostNames() throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT postIdentifier FROM applicationposts";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();

        List<String> postNames = new ArrayList<String>();
        while (rs.next()) {
            postNames.add(rs.getString("postIdentifier"));
        }
        preparedStatement.close();
        connection.close();
        return postNames;
    }
    // End Post

    // Start Application
    /**
     * Returns a list of applicants for a particular application post usign the
     * applicant's application status as a filter
     * 
     * @param postIdentifier
     * @param selectedStatuses
     * @return
     * @throws SQLException
     */
    public List<String> getPostApplicants(String postIdentifier, String selectedStatuses) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT studentNumber FROM applicants WHERE postIdentifier = ? AND applicationStatus IN ("
                + selectedStatuses + ")";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, postIdentifier);

        ResultSet rs = preparedStatement.executeQuery();
        List<String> lstApplicants = new ArrayList<String>();

        while (rs.next()) {
            lstApplicants.add(rs.getString("studentNumber"));
        }

        preparedStatement.close();
        connection.close();
        return lstApplicants;
    }

    /**
     * Checks whether a applicant exists given a username
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public boolean applicantExists(String username) throws SQLException {
        int applicantID = getApplicantID(username);

        if (applicantID <= 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param applicantID
     * @param username
     * @return
     * @throws SQLException
     */
    public Application getApplicantObject(int applicantID, String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM applicants WHERE applicantID = ? AND studentNumber = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, applicantID);
        preparedStatement.setString(2, username);

        ResultSet rs = preparedStatement.executeQuery();
        applicantID = 0;
        if (rs.next()) {
            applicantID = rs.getInt("applicantID");
        }
        if (applicantID == 0) {
            preparedStatement.close();
            connection.close();
            return null;
        }

        preparedStatement.close();
        connection.close();
        return getApplicantObj(applicantID);

    }

    /**
     * Returns a list of applicant student numbers for a given application post
     * identifier
     * 
     * @param postIdentifier
     * @return
     * @throws SQLException
     */
    public List<String> getPostApplicants(String postIdentifier) throws SQLException {

        int lenIdentifier = postIdentifier.length();
        Connection connection = DBConnection.getConnection();

        String query = "SELECT studentNumber FROM applicants";
        PreparedStatement preparedStatement;
        if (lenIdentifier > 0) {

            query += " WHERE postIdentifier = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, postIdentifier);
        } else {
            preparedStatement = connection.prepareStatement(query);
        }

        ResultSet rs = preparedStatement.executeQuery();

        List<String> applicantUsernames = new ArrayList<String>();
        while (rs.next()) {
            applicantUsernames.add(rs.getString("studentNumber"));
        }
        preparedStatement.close();
        connection.close();
        return applicantUsernames;

    }

    /**
     * Gets the applicantID using the username of the applicant
     * 
     * @param username
     * @return
     * @throws SQLException
     */
    public int getApplicantID(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT applicantID FROM applicants WHERE studentNumber = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);

        ResultSet rs = preparedStatement.executeQuery();
        int applicantID = 0;
        if (rs.next()) {
            applicantID = rs.getInt("applicantID");
        }
        preparedStatement.close();
        connection.close();
        return applicantID;
    }

    /**
     * @param applicant
     * @return
     * @throws SQLException
     */
    public int getApplicantID(Application applicant) throws SQLException {
        return getApplicantID(applicant.getPostIdentifier(), applicant.getUsername());
    }

    public int getLatestApplication(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT applicantID FROM applicants WHERE StudentNumber = ? ORDER BY applicantID DESC LIMIT 1";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();

        int applicantID = 0;
        if (rs.next()) {
            applicantID = rs.getInt("applicantID");
        }

        connection.close();
        return applicantID;
    }

    /**
     * @param postIdentifier
     * @param username
     * @return
     * @throws SQLException
     */
    public Application getApplicantObj(String postIdentifier, String username) throws SQLException {
        int applicantID = getApplicantID(postIdentifier, username);
        return getApplicantObj(applicantID);
    }

    /**
     * @param applicant
     * @return
     * @throws SQLException
     */
    public Application getApplicantObj(Application applicant) throws SQLException {
        int applicantID = getApplicantID(applicant.getPostIdentifier(), applicant.getUsername());
        return getApplicantObj(applicantID);
    }

    /**
     * @param applicant
     * @return
     * @throws SQLException
     */
    public Application getUploadedDocuments(Application applicant) throws SQLException {
        int applicantID = getApplicantID(applicant.getPostIdentifier(), applicant.getUsername());

        return getUploadedDocuments(applicant, applicantID);
    }

    /**
     * Gets the uploaded documents of the applicant
     * 
     * @param applicant
     * @param applicantID
     * @return
     * @throws SQLException
     */
    public Application getUploadedDocuments(Application applicant, int applicantID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM applicantsdocuments WHERE applicantID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, applicantID);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            applicant.addDoc(rs.getString("DocumentType"), rs.getString("DocumentName"), rs.getString("StoragePath"));
        }
        preparedStatement.close();
        connection.close();
        return applicant;
    }

    /**
     * Gets the marks of a particular applicant
     * 
     * @param applicant
     * @param applicantID
     * @return
     * @throws SQLException
     */
    public Application getCourseMarks(Application applicant, int applicantID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM applicantsmarks WHERE applicantID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, applicantID);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            applicant.addMarks(rs.getString("CourseCode"), rs.getInt("CourseMark"));
        }
        preparedStatement.close();
        connection.close();
        return applicant;
    }

    /**
     * Returns the submitted application using the database applicantID
     * 
     * @param applicantID
     * @return
     * @throws SQLException
     */
    public Application getApplicantObj(int applicantID) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM applicants WHERE applicantID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, applicantID);
        ResultSet rs = preparedStatement.executeQuery();

        Application applicant = null;
        if (rs.next()) {
            applicant = new Application("", "", "", "", "", "", "", "", "", 0, "");
            applicant.setFirstName(rs.getString("ApplicantName"));
            applicant.setLastName(rs.getString("ApplicantLastName"));
            applicant.setUsername(rs.getString("StudentNumber"));
            applicant.setEmailAddress(rs.getString("ApplicantEmail"));
            applicant.setQualifications(rs.getString("Qualifications"));
            applicant.setContactNumber(rs.getString("ContactNumber"));
            applicant.setStudyLevel(rs.getString("studyLevel"));
            applicant.setPostIdentifier(rs.getString("PostIdentifier"));
            applicant.setYearOfStudy(rs.getInt("YearOfStudy"));
            applicant.setApplicationStatus(rs.getString("applicationStatus"));
            applicant = getUploadedDocuments(applicant, applicantID);
            applicant = getCourseMarks(applicant, applicantID);
        }
        preparedStatement.close();
        connection.close();
        return applicant;
    }

    /**
     * Get database applicantID using the username of the applicant and the
     * identifier of an application post.
     * 
     * @param postIdentifier
     * @param username
     * @return
     * @throws SQLException
     */
    public int getApplicantID(String postIdentifier, String username) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT applicantID FROM applicants WHERE studentNumber = ? AND postIdentifier = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, postIdentifier);

        ResultSet rs = preparedStatement.executeQuery();
        int applicantID = 0;
        if (rs.next()) {
            applicantID = rs.getInt("applicantID");
        }
        preparedStatement.close();
        connection.close();
        return applicantID;
    }

    /**
     * This method is used to get the applicants information for a particular
     * application post.
     * 
     * @param lstUsernames
     * @param postIdentifier
     * @return
     * @throws SQLException
     */
    public List<String> getApplicantsInfo(String lstUsernames, String postIdentifier) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM applicants WHERE postIdentifier = ? AND  studentNumber IN (" + lstUsernames + ")";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, postIdentifier);

        ResultSet rs = preparedStatement.executeQuery();
        List<String> lstInfo = new ArrayList<String>();

        while (rs.next()) {
            String info = String.format("%5d%15s%20s%20s", rs.getInt("applicantID"), rs.getString("studentNumber"),
                    rs.getString("postIdentifier"), rs.getString("applicationStatus"));
            lstInfo.add(info);
        }

        preparedStatement.close();
        connection.close();
        return lstInfo;
    }

    /**
     * This method is used to get a list of applicant usernames for a particular
     * application post.
     * 
     * @param lstUsernames
     * @param postIdentifier
     * @return
     * @throws SQLException
     */
    public List<String> getApplicantsUsername(String lstUsernames, String postIdentifier) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM applicants WHERE postIdentifier = ? AND  studentNumber IN (" + lstUsernames + ")";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, postIdentifier);

        ResultSet rs = preparedStatement.executeQuery();
        List<String> lstInfo = new ArrayList<String>();

        while (rs.next()) {
            String info = rs.getString("studentNumber");
            lstInfo.add(info);
        }

        preparedStatement.close();
        connection.close();
        return lstInfo;
    }

    /**
     * This method is used to chack whether a applicant uploaded documents
     * 
     * @param applicantID
     * @param docType
     * @return
     * @throws SQLException
     */
    public boolean applicantDocExists(int applicantID, String docType) throws SQLException {
        Connection connection = DBConnection.getConnection();

        String query = "SELECT * FROM applicantsdocuments WHERE applicantID = ? AND documentType = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, applicantID);
        preparedStatement.setString(2, docType);

        ResultSet rs = preparedStatement.executeQuery();
        boolean exists = rs.next();
        preparedStatement.close();
        connection.close();
        return exists;
    }

    // End Application

}