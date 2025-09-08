package persistence;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Course;
import model.TimeBlock;
import model.TimeTable;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            TimeTable timeTable = reader.read();
            assertNull(timeTable);
            fail("IOException expected");
        } catch (IOException e) {
        }
    }

    @Test
    void testReaderEmptyTimeTable() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyTimeTable.json");
        try {
            TimeTable timeTable = reader.read();
            assertEquals(0, timeTable.getAllCourses().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralTimeTable() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralTimeTable.json");
        try {
            TimeTable timeTable = reader.read();
            List<Course> courses = timeTable.getAllCourses();
            assertEquals(2, courses.size());

            checkCourse("CPSC110", courses.get(0));
            checkCourse("MATH200", courses.get(1));

            List<TimeBlock> lectureTimes = courses.get(0).getLectureTimes();
            assertEquals(2, lectureTimes.size());
            checkTimeBlock("MONDAY", "10:00", "11:30", lectureTimes.get(0));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderTimeTableWithLabAndDiscussion() {
        JsonReader reader = new JsonReader("./data/testReaderWithLabAndDiscussion.json");
        try {
            TimeTable timeTable = reader.read();
            List<Course> courses = timeTable.getAllCourses();
            assertEquals(1, courses.size());

            Course course = courses.get(0);
            assertEquals("CPSC210", course.getCourseCode());

            // check Lecture, Lab, Discussion
            assertEquals(1, course.getLectureTimes().size());
            checkTimeBlock("MONDAY", "09:00", "10:30", course.getLectureTimes().get(0));

            assertNotNull(course.getLabTime());
            checkTimeBlock("TUESDAY", "11:00", "12:00", course.getLabTime());

            assertNotNull(course.getDiscussionTime());
            checkTimeBlock("WEDNESDAY", "13:00", "14:00", course.getDiscussionTime());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
