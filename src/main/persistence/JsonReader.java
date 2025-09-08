package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Course;
import model.TimeBlock;
import model.TimeTable;

// CITATION: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// This class is based on the JSON class from the demo application above.
// Represents a reader that reads TimeTable from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads TimeTable from file and returns it; (String > JSON Object > TimeTable Object)
    // throws IOException if an error occurs reading data from file
    public TimeTable read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseTimeTable(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();  
    }

    // EFFECTS: parses TimeTable from JSON object and returns it
    private TimeTable parseTimeTable(JSONObject jsonObject) {
        TimeTable timeTable = new TimeTable();
        JSONArray coursesArray = jsonObject.getJSONArray("courses");

        for (int i = 0; i < coursesArray.length(); i++) {
            JSONObject courseJson = coursesArray.getJSONObject(i);
            Course course = parseCourse(courseJson);
            timeTable.addCourse(course);
        }
        return timeTable;
    }

    // EFFECTS: parses a Course from JSON object and returns it
    private Course parseCourse(JSONObject jsonObject) {
        Course course = new Course(jsonObject.getString("courseCode"));

        JSONArray lecturesArray = jsonObject.getJSONArray("lectureTimes");
        for (int i = 0; i < lecturesArray.length(); i++) {
            JSONObject timeBlockJson = lecturesArray.getJSONObject(i);
            course.addLectureTime(parseTimeBlock(timeBlockJson));
        }

        if (jsonObject.has("labTime")) {
            course.setLabTime(parseTimeBlock(jsonObject.getJSONObject("labTime")));
        }
        if (jsonObject.has("discussionTime")) {
            course.setDiscussionTime(parseTimeBlock(jsonObject.getJSONObject("discussionTime")));
        }

        return course;
    }

    // EFFECTS: parses a TimeBlock from JSON object and returns it
    private TimeBlock parseTimeBlock(JSONObject jsonObject) {
        String day = jsonObject.getString("day");
        LocalTime startTime = LocalTime.parse(jsonObject.getString("startTime"));
        LocalTime endTime = LocalTime.parse(jsonObject.getString("endTime"));
        return new TimeBlock(day, startTime, endTime);
    }
}