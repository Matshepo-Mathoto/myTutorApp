package com.mytutor.demo.object_files;

/**
 * A Person super class object for each user as their profile and for applicants details as well
 */
public class Person {
    private String username;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String emailAddress;
    private String title;

    public Person() {

    }

    public Person(String firstName, String username, String lastName, String contactNumber, String emailAddress,
            String title) {
        this.firstName = firstName;
        this.username = username;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.title = title;
    }

    public Person(Person copyPerson) {
        if (copyPerson != null) {
            this.firstName = copyPerson.firstName;
            this.username = copyPerson.username;
            this.lastName = copyPerson.lastName;
            this.contactNumber = copyPerson.contactNumber;
            this.emailAddress = copyPerson.emailAddress;
            this.title = copyPerson.title;
        }

    }

    public Person updatePerson(Person anotherPerson){
        if (anotherPerson != null){
            if (anotherPerson.username != null && (anotherPerson.username.length() > 1)){
                this.username = anotherPerson.username;
            }
             if (anotherPerson.firstName != null && (anotherPerson.firstName.length() > 1)){
                this.firstName = anotherPerson.firstName;
            }
             if (anotherPerson.lastName != null && (anotherPerson.lastName.length() > 1)){
                this.lastName = anotherPerson.lastName;
            }
             if (anotherPerson.emailAddress != null && (anotherPerson.emailAddress.length() > 5)){
                this.emailAddress = anotherPerson.emailAddress;
            }
             if (anotherPerson.contactNumber != null && (anotherPerson.contactNumber.length() > 1)){
                this.contactNumber = anotherPerson.contactNumber;
            }
             if (anotherPerson.title != null && (anotherPerson.title.length() > 1)){
                this.title = anotherPerson.title;
            }
            
        }
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "\nTitle:\n" + title + "\n\nFirst Name:\n" + firstName +
                "\n\nLast Name:\n" + lastName + "\n\nUsername:\n" + username +
                "\n\nEmail Address:\n" + emailAddress + "\n\nContact Number:\n" + contactNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((contactNumber == null) ? 0 : contactNumber.hashCode());
        result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (contactNumber == null) {
            if (other.contactNumber != null)
                return false;
        } else if (!contactNumber.equals(other.contactNumber))
            return false;
        if (emailAddress == null) {
            if (other.emailAddress != null)
                return false;
        } else if (!emailAddress.equals(other.emailAddress))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

}