package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;
import com.mytutor.demo.EmailFiles.EmailContent;
import com.mytutor.demo.EmailFiles.EmailController;

import jakarta.servlet.http.HttpSession;

/**
 * Manages system users to reset their passwords
 */
@Controller
public class ResetPasswordController {
    @Autowired
    private DatabaseUpdateController dbUpdateController;
    @Autowired
    private DatabaseQueryController dbQueryController;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private static String subjectLine = "Reset Password OTP";
    private static String emailBody = "Use this OTP to reset your password:\n";

    private final HttpSession httpSession;

    /**
     * Takes in the http session for the request to this specific user to this controller with set attributes
     * @param httpSession
     */
    public ResetPasswordController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Receives request to show the username for the user to search for details and change them
     * @return redirects to show the username section
     */
    @GetMapping("/reset/password")
    public String showReset() {
        httpSession.setAttribute("resetShow", "inputEmail");
        return "redirect:/resetPassword";
    }

    /**
     * Shows the reset password View or wepbage
     * @param model
     * @return a View to ResetPassword template
     * @throws SQLException
     */
    @GetMapping("/resetPassword")
    public String showEmailForm(Model model) throws SQLException {
        return "ResetPassword";
    }

    /**
     * Gets the username of user and gets if the email address if the user is registed, generates a
     * One-Time-Pin(OTP) to confirm the identity of this user using the saved email address of the user
     * @param username:the person requesting reset password
     * @return redirect to show ResetPassword view
     * @throws SQLException
     */
    @PostMapping("/resetPassword")
    public String processEmailForm(@RequestParam("username") String username) throws SQLException {
        String toEmail = dbQueryController.getEmailAddress(username);
        httpSession.setAttribute("username", username);
        if (toEmail != null && toEmail.length() > 5) {
            httpSession.setAttribute("toEmail", toEmail);
            int generatedOTP = (new Random()).nextInt(9000) + 1000;
            String emailText = emailBody + generatedOTP;
            System.out.println(emailText);
            EmailContent emailContent = new EmailContent(toEmail, subjectLine, emailText);
            EmailController.sendEmail(emailContent);
            httpSession.setAttribute("OTP", generatedOTP);
            httpSession.setAttribute("resetShow", "verifyOTP");
            return "redirect:/resetPassword";
        }
        httpSession.setAttribute("resultMessage", "User not found!");
        return "redirect:/resetPassword";
    }

    /**
     * Gets the entered OTP and checks it the generated OTP and if they match allows the user to 
     * be able change their password
     * @param otp: entered One-Time-Pin for confirming the identity of the user
     * @return redirect to show ResetPassword view
     */
    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam("otp") int otp){
        int generatedOTP = (int) httpSession.getAttribute("OTP");
        if (generatedOTP == otp) {
            httpSession.setAttribute("resetShow", "passwords");
            return "redirect:/resetPassword";
        }
        httpSession.setAttribute("resultMessage", "Incorrect OTP");
        return "redirect:/resetPassword";
    }

    /**
     * Allows resending the OTP for confirming the user so can change the login password
     * @return
     */
    @GetMapping("/resendOTP")
    public String resendingOTP() {
        int generatedOTP = (int) httpSession.getAttribute("OTP");
        String toEmail = (String) httpSession.getAttribute("toEmail");
        String emailText = emailBody + generatedOTP;
        EmailContent emailContent = new EmailContent(toEmail, emailText, subjectLine);
        EmailController.sendEmail(emailContent);

        return "redirect:/resetPassword";
    }

    /**
     * Receives the new password from the form, encodes it and updates the user password in the database
     * @param newPassword: new login password to be replaced with
     * @return a View to ResetPassword 
     * @throws SQLException
     */
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("newPassword") String newPassword) throws SQLException {
        String password = passwordEncoder.encode(newPassword);
        String username = (String) httpSession.getAttribute("username");
        dbUpdateController.updatePassword(username, password);
        httpSession.setAttribute("resultMessage", "Password has been successfully reset");
        httpSession.removeAttribute("resetShow");
        return "redirect:/resetPassword";
    }
}
