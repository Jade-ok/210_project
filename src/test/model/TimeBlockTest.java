package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimeBlockTest {
    private TimeBlock testTimeBlock1;
    private TimeBlock testTimeBlock2;
    private TimeBlock testTimeBlock3;
    private TimeBlock testTimeBlock4;
    private TimeBlock testTimeBlock5;
    private TimeBlock testTimeBlock6;
    private TimeBlock testTimeBlock7;
    private TimeBlock testTimeBlock8;
    private TimeBlock testTimeBlock9;
    private TimeBlock testTimeBlock10;
    private TimeBlock testTimeBlock11;



    @BeforeEach
    void runBefore() {
        testTimeBlock1 = new TimeBlock("Monday", LocalTime.of(9, 0), LocalTime.of(10, 30));
        testTimeBlock2 = new TimeBlock("Monday", LocalTime.of(9, 1), LocalTime.of(10, 31));
        testTimeBlock3 = new TimeBlock("Monday", LocalTime.of(10, 30), LocalTime.of(12, 0));
        testTimeBlock4 = new TimeBlock("Tuesday", LocalTime.of(9, 0), LocalTime.of(10, 30));
        testTimeBlock5 = new TimeBlock("Monday", LocalTime.of(7, 0), LocalTime.of(9, 30));
        testTimeBlock6 = new TimeBlock("Monday", LocalTime.of(8, 0), LocalTime.of(11, 30));
        testTimeBlock7 = new TimeBlock("Monday", LocalTime.of(9, 10), LocalTime.of(10, 20));
        testTimeBlock8 = new TimeBlock("Monday", LocalTime.of(9, 0), LocalTime.of(10, 40));
        testTimeBlock9 = new TimeBlock("Monday", LocalTime.of(10, 0), LocalTime.of(10, 30));
        testTimeBlock10 = new TimeBlock("Monday", LocalTime.of(9, 0), LocalTime.of(10, 30));
        testTimeBlock11 = new TimeBlock("Monday", LocalTime.of(7, 30), LocalTime.of(9, 0));

    }

    @Test
    void testTimeBlock() {
        assertEquals("Monday", testTimeBlock1.getDay());
        assertEquals(LocalTime.of(9, 0), testTimeBlock1.getStartTime());
        assertEquals(LocalTime.of(10, 30), testTimeBlock1.getEndTime());
    }

    @Test
    void testConflictsWith() {
        // conflict
        assertTrue(testTimeBlock1.isConflictsWith(testTimeBlock2)); //start later, end later
        assertTrue(testTimeBlock1.isConflictsWith(testTimeBlock5)); //start early, end early
        assertTrue(testTimeBlock1.isConflictsWith(testTimeBlock6)); //start early, end later
        assertTrue(testTimeBlock1.isConflictsWith(testTimeBlock7)); //start later, end early
        assertTrue(testTimeBlock1.isConflictsWith(testTimeBlock8)); //same start
        assertTrue(testTimeBlock1.isConflictsWith(testTimeBlock9)); //same end
        assertTrue(testTimeBlock1.isConflictsWith(testTimeBlock10)); //same day, same time

        // not conflict
        assertFalse(testTimeBlock1.isConflictsWith(testTimeBlock3)); //same day, consecutive time1
        assertFalse(testTimeBlock1.isConflictsWith(testTimeBlock4)); //differday day, same time
        assertFalse(testTimeBlock1.isConflictsWith(testTimeBlock11)); //same day, consecutive time2

    }

    @Test
    void testToStringFormat() {
        assertEquals("Monday 09:00-10:30", testTimeBlock1.toString());
        assertEquals("Tuesday 09:00-10:30", testTimeBlock4.toString());
    }
}
