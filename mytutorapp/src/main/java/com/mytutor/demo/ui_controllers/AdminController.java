package com.mytutor.demo.ui_controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.HelperClasses.SpringSessionController;

import jakarta.servlet.http.HttpSession;

/**
 * Manage Admin home page and reports to which the admin can generate about the system and views them on screen
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    /**
     *Database query controller with query/select methods to get certain data from the database
     */
    @Autowired
    private DatabaseQueryController dbQueryController;

    /**
     * Clears the admin httpSession from the saved attributes that were previously used in the other screens
     * @param httpSession: the user specific http session that its attributes or data is cleared
     * @return a view to the AdminHomepage/dashboard
     */
    @GetMapping("/homepage")
    public String showPage(HttpSession httpSession) {
        SpringSessionController.clearSessionAttributes(httpSession);
        return "AdminHomepage";
    }

    /**
     * Gets the System reports based on queries in retriving data about saved records on the database
     * @param model: allows to add the list of data from the database queries to be displayed as tables in the template/view
     * @return a view to AdminReports page with loaded attributes to displayed
     */
    @GetMapping("/view/reports")
    public String showReports(Model model){
        try {
            // Get 2D (table format, first row as headers) for tables and their record count in the database 
            List<List<String>> tablesRecordCount = dbQueryController.getRecordCounts();
            // Get a table information with number of participants per course grouped according to their role in that course
            List<List<String>> participantspercourse = dbQueryController.getParticipantsPerCourse();
            //Get the info for the participants added to course but aren't participating or have login accounts to the system
            List<List<String>> participantswithoutaccounts = dbQueryController.getParticipantsWithoutAccounts();

            model.addAttribute("tableData", tablesRecordCount);
            model.addAttribute("participantspercourse", participantspercourse);
            model.addAttribute("participantswithoutaccounts", participantswithoutaccounts);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "AdminReports";
    }

}
