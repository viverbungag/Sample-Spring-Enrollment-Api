package com.skipp.enlistment.domain;

/**
 * Should be thrown when there is a conflict in a room's schedule.
 */
public class RoomScheduleConflictException extends SectionCreationException {

    public RoomScheduleConflictException(String message) {
        super(message);
    }

}

