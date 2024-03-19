package com.skipp.enlistment.domain;

/**
 * Should be thrown when there is conflict of schedule.
 */
public class ScheduleConflictException extends EnlistmentException {

    public ScheduleConflictException(String message) {
        super(message);
    }

}

