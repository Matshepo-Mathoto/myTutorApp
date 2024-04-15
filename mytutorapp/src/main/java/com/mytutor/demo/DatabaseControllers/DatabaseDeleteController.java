package com.mytutor.demo.DatabaseControllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mytutor.DBConnection;

@Component
public class DatabaseDeleteController {
    @Autowired
    DatabaseQueryController dbQueryController;

    /**
     * This methods deletes a person/user from the database given the username. This method is used by the administrator.
     * @param username
     * @return
     * @throws SQLException
     */
    public int deletePerson(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        String query = "DELETE FROM userprofiles WHERE username = ?";
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            res = preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
        } catch (Exception e) {
            connection.rollback();
            System.err.println(e);
        }
        connection.close();
        return res;
    }

    /**
     * This methods deletes a student from the database given the username. This method is used by the administrator.
     * @param username
     * @return
     * @throws SQLException
     */
    public int deleteStudent(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        String query = "DELETE FROM students WHERE StudentNumber = ?";
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ;

            res = preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
        } catch (Exception e) {
            connection.rollback();
            System.err.println(e);
        }
        connection.close();
        return res;
    }

    /**
     * This methods deletes a administrator from the database given the username. This method is used by the administrator.
     * @param username
     * @return
     * @throws SQLException
     */
    public int deleteAdmin(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        String query = "DELETE FROM administrators WHERE username = ?";
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ;

            res = preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
        } catch (Exception e) {
            connection.rollback();
            System.err.println(e);
        }
        connection.close();
        return res;
    }

    /**
     * 
     * This methods deletes a lecturer from the database given the username. This method is used by the administrator.
     * @param username
     * @return
     * @throws SQLException
     */
    public int deleteLecturer(String username) throws SQLException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        String query = "DELETE FROM lecturers WHERE username = ?";
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ;

            res = preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
        } catch (Exception e) {
            connection.rollback();
            System.err.println(e);
        }
        connection.close();
        return res;
    }

    /**
     * This methods deletes a course from the database given the username. This method is used by the administrator.
     * @param courseName
     * @return
     * @throws SQLException
     */
    public int deleteCourse(String courseName) throws SQLException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        String query = "DELETE FROM courses WHERE course_name = ?";
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, courseName);
            ;

            res = preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
        } catch (Exception e) {
            connection.rollback();
            System.err.println(e);
        }
        connection.close();
        return res;
    }

    /**
     * This methods deletes a activity from the database given the activityID.
     * @param activityID
     * @return
     * @throws SQLException
     */
    public int deleteActivity(int activityID) throws SQLException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        String query = "DELETE FROM activities WHERE activityID = ?";
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, activityID);
            res = preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
        } catch (Exception e) {
            connection.rollback();
            System.err.println(e);
        }
        connection.close();
        return res;
    }

    /**
     * This methods deletes a applicant from the database given the applicantID
     * @param applicantID
     * @return
     * @throws SQLException
     */
    public int deleteApplicant(int applicantID) throws SQLException {
        Connection connection = DBConnection.getConnection();
        connection.setAutoCommit(false);
        String query = "DELETE FROM applicants WHERE applicantID = ?";
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, applicantID);

            res = preparedStatement.executeUpdate();
            connection.commit();
            preparedStatement.close();
        } catch (Exception e) {
            connection.rollback();
            System.err.println(e);
        }
        connection.close();
        return res;
    }
    /**
     * This methods deletes a applicant from the database given the applicant's username and the application post identifier
     * @param username
     * @return
     * @throws SQLException
     */
    public int deleteApplicant(String username, String postIdentifier) throws SQLException {
        int applicantID = dbQueryController.getApplicantID(postIdentifier, username);

        return deleteApplicant(applicantID);
    }
}
