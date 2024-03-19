package com.skipp.enlistment.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {

    @Test
    public void testValueOf() {
        assertEquals(new Schedule(Days.MTH, LocalTime.of(8, 30), LocalTime.of(11, 30)), Schedule.valueOf("MTH 08:30-11:30"));
    }
}

