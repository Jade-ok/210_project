package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

import persistence.Writable;

// Represents a specific time block in the schedule, including day and time range.
public class TimeBlock implements Writable {
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;

    // EFFECTS: Creates a TimeBlock with given day of the week, startTime, and
    // endTime
    public TimeBlock(String day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // EFFECTS: Checks if this TimeBlock overlaps with another TimeBlock
    public boolean isConflictsWith(TimeBlock timeBlock) {
        if (!this.day.equals(timeBlock.day)) {
            return false;
        } else if (this.startTime.isBefore(timeBlock.endTime) && this.endTime.isAfter(timeBlock.startTime)) {
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: Returns a formatted string representation of the TimeBlock.
    // e.g., Monday 09:00-10:30
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return day + " " + startTime.format(formatter) + "-" + endTime.format(formatter);
    }

    // getter
    public String getDay() {
        return this.day;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("day", day);
        json.put("startTime", startTime.toString());
        json.put("endTime", endTime.toString());
        return json;
    }
}
