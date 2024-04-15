-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: mytutordb
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activities`
--

DROP TABLE IF EXISTS `activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activities` (
  `ActivityID` int NOT NULL AUTO_INCREMENT,
  `ActivityName` mediumtext NOT NULL,
  `ActivityDescription` longtext,
  `ActivityType` mediumtext,
  `CourseID` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`ActivityID`),
  KEY `CourseActivityFK_idx` (`CourseID`),
  CONSTRAINT `ActivityCourseFK` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`CourseID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activities`
--

LOCK TABLES `activities` WRITE;
/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
/*!40000 ALTER TABLE `activities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activitysessions`
--

DROP TABLE IF EXISTS `activitysessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activitysessions` (
  `SessionID` int NOT NULL AUTO_INCREMENT,
  `ActivityID` int NOT NULL,
  `StartTime` time NOT NULL,
  `EndTime` time NOT NULL,
  `Day` varchar(45) NOT NULL,
  `Venue` varchar(45) NOT NULL,
  `NumberOfTutors` int NOT NULL,
  `ExpectedStudents` int DEFAULT NULL,
  `Recurring` tinyint(1) NOT NULL,
  `RecurFrom` date DEFAULT NULL,
  `RecurUntil` date DEFAULT NULL,
  `OpeningDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ClosingDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`SessionID`),
  UNIQUE KEY `SessionID_UNIQUE` (`SessionID`),
  KEY `ActivityID_idx` (`ActivityID`),
  CONSTRAINT `ActivityID` FOREIGN KEY (`ActivityID`) REFERENCES `activities` (`ActivityID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitysessions`
--

LOCK TABLES `activitysessions` WRITE;
/*!40000 ALTER TABLE `activitysessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `activitysessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrators`
--

DROP TABLE IF EXISTS `administrators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrators` (
  `AdminID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(45) NOT NULL,
  `Faculty` varchar(100) DEFAULT NULL,
  `Department` varchar(45) DEFAULT NULL,
  `AdminRole` varchar(45) NOT NULL DEFAULT 'Admin',
  PRIMARY KEY (`AdminID`,`Username`),
  UNIQUE KEY `AdminID_UNIQUE` (`AdminID`),
  UNIQUE KEY `Username_UNIQUE` (`Username`),
  CONSTRAINT `AdminUsernameFK` FOREIGN KEY (`Username`) REFERENCES `userprofiles` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrators`
--

LOCK TABLES `administrators` WRITE;
/*!40000 ALTER TABLE `administrators` DISABLE KEYS */;
INSERT INTO `administrators` VALUES (1,'mytutoradmin',NULL,NULL,'Admin');
/*!40000 ALTER TABLE `administrators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applicants`
--

DROP TABLE IF EXISTS `applicants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicants` (
  `ApplicantID` int NOT NULL AUTO_INCREMENT,
  `ApplicantName` varchar(45) NOT NULL,
  `ApplicantLastname` varchar(45) NOT NULL,
  `StudentNumber` varchar(45) NOT NULL,
  `ApplicantEmail` varchar(45) NOT NULL,
  `ContactNumber` varchar(45) NOT NULL,
  `postIdentifier` varchar(45) NOT NULL,
  `Qualifications` mediumtext,
  `YearOfStudy` int DEFAULT NULL,
  `StudyLevel` varchar(45) DEFAULT NULL,
  `ApplicationStatus` varchar(45) NOT NULL DEFAULT 'SUBMITTED',
  PRIMARY KEY (`ApplicantID`),
  UNIQUE KEY `ApplicantID_UNIQUE` (`ApplicantID`),
  UNIQUE KEY `applicantUniq` (`StudentNumber`,`postIdentifier`),
  KEY `ApplicationPostID_idx` (`postIdentifier`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicants`
--

LOCK TABLES `applicants` WRITE;
/*!40000 ALTER TABLE `applicants` DISABLE KEYS */;
/*!40000 ALTER TABLE `applicants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applicantsdocuments`
--

DROP TABLE IF EXISTS `applicantsdocuments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicantsdocuments` (
  `DocumentID` int NOT NULL AUTO_INCREMENT,
  `DocumentType` varchar(45) NOT NULL,
  `DocumentName` mediumtext NOT NULL,
  `ApplicantID` int NOT NULL,
  `StoragePath` mediumtext NOT NULL,
  PRIMARY KEY (`DocumentID`),
  KEY `DocumentApplicantFK_idx` (`ApplicantID`),
  CONSTRAINT `DocumentApplicantFK` FOREIGN KEY (`ApplicantID`) REFERENCES `applicants` (`ApplicantID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicantsdocuments`
--

LOCK TABLES `applicantsdocuments` WRITE;
/*!40000 ALTER TABLE `applicantsdocuments` DISABLE KEYS */;
/*!40000 ALTER TABLE `applicantsdocuments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applicantsmarks`
--

DROP TABLE IF EXISTS `applicantsmarks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicantsmarks` (
  `MarksID` int NOT NULL AUTO_INCREMENT,
  `ApplicantID` int NOT NULL,
  `CourseCode` varchar(45) NOT NULL,
  `CourseMark` int NOT NULL,
  PRIMARY KEY (`MarksID`),
  UNIQUE KEY `MarksID_UNIQUE` (`MarksID`),
  KEY `MarksFK_idx` (`ApplicantID`),
  CONSTRAINT `MarksFK` FOREIGN KEY (`ApplicantID`) REFERENCES `applicants` (`ApplicantID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicantsmarks`
--

LOCK TABLES `applicantsmarks` WRITE;
/*!40000 ALTER TABLE `applicantsmarks` DISABLE KEYS */;
/*!40000 ALTER TABLE `applicantsmarks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `applicationposts`
--

DROP TABLE IF EXISTS `applicationposts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applicationposts` (
  `postID` int NOT NULL AUTO_INCREMENT,
  `postIdentifier` varchar(100) NOT NULL,
  `postDescription` longtext NOT NULL,
  `Role` varchar(45) NOT NULL,
  `OpenDate` date NOT NULL,
  `ClosingDate` date NOT NULL,
  `CreatorID` varchar(45) NOT NULL,
  `Department` varchar(45) NOT NULL,
  PRIMARY KEY (`postID`,`postIdentifier`),
  UNIQUE KEY `postID_UNIQUE` (`postID`),
  UNIQUE KEY `postIdentifier_UNIQUE` (`postIdentifier`),
  KEY `PostCreatorFK_idx` (`CreatorID`),
  CONSTRAINT `PostCreatorFK` FOREIGN KEY (`CreatorID`) REFERENCES `userprofiles` (`Username`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applicationposts`
--

LOCK TABLES `applicationposts` WRITE;
/*!40000 ALTER TABLE `applicationposts` DISABLE KEYS */;
/*!40000 ALTER TABLE `applicationposts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coursedetails`
--

DROP TABLE IF EXISTS `coursedetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coursedetails` (
  `DetailsID` int NOT NULL AUTO_INCREMENT,
  `CourseID` int NOT NULL,
  `Role` varchar(45) NOT NULL,
  `Username` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`DetailsID`),
  UNIQUE KEY `detailsID_UNIQUE` (`DetailsID`),
  UNIQUE KEY `uniqTutorperCourse` (`CourseID`,`Username`),
  UNIQUE KEY `uniqTutorForCourse` (`CourseID`,`Username`,`Role`),
  KEY `DetailsCourseID_idx` (`CourseID`),
  CONSTRAINT `DetailsCourseID` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`CourseID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coursedetails`
--

LOCK TABLES `coursedetails` WRITE;
/*!40000 ALTER TABLE `coursedetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `coursedetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `CourseID` int NOT NULL AUTO_INCREMENT,
  `CourseCode` varchar(20) NOT NULL,
  `CourseYear` year NOT NULL,
  `Course_Name` varchar(45) GENERATED ALWAYS AS (concat(`CourseCode`,_utf8mb4',',`CourseYear`)) VIRTUAL,
  `CourseDescription` varchar(45) NOT NULL,
  `CreatorID` varchar(45) NOT NULL,
  `Department` varchar(45) DEFAULT NULL,
  `NumberOfStudents` int DEFAULT NULL,
  PRIMARY KEY (`CourseID`),
  UNIQUE KEY `coursesNameUniq` (`CourseCode`,`CourseYear`),
  KEY `CourseCreatorFK_idx` (`CreatorID`),
  CONSTRAINT `CourseCreatorFK` FOREIGN KEY (`CreatorID`) REFERENCES `administrators` (`Username`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturers`
--

DROP TABLE IF EXISTS `lecturers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturers` (
  `LecturerID` int NOT NULL AUTO_INCREMENT,
  `LecturerRole` varchar(45) NOT NULL DEFAULT 'Lecturer',
  `Department` varchar(45) DEFAULT NULL,
  `Username` varchar(45) NOT NULL,
  PRIMARY KEY (`LecturerID`,`Username`),
  UNIQUE KEY `LecturerID_UNIQUE` (`LecturerID`),
  UNIQUE KEY `Username_UNIQUE` (`Username`),
  KEY `LecturerUsernameFK_idx` (`Username`),
  CONSTRAINT `LecturerUsernameFK` FOREIGN KEY (`Username`) REFERENCES `userprofiles` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturers`
--

LOCK TABLES `lecturers` WRITE;
/*!40000 ALTER TABLE `lecturers` DISABLE KEYS */;
/*!40000 ALTER TABLE `lecturers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logindetails`
--

DROP TABLE IF EXISTS `logindetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logindetails` (
  `EntryID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(45) NOT NULL,
  `LoginPassword` mediumtext NOT NULL,
  `Role` varchar(45) NOT NULL,
  `LastLogin` datetime DEFAULT NULL,
  `LoggedOut` datetime DEFAULT NULL,
  PRIMARY KEY (`EntryID`,`Username`),
  UNIQUE KEY `username_UNIQUE` (`Username`),
  UNIQUE KEY `EntryID_UNIQUE` (`EntryID`),
  CONSTRAINT `loginUsername` FOREIGN KEY (`Username`) REFERENCES `userprofiles` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logindetails`
--

LOCK TABLES `logindetails` WRITE;
/*!40000 ALTER TABLE `logindetails` DISABLE KEYS */;
INSERT INTO `logindetails` VALUES (1,'mytutoradmin','password','ROLE_ADMIN',NULL,NULL);
/*!40000 ALTER TABLE `logindetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessionlogs`
--

DROP TABLE IF EXISTS `sessionlogs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessionlogs` (
  `LogID` int NOT NULL AUTO_INCREMENT,
  `StartTime` datetime DEFAULT NULL,
  `EndTime` datetime DEFAULT NULL,
  `SessionFeedback` longtext,
  `SessionID` int NOT NULL,
  `StartingCoordinates` varchar(45) DEFAULT NULL,
  `FinishingCoordinates` varchar(45) DEFAULT NULL,
  `TutorUsername` varchar(45) NOT NULL,
  PRIMARY KEY (`LogID`),
  KEY `LogSignupIDFK_idx` (`SessionID`),
  KEY `LogTutorUsername_idx` (`TutorUsername`),
  CONSTRAINT `LogSignupIDFK` FOREIGN KEY (`SessionID`) REFERENCES `activitysessions` (`SessionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LogTutorUsername` FOREIGN KEY (`TutorUsername`) REFERENCES `userprofiles` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessionlogs`
--

LOCK TABLES `sessionlogs` WRITE;
/*!40000 ALTER TABLE `sessionlogs` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessionlogs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessionsignups`
--

DROP TABLE IF EXISTS `sessionsignups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessionsignups` (
  `SignupID` int NOT NULL AUTO_INCREMENT,
  `TutorUsername` varchar(45) NOT NULL,
  `SessionID` int NOT NULL,
  `SignupTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`SignupID`),
  UNIQUE KEY `signupID_UNIQUE` (`SignupID`) /*!80000 INVISIBLE */,
  UNIQUE KEY `OneTutorPerSession` (`TutorUsername`,`SessionID`) /*!80000 INVISIBLE */,
  UNIQUE KEY `RestrictTutor` (`TutorUsername`,`SessionID`),
  KEY `SessionFK_idx` (`SessionID`),
  KEY `TutorSessionFK_idx` (`TutorUsername`),
  CONSTRAINT `SessionFK` FOREIGN KEY (`SessionID`) REFERENCES `activitysessions` (`SessionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TutorSessionFK` FOREIGN KEY (`TutorUsername`) REFERENCES `students` (`StudentNumber`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessionsignups`
--

LOCK TABLES `sessionsignups` WRITE;
/*!40000 ALTER TABLE `sessionsignups` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessionsignups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session`
--

DROP TABLE IF EXISTS `spring_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session`
--

LOCK TABLES `spring_session` WRITE;
/*!40000 ALTER TABLE `spring_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session_attributes`
--

DROP TABLE IF EXISTS `spring_session_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session_attributes`
--

LOCK TABLES `spring_session_attributes` WRITE;
/*!40000 ALTER TABLE `spring_session_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `StudentID` int NOT NULL AUTO_INCREMENT,
  `StudentNumber` varchar(45) NOT NULL,
  `Role` varchar(45) NOT NULL DEFAULT 'student',
  `StudyLevel` varchar(45) DEFAULT NULL,
  `YearOfStudy` int DEFAULT '2',
  `Majors` mediumtext,
  `Faculty` mediumtext,
  PRIMARY KEY (`StudentID`,`StudentNumber`),
  UNIQUE KEY `StudentID_UNIQUE` (`StudentID`),
  UNIQUE KEY `StudentNumber_UNIQUE` (`StudentNumber`),
  CONSTRAINT `StudentUsername` FOREIGN KEY (`StudentNumber`) REFERENCES `userprofiles` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userprofiles`
--

DROP TABLE IF EXISTS `userprofiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userprofiles` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(45) NOT NULL DEFAULT 'name',
  `LastName` varchar(45) NOT NULL DEFAULT 'lastname',
  `Username` varchar(45) NOT NULL,
  `EmailAddress` varchar(45) NOT NULL DEFAULT 'email',
  `ContactNumber` varchar(45) NOT NULL DEFAULT '0123456789',
  `Title` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`UserID`,`Username`),
  UNIQUE KEY `AccountID_UNIQUE` (`UserID`),
  UNIQUE KEY `username_UNIQUE` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userprofiles`
--

LOCK TABLES `userprofiles` WRITE;
/*!40000 ALTER TABLE `userprofiles` DISABLE KEYS */;
INSERT INTO `userprofiles` VALUES (1,'name','lastname','mytutoradmin','my2023tutor@gmail.com','0123456789',NULL);
/*!40000 ALTER TABLE `userprofiles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-25 23:36:52
