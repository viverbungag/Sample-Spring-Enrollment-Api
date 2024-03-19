package com.skipp.enlistment.domain;

/**
 * Should be thrown when there is an attempt to create a new record in a table
 * but the primary key already exists.
 */
// TODO examine each table to know what the primary keys are
public class RecordAlreadyExistsException extends RuntimeException {

    public RecordAlreadyExistsException(String message) {
        super(message);
    }

}
