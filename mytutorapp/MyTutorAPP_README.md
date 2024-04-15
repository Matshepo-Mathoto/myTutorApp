# How to run MyTutorApp(Server):

## External Structures or services:
Dependencies for these are added in "pom.xml" file and are downloaded when the system is run or compiled 
- The Uses a MySQL database which can be locally or remote
- Makes connection to database using jdbc driver
- Has HttpSession management using jdbc and database
- Uses Spring framework Security with SecurityFilterChain, BcryptPasswordEncoder, JDBCHttpSession, etc
- Makes use of Google Email to send messages from the App
- Uses an IpInfo API for getting client geolocation
- Uses Local Storage and directory for creating, storing and managing documents and files for Templates or WebPage use. These must be within src/resources folder.
- Java Runtime Environment (JRE)
- Uses maven and java 8 or higher (must be added or upgraded on the system to run the application on)

## Preparations:
1. Must have MyTutorDB sql dump with Database tables, fields, constraints and foreign keys configured
2. Create a database schema named "mytutordb" and run the mytutordb sql dump to add tables and fields
3. There must be atleast one record in the Database tables UserProfiles, LoginDetails and Administrators.
4. The LoginDetails role must be "ROLE_ADMIN".
5. A valid email address for the user in UserProfiles for resetting password.
Example: 
- INSERT INTO userprofiles (username, emailAddress) VALUES ("mytutoradmin","my2023tutor@gmail.com");
- INSERT INTO administrators (username) VALUES ("mytutoradmin");
- INSERT INTO logindetails (username, loginpassword, role) VALUES ("mytutoradmin", "password","ROLE_ADMIN");
6. Have access to "mytutor2023@gmail.com" email

## Application properties:
### MySQL Database connection
1. You must provide the remote or localhost address to your MySQL database to the created database schema.
2. Provide datasource username and password for connection to your database schema
3. The connection should support multiple access and have administrative rights to it
4. Provide these details again on DBConnection.java file for the database connection using jdbc

### System Email
1. Provide mail host, port number, username(email address) and "App password" for the System email
2. Set the messageFrom parameter in EmailSenderService to the username you provided for your email adrdess

### Location services
1. Add your ip key for the location service api
2. It makes use of services by IpInfo
3. Can sign up at [Ip Info site to register and get api key](https://ipinfo.io/)

## Compiling or Running the application:
1. Run command "java -jar my-spring-boot-app-1.0.0.jar" on a command line if jar file was provided
2. mvn clean install -U and mvn clean package (to build from java files and whole package with system files and get a jar)
3. 

## Access to the website
1.hostIpAddress:8080/login e.g. [Local Access login](http://localhost:8080/login), [remote access login](http://196.47.239.204:8080/login)

## Excecuting the code on VisualStudio

# Required packages:
1. Java Extension
2. Maven for java
3. Spring boot dashboard
3. Spring boot Externsion package
4. Spring boot initializer java support

# To Excecute the program:
1. Add new java Spring boot Project
3. Select 3.2.0 snapshot version
4. Name the package com.mytutor.demo
5. Clone the git project to the new spring boot project.
6. Follow the instructions under subtopic: Preparations- to build the database and Application properties subtopics-to connect to the database and send emails.









