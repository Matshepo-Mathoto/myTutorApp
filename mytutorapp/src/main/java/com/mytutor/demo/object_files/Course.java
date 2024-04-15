package com.mytutor.demo.object_files;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * For course site data/information and its participants
 */
public class Course implements Serializable {
    private String courseCode;
    private String department;
    private String courseDescription;
    private String creatorID;
    private int courseYear;
    private int numberOfStudents;
    private String teachingAssistant;
    private String convenor;
    private int numTutors;
    private String displayCourse;
    private List<String> tutors;
    private List<String> lecturers;

    public Course() {
        this("", "", "", "", 0, 0);
    }

    /**Sets the course attributes
     * @param courseCode: the code of the course 
     * @param department: the department the course belongs to
     * @param courseDescription: the long name or description of the course
     * @param creatorID: the username of the creator of this course
     * @param courseYear: the year the course if effective for
     * @param numberOfStudents: general number of students to the course
     */
    public Course(String courseCode, String department, String courseDescription, String creatorID, int courseYear,
            int numberOfStudents) {
        this.courseCode = courseCode;
        this.department = department;
        this.courseDescription = courseDescription;
        this.creatorID = creatorID;
        this.courseYear = courseYear;
        this.numberOfStudents = numberOfStudents;
        this.tutors = new ArrayList<String>();
        this.lecturers = new ArrayList<String>();
        this.numTutors = 0;
    }

    public Course(String courseCode, int courseYear) {
        this(courseCode, courseYear, null); // ,null)
    }

    public Course(String courseCode, int courseYear, String courseDescription) { // , Convenor convenor {
        this.courseCode = courseCode;
        this.courseYear = courseYear;
        this.courseDescription = courseDescription;
        this.tutors = new ArrayList<String>();
        this.lecturers = new ArrayList<String>();
        this.numberOfStudents = 0;
        this.creatorID = "";
        numTutors = 0;

    }

    public Course updateCourse(Course course) {
        if (course.courseCode != null && course.courseCode.length() > 5) {
            this.courseCode = course.courseCode;
        }
        if (course.courseYear != 0) {
            this.courseYear = course.courseYear;
        }
        if (course.courseDescription != null) {
            this.courseDescription = course.courseDescription;
        }
        if (course.department != null) {
            this.department = course.department;
        }
        if (course.creatorID != null && course.creatorID.length() > 0) {
            this.creatorID = course.creatorID;
        }
        if (course.numberOfStudents != 0) {
            this.numberOfStudents = course.numberOfStudents;
        }
        if (course.tutors != null) {
            this.tutors = course.tutors;
        }
        if (course.numTutors != 0) {
            this.numTutors = course.numTutors;
        }
        if (course.teachingAssistant != null) {
            this.teachingAssistant = course.teachingAssistant;
        }
        if (course.convenor != null) {
            this.convenor = course.convenor;
        }
        if (course.lecturers != null) {
            this.lecturers = course.lecturers;
        }
        return this;
    }

    /**
     * Set the display course name of the course from courseCode and year
     * @param courseCode: code of the course
     * @param courseYear: effective year for the course
     */
    public void setDisplayCourse(String courseCode, int courseYear) {
        this.displayCourse = courseCode + "," + Integer.toString(courseYear);
    }

    public String getDisplayCourse() {
        this.displayCourse = courseCode + "," + Integer.toString(courseYear);
        return displayCourse;
    }

    public String getcourseCode() {
        return courseCode;
    }

    public void setcourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(int courseYear) {
        this.courseYear = courseYear;
    }

    public String getTutorID(int index) {
        return tutors.get(index);
    }

    public int addTutor(String tutor) {
        this.tutors.add(numTutors++, tutor);
        return numTutors - 1;
    }

    public int getNumTutors() {
        return this.tutors.size();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public List<String> getTutors() {
        return tutors;
    }

    public void setTutors(List<String> tutors) {
        this.tutors = tutors;
        this.numTutors = tutors.size();
    }

    public void setNumTutors(int numTutors) {
        this.numTutors = numTutors;
    }

    public String getTeachingAssistant() {
        return teachingAssistant;
    }

    public void setTeachingAssistant(String teachingAssistant) {
        this.teachingAssistant = teachingAssistant;
    }

    public String getConvenor() {
        return convenor;
    }

    public void setConvenor(String convenor) {
        this.convenor = convenor;
    }

    @Override
    public String toString() {
        return "Course Code: " + courseCode + "\nCourse Year: " + courseYear + "\nDepartment: " + department
                + "\nCourse Description: " + courseDescription + "\nCreatorID: " + creatorID
                + "\nNumber Of Students: " + numberOfStudents + "\nConvenor: " + convenor
                + "\nTeaching Assistant: " + teachingAssistant + "\nLecturers: " + lecturers
                + "\nNumber Of Lecturers:" + lecturers.size() + "\nTutors: " + tutors
                + "\nNumber Of Tutors:" + tutors.size();
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public void setDisplayCourse(String displayCourse) {
        this.displayCourse = displayCourse;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseCode == null) ? 0 : courseCode.hashCode());
        result = prime * result + ((department == null) ? 0 : department.hashCode());
        result = prime * result + ((courseDescription == null) ? 0 : courseDescription.hashCode());
        result = prime * result + ((creatorID == null) ? 0 : creatorID.hashCode());
        result = prime * result + courseYear;
        result = prime * result + numberOfStudents;
        result = prime * result + ((tutors == null) ? 0 : tutors.hashCode());
        result = prime * result + ((teachingAssistant == null) ? 0 : teachingAssistant.hashCode());
        result = prime * result + ((convenor == null) ? 0 : convenor.hashCode());
        result = prime * result + numTutors;
        result = prime * result + ((displayCourse == null) ? 0 : displayCourse.hashCode());
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
        Course other = (Course) obj;
        if (courseCode == null) {
            if (other.courseCode != null)
                return false;
        } else if (!courseCode.equals(other.courseCode))
            return false;
        if (department == null) {
            if (other.department != null)
                return false;
        } else if (!department.equals(other.department))
            return false;
        if (courseDescription == null) {
            if (other.courseDescription != null)
                return false;
        } else if (!courseDescription.equals(other.courseDescription))
            return false;
        if (creatorID == null) {
            if (other.creatorID != null)
                return false;
        } else if (!creatorID.equals(other.creatorID))
            return false;
        if (courseYear != other.courseYear)
            return false;
        if (numberOfStudents != other.numberOfStudents)
            return false;
        if (tutors == null) {
            if (other.tutors != null)
                return false;
        } else if (!tutors.equals(other.tutors))
            return false;
        if (teachingAssistant == null) {
            if (other.teachingAssistant != null)
                return false;
        } else if (!teachingAssistant.equals(other.teachingAssistant))
            return false;
        if (convenor == null) {
            if (other.convenor != null)
                return false;
        } else if (!convenor.equals(other.convenor))
            return false;
        if (numTutors != other.numTutors)
            return false;
        if (displayCourse == null) {
            if (other.displayCourse != null)
                return false;
        } else if (!displayCourse.equals(other.displayCourse))
            return false;
        return true;
    }

    public List<String> getLecturers() {
        return lecturers;
    }

    public void setLecturers(List<String> lecturers) {
        this.lecturers = lecturers;
    }

    public void addLecturer(String username){
        lecturers.add(username);
    }

    public int getNumberOfLecturers() {
        return lecturers.size();
    }
}
