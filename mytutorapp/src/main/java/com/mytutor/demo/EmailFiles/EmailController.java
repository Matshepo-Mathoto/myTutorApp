package com.mytutor.demo.EmailFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mytutor.demo.object_files.Application;
import com.mytutor.demo.object_files.ApplicationPost;
import com.mytutor.demo.object_files.UserLogin;

/**
 * Manages outgoing email contents to specified recipients
 */
@RestController
public class EmailController {
    @Autowired
    private static EmailSenderService emailService;

    public EmailController(EmailSenderService emailService) {
        EmailController.emailService = emailService;
    }

    /**
     * Uses an EmailContent object with email structure to send an email to the specified recipient
     * @param emailContent: email object with recipient, subject line and body texts.
     * @return status of sending the email with 1 being successful
     */
    public static int sendEmail(EmailContent emailContent) {
        int status = emailService.sendSimpleEmail(emailContent);
        return status;
    }

    /**
     * Sends the confirmation email for the tutor session signup for a specific course
     * @param toEmail: the recipient of the email
     * @param sessionID: Id of the session the tutor has signed up for
     * @param courseName: The course for which the session they have signed up belongs
     * @return 1 for success
     */
    public static int sendSignUpEmail(String toEmail, int sessionID, String courseName){
        String subjectLine = "Confirmation: Signed up for session : " + sessionID;
        String body = "You have successfully signed up for session number: " + sessionID + " for " + courseName + " course.";
        EmailContent emailContent = new EmailContent(toEmail, subjectLine, body);
        return EmailController.sendEmail(emailContent);
    }

    /**
     * Sends a confirmation email to the applicant after successful capture of their application for job and details to
     * track their application
     * @param applicant: object of an Application for an applicant with their provided data including list of courses
     * @param applicantID: the submission ID of the applicant relating to their record in the database for this application
     * @return Successful if no errors were encounted while sending the email
     */
    public static String sendApplicationEmail(Application applicant, int applicantID) {
        String link = "http://196.47.239.204:8080/applicant/check/application/"
                + applicant.getUsername() + "/" + applicantID;
        String subjectLine = "Application submitted, ApplicantID: " + applicantID;
        String emailBody = "Application to " + applicant.getPostIdentifier() + " submitted successfull";
        emailBody += "\n\n" + applicant.toString();
        emailBody += "\n\nUse this link to check your application:\n" + link;
        EmailContent emailContent = new EmailContent(applicant.getEmailAddress(), subjectLine, emailBody);
        EmailController.sendEmail(emailContent);
        return "Successful";
    }
    
    /**
     * Sends a confirmation email for the user successfully added to MyTutorApp to a specific role in the system
     * with link to access login for the App
     * @param emailAddress: recipient of the email (new user added to the system)
     * @param userLogin: object for use login with username and role for the added user
     * @param password: the non-encrypted temporary password for user login
     * @return "Successful" if no error occured
     */
    public static String sendNewUserEmail(String emailAddress, UserLogin userLogin, String password) {
        String subjectLine = "Added to MyTutorApp with role: " + userLogin.getRole();
        String emailBody = "Use the following details to login or reset your password:";
        emailBody += "\nUsername: " + userLogin.getUsername();
        emailBody += "\nPassword: " + password;
        emailBody += "\n\n link: http://196.47.239.204:8080/login";
        //System.out.println(emailBody);
        EmailContent emailContent = new EmailContent(emailAddress, subjectLine, emailBody);
        EmailController.sendEmail(emailContent);
        return "Successful";
    }

    /**
     * Sends a confirmation email for successful creation of the Application Post to the Creator with the link
     * for applicants to use for making the application
     * @param toEmail: the Post creator email to send confirmation too
     * @param result: has the link and postIdentifier for the making applications in response to this post
     * @param applicationPost: the Post object with the information about the created post
     * @return 1 for successful
     */
    public static int sendPostCreationConfirmation(String toEmail,String result, ApplicationPost applicationPost){
            String subjectline = "MyTutorApp: Application post successfully created";
            String body = result + "\n\n" + applicationPost.toString();
            EmailContent emailContent = new EmailContent(toEmail, subjectline, body);
            EmailController.sendEmail(emailContent);

            return 1;
    }

}
