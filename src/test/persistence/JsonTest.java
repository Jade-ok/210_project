package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import model.Course;
import model.TimeBlock;

          
public class JsonTest {
     // Helper method to check Course properties
    protected void checkCourse(String courseCode, Course course) {
        assertEquals(courseCode, course.getCourseCode());
    }

    // Helper method to check TimeBlock properties
    protected void checkTimeBlock(String day, String startTime, String endTime, TimeBlock timeBlock) {
        assertEquals(day, timeBlock.getDay());
        assertEquals(startTime, timeBlock.getStartTime().toString());
        assertEquals(endTime, timeBlock.getEndTime().toString()); 
    }

}
