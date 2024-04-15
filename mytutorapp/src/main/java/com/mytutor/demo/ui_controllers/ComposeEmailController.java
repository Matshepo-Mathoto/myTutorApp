package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.EmailFiles.EmailSenderService;
import com.mytutor.demo.HelperClasses.SpringSessionController;

import jakarta.servlet.http.HttpSession;

/**
 * Allows users to send emails from the System email to system users
 */
@Controller
@RequestMapping("/user")
public class ComposeEmailController {
    @Autowired
    DatabaseQueryController dbQueryController;

    @Autowired
    EmailSenderService emailservice;

    /**
     * Get the emailing lists (usernames + emailAddress of the users or tutors on the system)
     * @param model: allows to pass the emailing list to the template to compose and send the email
     * @param httpSession: has useful attributes
     * @return a View to ComposeEmail Template
     */
    @GetMapping("/compose/email")
    public String getList(Model model, HttpSession httpSession) {
        int courseID = (int) httpSession.getAttribute("openedCourse");

        String myUsername = SpringSessionController.getLoggedUser();
        String myEmail = "";
        List<String> usernames = new ArrayList<>();
        String user_role = SpringSessionController.getRole();
        try {
            if (user_role.equals("ROLE_TA") || user_role.equals("ROLE_CONVENOR")) {
                usernames = dbQueryController.getCourseEmailingList(courseID, "ROLE_TUTOR");
            } else {
                usernames = dbQueryController.getEmailingList();
            }
            myEmail = dbQueryController.getEmailAddress(myUsername);
        } catch (Exception e) {
            System.err.println(e);
        }
        model.addAttribute("myEmailAddress", myEmail);
        model.addAttribute("users", usernames);
        return "ComposeEmail";
    }

    /**
     * Extract emails from the username + emailAddress list
     * @param lstEmails: list of username + emailAddresses elements
     * @return an array of user email addresses
     */
    public String[] extractEmails(String[] lstEmails) {
        int size = lstEmails.length;
        String[] arrEmails = new String[size];

        for (int i = 0; i < size; i++) {
            String[] val = lstEmails[i].split(" - ");
            val[1] = val[1].trim();
            if (val[1].length() > 0) {
                arrEmails[i] = val[1];
            }
        }
        return arrEmails;
    }

    /**
     * Sends an the composed email to the selected users by CC them and recipient as the composer
     * @param message: email text
     * @param subjectLine: subject line of the email
     * @param receivers: list of usernames + email addresses to CC the email to
     * @return
     */
    @PostMapping("/submit/email")
    public String getEmails(@RequestParam("message") String message, @RequestParam("subjectLine") String subjectLine,
            @RequestParam("listOfReceivers") String receivers) {
        String arrReceivers[] = receivers.split("\n");
        String cc[] = extractEmails(arrReceivers);
        String myUsername = SpringSessionController.getLoggedUser();
        String myEmail = "";
        try {
            myEmail = dbQueryController.getEmailAddress(myUsername);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        emailservice.sendCCEmail(myEmail, subjectLine, message, cc);
        return "redirect:/user/compose/email";
    }
}
