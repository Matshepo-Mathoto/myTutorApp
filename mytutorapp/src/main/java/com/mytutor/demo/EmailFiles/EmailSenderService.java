package com.mytutor.demo.EmailFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends a simple email structure as subject line, body text and recipients or whom the email is directed to
 */
@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    public int sendSimpleEmail(EmailContent email) {
        return sendSimpleEmail(email.getRecipient(), email.getSubject(), email.getText());
    }

    /**
     * Sends a simple email to a single recipient
     * @param toEmail: to whom the email is being sent to
     * @param subject: subject line of the mail
     * @param body: text to be sent to the recipient
     * @return 1 for successful
     */
    public int sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("my2023tutor@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        javaMailSender.send(message);
        System.out.println("Mail Send successfully...");

        return 1;
    }

    /**
     * Sends a simple email to multiple recipients
     * @param toEmail: the specified recipient of the email
     * @param subject: header subject for the email sent
     * @param body: text or content of the email to be sent
     * @param cc: other recipients of the email being cc
     * @return 1 for successful
     */
    public int sendCCEmail(String toEmail, String subject, String body, String[] cc) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("my2023tutor@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        if (cc != null && cc.length > 0){
            message.setCc(cc);
        }
        javaMailSender.send(message);
        System.out.println("Mail Send successfully...");

        return 1;
    }

}
