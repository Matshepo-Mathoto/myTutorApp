package com.mytutor.demo.object_files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Person {
    public final String SEPARATOR = "##";
    private String postIdentifier;
    private String qualifications;
    private String studyLevel;
    private int yearOfStudy;
    private String course;
    private List<String> applicantMarks;
    private String applicationStatus;
    private Map<String, Integer> allCourses; // adding a course with corresponding averagemark
    private List<List<String>> applicationDocs;
    
    public Application(String firstName, String username, String lastName, String contactNumber, String emailAddress,
            String title, String postIdentifier, String qualifications, String studyLevel, int yearOfStudy,
            String course, String applicationStatus) {
        super(firstName, username, lastName, contactNumber, emailAddress, title);
        this.postIdentifier = postIdentifier;
        this.qualifications = qualifications;
        this.studyLevel = studyLevel;
        this.yearOfStudy = yearOfStudy;
        this.course = course;
        this.applicationStatus = applicationStatus;
        this.applicantMarks = new ArrayList<String>();
        this.applicationDocs = new ArrayList<>();
        this.allCourses = new HashMap<>();
    }

    public Application(String postIdentifier, String qualifications, String studyLevel, int yearOfStudy,
            String applicationStatus) {
        this.postIdentifier = postIdentifier;
        this.qualifications = qualifications;
        this.studyLevel = studyLevel;
        this.yearOfStudy = yearOfStudy;
        this.applicantMarks = new ArrayList<String>();
        this.allCourses = new HashMap<>();
        this.applicationDocs = new ArrayList<>();
    }

    public Application(String firstName, String username, String lastName, String contactNumber, String emailAddress,
            String title, String postIdentifier, String qualifications, String studyLevel, int yearOfStudy,
            String applicationStatus) {
        super(firstName, username, lastName, contactNumber, emailAddress, title);
        this.postIdentifier = postIdentifier;
        this.qualifications = qualifications;
        this.studyLevel = studyLevel;
        this.yearOfStudy = yearOfStudy;
        this.applicationStatus = applicationStatus;
        this.applicantMarks = new ArrayList<String>();
        this.allCourses = new HashMap<>();
        this.applicationDocs = new ArrayList<>();
    }

    public Application(Person copyPerson, String postIdentifier, String qualifications, String studyLevel,
            int yearOfStudy, String applicationStatus) {
        super(copyPerson);
        this.postIdentifier = postIdentifier;
        this.qualifications = qualifications;
        this.studyLevel = studyLevel;
        this.yearOfStudy = yearOfStudy;
        this.applicationStatus = applicationStatus;
        this.applicationDocs = new ArrayList<>();
    }

    public Application() {
        this.applicantMarks = new ArrayList<String>();
        this.allCourses = new HashMap<>();
        this.applicationDocs = new ArrayList<>();
    }

    public int addDoc(String docType, String docName, String storagePath) {
        List<String> docItem = new ArrayList<>();
        docItem.add(docName);
        docItem.add(docType);
        docItem.add(storagePath);
        applicationDocs.add(docItem);
        return applicationDocs.size() - 1;
    }

    public int addMarks(String courseCode, int courseMark) {
        String markString = courseCode + SEPARATOR + courseMark;
        applicantMarks.add(markString);
        return applicantMarks.size() - 1;
    }

    public String getPostIdentifier() {
        return postIdentifier;
    }

    public void setPostIdentifier(String postIdentifier) {
        this.postIdentifier = postIdentifier;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getCourse() {
        return course;
    }

    public String getStudyLevel() {
        return studyLevel;
    }

    public void setStudyLevel(String studyLevel) {
        this.studyLevel = studyLevel;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void addCourse(String previousCourse, int averageMark) {
        if (checkExist(previousCourse) == false) {
            allCourses.put(previousCourse, averageMark);
        }

    }

    public boolean checkExist(String previousCourse) {
        boolean check = false;
        if (allCourses.containsKey(previousCourse)) {
            check = true;
        }
        return check;
    }

    public Map<String, Integer> getCourseMarks() {
        return allCourses;
    }

    public String getSEPARATOR() {
        return SEPARATOR;
    }

    public List<String> getApplicantMarks() {
        return applicantMarks;
    }

    public void setApplicantMarks(List<String> applicantMarks) {
        this.applicantMarks = applicantMarks;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public void setAllCourses(Map<String, Integer> allCourses) {
        this.allCourses = allCourses;
    }

    public List<List<String>> getApplicationDocs() {
        return applicationDocs;
    }

    public void setApplicationDocs(List<List<String>> applicationDocs) {
        this.applicationDocs = applicationDocs;
    }

    public String getMarks() {
        String marksToString = "";

        for (String s : applicantMarks) {
            String markList[] = s.split(SEPARATOR);
            marksToString += "Course Code:\t\t" + markList[0]
                    + "\nCourse Mark:\t\t" + markList[1] + "\n\n";
        }
        return marksToString;
    }

    public String getDocs() {
        String docsToString = "";

        for (List<String> docList : applicationDocs) {
            docsToString += "Document Type:\n" + docList.get(0)
                    + "\nDocument Name:\n" + docList.get(1) + "\n\n";// + "\nStorage Path:\n" + docList[2] + "\n\n";
        }
        return docsToString;
    }

    @Override
    public String toString() {
        String result = "Applicant Details:" + "\nFirst Name:\n"
                + super.getFirstName() + "\n\nLast Name:\n" + super.getLastName() + "\n\nStudent Number:\n"
                + super.getUsername() + "\n\nEmail Address:\n" + super.getEmailAddress() + "\n\nContact Number:\n"
                + super.getContactNumber() + "\n\nApplied to Post:\n" + postIdentifier
                + "\n\nQualifications or Majors:\n"
                + qualifications + "\n\nLevel Of Study:\n" + studyLevel + "\n\nYear Of Study:\n"
                + yearOfStudy + "\n\nApplication Status:\n" + applicationStatus
                + "\n\n\nCourse Marks:\n" + getMarks() + "\n\nUploaded Documents:\n" + getDocs() + "\n";
        return result;
    }
}
