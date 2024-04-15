package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.EmailFiles.EmailController;
import com.mytutor.demo.object_files.Application;

import jakarta.servlet.http.HttpSession;

/**
 * Manages applicant capturing of an job application in response to a specified
 * post
 */
@Controller
@RequestMapping("/applicant")
public class ApplicationController {
    @Autowired
    private DatabaseCreateController dbCreateController = new DatabaseCreateController();
    @Autowired
    private DatabaseQueryController dbQueryController = new DatabaseQueryController();

    /**
     * Gets the postIdentifier from the url which is the id of the post the
     * applicant is making application for
     * 
     * @param postName:    post identifier extracted from the passed url
     * @param httpSession: user session to save the postIdentifier
     * @return redirect to view application form
     */
    @GetMapping("/new/application/{postID}")
    public String getRequiredPost(@PathVariable("postID") String postName, HttpSession httpSession) {
        String postIdentifier = postName;
        httpSession.setAttribute("postIdentifier", postIdentifier);

        return "redirect:/applicant/new/application";
    }

    /**
     * Checks whether the postIdentifier is valid and/or the application is still
     * open and fetches the
     * description of the post
     * 
     * @param model:       models attribute for a new Application form if
     *                     application is found and the post description
     * @param httpSession: used to setAttribute to show form or not and the result
     *                     messages
     * @return a view to the NewApplication form
     */
    @GetMapping("/new/application")
    public String showForm(Model model, HttpSession httpSession) {
        String result = "";
        String postDescription = "";
        httpSession.removeAttribute("errorMessage");
        httpSession.removeAttribute("resultMessage");
        try {
            String postName = (String) httpSession.getAttribute("postIdentifier");
            postDescription = dbQueryController.getPostDecription(postName);
            boolean isOpen = dbQueryController.isApplicationOpen(postName);
            if (!isOpen) {
                result = "The application is no longer open";
                httpSession.setAttribute("showForm", false);
                httpSession.setAttribute("errorMessage", result);
            } else {
                httpSession.setAttribute("showForm", true);
            }
        } catch (Exception e) {
            System.err.println(e);
            result = "Invalid Link";
            httpSession.setAttribute("showForm", false);
            httpSession.setAttribute("errorMessage", result);
        }
        model.addAttribute("applicant", new Application());
        model.addAttribute("postDescription", postDescription);

        return "NewApplication";
    }

    /**
     * Receives an Application object from the application form the applicant filled
     * with their details
     * Adds the application to the database and sends an Email for successful
     * capturing with the provided
     * details and how to track their application
     * 
     * @param applicant:   application form object from the template
     * @param courseList:  list of courses and the previous mark as course_code +
     *                     '##' + course_mark
     * @param httpSession: user session to extract postIdentifier and add result
     *                     messages for the use application process
     * @return redirect to showing a application form and results messages
     * @throws SQLException
     */
    @PostMapping("/submit/application")
    public String submitForm(@ModelAttribute("applicant") Application applicant,
            @RequestParam("courses") List<String> courseList, HttpSession httpSession)
            throws SQLException {
        String result = "";
        try {
            String postIdentifier = (String) httpSession.getAttribute("postIdentifier");
            applicant.setPostIdentifier(postIdentifier);
            applicant.setApplicationStatus("Submitted");
            applicant.setApplicantMarks(courseList);

            int applicantID = dbCreateController.addApplicant(applicant);
            int recordCount = dbCreateController.addApplicantMarks(applicantID, applicant);

            System.out.println(recordCount + " record(s) added for Course Marks");
            // int applicantID = dbCreateController.addApplication(applicant);
            applicant = dbQueryController.getApplicantObj(applicantID);

            // Send a confirmation email for successfully application submission
            EmailController.sendApplicationEmail(applicant, applicantID);

            result = "Application Submitted succesfully";
            result += "\n\nApplication ID: " + applicantID;
            result += "\nUse this link check your application or upload documents if required";
            result += "\n http://196.47.239.204:8080/applicant/check/application/"
                    + applicant.getUsername() + "/" + applicantID;
            httpSession.setAttribute("resultMessage", result);
        } catch (Exception e) {
            // System.err.println(e);
            result += "\n" + "Error: " + e.getMessage();
            httpSession.setAttribute("errorMessage", result);
            return "redirect:/applicant/new/application";
        }

        return "redirect:/applicant/new/application";
    }

}
