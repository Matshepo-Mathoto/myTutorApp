package com.mytutor.demo.object_files;

/**
 * Tutor session logs for starting their signed up sessions meetings and time
 * the finish doing them with some feedback from it
 */
public class SessionLog {
    private int logID;
    private String tutorUsername;
    private String startTime;
    private String endTime;
    private String sessionFeedback;
    private int sessionID;
    private String startingCoordinates;
    private String finishingCoordinates;

    public SessionLog() {
    }

    public SessionLog(String tutorUsername, int sessionID, String startingCoordinates) {
        this.tutorUsername = tutorUsername;
        this.sessionID = sessionID;
        this.startingCoordinates = startingCoordinates;
    }

    /**
     * Sets attribute for the sessionlog object
     * @param logID: sessionlog id for the record on the database
     * @param tutorUsername: the username of the tutor provided this session log
     * @param startTime: the date and time the tutor started their session meeting
     * @param endTime: the date and time the tutor ended their session meeting
     * @param sessionFeedback: the feedback their provided after the session
     * @param sessionID: the activity session they signed up for providing the log for
     * @param startingCoordinates: where they were when they started the meeting
     * @param finishingCoordinates: where they were when they ended the meeting
     */
    public SessionLog(int logID, String tutorUsername, String startTime, String endTime, String sessionFeedback,
            int sessionID, String startingCoordinates, String finishingCoordinates) {
        this.logID = logID;
        this.tutorUsername = tutorUsername;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionFeedback = sessionFeedback;
        this.sessionID = sessionID;
        this.startingCoordinates = startingCoordinates;
        this.finishingCoordinates = finishingCoordinates;
    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public String getTutorUsername() {
        return tutorUsername;
    }

    public void setTutorUsername(String tutorUsername) {
        this.tutorUsername = tutorUsername;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSessionFeedback() {
        return sessionFeedback;
    }

    public void setSessionFeedback(String sessionFeedback) {
        this.sessionFeedback = sessionFeedback;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getStartingCoordinates() {
        return startingCoordinates;
    }

    public void setStartingCoordinates(String startingCoordinates) {
        this.startingCoordinates = startingCoordinates;
    }

    public String getFinishingCoordinates() {
        return finishingCoordinates;
    }

    public void setFinishingCoordinates(String finishingCoordinates) {
        this.finishingCoordinates = finishingCoordinates;
    }

    @Override
    public String toString() {
        return "SessionLog [tutorUsername=" + tutorUsername + ", startTime=" + startTime + ", endTime=" + endTime
                + ", sessionFeedback=" + sessionFeedback + ", sessionID=" + sessionID + ", startingCoordinates="
                + startingCoordinates + ", finishingCoordinates=" + finishingCoordinates + "]";
    }
}
