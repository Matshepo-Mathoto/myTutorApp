package com.mytutor.demo.object_files;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity for Lecturer and Convenor object
 */
public class Lecturer extends Person {
    private String role;
    private String department;
    private List<String> listOfCourses;

    public Lecturer() {
        super();
    }

    /**
     * Sets Lecturer attributes
     * @param role: LecturerRole as Lecturer or Convenor
     * @param department: department which the lecturer works under
     */
    public Lecturer(String role, String department) {
        this.role = role;
        this.department = department;
    }

    public Lecturer(String firstName, String username, String lastName, String contactNumber, String emailAddress,
            String title, String role, String department) {
        super(firstName, username, lastName, contactNumber, emailAddress, title);
        this.role = role;
        this.department = department;
        this.listOfCourses = new ArrayList<>();
    }

    public Lecturer(Person copyPerson, String role, String department) {
        super(copyPerson);
        this.role = role;
        this.department = department;
        this.listOfCourses = new ArrayList<>();
    }

    public Lecturer(Lecturer lecturer) {
        super(lecturer);
        if (lecturer != null) {
            this.role = lecturer.role;
            this.department = lecturer.department;
            this.listOfCourses = lecturer.getListOfCourses();
        }
    }

    public String getLectureRole() {
        return role;
    }

    public void setLectureRole(String lectureRole) {
        this.role = lectureRole;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the list of courses the Lecturer has access to
     * @return list of course names
     */
    public List<String> getListOfCourses() {
        return listOfCourses;
    }

    public void setListOfCourses(List<String> listOfCourses) {
        this.listOfCourses = listOfCourses;
    }

    @Override
    public String toString() {
        return super.toString() + "\n\nLecturer Details: \nLecturer role:\n" + role + "\n\nDepartment:\n" + department
                + "\n\nList Of Courses:\n" + listOfCourses;
    }

}
