package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.DatabaseControllers.DatabaseDeleteController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.EmailFiles.EmailController;
import com.mytutor.demo.object_files.Administrator;
import com.mytutor.demo.object_files.Lecturer;
import com.mytutor.demo.object_files.Person;
import com.mytutor.demo.object_files.Student;
import com.mytutor.demo.object_files.UserLogin;

import jakarta.servlet.http.HttpSession;

/**
 * Allows administrators to Manage Users (profiles, login details and user
 * account) to the system
 */
@Controller
@RequestMapping("/admin")
public class ManageUsersController {
    @Autowired
    private DatabaseCreateController dbCreateController;
    @Autowired
    private DatabaseDeleteController dbDeleteController;
    @Autowired
    private DatabaseQueryController dbQueryController;
    /**
     * Allows use of the Bcrypt password encode() method to hash the passwords added
     * to the database
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Gets incoming mappings to the open ManageUsers template
     * 
     * @param model: loads the user roles to the View template and the new Person
     *               object
     * @return the View to ManageUsers screen
     */
    @GetMapping("/manage/users")
    public String showForm(Model model, HttpSession httpSession) {
        List<String> user_roles = new ArrayList<String>();
        user_roles.add("ROLE_TUTOR");
        user_roles.add("ROLE_TA");
        user_roles.add("ROLE_LECTURER");
        user_roles.add("ROLE_CONVENOR");
        user_roles.add("ROLE_ADMIN");

        String user_details = "";
        Person person = null;
        if (httpSession.getAttribute("selectedUser") != null) {
            String username = (String) httpSession.getAttribute("selectedUser");
            try {
                String userRole = dbQueryController.getUserRole(username);
                switch (userRole) {
                    case "ROLE_ADMIN":
                        person = dbQueryController.getAdministrator(username);
                        break;
                    case "ROLE_CONVENOR":
                        person = dbQueryController.getLecturer(username);
                        break;
                    case "ROLE_LECTURER":
                        person = dbQueryController.getLecturer(username);
                        break;
                    case "ROLE_TUTOR":
                        person = dbQueryController.getStudent(username);
                        break;
                    case "ROLE_TA": 
                        person = dbQueryController.getStudent(username);
                        break;

                }
                if (person == null) {
                    person = new Person();
                }
                user_details = person.toString();

            } catch (Exception e) {
                System.err.println(e);
            }
        }

        List<String> lstUsernames = new ArrayList<>();
        try {
            lstUsernames = dbQueryController.getUsernames();
        } catch (Exception e) {
            System.err.println(e);
        }

        model.addAttribute("userRoles", user_roles);
        model.addAttribute("UserForm", new Person());
        model.addAttribute("users", lstUsernames);
        model.addAttribute("user_details", user_details);
        return "ManageUsers";
    }

    /**
     * Receives a post mapping to add the user(Tutor, Lecturer, TA, Convenor or
     * Lecturer) to the system
     * And adds them to the respective databases with provided and generated
     * temporary details
     * Then sends an email of to the user added with login details
     * 
     * @param person:       object of person (with username, email address) for the
     *                      UserProfile
     * @param selectedRole: the role of the user to the system from the list of
     *                      user-roles options
     * @param httpSession:  user specific session to add the result messages or
     *                      errors for the admin
     * @return redirect to the show form method
     */
    @PostMapping("/add/user")
    public String processUserForm(@ModelAttribute("UserForm") Person person,
            @RequestParam("selectedRole") String selectedRole, HttpSession httpSession) {
        String resultString = "";
        person.setUsername(person.getUsername().toUpperCase());

        try {
            // Add the person profile
            dbCreateController.addProfile(person);

            // Check what is the selected role and add the user object of that user_role to
            // the database records
            if (selectedRole.equalsIgnoreCase("ROLE_LECTURER") || selectedRole.equalsIgnoreCase("ROLE_CONVENOR")) {
                Lecturer lecturer = new Lecturer(person, selectedRole, null);
                dbCreateController.addLecturer(lecturer);

            } else if (selectedRole.equalsIgnoreCase("ROLE_ADMIN")) {
                Administrator admin = new Administrator(person, null, null, selectedRole);
                dbCreateController.addAdmin(admin);
            } else if (selectedRole.equalsIgnoreCase("ROLE_TUTOR")) {
                Student student = new Student(person, null, 0, selectedRole, null, null);
                dbCreateController.addStudent(student);
            } else if (selectedRole.equalsIgnoreCase("ROLE_TA")) {
                Student student = new Student(person, null, 0, selectedRole, null, null);
                dbCreateController.addStudent(student);
            }

            // Create a temporary password with username and 4 random numbers, create and
            // add user login to the database
            int number = (new Random()).nextInt(9000) + 1000;
            String password = person.getUsername().toUpperCase() + "_" + number;
            UserLogin loginInfo = new UserLogin(person.getUsername(), passwordEncoder.encode(password), selectedRole);
            dbCreateController.addLoginDetails(loginInfo);

            // If successful email the login details and confirmation to the added user so
            // they can login and/or reset password
            EmailController.sendNewUserEmail(person.getEmailAddress(), loginInfo, password);
            resultString = "User '" + person.getUsername() + "' has been successfully added as: " + selectedRole;

        } catch (Exception e) {
            System.err.println(e);
            resultString = "Internal Server Error! User NOT added";
        }

        httpSession.setAttribute("resultMessage", resultString);
        return "redirect:/admin/manage/users";
    }

    /**
     * Receives request to send the details of the selected User
     * @param selectedUser: the username of the User to get details of
     * @param httpSession: used to store
     * @return
     */
    @PostMapping("/view/user")
    public String viewUser(@RequestParam("username") String selectedUser, HttpSession httpSession) {
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        httpSession.setAttribute("selectedUser", selectedUser);
        return "redirect:/admin/manage/users";
    }

    /**
     * Deletes the previously selected user from the system database and their records
     * @param httpSession: used to retrieve previously selected user
     * @return a redirect to show the user was deleted or not as a resultMessage on the Webpage
     * @throws SQLException
     */
    @PostMapping("/delete/user")
    public String deleteUser(HttpSession httpSession) throws SQLException {
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        String username = (String) httpSession.getAttribute("selectedUser");
        int isDeleted = dbDeleteController.deletePerson(username);
        String resulString = "";
        if (isDeleted > 0) {
            resulString = username + " user has been successfully deleted.";
            httpSession.removeAttribute("selectedUser");
        } else {
            resulString = "Couldn't delete the user: " + username;
        }
        httpSession.setAttribute("resultMessage", resulString);

        return "redirect:/admin/manage/users";
    }
}
