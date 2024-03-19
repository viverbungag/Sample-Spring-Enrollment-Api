package com.skipp.enlistment.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents a subject to be taught in class
 */
public class Subject {

    /**
     * Unique identifier of the subject
     */
    private final String subjectId;

    @JsonCreator
    public Subject(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    @Override
    public String toString() {
        return getSubjectId();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((subjectId == null) ? 0 : subjectId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Subject other = (Subject) obj;
        if (subjectId == null) {
            if (other.subjectId != null)
                return false;
        } else if (!subjectId.equals(other.subjectId))
            return false;
        return true;
    }

}

