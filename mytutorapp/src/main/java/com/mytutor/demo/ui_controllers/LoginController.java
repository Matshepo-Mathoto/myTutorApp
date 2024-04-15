package com.mytutor.demo.ui_controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mytutor.demo.object_files.UserLogin;

import jakarta.servlet.http.HttpSession;

/**
 * Accepts incoming requests to show the login page and to exit where user session needs to invalidated
 */
@Controller
public class LoginController {

    /**
     * Invalidates the user session and shows the Login View Page
     * @param model: allows to pass the UserLogin object/form to capture login details
     * @param httpSession: used to invalidate the previous user session same as logging out 
     * @return a View to the Login Page
     */
    @GetMapping("/login")
    public String showForm(Model model, HttpSession httpSession) {
        httpSession.invalidate();
        model.addAttribute("LoginForm", new UserLogin());
        return "LoginTemplate";
    }

    /**
     * Gets exit mappings to invalidate user session (such as information stored on resetPassword or application capture)
     * @return a redirect to show the login Page
     */
    @GetMapping("/exit")
    public String exitPage() {
        //httpSession.invalidate();
        return "redirect:/login";
    }

    /**
     * Receives "/" mapping to redirect to login page
     * @return a redirect to show the Login Page
     */
    @GetMapping("/")
    public String showLogin(){
        return "redirect:/login";
    }

    @GetMapping("/help/{section}")
    public String showUserManual(@PathVariable("section") String section, Model model){
        model.addAttribute("open_section", section);
        return "UserManual";
    }

}
