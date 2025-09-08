package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

//Represents an course with a course code and multiple course components(Lecture/Lab/Discussion)
//each associated with a specific time block. 
public class Course implements Writable {
    private String courseCode;
    private String instructor;
    private int credits;
    private String location;

    private List<TimeBlock> lectureTimes;
    private TimeBlock labTime;
    private TimeBlock discussionTime;
    // private List<TimeBlock> allTimes;

    // EFFECTS: Creates a Course with the given courseCode.
    // Initializes an empty list for lecture times.
    // Lab and Discussion times are set to empty
    // Instructor, credits, and location are set to default values (empty or 0).
    public Course(String courseCode) {
        this.courseCode = courseCode;
        this.lectureTimes = new ArrayList<>();
        this.labTime = null;
        this.discussionTime = null;
        this.instructor = "";
        this.credits = 0;
        this.location = "";
    }

    // MODIFIES: this
    // EFFECTS: add a new lecture timeblock to the list of 'lectureTimes'
    // (since a course can have more than one lectures)
    public void addLectureTime(TimeBlock timeBlock) {
        lectureTimes.add(timeBlock);
    }

    public void setLabTime(TimeBlock timeBlock) {
        this.labTime = timeBlock;
    }

    public void setDiscussionTime(TimeBlock timeBlock) {
        this.discussionTime = timeBlock;
    }

    // REQUIRES: timeBlock must not be null.
    // MODIFIES: this
    // EFFECTS: remove a specific time block from 'lectureTimes'
    public void removeLectureTime(TimeBlock timeBlock) {
        lectureTimes.remove(timeBlock);
    }

    // MODIFIES: this
    // EFFECTS: remove a specific time of LAB
    public void removeLab() {
        this.labTime = null;
    }

    // MODIFIES: this
    // EFFECTS: remove a specific time of DISCUSSION
    public void removeDiscussion() {
        this.discussionTime = null;
    }

    // MODIFIES: this
    // EFFECTS: remove whole course
    public void removeCourse() {
        lectureTimes.clear();
        labTime = null;
        discussionTime = null;
    }

    // setter
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // getter
    public String getCourseCode() {
        return courseCode;
    }

    public List<TimeBlock> getLectureTimes() {
        return lectureTimes;
    }

    public TimeBlock getLabTime() {
        return labTime;
    }

    public TimeBlock getDiscussionTime() {
        return discussionTime;
    }

    public List<TimeBlock> getAllTimeBlock() {
        List<TimeBlock> copyAllTimes = new ArrayList<>(lectureTimes);

        if (getLabTime() != null) {
            copyAllTimes.add(getLabTime());
            // allTimes.add(getLabTime());
        }
        if (getDiscussionTime() != null) {
            copyAllTimes.add(getDiscussionTime());
            // allTimes.add(getDiscussionTime());
        }

        return copyAllTimes;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getCredits() {
        return credits;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray lecturesArray = new JSONArray();

        for (TimeBlock t : lectureTimes) {
            lecturesArray.put(t.toJson());
        }

        json.put("courseCode", courseCode);
        json.put("lectureTimes", lecturesArray);
        if (labTime != null) {
            json.put("labTime", labTime.toJson());
        }
        if (discussionTime != null) {
            json.put("discussionTime", discussionTime.toJson());
        }
        return json;
    }
}
