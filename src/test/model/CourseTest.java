package model;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourseTest {
    private Course testCourse;
    private TimeBlock testLectureBlock1;
    private TimeBlock testLectureBlock2;
    private TimeBlock testLabBlock;
    private TimeBlock testDiscussionBlock;

    @BeforeEach
    void runBefore() {
        testCourse = new Course("CPSC210");

        testLectureBlock1 = new TimeBlock("Monday", LocalTime.of(9, 0), LocalTime.of(10, 30));
        testLectureBlock2 = new TimeBlock("Wednesday", LocalTime.of(11, 0), LocalTime.of(12, 30));
        testLabBlock = new TimeBlock("Friday", LocalTime.of(14, 0), LocalTime.of(16, 0));
        testDiscussionBlock = new TimeBlock("Tuesday", LocalTime.of(13, 0), LocalTime.of(14, 0));

    }

    @Test
    void testConstructor() {
        assertEquals("CPSC210", testCourse.getCourseCode());
        assertTrue(testCourse.getLectureTimes().isEmpty());
        assertNull(testCourse.getLabTime());
        assertNull(testCourse.getDiscussionTime());
        assertEquals("", testCourse.getInstructor());
        assertEquals(0, testCourse.getCredits());
        assertEquals("", testCourse.getLocation());
    }

    @Test
    void testAddLectureTime() {
        testCourse.addLectureTime(testLectureBlock1);
        assertTrue(testCourse.getLectureTimes().contains(testLectureBlock1));
        assertEquals(testLectureBlock1, testCourse.getLectureTimes().get(0));

        testCourse.addLectureTime(testLectureBlock2);
        assertEquals(2, testCourse.getLectureTimes().size());
        assertTrue(testCourse.getLectureTimes().contains(testLectureBlock2));
        assertEquals(testLectureBlock2, testCourse.getLectureTimes().get(1));
    }

    @Test
    void testRemoveLectureTime() {
        testCourse.addLectureTime(testLectureBlock1);
        testCourse.addLectureTime(testLectureBlock2);
        testCourse.removeLectureTime(testLectureBlock1);
        assertFalse(testCourse.getLectureTimes().contains(testLectureBlock1));
        assertEquals(1, testCourse.getLectureTimes().size());
        assertEquals(testLectureBlock2, testCourse.getLectureTimes().get(0));
    }

    @Test
    void testSetLabTime() {
        testCourse.setLabTime(testLabBlock);
        assertEquals(testLabBlock, testCourse.getLabTime());
    }

    @Test
    void testRemoveLab() {
        testCourse.setLabTime(testLabBlock);
        testCourse.removeLab();
        assertNull(testCourse.getLabTime());
    }

    @Test
    void testSetDiscussionTime() {
        testCourse.setDiscussionTime(testDiscussionBlock);
        assertEquals(testDiscussionBlock, testCourse.getDiscussionTime());
    }

    @Test
    void testRemoveDiscussion() {
        testCourse.setDiscussionTime(testDiscussionBlock);
        testCourse.removeDiscussion();
        assertNull(testCourse.getDiscussionTime());
    }

    @Test
    void testRemoveCourse() {
        testCourse.addLectureTime(testLectureBlock1);
        testCourse.setLabTime(testLabBlock);
        testCourse.setDiscussionTime(testDiscussionBlock);
        testCourse.removeCourse();

        assertTrue(testCourse.getLectureTimes().isEmpty());
        assertNull(testCourse.getLabTime());
        assertNull(testCourse.getDiscussionTime());
    }

    @Test
    void testSetters() {
        testCourse.setInstructor("Steve Wolfman");
        testCourse.setCredits(4);
        testCourse.setLocation("SCRF 100");

        assertEquals("Steve Wolfman", testCourse.getInstructor());
        assertEquals(4, testCourse.getCredits());
        assertEquals("SCRF 100", testCourse.getLocation());

        testCourse.setInstructor("Norm Hutchinson");
        assertEquals("Norm Hutchinson", testCourse.getInstructor());
    }

    @Test
    void testCourseToJsonWithLabAndDiscussion() {
        testCourse.addLectureTime(testLectureBlock1);
        testCourse.setLabTime(testLabBlock); 
        testCourse.setDiscussionTime(testDiscussionBlock);

        JSONObject json = testCourse.toJson();

        assertEquals("CPSC210", json.getString("courseCode"));
        assertTrue(json.has("lectureTimes"));
        assertTrue(json.has("labTime")); 
        assertTrue(json.has("discussionTime")); 
    }
}
