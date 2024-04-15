package com.mytutor.demo.object_files;

import java.util.List;

/**
 * Tutor sign-ups to the activity session
 */
public class ActivitySignUp {
    private SessionExtra sessionExtra;
    private int sessionID;
    private List<String> tutors;
    private int numberOfSignups;

   /**
    * Sets the attribute of the object
     * @param sessionExtra: the activity session details for the sign-ups 
     * @param sessionID: activity session id on database table records
     * @param tutors:list of tutors signed up for this activity session
     * @param numberOfSignups: number of tutors or signups
     */
     public ActivitySignUp(SessionExtra sessionExtra, int sessionID, List<String> tutors,
            int numberOfSignups) {
        this.sessionExtra = sessionExtra;
        this.sessionID = sessionID;
        this.tutors = tutors;
        this.numberOfSignups = numberOfSignups;
    }

    public ActivitySignUp() {
    }

    public SessionExtra getSessionExtra() {
        return sessionExtra;
    }

    public void setSessionExtra(SessionExtra sessionExtra) {
        this.sessionExtra = sessionExtra;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public List<String> getTutors() {
        return tutors;
    }

    public void setTutors(List<String> tutors) {
        this.tutors = tutors;
    }

    public int getNumberOfSignups() {
        return numberOfSignups;
    }

    public void setNumberOfSignups(int numberOfSignups) {
        this.numberOfSignups = numberOfSignups;
    }

    @Override
    public String toString() {
        return "ActivitySignUp [sessionExtra=" + sessionExtra + ", sessionID=" + sessionID
                + ", tutors=" + tutors + ", numberOfSignups=" + numberOfSignups + "]";
    }

}
