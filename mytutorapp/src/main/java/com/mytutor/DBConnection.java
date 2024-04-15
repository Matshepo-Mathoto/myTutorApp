package com.mytutor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * Allows creation of connection to MySQL database server
 */
@AutoConfiguration
public class DBConnection {
    /**
     * Creates a connection to the MySQL database saved on a remote or local MySQL server using JDBC and
     * driver manager;
     * @return the sql.Connection object for the connection to the database
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        /*if (connection != null){
            return connection;
        }*/
        String url = "jdbc:mysql://NDLMDU011:3306/mytutordb";
        String username = "ndlmdu011";//"nynsph001";// "mthmat006";
        String password = "Ntokozo01";//"Sphiwe01"; // "Matshepo06";

        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }
}

