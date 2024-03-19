package com.skipp.enlistment.domain;

/**
 * Generic exception thrown when there was an issue enlisting a student into a section.
 */
public class EnlistmentException extends RuntimeException {

    public EnlistmentException(String message) {
        super(message);
    }

}

