package com.mytutor.demo.ui_controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mytutor.demo.LocationService;
import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;
import com.mytutor.demo.EmailFiles.EmailController;
import com.mytutor.demo.HelperClasses.StaticHelperMethods;
import com.mytutor.demo.HelperClasses.SpringSessionController;
import com.mytutor.demo.object_files.Activity;
import com.mytutor.demo.object_files.ActivitySession;
//import com.mytutor.demo.object_files.Course;
import com.mytutor.demo.object_files.Event;
import com.mytutor.demo.object_files.SessionExtra;

import jakarta.servlet.http.HttpSession;

/**
 * Controls all the activities of a tutor on their webpage and their course
 * actions as well
 */
@Controller
@RequestMapping("/tutor")
public class TutorUIController {
    @Autowired
    private DatabaseQueryController dbQueryController;

    @Autowired
    private DatabaseCreateController dbCreateController;

    @Autowired
    private DatabaseUpdateController dbUpdateController;

    @Autowired
    private LocationService locationService;

    private final HttpSession httpSession;

    /**
     * Takes in the http session for the request to this specific user to this
     * controller with set attributes
     * 
     * @param httpSession: user specific session
     */
    public TutorUIController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Gets the SessionExtra object of an activity session and checks which session
     * and activity the tutor has signed up for
     * 
     * @param sessionID: activity session ID for the tutor signups
     * @return session extra object with set attribute for the course activity
     *         session
     * @throws SQLException
     */
    private SessionExtra getSessionExtra(int sessionID) throws SQLException {
        ActivitySession activitySession = dbQueryController.getActivitySession(sessionID);
        SessionExtra sessionExtra = new SessionExtra(activitySession);
        Activity activity = dbQueryController.getActivity(sessionExtra.getActivityID());
        sessionExtra.setActivity(activity);

        sessionExtra.setActivitySessionID(sessionID);
        int numAvailable = dbQueryController.getNumSignups(sessionID);
        numAvailable = activitySession.getNumberOfTutors() - numAvailable;
        sessionExtra.setAvailableSlots(numAvailable);

        boolean isSigned = dbQueryController.checkSignup(activitySession.getActivityID(),
                SpringSessionController.getLoggedUser());
        sessionExtra.setSigned(isSigned);

        if (isSigned) {
            httpSession.setAttribute("signedSession", sessionID);
        }

        boolean isFull = numAvailable == 0;
        sessionExtra.setFull(isFull);

        boolean isOpen = dbQueryController.isSessionOpen(sessionID);
        sessionExtra.setOpen(isOpen);
        return sessionExtra;
    }

    /**
     * Gets the list of Event objects to be shown on the calendar for the tutor
     * sign-ups to course activity sessions
     * 
     * @param lstSessionIDs: the sessionIDs which the tutor has signed up for
     * @return list of Event object for calendar use
     * @throws SQLException
     */
    private List<Event> getEventsList(List<Integer> lstSessionIDs) throws SQLException {
        List<Event> eventList = new ArrayList<>();
        for (int sessionID : lstSessionIDs) {
            ActivitySession activitySession = dbQueryController.getActivitySession(sessionID);
            SessionExtra sessionExtra = new SessionExtra(activitySession);
            Activity activity = dbQueryController.getActivity(sessionExtra.getActivityID());
            sessionExtra.setActivity(activity);

            List<Event> lstOccurences = StaticHelperMethods.eventOccurrences(sessionExtra);

            for (Event event : lstOccurences) {
                eventList.add(event);
            }
        }
        return eventList;
    }

    /**
     * Models the views or passing of the requested information by tutor to be shown
     * on the web template
     * 
     * @param model: models and passes the attributes for displaying the information
     *               on the web template
     * @return a View to TutorHomepage
     * @throws SQLException
     */
    @GetMapping("/homepage")
    public String showPage(Model model) throws SQLException {
        // List<Course> courses = new ArrayList<Course>();
        List<String> courses = new ArrayList<>();
        List<Event> eventList = new ArrayList<>();
        List<Integer> lstSessionIDs = new ArrayList<>();
        // List<Integer> courseIDs;
        String username = SpringSessionController.getLoggedUser();

        int openedCourseID = 0;

        if (httpSession.getAttribute("hasSelected") == null) {
            httpSession.setAttribute("hasSelected", false);
        }
        if (httpSession.getAttribute("openedCourse") != null) {
            openedCourseID = (int) httpSession.getAttribute("openedCourse");
        }

        courses = dbQueryController.getCourseNameList(username, SpringSessionController.getRole());
        if (httpSession.getAttribute("tutor_view") != null) {
            String tutor_view = (String) httpSession.getAttribute("tutor_view");

            // For sessionslist or sectioninfo view populate ac
            if ((tutor_view.equals("sessionslist") || tutor_view.equals("sectioninfo"))) {
                Map<Integer, ActivitySession> mapSessions = new HashMap<>();
                int courseID = (int) httpSession.getAttribute("openedCourse");

                // Get the list of sessions for a course not specific to a tutor
                List<Integer> sessionIDs = dbQueryController.getSessionsIDList(courseID);
                for (int sessionID : sessionIDs) {
                    SessionExtra sessionExtra = getSessionExtra(sessionID);

                    mapSessions.put(sessionID, sessionExtra);
                }
                model.addAttribute("mapSessions", mapSessions);
            }

            // If a session was selected from the sign-ups list, add the session to the
            // model to show it details before tutor signs up for it
            if (tutor_view.equals("sessioninfo")) {
                int sessionID = (int) httpSession.getAttribute("sessionID");
                SessionExtra sessionExtra = getSessionExtra(sessionID);

                model.addAttribute("activitysession", sessionExtra);
            }

            // Get the list sessionIDs the tutor signed up for if sessionlog view was
            // requested or calendar and get the calendar events for it
            if (tutor_view.equals("calendar") || tutor_view.equals("sessionlog")) {
                if (openedCourseID == 0) {
                    lstSessionIDs = dbQueryController.getTutorSessions(username);
                } else {
                    lstSessionIDs = dbQueryController.getTutorSessions(username, openedCourseID);
                }

                // Get the Events for the calendar view if calendar view was requested
                if (tutor_view.equals("calendar")) {
                    eventList = getEventsList(lstSessionIDs);
                }
            }
        }

        model.addAttribute("courses", courses);
        model.addAttribute("events", eventList);
        model.addAttribute("sessionIDs", lstSessionIDs);

        return "TutorHomepage";
    }

    /**
     * Clears the tutor actions from course if the Home icon was clicked on the
     * Webpage
     * 
     * @param httpSession: used to clear tutor's course actions and return to
     *                     homepage
     * @return a redirect to model the webpage
     */
    @GetMapping("/home")
    public String showHome(HttpSession httpSession) {
        httpSession.setAttribute("tutor_view", "calendar");
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        
        httpSession.removeAttribute("openedCourse");
        httpSession.setAttribute("hasSelected", false);
        httpSession.removeAttribute("coursename");
        return "redirect:/tutor/homepage";
    }

    /**
     * Sets the view to display calendar section and the events of the tutor and/or
     * course
     * 
     * @param httpSession: sets which view to be displayed on the calendar
     * @return a redirect to model the webpage
     */
    @GetMapping("/display/calendar")
    public String showCalendar(HttpSession httpSession) {
        httpSession.setAttribute("tutor_view", "calendar");
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        return "redirect:/tutor/homepage";
    }

    /**
     * Receives a request to show the section info of the course and sign-ups
     * 
     * @param httpSession: sets to show the section of sectioninfo on the web
     *                     template
     * @return a redirect to model the webpage
     */
    @GetMapping("/display/sectioninfo")
    public String showSignups(HttpSession httpSession) {
        httpSession.setAttribute("tutor_view", "sectioninfo");
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        
        return "redirect:/tutor/homepage";
    }

    /**
     * Receives a request to show the sign-up sessions list and set the view section
     * to it
     * 
     * @param httpSession: sets the section to show the sessions sign up list on the
     *                     Webpage View
     * @return a redirect to model the webpage
     */
    @GetMapping("/show/signups")
    public String showSessions(HttpSession httpSession) {
        httpSession.setAttribute("tutor_view", "sessionslist");
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        return "redirect:/tutor/homepage";
    }

    /**
     * Gets a request to show the session log
     * 
     * @param httpSession: sets the section to be view as sessionlog on the webpage
     * @return a redirect to model the webpage
     */
    @GetMapping("/display/sessionlog")
    public String showSessionLog(HttpSession httpSession) {
        httpSession.setAttribute("tutor_view", "sessionlog");
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        return "redirect:/tutor/homepage";
    }

    /**
     * Receives a tutor session log to update for the participating to the session
     * 
     * @param sessionID:       the ID of the session the tutor signed up for and is
     *                         providing the log for
     * @param isEnd:           boolean value to specify whether the log is for start
     *                         or end of the session
     * @param sessionFeedback: if end of session, the feedback from the session
     * @param httpSession:     sets the result message for adding the log to show to
     *                         the webpage
     * @return a redirect to model the webpage
     */
    @PostMapping("/add/sessionlog")
    public String addSessionlog(@RequestParam("sessionID") int sessionID,
            @RequestParam(name = "isEnd", required = false) String isEnd,
            @RequestParam("feedback") String sessionFeedback, HttpSession httpSession) {
        String result = "";
        try {
            String tutorUsername = SpringSessionController.getLoggedUser();
            String coordinates = locationService.getLoc();

            if (isEnd != null) {
                dbUpdateController.updateSessionlog(tutorUsername, coordinates, sessionID, sessionFeedback);
                result = "End of session Captured.";
            } else {
                dbCreateController.addSessionlog(tutorUsername, coordinates, sessionID);
                result = "Start of session Captured.";
            }
            httpSession.setAttribute("resultMessage", result);
        } catch (Exception e) {
            System.err.println(e);
            result = "Error occured could not capture session log";
            httpSession.setAttribute("errorMessage", result);
        }

        return "redirect:/tutor/homepage";
    }

    /**
     * Gets a request to display the information of the selected activity session
     * the tutor wants to sign up for
     * 
     * @param sessionID:   the selected activity session
     * @param httpSession: sets the view to sessioninfo for signing to the session
     *                     and display data
     * @return a redirect to model the webpage
     */
    @GetMapping("/display/sessionInfo/{sessionID}")
    public String addGetSignup(@PathVariable("sessionID") int sessionID, HttpSession httpSession) {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        
        try {
            httpSession.setAttribute("sessionID", sessionID);
            httpSession.setAttribute("tutor_view", "sessioninfo");
        } catch (Exception e) {
            System.err.println(e);
            String errorMessage = "An error occured while trying to add sign-up";
            httpSession.setAttribute("errorMessage", errorMessage);
        }
        return "redirect:/tutor/homepage";
    }

    /**
     * Adds the tutor sign-up to an activity session and sends confirmation of via
     * the tutor's email address
     * 
     * @param httpSession: sets results for signing up and the section to view next
     *                     on the View webpage
     * @return a redirect to model the webpage
     */
    @GetMapping("/add/signup")
    public String addSignup(HttpSession httpSession) {
        httpSession.removeAttribute("resultMessage");
        httpSession.removeAttribute("errorMessage");
        ;
        int sessionID = (int) httpSession.getAttribute("sessionID");
        String tutorUsername = SpringSessionController.getLoggedUser();
        String resultMessage = "";

        try {
            dbCreateController.addSessionSignup(sessionID, tutorUsername);
            resultMessage = "You have been successfully signed up to session with ID: " + sessionID;
            httpSession.setAttribute("resultMessage", resultMessage);

            String toEMail = dbQueryController.getEmailAddress(tutorUsername);
            String courseName = (String) httpSession.getAttribute("coursename");
            EmailController.sendSignUpEmail(toEMail, sessionID, courseName);
            httpSession.setAttribute("resultMessage", resultMessage);
        } catch (Exception e) {
            System.err.println(e);
            resultMessage = "Error: " + e.getMessage();// "An error occured while trying to add sign-up";
            httpSession.setAttribute("errorMessage", resultMessage);
        }

        httpSession.removeAttribute("sessionID");
        httpSession.setAttribute("tutor_view", "sessionslist");
        return "redirect:/tutor/homepage";
    }

    /**
     * Receives request to show or set active the clicked course
     * 
     * @param clickedCourse: the course name of the request course to be set active
     * @param httpSession:   set the view and name of the course to be active
     * @return a redirect to show the Course and the its calendar view
     */
    @PostMapping("/display/course")
    public String showCourse(@RequestParam(value = "clickedButton") String clickedCourse, HttpSession httpSession) {
        String[] array = clickedCourse.split(",");
        try {
            int selectedCourse = dbQueryController.getCourseID(array[0], Integer.parseInt(array[1]));
            httpSession.setAttribute("openedCourse", selectedCourse);
            httpSession.setAttribute("hasSelected", true);
            httpSession.setAttribute("coursename", clickedCourse);
        } catch (NumberFormatException e) {
            System.err.println(e);
            return "redirect:/tutor/homepage";
        } catch (SQLException e) {
            System.err.println(e);
            return "redirect:/tutor/homepage";
        }
        return "redirect:/tutor/display/calendar";
    }

}
