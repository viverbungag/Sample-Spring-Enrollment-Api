package com.skipp.enlistment.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a faculty member.
 */
public class Faculty {

    public static final Faculty TBA = new Faculty(0, "", "");

    /**
     * Unique identifier for a faculty member
     */
    private final Integer facultyNumber;

    /**
     * The first name of the faculty member
     */
    private String firstName;

    /**
     * The last name of the faculty member
     */
    private String lastName;

    /**
     * The sections that this faculty member will be teaching
     */
    private final Collection<Section> sections = new HashSet<>();

    @JsonCreator
    public Faculty(Integer facultyNumber, String firstName, String lastName) {
        this.facultyNumber = facultyNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Faculty(Integer facultyNumber) {
        this(facultyNumber, "", "");
    }

    public Integer getFacultyNumber() {
        return facultyNumber;
    }

    public Collection<Section> getSections() {
        return new ArrayList<>(sections);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    @Override
    public String toString() {
        return this == TBA ? "TBA"
                : firstName + " " + lastName + " FN#" + facultyNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((facultyNumber == null) ? 0 : facultyNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Faculty other))
            return false;
        if (facultyNumber == null) {
            return other.facultyNumber == null;
        } else return facultyNumber.equals(other.facultyNumber);
    }

}

