package com.mytutor.demo.object_files;

/**
 * Activity session sign-ups for tutor duties (activities) for their meetings in doing the duties
 */
public class ActivitySession {
    private int activityID;
    private String startTime;
    private String endTime;
    private String day;
    private String venue;
    private int numberOfTutors;
    private int expectedStudents;
    private boolean recurring;
    private String recurringFrom;
    private String recurringTo;
    private String openingDate;
    private String closingDate;
    private String[] slotArray;

    /**
     * Sets the attributes of the object
     * @param activityID: the activity the sign-up is for
     * @param startTime: when the session meeting will start
     * @param endTime: when the session meeting will end
     * @param day: day the of the meeting on a week
     * @param venue: where the meeting takes place
     * @param numberOfTutors: Maximum number of expected tutors to the meeting
     * @param expectedStudents: Number of possible expected students to the meeting for tutor help
     * @param recurring: boolean condition for whether the the meeting is once or or repeats weekly
     * @param recurringFrom: start date of the session meeting
     * @param recurringTo: end date of the session meeting
     * @param openingDate: opening for sign-ups to the session 
     * @param closingDate: closing for sign-ups to the session
     */
    public ActivitySession(int activityID, String startTime, String endTime, String day, String venue,
            int numberOfTutors, int expectedStudents, boolean recurring, String recurringFrom, String recurringTo,
            String openingDate, String closingDate) {
        this.activityID = activityID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.venue = venue;
        this.numberOfTutors = numberOfTutors;
        this.expectedStudents = expectedStudents;
        this.recurring = recurring;
        this.recurringFrom = recurringFrom;
        this.recurringTo = recurringTo;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.slotArray = new String[10];
    }

    public ActivitySession() {
    }

    public ActivitySession(int activityID, String startTime, String endTime, String day, String venue,
            int numberOfTutors, int expectedStudents, boolean recurring, String openingDate, String closingDate) {
        this.activityID = activityID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.venue = venue;
        this.numberOfTutors = numberOfTutors;
        this.expectedStudents = expectedStudents;
        this.recurring = recurring;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.slotArray = new String[10];
    }

    public ActivitySession(ActivitySession sess) {
        this(sess.activityID, sess.startTime, sess.endTime, sess.day, sess.venue, sess.numberOfTutors,
                sess.expectedStudents, sess.recurring, sess.recurringFrom, sess.recurringTo, sess.openingDate, sess.closingDate);
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getNumberOfTutors() {
        return numberOfTutors;
    }

    public void setNumberOfTutors(int numberOfTutors) {
        this.numberOfTutors = numberOfTutors;
    }

    public int getExpectedStudents() {
        return expectedStudents;
    }

    public void setExpectedStudents(int expectedStudents) {
        this.expectedStudents = expectedStudents;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecurringFrom() {
        return recurringFrom;
    }

    public void setRecurringFrom(String recurringFrom) {
        this.recurringFrom = recurringFrom;
    }

    public String getRecurringTo() {
        return recurringTo;
    }

    public void setRecurringTo(String recurringTo) {
        this.recurringTo = recurringTo;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    @Override
    public String toString() {
        return "ActivitySessions [activityID=" + activityID + ", startTime=" + startTime + ", endTime=" + endTime
                + ", day=" + day + ", venue=" + venue + ", numberOfTutors=" + numberOfTutors + ", expectedStudents="
                + expectedStudents + ", recurring=" + recurring + ", recurringFrom=" + recurringFrom + ", recurringTo="
                + recurringTo + ", openingDate=" + openingDate + ", closingDate=" + closingDate + "]";
    }

    public String[] getSlotArray() {
        return slotArray;
    }

    public void setSlotArray(String[] slotArray) {
        this.slotArray = slotArray;
    }
}