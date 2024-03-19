package com.skipp.enlistment.domain;

/**
 * Should be thrown when a student is trying to enlist in a section, but has already enrolled in another section
 * that has the same subject.
 */
public class SameSubjectException extends EnlistmentException {

    public SameSubjectException(String message) {
        super(message);
    }

}
