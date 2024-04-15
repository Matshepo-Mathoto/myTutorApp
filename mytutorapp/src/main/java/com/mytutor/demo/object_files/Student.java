package com.mytutor.demo.object_files;

import java.util.ArrayList;
import java.util.List;

/**
 * An Entity for Tutor and Teaching Assistants with their info on the system such as courses registered for
 */
public class Student extends Person {
    private String studyLevel;
    private int yearOfStudy;
    private String role;
    private String majors;
    private String faculty;
    private List<String> listOfCourses;

    /**
     * Sets student details to object
     * @param studyLevel: level of study of the student such under-graduate or postgraduate
     * @param yearOfStudy: current year of study
     * @param role: TA or Tutor
     * @param majors: the majors or qualifications of the student
     * @param faculty: the faculty which the student is studying in
     */
    public Student(String studyLevel, int yearOfStudy, String role, String majors, String faculty) {
        this.studyLevel = studyLevel;
        this.yearOfStudy = yearOfStudy;
        this.role = role;
        this.majors = majors;
        this.faculty = faculty;
        this.listOfCourses = new ArrayList<String>();
    }

    public Student(String firstName, String username, String lastName, String contactNumber, String emailAddress,
            String title, String studyLevel, int yearOfStudy, String role, String majors, String faculty) {
        super(firstName, username, lastName, contactNumber, emailAddress, title);
        this.studyLevel = studyLevel;
        this.yearOfStudy = yearOfStudy;
        this.role = role;
        this.majors = majors;
        this.faculty = faculty;
        this.listOfCourses = new ArrayList<String>();
    }

    public Student(Person copyPerson, String studyLevel, int yearOfStudy, String role, String majors, String faculty) {
        super(copyPerson);
        this.studyLevel = studyLevel;
        this.yearOfStudy = yearOfStudy;
        this.role = role;
        this.majors = majors;
        this.faculty = faculty;
        this.listOfCourses = new ArrayList<String>();
    }

    public Student() {
        super();
    }

    public Student(Student student) {
        super(student);
        this.studyLevel = student.studyLevel;
        this.yearOfStudy = student.yearOfStudy;
        this.role = student.role;
        this.majors = student.majors;
        this.faculty = student.faculty;
        this.listOfCourses = student.listOfCourses;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStudentNumber() {
        return super.getUsername();
    }

    public void setStudentNumber(String studentNo) {
        super.setUsername(studentNo);
    }

    public String getMajors() {
        return majors;
    }

    public void setMajors(String majors) {
        this.majors = majors;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    /**
     * Gets the list of courses the Tutor or TA has access to
     * @return list of course names
     */
    public List<String> getListOfCourses() {
        return listOfCourses;
    }

    public void setListOfCourses(List<String> listOfCourses) {
        this.listOfCourses = listOfCourses;
    }

    public void addCourse(String course_name) {
        this.listOfCourses.add(course_name);
    }

    public int getNumberOfCourses() {
        return this.listOfCourses.size();
    }

    @Override
    public String toString() {
        return super.toString() + "\n\nStudent Details:\n" + "Study Level:\n" + studyLevel + "\n\nYear Of Study:\n"
                + yearOfStudy + "\n\nStudent role:\n" + role
                + "\n\nMajors:\n" + majors + "\n\nFaculty:\n" + faculty + "\n\nList Of Courses:\n" + listOfCourses;
    }

}