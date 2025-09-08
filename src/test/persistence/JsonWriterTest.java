package persistence;

import model.Course;
import model.TimeTable;
import model.TimeBlock;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./not/exist/invalidFile.json");
            writer.open();
            fail("IOException expected");
        } catch (IOException e) {
        }
    }

    @Test
    void testWriterEmptyTimeTable() {
        try {
            TimeTable timeTable = new TimeTable();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyTimeTable.json");
            writer.open();
            writer.write(timeTable);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyTimeTable.json");
            timeTable = reader.read();
            assertEquals(0, timeTable.getAllCourses().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralTimeTable() {
        try {
            TimeTable timeTable = new TimeTable();
            Course course1 = new Course("CPSC110");
            course1.addLectureTime(
                    new TimeBlock("MONDAY", java.time.LocalTime.of(10, 0), java.time.LocalTime.of(11, 30)));
            timeTable.addCourse(course1);

            Course course2 = new Course("MATH200");
            course2.addLectureTime(
                    new TimeBlock("TUESDAY", java.time.LocalTime.of(14, 0), java.time.LocalTime.of(15, 30)));
            timeTable.addCourse(course2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralTimeTable.json");
            writer.open();
            writer.write(timeTable);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralTimeTable.json");
            TimeTable loadedTimeTable = reader.read();

            List<Course> courses = loadedTimeTable.getAllCourses();
            assertEquals(2, courses.size());

            checkCourse("CPSC110", courses.get(0));
            checkCourse("MATH200", courses.get(1));

            List<TimeBlock> lectureTimes = courses.get(0).getLectureTimes();
            assertEquals(1, lectureTimes.size());
            checkTimeBlock("MONDAY", "10:00", "11:30", lectureTimes.get(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
