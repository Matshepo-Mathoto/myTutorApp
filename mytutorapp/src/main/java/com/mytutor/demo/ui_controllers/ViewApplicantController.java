package com.mytutor.demo.ui_controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;
import com.mytutor.demo.HelperClasses.FileController;
import com.mytutor.demo.HelperClasses.SpringSessionController;
import com.mytutor.demo.object_files.Application;

import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;

/**
 * Allows viewing of applicants one by one for the selected post and change
 * their application status during shortlistings
 */
@Controller
@RequestMapping("/admin")
public class ViewApplicantController {
    @Autowired
    private DatabaseUpdateController dbUpdateController;
    @Autowired
    private DatabaseQueryController dbQueryController;

    private final HttpSession httpSession;

    public ViewApplicantController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Gets mapping from Admin webpages and shows a clean template for selecting
     * afresh the applicants and the application post they from with their
     * application data
     * 
     * @param model: allows passing in the ApplicationPost names or IDs to allow
     *               selection and the applicant's data
     * @return ViewApplicant View or Webpage
     * @throws SQLException
     */
    @RequestMapping("/view/applicant")
    public String showNew(Model model) throws SQLException {
        SpringSessionController.clearSessionAttributes(httpSession);
        Application applicant = new Application("", "", "", "", "", "", "", "", "", 0, "");
        List<String> postNames = dbQueryController.getPostNames();
        List<String> applicantNames = new ArrayList<>();

        List<String> applicationStatuses = FileController.getStatuses();

        model.addAttribute("ApplicantForm", applicant);
        model.addAttribute("applicationNames", postNames);
        model.addAttribute("applicantsNames", applicantNames);
        model.addAttribute("applicationStatuses", applicationStatuses);

        return "ViewApplicant";
    }

    /**
    * Show the ViewApplicant's page with the selected post and the applicant to
    * view their Application details and update their status
    * 
    * @param model: allows passing in the ApplicationPost names or IDs to allow
    *               selection and the requested applicant's Application
    * @return ViewApplicant View or Webpage
    * @throws SQLException
    */
    @RequestMapping("/applicant")
    public String showForm(Model model) throws SQLException {
        Application applicant = new Application();
        String lastSelectedStatus = "";
        String postIdentifier = "";

        if (httpSession.getAttribute("current_applicant") != null) {
            int applicantID = (int) httpSession.getAttribute("current_applicant");
            applicant = dbQueryController.getApplicantObj(applicantID);

            lastSelectedStatus = applicant.getApplicationStatus();
        }
        if (httpSession.getAttribute("postIdentifier") != null) {
            postIdentifier = (String) httpSession.getAttribute("postIdentifier");
        }

        List<String> postNames = dbQueryController.getPostNames();
        List<String> applicationStatuses = FileController.getStatuses();
        List<String> applicantNames = dbQueryController.getPostApplicants(postIdentifier);

        model.addAttribute("applicant", applicant);
        model.addAttribute("applicationNames", postNames);
        model.addAttribute("applicantsNames", applicantNames);
        model.addAttribute("applicationStatuses", applicationStatuses);
        model.addAttribute("lastSelectedStatus", lastSelectedStatus);

        return "ViewApplicant";
    }

    /**
     * Receives a selected application post to query on applicant/application
     * details made for this post
     * 
     * @param selectedPost: postIdentifier to show applicants of
     * @param httpSession:  used to store the selected post to be later used
     * @return a redirect to show applicants for this Application posts to select
     *         and view their applications
     * @throws SQLException
     */
    @PostMapping("/getApplicantsList")
    public String populateApplicants(@RequestParam("selectedPost") String selectedPost, HttpSession httpSession)
            throws SQLException {

        httpSession.setAttribute("postIdentifier", selectedPost);

        return "redirect:/admin/applicant";
    }

    /**
     * Receives the username of an applicant to show their application for the
     * previously selected post then gets the number of the application to the post
     * and saves the info on the user httpSession
     * 
     * @param selectedApplicant: username of the applicant to show their application
     *                           details
     * @param httpSession:       used to store and retrieve applicantID and
     *                           previously selectedPost
     * @return a redirect to showing the Applicant's data or application
     * @throws SQLException
     */
    @PostMapping("/getApplicantData")
    public String showApplicantData(@RequestParam("selectedApplicant") String selectedApplicant,
            HttpSession httpSession) throws SQLException {
        String postIdentifier = (String) httpSession.getAttribute("postIdentifier");
        List<String> applicantNames = dbQueryController.getPostApplicants(postIdentifier);
        int traverse_count = applicantNames.indexOf(selectedApplicant);
        traverse(traverse_count);

        return "redirect:/admin/applicant";
    }

    /**
     * Used to save the applicantID of the applicant requested to be shown on
     * traversing through the applications for the selected post and keeps track the
     * position of the applicant to be viewed
     * 
     * @param traverse_count: position of the applicant in applications record to
     *                        view their information
     * @return the position of the application to be displayed
     * @throws SQLException
     */
    private int traverse(int traverse_count) throws SQLException {
        String postIdentifier = (String) httpSession.getAttribute("postIdentifier");
        List<String> applicantNames = dbQueryController.getPostApplicants(postIdentifier);
        if (traverse_count >= applicantNames.size()) {
            traverse_count = applicantNames.size() - 1;
        } else if (traverse_count < 0) {
            traverse_count = 0;
        }
        int applicantID = dbQueryController.getApplicantID(postIdentifier, applicantNames.get(traverse_count));

        httpSession.setAttribute("current_applicant", applicantID);
        httpSession.setAttribute("traverse_count", traverse_count);
        return traverse_count;
    }

    /**
     * Receives a request to show the first applicant for the applications to the
     * selected post and retrieves their applicantID and saving it to the
     * httpSession
     * 
     * @return redirect to show the requested applicant's details
     */
    @RequestMapping("/firstApplicant")
    public String showFirstApplicant() throws SQLException {
        traverse(0);
        return "redirect:/admin/applicant";
    }

    /**
     * Receives a request to show the previous applicant based on the previous
     * viewed applicant for the applications to the
     * selected post and retrieves their applicantID and saving it to the
     * httpSession
     * 
     * @return redirect to show the requested applicant's details
     */
    @RequestMapping("/priorApplicant")
    public String showPriorApplicant() throws SQLException {
        int traverse_count = (int) httpSession.getAttribute("traverse_count");
        traverse_count--;
        traverse(traverse_count);
        return "redirect:/admin/applicant";
    }

    /**
     * Receives a request to show the next applicant based on the previous viewed
     * applicant for the applications to the
     * selected post and retrieves their applicantID and saving it to the
     * httpSession
     * 
     * @return redirect to show the requested applicant's details
     */
    @RequestMapping("/nextApplicant")
    public String showNextApplicant() throws SQLException {
        int traverse_count = (int) httpSession.getAttribute("traverse_count");
        traverse_count++;
        traverse(traverse_count);
        return "redirect:/admin/applicant";
    }

    /**
     * Receives a request to show the last applicant for the applications to the
     * selected post and retrieves their applicantID and saving it to the
     * httpSession
     * 
     * @return redirect to show the requested applicant's details
     * @throws SQLException
     */
    @RequestMapping("/lastApplicant")
    public String showLastApplicant() throws SQLException {
        int traverse_count = 10000;
        traverse(traverse_count);
        return "redirect:/admin/applicant";
    }

    /**
     * Update the previously selected applicant (get from http session) status with
     * a new selected one
     * 
     * @param selectedStatus the new status for the applicant's application
     * @return redirect to show webpage or model method with updated status
     *         information
     * @throws SQLException
     */
    @PostMapping("/updateApplicant")
    public String submitForm(
            @RequestParam("selectedStatus") String selectedStatus) throws SQLException {

        int applicantID = (int) httpSession.getAttribute("current_applicant");
        dbUpdateController.updateStatus(applicantID, selectedStatus);
        List<String> applicationStatuses = FileController.getStatuses();
        if (!applicationStatuses.contains(selectedStatus)) {
            FileController.addStatus(selectedStatus);
        }
        return "redirect:/admin/applicant";
    }

}
