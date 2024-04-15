package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.object_files.Application;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.EmailFiles.EmailContent;
import com.mytutor.demo.EmailFiles.EmailSenderService;
import com.mytutor.demo.object_files.Student;

@Controller
public class RegisterController 
{
    // Do not create email service or OtpController object. Autowired automatically
    // creates the object at compile time and set up dependencies
    @Autowired
    private EmailSenderService emailService;
    @Autowired
    private OtpController otpController;
    private String resultString = "";
    //private Student stud = new Student();
   


    // This directs the user to the register page
    @GetMapping("/newUser")
    public String showForm(Model model) {
        model.addAttribute("newUserForm",  new Student());
        model.addAttribute("resultString", resultString);
        return "RegistrationTemplate";
    }

    // this posts the html and gets input
    @PostMapping("/newUser")
    public String submitForm(@ModelAttribute("newUserForm") Student newUserForm, @RequestParam("password") String password) throws SQLException 
    {
        DatabaseQueryController databaseQueryController = new DatabaseQueryController();
        System.out.println(newUserForm.getRole());
        
        //boolean profileExists = databaseQueryController.profileExists(newUserForm.getUsername());
        if (newUserForm.getRole().equals("tutor") || newUserForm.getRole().equals("ta")) 
        {

            
            int applicantID =  databaseQueryController.getLatestApplication(newUserForm.getUsername());
            Application applicant = databaseQueryController.getApplicantObj(applicantID);
            System.out.println(applicant.toString());

            if (applicant != null)  
            {
                System.out.println("We are in");
                if(applicant.getApplicationStatus() != null && applicant.getApplicationStatus().equalsIgnoreCase("Successful" ))
                {
                    if (databaseQueryController.profileExists(applicant.getUsername())) 
                    {
                        resultString = "Profile already exists. Go to login.";
                        return "redirect:/newUser"; 

                    } 
                    else
                    {
                        // create a random 4 digit OTP number
                        Random random = new Random();
                        int max = 9999, min = 1000;
                        int generate = random.nextInt((max - min + 1) )+ min;

                        String text = "Hello " + newUserForm.getUsername() + "\n" + "Please use this OTP code to verify your email: \n" + Integer.toString(generate) + "\nIf you didn't request this email you can ignore it. \nThank you, \nMyTutor";
                        
                        EmailContent emailDetails = new EmailContent(newUserForm.getEmailAddress(), "welcome", text);
                        emailService.sendSimpleEmail(emailDetails); // Send email to the user email address
                        otpController.userOtp(generate, newUserForm, password); // Send the auto-generated OTP to the otp controller class for  
                        return "redirect:/verify-email";                                   
                    }
                }
                else if(applicant.getApplicationStatus() != null && !applicant.getApplicationStatus().equalsIgnoreCase("Successful" ))
                {
                    resultString = "your application was not successful";
                    return "redirect:/newUser";
                }
                else
                {
                    resultString = "you dont have an application";
                    System.out.println(resultString);
                    return "redirect:/newUser";
                }    
            }

        }
        
        

        // generate tutorID
        return "redirect:/login"; // redirect to the otp pag
    }
}
