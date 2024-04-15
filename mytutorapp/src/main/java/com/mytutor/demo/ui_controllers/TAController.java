package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.HelperClasses.SpringSessionController;
import com.mytutor.demo.HelperClasses.StaticHelperMethods;
import com.mytutor.demo.object_files.Activity;
import com.mytutor.demo.object_files.ActivitySession;
import com.mytutor.demo.object_files.ActivitySignUp;
import com.mytutor.demo.object_files.Event;
import com.mytutor.demo.object_files.SessionExtra;
import com.mytutor.demo.object_files.SessionLog;

import jakarta.servlet.http.HttpSession;

/**
 * Controls the Teaching assistant duties and functions to their registered course
 */
@Controller
@RequestMapping("/ta")
public class TAController {
    @Autowired
    private DatabaseQueryController dbQueryController;
    @Autowired
    private DatabaseCreateController dbCreateController;

    /**
     * Get the sessionExtra object for the activity session 
     * @param sessionID: activity sessionID
     * @return sessionExtra object with attributes on tutor signups
     * @throws SQLException
     */
    public SessionExtra getSessionExtra(int sessionID) throws SQLException {
        ActivitySession activitySession = dbQueryController.getActivitySession(sessionID);
        SessionExtra sessionExtra = new SessionExtra(activitySession);
        Activity activity = dbQueryController.getActivity(sessionExtra.getActivityID());
        sessionExtra.setActivity(activity);

        int num_signups = dbQueryController.getNumSignups(sessionID);
        int numAvailable = activitySession.getNumberOfTutors() - num_signups;
        sessionExtra.setAvailableSlots(numAvailable);

        boolean isFull = numAvailable == 0;
        sessionExtra.setFull(isFull);

        boolean isOpen = dbQueryController.isSessionOpen(sessionID);
        sessionExtra.setOpen(isOpen);
        return sessionExtra;
    }

    /**
     * Gets sign-up information to the activity session
     * @param sessionID: the activity session Id to get sign-up info
     * @return ActivitySignup object for the session signup info
     * @throws SQLException
     */
    public ActivitySignUp getSectionInfo(int sessionID) throws SQLException {
        List<String> lstTutors = dbQueryController.getTutorSignup(sessionID);

        SessionExtra sessionExtra = getSessionExtra(sessionID);
        int num_signups = sessionExtra.getNumberOfTutors() - sessionExtra.getAvailableSlots();
        ActivitySignUp activitySignUp = new ActivitySignUp(sessionExtra, sessionID, lstTutors, num_signups);
        return activitySignUp;
    }

    /**
     * Models the requested values for the webpage template by checking which sections to be shown 
     * @param model: adds the values for the View template
     * @param httpSession: has the section to be viewed on the template and the current active course
     * @return a View to the TAHomepage
     * @throws SQLException
     */
    @GetMapping("/homepage")
    public String showPage(Model model, HttpSession httpSession) throws SQLException {
        //List<Course> lstCourses = new ArrayList<Course>();
        List<Event> eventList = new ArrayList<>();
        //List<Integer> courseIDs;
        List<ActivitySignUp> lstSessionSignUps = new ArrayList<>();
        List<SessionLog> lstSessionLogs = new ArrayList<>();
        //Course course = new Course();

        if (httpSession.getAttribute("hasSelected") == null) {
            httpSession.setAttribute("hasSelected", false);
        }
        String role = SpringSessionController.getRole();
        //courseIDs = dbQueryController.getCourseIDList(SpringSessionController.getLoggedUser(), role);
        
        List<String> lstCourses = dbQueryController.getCourseNameList(SpringSessionController.getLoggedUser(), role);

        if (httpSession.getAttribute("ta_view") != null) {
            int courseID = (int) httpSession.getAttribute("openedCourse");
            String ta_view = (String) httpSession.getAttribute("ta_view");

            // Adds the activities for creating the activity sessions on the application form
            if (ta_view.equals("sessionform")) {
                List<Activity> lstActivities = dbQueryController.getActivitiesForCourse(courseID);
                model.addAttribute("activities", lstActivities);
            }
            // Gets the Activity Session Signups and Events list for the sectioninfo and calendar sections
            if (ta_view.equals("sectioninfo") || ta_view.equals("calendar")) {
                List<Integer> lstSessions = dbQueryController.getSessionsIDList(courseID);

                for (int sessionID : lstSessions) {
                    ActivitySignUp activitySignup = getSectionInfo(sessionID);
                    lstSessionSignUps.add(activitySignup);

                    List<Event> lstOccurences = StaticHelperMethods.eventOccurrences(activitySignup.getSessionExtra());

                    for (Event event : lstOccurences) {
                        eventList.add(event);
                    }
                }
            }
            // Get tutors' session logs for the requested day
            if (ta_view.equals("sessionlogs")) {
                String dayWhen = (String) httpSession.getAttribute("logsDay");
                lstSessionLogs = dbQueryController.getSessionLogs(courseID, dayWhen);
                model.addAttribute("dayOf", dayWhen);
            }
            model.addAttribute("signups", lstSessionSignUps);

        }

        model.addAttribute("activityform", new Activity());
        model.addAttribute("sessionform", new ActivitySession());
        model.addAttribute("courses", lstCourses);
        model.addAttribute("events", eventList);
        model.addAttribute("sessionlogs", lstSessionLogs);
        model.addAttribute("user_role", role);

        return "TAHomepage";
    }

    /**
     * Receives request to show or set active the clicked course
     * 
     * @param clickedCourse: the course name of the request course to be set active
     * @param httpSession:   set the view and name of the course to be active
     * @return a redirect to show the Course and the its calendar view
     */
    @PostMapping("/display/course")
    public String showCourse(@RequestParam(value = "clickedCourse") String clickedCourse, HttpSession httpSession) {
        String[] array = clickedCourse.split(",");
        try {
            int selectedCourse = dbQueryController.getCourseID(array[0], Integer.parseInt(array[1]));
            httpSession.setAttribute("openedCourse", selectedCourse);
            httpSession.setAttribute("hasSelected", true);
            httpSession.setAttribute("coursename", clickedCourse);
        } catch (NumberFormatException e) {
            System.err.println(e);
            return "redirect:/ta/homepage";
        } catch (SQLException e) {
            System.err.println(e);
            return "redirect:/ta/homepage";
        }
        return "redirect:/ta/display/calendar";
    }

    /**
     * Receives a request to show create an Activity section on the View page
     * 
     * @param httpSession: sets to show the activityform on the web template
     * @return a redirect to model for web template mapping
     */
    @GetMapping("/create/activity")
    public String showActivity(HttpSession httpSession) {
        httpSession.setAttribute("ta_view", "activityform");
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        return "redirect:/ta/homepage";
    }

    /**
     * Receives a request to show create Activity Session section on the View page
     * 
     * @param httpSession: sets to show the sessionform on the web template
     * @return a redirect to model for web template mapping
     */
    @GetMapping("/create/sessions")
    public String showSession(HttpSession httpSession) {
        httpSession.setAttribute("ta_view", "sessionform");
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        return "redirect:/ta/homepage";
    }

    /**
     * A post submission for the new activity to be added to the database for the
     * course
     * 
     * @param activity:    activity object with the attributes of the new activity
     *                     to be added
     * @param httpSession: used retrieve the courseID of the active course page
     * @return a redirect to model for web template mapping
     */
    @PostMapping("/add/activity")
    public String submitForm(@ModelAttribute("activityform") Activity activity, HttpSession httpSession) {
        int courseID = 0;
        if (httpSession.getAttribute("openedCourse") == null) {
            return "redirect:/ta/homepage";
        }
        courseID = (int) httpSession.getAttribute("openedCourse");
        try {
            dbCreateController.addActivity(activity, courseID);
            String result = "Activity was successfully added";
            httpSession.setAttribute("resultMessage", result);

        } catch (Exception e) {
            System.err.println(e);
            String result = "Error: " + e.getMessage();
            httpSession.setAttribute("resultMessage", result);
        }

        return "redirect:/ta/homepage";
    }

    /**
     * Receives a form submission for a new activity session for tutor sign-ups and
     * adds the sessions to the database and set resultMessage for adding the
     * session
     * 
     * @param selectedActivity:    the activity the session is for
     * @param activitysessionform: the activity session object with attributes of
     *                             the activity session
     * @param SignUpOpenDate:      sign-up open date for the activity session
     * @param SignUpCloseDate:     sign-up closing date for the activity session
     * @param slotArr:             an array of slots for the multiple activity
     *                             sessions on the same day and venue
     * @param httpSession:         used retrieve the courseID of the active course
     *                             page
     * @return a redirect to model for web template mapping
     */
    @PostMapping("/add/sessions")
    public String submitActivity(@RequestParam("selectedActivity") String selectedActivity,
            @ModelAttribute("activityform") ActivitySession activitysessionform,
            @RequestParam("SignUpOpenDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String SignUpOpenDate,
            @RequestParam("SignUpCloseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String SignUpCloseDate,
            @RequestParam("inputArray") String[] slotArr, HttpSession httpSession) {
        activitysessionform.setClosingDate(SignUpCloseDate);
        activitysessionform.setOpeningDate(SignUpOpenDate);
        activitysessionform.setSlotArray(slotArr);
        int courseID = (int) httpSession.getAttribute("openedCourse");
        try {
            int activityID = dbQueryController.getActivityID(selectedActivity, courseID);
            activitysessionform.setActivityID(activityID);
            String save = dbCreateController.addSessions(activitysessionform, activityID);
            System.out.println(save);
            String result = save + " Activity session(s) were successfully added";
            httpSession.setAttribute("resultMessage", result);

        } catch (Exception e) {
            System.err.println(e);
            httpSession.setAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/ta/homepage";
    }

    /**
     * Receives a request to show the Calendar section for a Course on the View page
     * 
     * @param httpSession: sets to show the sessionform on the web template
     * @return a redirect to model for web template mapping
     */
    @GetMapping("/display/calendar")
    public String showCalendar(HttpSession httpSession) {
        httpSession.setAttribute("ta_view", "calendar");
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        return "redirect:/ta/homepage";
    }

    /**
     * Receives a request to show the section for course section info on the View
     * page
     * 
     * @param httpSession: sets to show the sectioninfo section the web template
     * @return a redirect to model for web template mapping
     */
    @GetMapping("/display/signup")
    public String showSignups(HttpSession httpSession) {
        httpSession.setAttribute("ta_view", "sectioninfo");
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        return "redirect:/ta/homepage";
    }

    /**
     * Receives a request to show tutor session logs section on the View page
     * 
     * @param httpSession: sets to show the sessionlogs section the web template
     * @return a redirect to model for web template mapping
     */
    @GetMapping("/display/sessionlogs")
    public String showSessionLogs(HttpSession httpSession) {
        httpSession.setAttribute("ta_view", "sessionlogs");
        String today = StaticHelperMethods.getToday();
        httpSession.setAttribute("logsDay", today);
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        return "redirect:/ta/homepage";
    }

    /**
     * Receives a mapping to show the tutor session logs on specific date
     * 
     * @param viewDate:    the date to show the session logs of
     * @param httpSession: saves the day show the logs for
     * @return a redirect to model for web template mapping
     */
    @PostMapping("/get/sessionlogs")
    public String getLogs(@RequestParam("viewDate") String viewDate, HttpSession httpSession) {
        httpSession.setAttribute("logsDay", viewDate);
        httpSession.removeAttribute("resultMessage");httpSession.removeAttribute("errorMessage");;
        return "redirect:/ta/homepage";
    }

}
