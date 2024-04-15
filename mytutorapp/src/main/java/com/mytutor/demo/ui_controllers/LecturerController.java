package com.mytutor.demo.ui_controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mytutor.demo.HelperClasses.SpringSessionController;


/**
 * Manages Lecturer and Convenor functions and mappings to their user Homepage
 */
@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    /**
     * Checks which type or lecturer is this and shows the View or Webpage for their role
     * @return a webpage view
     */
    @GetMapping("/homepage")
    public String profile() {
        String role = SpringSessionController.getRole();
        if (role.equalsIgnoreCase("ROLE_CONVENOR")){
            return "ConvenorHomepage";
        } 
        return "LecturerHomepage";
    }
}
