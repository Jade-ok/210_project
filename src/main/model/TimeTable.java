package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// Represents a timetable that manages multiple courses and their scheduled time blocks.
public class TimeTable implements Writable {
    private List<Course> courses;

    // EFFECTS: creates an empty TimeTable with no courses
    public TimeTable() {
        this.courses = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a course to the timetable if it does not conflict with existing courses.
    // Logs the addition as an event if successful.
    // Returns true if the course was added successfully, otherwise false.
    public boolean addCourse(Course newCourse) {
        for (Course c : courses) {
            for (TimeBlock existingTime : c.getAllTimeBlock()) {
                for (TimeBlock newTime : newCourse.getAllTimeBlock()) {
                    if (existingTime.isConflictsWith(newTime)) {
                        return false;
                    }
                }
            }
        }
        courses.add(newCourse);
        EventLog.getInstance().logEvent(new Event("Added course: " + newCourse.getCourseCode()));
        return true;
    }

    // REQUIRES: course is existed
    // MODIFIES: this
    // EFFECTS: remove the course from the timetable
    //          Logs the removal as an event if successful.
    public void removeCourse(Course course) {
        courses.remove(course);
        EventLog.getInstance().logEvent(new Event("Removed course: " + course.getCourseCode()));
    }

    // getter
    public List<Course> getAllCourses() {
        return courses;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray coursesArray = new JSONArray();

        for (Course c : courses) {
            coursesArray.put(c.toJson());
        }

        json.put("courses", coursesArray);
        return json;
    }

    // EFFECTS: Returns the Course object that matches the given courseCode.
    //          If no matching course is found, returns null.
    public Course getCourseByName(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null;
    }

    // EFFECTS: Prints all events recorded in the EventLog to the console
    public void printEventLog() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.toString());
            System.out.println();
        }
    }

}
