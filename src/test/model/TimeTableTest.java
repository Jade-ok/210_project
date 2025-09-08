package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimeTableTest {
    private TimeTable testTimeTable;
    private Course testCourse1;
    private Course testCourse2;
    private TimeBlock testLectureBlock1;
    private TimeBlock testLectureBlock2;
    private TimeBlock testLabBlock1;
    private TimeBlock testLabBlock2;
    private TimeBlock testDiscussionBlock1;
    private TimeBlock testDiscussionBlock2;

    @BeforeEach
    void runBefore() {
        testTimeTable = new TimeTable();

        testCourse1 = new Course("CPSC210");
        testCourse2 = new Course("MATH200");

        // Lecture (no Conflict)
        testLectureBlock1 = new TimeBlock("Monday", LocalTime.of(9, 0), LocalTime.of(10, 30));
        testLectureBlock2 = new TimeBlock("Friday", LocalTime.of(15, 0), LocalTime.of(16, 30));

        testCourse1.addLectureTime(testLectureBlock1);
        testCourse2.addLectureTime(testLectureBlock2);
    }

    @Test
    void testTimeTable() {
        assertTrue(testTimeTable.getAllCourses().isEmpty());
    }

    @Test
    void testRemoveCourse() {
        testTimeTable.addCourse(testCourse1);
        assertEquals(1, testTimeTable.getAllCourses().size());

        testTimeTable.addCourse(testCourse2);
        assertEquals(2, testTimeTable.getAllCourses().size());

        testTimeTable.removeCourse(testCourse1);

        assertEquals(1, testTimeTable.getAllCourses().size());
        assertFalse(testTimeTable.getAllCourses().contains(testCourse1));
        assertTrue(testTimeTable.getAllCourses().contains(testCourse2));
    }

    @Test
    void testAddCourseNoConflict() {
        assertTrue(testTimeTable.addCourse(testCourse1));
        assertTrue(testTimeTable.addCourse(testCourse2));

        assertEquals(2, testTimeTable.getAllCourses().size());
        assertTrue(testTimeTable.getAllCourses().contains(testCourse1));
        assertEquals(testCourse1, testTimeTable.getAllCourses().get(0));
        assertTrue(testTimeTable.getAllCourses().contains(testCourse2));
        assertEquals(testCourse2, testTimeTable.getAllCourses().get(1));
    }

    @Test
    void testAddCourseLabConflict() {
        testLabBlock1 = new TimeBlock("Wednesday", LocalTime.of(14, 0), LocalTime.of(16, 0));
        testLabBlock2 = new TimeBlock("Wednesday", LocalTime.of(15, 59), LocalTime.of(17, 0));

        testCourse1.setLabTime(testLabBlock1);
        testCourse2.setLabTime(testLabBlock2);

        assertTrue(testTimeTable.addCourse(testCourse1));
        assertFalse(testTimeTable.addCourse(testCourse2));
        assertEquals(1, testTimeTable.getAllCourses().size());
        assertFalse(testTimeTable.getAllCourses().contains(testCourse2));
    }

    @Test
    void testAddCourseDiscussionConflict() {
        testDiscussionBlock1 = new TimeBlock("Thursday", LocalTime.of(13, 0), LocalTime.of(14, 0));
        testDiscussionBlock2 = new TimeBlock("Thursday", LocalTime.of(11, 0), LocalTime.of(13, 01));

        testCourse1.setDiscussionTime(testDiscussionBlock1);
        testCourse2.setDiscussionTime(testDiscussionBlock2);

        assertTrue(testTimeTable.addCourse(testCourse1));
        assertFalse(testTimeTable.addCourse(testCourse2));

        assertEquals(1, testTimeTable.getAllCourses().size());
        assertFalse(testTimeTable.getAllCourses().contains(testCourse2));
    }

    @Test
    void testGetCourseByName() {
        assertEquals(null, testTimeTable.getCourseByName("CPSC210"));
        assertEquals(null, testTimeTable.getCourseByName("MATH200"));

        testTimeTable.addCourse(testCourse1);
        testTimeTable.addCourse(testCourse2);

        assertEquals(testCourse1, testTimeTable.getCourseByName("CPSC210"));
        assertEquals(testCourse2, testTimeTable.getCourseByName("MATH200"));

        assertEquals(testCourse1, testTimeTable.getCourseByName("cpsc210"));
        assertEquals(testCourse2, testTimeTable.getCourseByName("math200"));

        assertEquals(null, testTimeTable.getCourseByName("PHYS101"));
    }

}
