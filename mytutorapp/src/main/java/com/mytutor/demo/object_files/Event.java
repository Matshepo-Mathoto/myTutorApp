package com.mytutor.demo.object_files;

/**
 * Event object for Calendar view on the Webpage to model the activity session
 * sign-ups and events for the duration of an activity session
 */
public class Event {
    private String title;
    private String start;
    private String end;
    private String location;

    /**
     * Sets the attribute of the Event object
     * @param title: the name or description of the activity or event
     * @param day: the day event occurs on
     * @param start: DateTime, when the event is starting
     * @param end: DateTime, when the event is ending
     * @param location: venue, setting or where the event would take able
     */
    public Event(String title, String day, String start, String end, String location) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    public Event() {
    }

    public Event(String title, String start, String end, String location) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Event [title=" + title + ", start=" + start + ", end=" + end + ", location=" + location + "]";
    }
}
