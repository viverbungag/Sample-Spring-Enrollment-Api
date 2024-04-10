package com.skipp.enlistment.domain;

/**
 * Generic exception thrown when a section cannot be created.
 */
public class SectionCreationException extends RuntimeException {

    public SectionCreationException(String message) {
        super(message);
    }

}

