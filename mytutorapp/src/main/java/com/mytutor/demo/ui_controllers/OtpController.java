package com.mytutor.demo.ui_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.EmailFiles.EmailContent;
import com.mytutor.demo.EmailFiles.EmailSenderService;
import com.mytutor.demo.object_files.Otp;
import com.mytutor.demo.object_files.Student;

/*import com.mytutor.demo.Database.DatabaseCreateController;
import com.mytutor.demo.Database.DatabaseQueryController;
import com.mytutor.demo.Email.EmailContent;
import com.mytutor.demo.Email.EmailSenderService;
import com.mytutor.demo.People.Lecturer;
import com.mytutor.demo.People.Student;*/

@Controller
public class OtpController {
    private int other;
    private Student student;
    private String errorMessage= "";
    private String password;
    @Autowired private EmailSenderService emailService;

    @Autowired 
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/verify-email")
    public String showForm(Model model) {
        model.addAttribute("emailForm", new Otp(0));
        model.addAttribute("error", errorMessage);
        return "OtpTemplate";
    }

    // Get the auto-generated OTP from the tutor registration class
    public void userOtp(int otp, Student student, String password) 
    {
        other = otp;
        this.student = student;
        this.password = password;
        System.out.println(student.getRole());
    }

    // Get the OTP from the user and compare with the auto-generated otp
    @PostMapping("/verify-email")
    public String submitForm(@ModelAttribute("emailForm") Otp emailForm, Model model) 
    {
        
        
        System.out.println(other);
        if (other == emailForm.getOtp()) 
        {
            try {
                DatabaseCreateController databaseCreateController = new DatabaseCreateController();
                //DatabaseQueryController databaseQueryController = new DatabaseQueryController();
                // DatabaseQueryController databaseQueryController = new
                // DatabaseQueryController();
                databaseCreateController.addProfile(student);
                //System.out.println(student.getRole());
                String role = student.getRole();
                if (role.equals("tutor") || role.equals("ta")) 
                {
                    role = "ROLE_" + role.toUpperCase();
                    student.setRole(role);
                    databaseCreateController.addStudent(student);
                    databaseCreateController.addLoginDetails(student.getUsername(), passwordEncoder.encode(password), role);
                }

            } catch (Exception e) {
                // TODO: handle exception
                System.err.println(e);
            }

            return "redirect:/login";
        }
        else
        {
            errorMessage = "Incorrect OTP. Please try again.";
            return "redirect:/verify-email";
        }
        
    }
    @PostMapping("/resend-otp")
    public String resend()
    {
        String text = "Hello " + student.getUsername() + "\n" + "Please use this OTP code to verify your email: \n" + other + "If you didn't request this email you can ignore it. \n Thank you, \n MyTutor";
        EmailContent emailDetails = new EmailContent(student.getEmailAddress(),text, "Resend OTP");
        emailService.sendSimpleEmail(emailDetails);
        return "redirect:/verify-email";

    }

}
