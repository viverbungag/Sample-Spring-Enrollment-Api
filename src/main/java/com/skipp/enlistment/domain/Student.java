package com.skipp.enlistment.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a student who is trying to enlist in classes.
 */
public class Student {

    /**
     * Unique identifier for the student
     */
    private final Integer studentNumber;

    /**
     * The first name of the student
     */
    private String firstName;

    /**
     * The last name of the student
     */
    private String lastName;

    /**
     * The sections where the student has enlisted in
     */
    private final Collection<Section> sections = new HashSet<>();

    @JsonCreator
    public Student(int studentNumber, String firstName, String lastName) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Collection<Section> getSections() {
        return new ArrayList<>(sections);
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " SN#" + studentNumber;
    }

    /**
     * Adds a section to the list of sections the student has enlisted in.
     *
     * @param section
     */
    public void addSection(Section section) {
        sections.add(section);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((studentNumber == null) ? 0 : studentNumber.hashCode());
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
        Student other = (Student) obj;
        if (studentNumber == null) {
            return other.studentNumber == null;
        } else return studentNumber.equals(other.studentNumber);
    }

}
