package com.mytutor.demo.object_files;

public class ApplicationPost {
    private String applicationPostId;
    private String description;
    private String adminId;
    private String openDate;
    private String closingDate;
    private String role;
    private String department;

    public ApplicationPost() {
        this.applicationPostId = "";
        this.description = "";
        this.adminId = "";
        this.openDate = "";
        this.closingDate = "";
        this.role = "";
        this.department = "";
    }

    public ApplicationPost(String applicationPostId, String description, String creator, String openDate,
            String closingDate, String role, String department) {
        this.applicationPostId = applicationPostId;
        this.description = description;
        this.adminId = creator;
        this.openDate = openDate;
        this.closingDate = closingDate;
        this.role = role;
        this.department = department;
    }

    public String getApplicationPostId() {
        return applicationPostId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setApplicationPostId(String applicationPostId) {
        this.applicationPostId = applicationPostId;
    }

    public void getAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        String result = "";
        result = "Application Post:" 
                + "\nPost Identifier: " + applicationPostId 
                + "\nDescription:\n" + description + "\n"
                + "\nCreator: " + adminId
                + "\nOpen date: " + openDate + " (midnight)"
                + "\nClosing date: " + closingDate + " (midnight)"
                + "\nApplying Position: " +  role 
                + "\nRelevant to the Department: " + department;
        return result;
    }

}