package com.mytutor.demo.ui_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.EmailFiles.EmailController;
import com.mytutor.demo.HelperClasses.SpringSessionController;
import com.mytutor.demo.object_files.ApplicationPost;

import jakarta.servlet.http.HttpSession;

/**
 * Allows Creating New Application Posts for Tutor or TA applications
 */
@Controller
@RequestMapping("/admin")
public class ApplicationPostController {
    @Autowired
    private DatabaseCreateController dbCreateController;
    @Autowired
    private DatabaseQueryController dbQueryController;

    /**
     * Shows template to Create or add details for the new ApplicationPost object
     * 
     * @param model: allows setting a new ApplicationPost object
     * @return a View to the NewApplicationPost template or Form
     */
    @GetMapping("/create/post")
    public String getPost(Model model) {
        model.addAttribute("ApplicationPostForm", new ApplicationPost());
        return "NewApplicationPost";
    }

    /**
     * Receives an ApplicationPost object from the form submission to add the new
     * ApplicationPost record to the database and then sends a confirmation email to
     * the creator with details about the post and how to make applications for this
     * post
     * 
     * @param ApplicationPostForm: post object with ApplicationPost details for the
     *                             record to be added
     * @param httpSession:         allows setting result messages for adding this
     *                             post to the database
     * @return a redirect mapping to show application results for adding this
     *         application and the link for applications
     */
    @PostMapping("/new/post")
    public String submitPost(@ModelAttribute("ApplicationPostForm") ApplicationPost ApplicationPostForm,
            HttpSession httpSession) {
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        ApplicationPostForm.setAdminId(SpringSessionController.getLoggedUser());
        String result = "";
        try {
            dbCreateController.addPost(ApplicationPostForm); // Add a user post to its DB table

            String toEmail = dbQueryController.getEmailAddress(SpringSessionController.getLoggedUser());
            result = "The Application Post was created successfully. \n\nApplications can be made using the following link:";
            result += "\n http://196.47.239.204:8080/applicant/new/application/"
                    + ApplicationPostForm.getApplicationPostId();

            // Send a confirmation email for successful creation of the application post
            // with the link to make applications for this
            EmailController.sendPostCreationConfirmation(toEmail, result, ApplicationPostForm);
        } catch (Exception e) {
            System.err.println(e);
            result = "An error occured try using a different Post Identifier";
        }
        httpSession.setAttribute("resultMessage", result);
        return "redirect:/admin/create/post";
    }
}