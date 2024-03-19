package com.skipp.enlistment.domain;

/**
 * Should be thrown when a student is trying to enlist in a section but there are already enough students
 * enlisted in the section to fill the room.
 */
public class RoomCapacityReachedException extends EnlistmentException {

    public RoomCapacityReachedException(String message) {
        super(message);
    }

}

