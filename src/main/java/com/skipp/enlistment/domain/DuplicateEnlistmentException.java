package com.skipp.enlistment.domain;

/**
 * Should be thrown when a student tries to enlist in a section more than once.
 */
public class DuplicateEnlistmentException extends EnlistmentException {

    public DuplicateEnlistmentException(String message) {
        super(message);
    }

}
