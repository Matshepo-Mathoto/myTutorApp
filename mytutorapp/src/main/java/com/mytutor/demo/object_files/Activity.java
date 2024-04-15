package com.mytutor.demo.object_files;

/**
 * Defines the duties of the tutors to which the session sign-ups to them are created on
 */
public class Activity {
    private String activityName;
    private String activityDescription;
    private String activityType;
    private int courseID;

    public Activity() {
    }

    /**Sets the activity attributes
     * @param activityName: name of the activity
     * @param activityDescription: description for the activity to which the activity sessions should be based on
     * @param activityType: type of the activity such as Hybrid, online or on-campus
     * @param courseID: the course the activity belongs to
     */
    public Activity(String activityName, String activityDescription, String activityType, int courseID) {
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.activityType = activityType;
        this.courseID = courseID;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }


    public String getActivityType() {
        return activityType;
    }


    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Activity [activityName=" + activityName + ", activityDescription=" + activityDescription
                + ", activityType=" + activityType + ", courseID=" + courseID + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activityName == null) ? 0 : activityName.hashCode());
        result = prime * result + ((activityDescription == null) ? 0 : activityDescription.hashCode());
        result = prime * result + ((activityType == null) ? 0 : activityType.hashCode());
        result = prime * result + courseID;
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
        Activity other = (Activity) obj;
        if (activityName == null) {
            if (other.activityName != null)
                return false;
        } else if (!activityName.equals(other.activityName))
            return false;
        if (activityDescription == null) {
            if (other.activityDescription != null)
                return false;
        } else if (!activityDescription.equals(other.activityDescription))
            return false;
        if (activityType == null) {
            if (other.activityType != null)
                return false;
        } else if (!activityType.equals(other.activityType))
            return false;
        if (courseID != other.courseID)
            return false;
        return true;
    }

}
