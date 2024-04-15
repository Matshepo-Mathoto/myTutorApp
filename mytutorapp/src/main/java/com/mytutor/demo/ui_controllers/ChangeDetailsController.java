package com.mytutor.demo.ui_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;
import com.mytutor.demo.HelperClasses.SpringSessionController;
import com.mytutor.demo.object_files.Administrator;
import com.mytutor.demo.object_files.Lecturer;
import com.mytutor.demo.object_files.Person;
import com.mytutor.demo.object_files.Student;

import jakarta.servlet.http.HttpSession;

/**
 * Manages user profile, viewing and updating details they are allowed to update
 */
@Controller
@RequestMapping("/user")
public class ChangeDetailsController {
    @Autowired
    private DatabaseUpdateController dbUpdateController;
    @Autowired
    private DatabaseQueryController dbQueryController;
    private final HttpSession httpSession;

    public ChangeDetailsController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Shows the ChangeDetails template to return the UserProfile/Person information
     * to the template
     * for viewing and updating relevant details. The person object passed is the
     * subclass for specific to
     * the logged user-role
     * 
     * @param model:      adds a person subclass objects for the logged user to pass
     *                    to the Web template for viewing and updating corresponding
     *                    details
     * @return a view to ChangeDetails page/template
     */
    @GetMapping("/change/details")
    public String showDetails(Model model) {
        SpringSessionController.clearSessionAttributes(httpSession);
        String userRole = SpringSessionController.getRole();
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        String rolemap = "";

        Person person = null;
        try {
            if (userRole.equalsIgnoreCase("ROLE_TUTOR") || userRole.equalsIgnoreCase("ROLE_TA")) {
                person = dbQueryController.getStudent(loggedUser);
                rolemap = "tutor";
                if (userRole.equalsIgnoreCase("ROLE_TA")) {
                    rolemap = "ta";
                }
                model.addAttribute("person", (Student) person);
            } else if (userRole.equalsIgnoreCase("ROLE_ADMIN")) {
                person = dbQueryController.getAdministrator(loggedUser);
                rolemap = "admin";
                model.addAttribute("person", (Administrator) person);
            } else if (userRole.equalsIgnoreCase("ROLE_LECTURER") || userRole.equalsIgnoreCase("ROLE_CONVENOR")) {
                person = dbQueryController.getLecturer(loggedUser);
                rolemap = "lecturer";
                model.addAttribute("person", (Lecturer) person);
            } else {
                person = dbQueryController.getPerson(loggedUser);
                model.addAttribute("person", person);
            }

            model.addAttribute("rolemap", rolemap);
        } catch (Exception e) {
            System.err.println(e);
        }
        return "ChangeDetails";
    }

    /**
     * Receives a form submission from the template to update UserProfile/Person information
     * @param PersonForm: person object with same or updated user profile fields to update
     * @return redirect to show the View template with updated details
     */
    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("Person") Person PersonForm) {
        httpSession.setAttribute("show_type", "profile");
        String loggedUser = SpringSessionController.getLoggedUser();
        try {
            Person person = dbQueryController.getPerson(loggedUser);
            person.updatePerson(PersonForm);
            person.setUsername(loggedUser);
            dbUpdateController.updateProfile(person);
        } catch (Exception e) {
            System.err.println(e);
        }
        return "redirect:/user/change/details";
    }

    /**
     * Receives a form submission
     * @param StudentForm: student object with updated student attribute values 
     * @return redirect to show the View template with updated details
     */
    @PostMapping("/updateStudent")
    public String updateStudent(@ModelAttribute("Person") Student StudentForm) {
        String loggedUser = SpringSessionController.getLoggedUser();
        try {
            Person person = dbQueryController.getPerson(loggedUser);
            StudentForm.updatePerson(person);
            StudentForm.setRole(SpringSessionController.getRole());
            person = StudentForm;
            Student student = new Student((Student) person);

            dbUpdateController.updateStudent(student);
        } catch (Exception e) {
            System.err.println(e);
        }
        return "redirect:/user/change/details";
    }

    /**
     * Receives a form submission from the template to update Admin information
     * @param AdminForm: object of Administrator with updated admin details
     * @return redirect to show the View template with updated details
     */
    @PostMapping("/updateAdmin")
    public String updateAdmin(@ModelAttribute("Person") Administrator AdminForm) {
        String loggedUser = SpringSessionController.getLoggedUser();
        try {
            Person person = dbQueryController.getPerson(loggedUser);
            AdminForm.updatePerson(person);
            dbUpdateController.updateAdmin(AdminForm);
        } catch (Exception e) {
            System.err.println(e);
        }
        return "redirect:/user/change/details";
    }

    /**
     * Receives a form submission from the template to update Lecturer or Convenor information
     * @param LecturerForm: object of Lecturer with updated Lecturer details
     * @return redirect to show the View template with updated details
     */
    @PostMapping("/updateLecturer")
    public String updateLecturer(@ModelAttribute("Person") Lecturer LecturerForm) {
        String loggedUser = SpringSessionController.getLoggedUser();
        try {
            Person person = dbQueryController.getPerson(loggedUser);
            LecturerForm.updatePerson(person);
            LecturerForm.setLectureRole(SpringSessionController.getRole());
            dbUpdateController.updateLecturer(LecturerForm);
        } catch (Exception e) {
            System.err.println(e);
        }
        return "redirect:/user/change/details";
    }
}
