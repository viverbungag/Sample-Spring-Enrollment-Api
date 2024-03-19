package com.skipp.enlistment.domain;

/**
 * Should be thrown when there is a conflict in a faculty's schedule.
 */
public class FacultyScheduleConflictException extends SectionCreationException {

    public FacultyScheduleConflictException(String message) {
        super(message);
    }

}

