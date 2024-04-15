package com.mytutor.demo.HelperClasses;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mytutor.demo.object_files.Event;
import com.mytutor.demo.object_files.SessionExtra;

/**
 * Provides helper methods to be used in controllers
 */
public class StaticHelperMethods {
    /**
     * Converts date in string to LocalDate
     * @param dateString: date as a string in format "yyyy-MM-dd"
     * @return LocalDate object
     */
    public static LocalDate stringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        return localDate;
    }

    /**
     * Converts date form LocalDate object to a string date
     * @param localDate object of LocalDate
     * @return a date in string in the format yyyy-MM-dd
     */
    public static String dateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = localDate.format(formatter);

        return dateString;
    }

    /**
     * Gets today's date as a string date
     * @return today's date as string
     */
    public static String getToday(){
        LocalDate localDate = LocalDate.now(); 
        String today = dateToString(localDate);
        return today;

    }

    /**
     * Overloaded method to create event objects for an Activity session
     * @param sessionExtra: subclass of activity session object with its extra details
     * @return List of event objects for the event occurences to display on the calendar
     */
    public static List<Event> eventOccurrences(SessionExtra sessionExtra) {
        return eventOccurrences(sessionExtra.getRecurringFrom(), sessionExtra.getRecurringTo(),
                sessionExtra.getStartTime(), sessionExtra.getEndTime(), sessionExtra.getDay(), sessionExtra.getVenue(),
                sessionExtra.getActivity().getActivityName());
    }

    /**
     * Gets a list of Event objects for the activity session of a specific activity and course to allow
     * them to be displayed on a calendar
     * @param startDate: start date or period for the activity session to run from
     * @param endDate: end date or period for the activity session to run until
     * @param startTime: start time of the activity session on a day
     * @param endTime: end time of the activity session on a day
     * @param day: day on which the activity session repeats on 
     * @param location: where the session is to take place
     * @param activityName: the name of the activity for the session and signups under it
     * @return List of event objects for the event occurences to display on the calendar
     */
    public static List<Event> eventOccurrences(String startDate, String endDate, String startTime, String endTime,
            String day, String location, String activityName) {
        day = day.toUpperCase();
        DayOfWeek targetDay = DayOfWeek.MONDAY;
        switch (day) {
            case "MONDAY":
                targetDay = DayOfWeek.MONDAY;
                break;
            case "TUESDAY":
                targetDay = DayOfWeek.TUESDAY;
                break;
            case "WEDNESDAY":
                targetDay = DayOfWeek.WEDNESDAY;
                break;
            case "THURSDAY":
                targetDay = DayOfWeek.THURSDAY;
                break;
            case "FRIDAY":
                targetDay = DayOfWeek.FRIDAY;
                break;
            case "SATURDAY":
                targetDay = DayOfWeek.SATURDAY;
                break;
            case "SUNDAY":
                targetDay = DayOfWeek.SUNDAY;
                break;
            default:
                System.out.println("Error");
        }
        LocalDate dStartDate = StaticHelperMethods.stringToDate(startDate);
        LocalDate dEndDate = StaticHelperMethods.stringToDate(endDate);

        LocalDate targetDayOfThisDate = dStartDate;

        // Get the valid Date of the targetDay i.e., if targetDay is Monday, next monday
        // if dStartDate is not on Monday
        while (targetDayOfThisDate.getDayOfWeek() != targetDay) {
            targetDayOfThisDate = targetDayOfThisDate.plusDays(1);
        }
        LocalDate nextDay = targetDayOfThisDate;

        List<Event> lstOccurences = new ArrayList<>();
        // Check if nextDay is between session start date & session end date, and add
        // the event to the list
        while ((nextDay.compareTo(dStartDate) >= 0) && (nextDay.compareTo(dEndDate) <= 0)) {
            Event event = new Event();
            event.setStart(StaticHelperMethods.dateToString(nextDay) + " " + startTime);
            event.setEnd(StaticHelperMethods.dateToString(nextDay) + " " + endTime);
            event.setLocation(location);
            event.setTitle(activityName);
            nextDay = nextDay.plusWeeks(1);
            lstOccurences.add(event);
        }
        return lstOccurences;

    }
}
