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

import com.mytutor.demo.DatabaseControllers.DatabaseDeleteController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;
import com.mytutor.demo.HelperClasses.FileController;
import com.mytutor.demo.HelperClasses.SpringSessionController;
import com.mytutor.demo.object_files.Application;

import jakarta.servlet.http.HttpSession;

/**
 * Manages changing application statuses for applicants applications and
 * deleting selected applications
 */
@Controller
@RequestMapping("/admin")
public class ManageApplicantsController {
    @Autowired
    private DatabaseUpdateController dbUpdateController;
    @Autowired
    private DatabaseQueryController dbQueryController;
    @Autowired
    private DatabaseDeleteController dbDeleteController;

    private final HttpSession httpSession;

    public ManageApplicantsController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Gets mapping from Admin webpages and shows a clean template for selecting
     * afresh the applicants and the application post they from
     * 
     * @param model: allows passing in the ApplicationPost names or IDs to allow
     *               selection
     * @return ManageApplicants View or Webpage
     * @throws SQLException
     */
    @RequestMapping("/manage/applicants")
    public String showNew(Model model) throws SQLException {
        SpringSessionController.clearSessionAttributes(httpSession);
        List<String> postNames = dbQueryController.getPostNames();

        List<String> applicationStatuses = FileController.getStatuses();
        List<String> lstSelectedStatuses = new ArrayList<>();

        model.addAttribute("applicationNames", postNames);
        model.addAttribute("applicationStatuses", applicationStatuses);
        model.addAttribute("selectedStatuses", lstSelectedStatuses);
        return "ManageApplicants";
    }

    /**
     * Show the ManageApplicants page with the selected post and the applicants to
     * update their statuses or remove them
     * 
     * @param model: allows passing in the ApplicationPost names or IDs to allow
     *               selection
     * @return ManageApplicants View or Webpage
     * @throws SQLException
     */
    @RequestMapping("/manage/applicants/statuses")
    public String showForm(Model model) throws SQLException {
        List<String> postNames = dbQueryController.getPostNames();

        List<String> applicationStatuses = FileController.getStatuses();
        List<String> applicantNames = new ArrayList<>();
        List<String> applicantsData = new ArrayList<>();
        List<String> lstSelectedStatuses = new ArrayList<>();

        if (httpSession.getAttribute("selectedPost") != null) {
            String postIdentifier = (String) httpSession.getAttribute("selectedPost");

            if (httpSession.getAttribute("selectedStatuses") != null) {
                String selectedStatuses = (String) httpSession.getAttribute("selectedStatuses");

                applicantNames = dbQueryController.getPostApplicants(postIdentifier, selectedStatuses);
                lstSelectedStatuses = FileController.stringInList(selectedStatuses);
            }

            if (httpSession.getAttribute("applicants_list") != null) {
                String selectedApplicants = (String) httpSession.getAttribute("applicants_list");

                applicantsData = dbQueryController.getApplicantsInfo(selectedApplicants, postIdentifier);
            }
        }

        model.addAttribute("applicationNames", postNames);
        model.addAttribute("applicantsNames", applicantNames);
        model.addAttribute("applicationStatuses", applicationStatuses);
        model.addAttribute("applicantsData", applicantsData);
        model.addAttribute("selectedStatuses", lstSelectedStatuses);
        return "ManageApplicants";
    }

    /**
     * Receives a form submission with the new status to update the application status
     * @param selectedStatus: new status for the applicant's application
     * @return redirect to Model for the page method
     */
    @PostMapping("/update/applicants/status")
    public String submitForm(
            @RequestParam("selectedStatus") String selectedStatus) {
        try {
            String selectedApplicants = (String) httpSession.getAttribute("applicants_list");
            String postIdentifier = (String) httpSession.getAttribute("selectedPost");
            int res = dbUpdateController.updateMultiStatuses(selectedApplicants, postIdentifier, selectedStatus);

            System.out.println("Number of rows affected: " + res);
            List<String> applicationStatuses = FileController.getStatuses();
            if (!applicationStatuses.contains(selectedStatus)) {
                FileController.addStatus(selectedStatus);
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        return "redirect:/admin/manage/applicants/statuses";
    }

    /**
     * Gets a request to get list of applicants and statuses based on the selected
     * applicationpost the status of their application
     * 
     * @param selectedPost:     the ApplicationPost the applicants are requested
     *                          from
     * @param selectedStatuses: the applicants with containing statuses to be
     *                          returnes
     * @return redirect to modelling mapping to get these requested applicants, the
     *         parameters and section to view are saved to the httpSession
     * @throws SQLException
     */
    @PostMapping("/request/applicants/usernames")
    public String populateApplicants(@RequestParam("selectedPost") String selectedPost,
            @RequestParam("getByStatuses") List<String> selectedStatuses) throws SQLException {
        String strSelectedStatuses = FileController.listInString(selectedStatuses);

        httpSession.setAttribute("selectedPost", selectedPost);
        httpSession.setAttribute("selectedStatuses", strSelectedStatuses);
        httpSession.setAttribute("manage_app_view", "select_applicants");
        return "redirect:/admin/manage/applicants/statuses";
    }

    /**
     * Receives a selected list of applicants to update their statuses and changes
     * the section to view on the template saving this info to the httpSession to be
     * modelled on show method
     * 
     * @param applicantsToUpdate: list of applicants to view their statuses, update
     *                            or delete
     * @return redirect to Model method
     */
    @PostMapping("/display/selected/applicants")
    public String displaySelected(@RequestParam("applicantsToUpdate") List<String> applicantsToUpdate) {
        String strApplicantsList = FileController.listInString(applicantsToUpdate);
        httpSession.setAttribute("applicants_list", strApplicantsList);
        httpSession.setAttribute("manage_app_view", "update_status");

        return "redirect:/admin/manage/applicants/statuses";
    }

    /**
     * Gets a request to download the usernames of the selected applicants, creates
     * a textfile to write the usernames on and send it as an attachment to the user
     * 
     * @param httpSession: used to retrieve the selected applicants list and post
     *                     for the applicants to print
     * @return a redirect to download the applicants usernames text if no errors
     *         occur
     */
    @GetMapping("/download/shortlist")
    public String downloadShortlist(HttpSession httpSession) {
        try {
            String selectedApplicants = (String) httpSession.getAttribute("applicants_list");
            String postIdentifier = (String) httpSession.getAttribute("selectedPost");
            String username = SpringSessionController.getLoggedUser();
            List<String> applicantsList = dbQueryController.getApplicantsUsername(selectedApplicants, postIdentifier);
            String filename = username + "_applicants.txt";
            String fileDir = "src/main/resources/static/textfiles/";

            String savedPath = FileController.writeFileLines(applicantsList, fileDir, filename, false);

            if (savedPath != null) {
                httpSession.setAttribute("filename", filename);
                httpSession.setAttribute("fileDir", fileDir);
                return "redirect:/user/file/download";
            }
            return "redirect:/admin/manage/applicants/statuses";
        } catch (Exception e) {
            System.err.println(e);
            return "redirect:/admin/manage/applicants/statuses";
        }
    }

    /**
     * Closes the view to the selected applicants and application post
     * @return redirect to model the ManageApplicants page
     */
    @GetMapping("/update/controls/close")
    public String closeDisplay() {
        SpringSessionController.clearSessionAttributes(httpSession);
        return "redirect:/admin/manage/applicants/statuses";
    }

    /**
     * Gets a request to delete the previously selected Applicants and Applications
     * from the database and removes all their records and documents in server
     * storage and relevant database tables
     * 
     * @return a redirect to show the ManageApplicants page
     */
    @PostMapping("/delete/applicants")
    public String deleteApplicants() {
        try {
            String selectedApplicants = (String) httpSession.getAttribute("applicants_list");
            String postIdentifier = (String) httpSession.getAttribute("selectedPost");

            List<String> applicantsToDelete = FileController.stringInList(selectedApplicants);
            int count = 0;
            int numDeleted = 0;
            for (String s : applicantsToDelete) {
                Application applicant = new Application();
                applicant.setUsername(s);
                applicant.setPostIdentifier(postIdentifier);
                applicant = dbQueryController.getUploadedDocuments(applicant);
                // System.out.println(applicant);

                count = dbDeleteController.deleteApplicant(s, postIdentifier);
                numDeleted = 0;
                if (count > 0) {
                    numDeleted = deleteDocs(applicant);
                }

            }
            System.out.println(count + " applicant(s) deleted and " + numDeleted + " document(s)");
        } catch (Exception e) {
            System.err.println(e);
        }
        return "redirect:/admin/manage/applicants/statuses";
    }

    /**
     * Used on deleting selected applicants to also delete their uploaded documents
     * 
     * @param applicant: used to retrieve the application docs and their location on
     *                   the server to delete them
     * @return the number of deleted files
     */
    private int deleteDocs(Application applicant) {
        int deletedFiles = 0;
        List<List<String>> applicantDocs = applicant.getApplicationDocs();
        for (List<String> doc : applicantDocs) {
            String docName = doc.get(0);
            String storagePath = doc.get(2);
            String fileDir = storagePath + "/";
            boolean isDeleted = FileController.deleteFile(fileDir, docName);
            if (isDeleted) {
                deletedFiles++;
            }
        }
        return deletedFiles;
    }
}
