package com.mytutor.demo.EmailFiles;

/**
 * Defines the structure of a simple email 
 */
public class EmailContent {
    private String recipient;
    private String text;
    private String subject;

    public EmailContent(String recipient, String subject, String text) {
        this.recipient = recipient;
        this.subject = subject;
        this.text = text;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "EmailContent [recipient=" + recipient + ", text=" + text + ", subject=" + subject + "]";
    }

}
