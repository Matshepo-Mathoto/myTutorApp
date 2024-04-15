package com.mytutor.demo.object_files;

/**
 * Activity session extention class to include the extra details of the activity session once has been created
 */
public class SessionExtra extends ActivitySession {
    private Activity activity;
    private int availableSlots;
    private boolean isOpen;
    private boolean isFull;
    private boolean isSigned;
    private int activitySessionID;

    public SessionExtra(ActivitySession sess) {
        super(sess);
    }

    /**
     * Sets the attribute of the session extra object
     * @param sess: activity session to add extension details for
     * @param activity: the activity object which the activity session is for
     * @param availableSlots: number of available slots after tutor sign-ups to activity session
     * @param isOpen: status of whether the sign-ups to the session are still open or not
     * @param isFull: status to check whether there are still available slots or not
     * @param isSigned: used to set if the tutor has signed up for one the sessions for the same activity
     * @param activitySessionID: session id of the activity from the database records
     */
    public SessionExtra(ActivitySession sess, Activity activity, int availableSlots, boolean isOpen, boolean isFull,
            boolean isSigned, int activitySessionID) {
        super(sess);
        this.activity = activity;
        this.availableSlots = availableSlots;
        this.isOpen = isOpen;
        this.isFull = isFull;
        this.isSigned = isSigned;
        this.activitySessionID = activitySessionID;
    }

    public SessionExtra() {
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean isFull) {
        this.isFull = isFull;
    }

    public int getActivitySessionID() {
        return activitySessionID;
    }

    public void setActivitySessionID(int activitySessionID) {
        this.activitySessionID = activitySessionID;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean isSigned) {
        this.isSigned = isSigned;
    }

}
