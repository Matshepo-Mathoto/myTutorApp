package com.mytutor.demo.object_files;

import java.util.ArrayList;
import java.util.List;

/**
 * Administrator objects for the users that have most accessibility to the
 * system such as creating courses, application posts, adding users, managing
 * applicants, courses and users on the system
 */
public class Administrator extends Person {
    private String department;
    private String faculty;
    private String adminRole;
    private List<String> createdCourses;
    private List<String> createdPosts;

    public Administrator() {
        super();
    }

    /**
     * Sets admin attributes
     * @param department: department which the admin works under if applicable
     * @param faculty: faculty which the admin manages if applicable 
     * @param adminRole: the role or name for what the admin manages on the system
     */
    public Administrator(String department, String faculty, String adminRole) {
        this.department = department;
        this.faculty = faculty;
        this.adminRole = adminRole;
        this.createdCourses = new ArrayList<>();
        this.createdPosts = new ArrayList<>();
    }

    public Administrator(String firstName, String username, String lastName, String contactNumber, String emailAddress,
            String title, String department, String faculty, String adminRole) {
        super(firstName, username, lastName, contactNumber, emailAddress, title);
        this.department = department;
        this.faculty = faculty;
        this.adminRole = adminRole;
        this.createdCourses = new ArrayList<>();
        this.createdPosts = new ArrayList<>();
    }

    public Administrator(Person copyPerson, String department, String faculty, String adminRole) {
        super(copyPerson);
        this.department = department;
        this.faculty = faculty;
        this.adminRole = adminRole;
        this.createdCourses = new ArrayList<>();
        this.createdPosts = new ArrayList<>();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }

    public List<String> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(List<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

    @Override
    public String toString() {
        return super.toString() + "\n\nAdministrator Details:\n" + "Department:\n" + department + "\n\nFaculty:\n"
                + faculty + "\n\nAdmin Role:\n" + adminRole
                + "\n\nCreated Courses:\n" + createdCourses + "\n\nCreated Application Posts:\n" + createdPosts;
    }

    public List<String> getCreatedPosts() {
        return createdPosts;
    }

    public void setCreatedPosts(List<String> createdPosts) {
        this.createdPosts = createdPosts;
    }

}
