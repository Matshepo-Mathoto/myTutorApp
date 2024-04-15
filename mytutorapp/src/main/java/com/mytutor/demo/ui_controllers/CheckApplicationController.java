package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;
import com.mytutor.demo.EmailFiles.EmailController;
import com.mytutor.demo.HelperClasses.FileController;
import com.mytutor.demo.object_files.Application;
import com.mytutor.demo.object_files.Student;
import com.mytutor.demo.object_files.UserLogin;

import jakarta.servlet.http.HttpSession;

/**
 * Manages the checking and uploading of documents for the applicant's application,
 * if application has been successful, allows the applicant to register to the system
 */
@Controller
@RequestMapping("/applicant")
public class CheckApplicationController {
    @Autowired
    private DatabaseQueryController dbQueryController;
    @Autowired
    private DatabaseCreateController dbCreateController;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Shows the Template View to the CheckApplication page, loads applicant details if requested
     * @param model: passes the attributes added to the WebPage or View
     * @param httpSession: stores the applicantID and which section is to be open on the template
     * @return a view to CheckApplication template
     */
    @GetMapping("/show")
    public String showForm(Model model, HttpSession httpSession) {
        Application applicant = null;
        int applicantID = 0;
        try {
            if (httpSession.getAttribute("applicantID") != null) {
                applicantID = (int) httpSession.getAttribute("applicantID");
            }
            applicant = dbQueryController.getApplicantObj(applicantID);
            if (httpSession.getAttribute("applicant_view") != null
                    && httpSession.getAttribute("applicant_view").equals("details")) {
                String applicantDetails = applicant.toString();
                model.addAttribute("applicantDetails", applicantDetails);
            }
        } catch (Exception e) {
            System.err.println(e);

        }
        if (applicant == null) {
            applicant = new Application();
        }
        model.addAttribute("applicationID", applicantID);
        model.addAttribute("username", applicant.getUsername());
        return "CheckApplication";
    }

    /**
     * Uses the provided details to check the status of the application and whether the applicant is previous
     * user of this system then sends results and options from this check
     * @param username: applicant username
     * @param applicantID: identifier to the records of this applicant to the database
     * @param httpSession: stores which section to be viewed on the template or webpage, and resultMessages
     * @return redirects to modelling for a view to CheckApplication template
     */
    @GetMapping("/check/application/{username}/{applicantID}")
    public String checkApplication(@PathVariable("username") String username,
            @PathVariable("applicantID") int applicantID, HttpSession httpSession) {
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        httpSession.removeAttribute("applicant_view");
        String resultString = "";

        try {
            boolean profileExists = dbQueryController.profileExists(username);
            Application applicant = dbQueryController.getApplicantObject(applicantID, username);

            if (applicant == null) {
                resultString = "There was no application found";
            } else {
                String applicantStatus = applicant.getApplicationStatus();
                applicantStatus = applicantStatus.toUpperCase();
                if (applicantStatus.equalsIgnoreCase("Successful") || applicantStatus.equalsIgnoreCase("Hired")
                        || applicantStatus.contains("ALLOCATED")) {
                    resultString = "Your application was successful";
                    if (!profileExists) {
                        resultString = "Your application was successful";
                        httpSession.setAttribute("applicant_view", "register");
                        httpSession.setAttribute("showOptions", false);
                    }
                } else {
                    httpSession.setAttribute("showOptions", true);
                }
            }
            httpSession.setAttribute("applicantID", applicantID);
        } catch (SQLException e) {
            System.err.println(e);
            resultString = "Error: " + e.getMessage();
        }
        httpSession.setAttribute("resultMessage", resultString);
        return "redirect:/applicant/show";
    }

    /**
     * Receives applicantID and username of the applicant from the form in the template
     * @param applicationID: application or applicant id for the record in the database
     * @param username: applicant's username
     * @return redirects to checking the application for the provided attributes
     */
    @PostMapping("/check/application")
    public String checkApplicant(@RequestParam("applicationID") int applicationID,
            @RequestParam("username") String username) {
        username = username.strip();
        String redirect = "redirect:/applicant/check/application/" + username + "/" + applicationID;
        return redirect;
    }

    /**
     * Receives a request to show application details 
     * @param httpSession: allows to change the section to be view on the template to 'details'
     * @return redirect to Modelling method or mapping to get applicant details and pass them to View
     */
    @GetMapping("/get/details")
    public String showDetails(HttpSession httpSession) {
        httpSession.setAttribute("applicant_view", "details");
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        return "redirect:/applicant/show";
    }

    /**
     * Receives a request to upload the documents for the application
     * @param httpSession: allows to change the section to be view on the template to 'upload'
     * @return redirect to Modelling method or mapping to show the template and this section
     */
    @GetMapping("/upload/documents")
    public String showUpload(HttpSession httpSession) {
        httpSession.setAttribute("applicant_view", "upload");
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        return "redirect:/applicant/show";
    }

    /**
     * Receives the document type and a MultipartFile for the uploaded document, determines which directory the
     * document is to be saved to and checks if the type of document has been uploaded already and replaces it 
     * else created a new file for the document being uploaded
     * @param docType: the type of document that is being uploaded and also a subdirectory
     * @param file: MultipartFile for the document that is being saved to the system
     * @param httpSession: has the applicantID for the applicant who is uploading the documents
     * @return redirect to the modelling mapping to be show the results of uploading this document
     */
    @PostMapping("/upload/document")
    public String uploadDoc(@RequestParam("docType") String docType, @RequestParam("document") MultipartFile file,
            HttpSession httpSession) {
        String resultMessage = "";
        String fileDir = "src/main/resources/static/docs/" + docType;
        String docName = file.getOriginalFilename();
        boolean isUploaded = FileController.writeFile(file, fileDir);

        int applicantID = (int) httpSession.getAttribute("applicantID");

        if (isUploaded) {
            resultMessage = "Document '" + docName + "' was uploaded successfully.";
            try {
                DatabaseUpdateController dbUpdateController = new DatabaseUpdateController();
                if (dbQueryController.applicantDocExists(applicantID, docType)) {
                    dbUpdateController.updateDocs(applicantID, docName, docType, fileDir);
                } else {
                    dbCreateController.addApplicantDocs(applicantID, docName, docType, fileDir);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            resultMessage = "Couldn't upload the file you provided";
        }
        httpSession.setAttribute("resultMessage", resultMessage);
        return "redirect:/applicant/show";
    }

    /**
     * Receives a request to register the user account if the application was successful and the user
     * was not previously registed, adds a user profile, a student account and temporary login details for them
     * to be able to login to the system and/or reset their passwords, sends a confirmation email when all records were added
     * @param httpSession: has the applicantID to get the record or details of the applicant from their application
     * @return redirect to the modelling mapping to be show the results for this registration
     */
    @GetMapping("/register/account")
    public String submitForm(HttpSession httpSession) {
        try {
            int applicantID = (int) httpSession.getAttribute("applicantID");
            DatabaseQueryController dbQueryController = new DatabaseQueryController();
            DatabaseCreateController dbCreateController = new DatabaseCreateController();

            Application applicant = dbQueryController.getApplicantObj(applicantID);
            String role = dbQueryController.getPostRole(applicant.getPostIdentifier());

            role = role.toUpperCase();
            if (role.contains("TUTOR")) {
                role = "ROLE_TUTOR";
            } else if (role.contains("TA") || role.indexOf("ASSISTANT") > 0) {
                role = "ROLE_TA";
            }

            String levelOfStudy = applicant.getStudyLevel();
            int yearOfStudy = applicant.getYearOfStudy();
            String majors = applicant.getQualifications();
            Student student = new Student(applicant, levelOfStudy, yearOfStudy, role, majors, null);

            // Add a new profile record for the student into the db
            dbCreateController.addProfile(student);

            // Student can't exist without Profile
            dbCreateController.addStudent(student);

            // Generates a temporary password made of username and 4 random digits
            int number = (new Random()).nextInt(9000) + 1000;
            String password = applicant.getUsername().toUpperCase() + "_" + number;
            UserLogin loginInfo = new UserLogin(applicant.getUsername(), passwordEncoder.encode(password), role);

            // Login details can't exist without profile info
            dbCreateController.addLoginDetails(loginInfo);

            EmailController.sendNewUserEmail(applicant.getEmailAddress(), loginInfo, password);
            String resultString = "You have been successfully registed, login details have been sent to the email address: "
                    + applicant.getEmailAddress();
            httpSession.setAttribute("resultMessage", resultString);
            httpSession.removeAttribute("applicant_view");
        } catch (Exception e) {
            System.err.println(e);
            String errorString = "Error: " + e.getMessage();
            httpSession.setAttribute("resultMessage", errorString);
        }
        return "redirect:/applicant/show";
    }

}