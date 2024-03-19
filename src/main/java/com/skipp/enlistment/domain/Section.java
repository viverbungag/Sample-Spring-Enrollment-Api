package com.skipp.enlistment.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a class where students would enlist in.
 */
public class Section {

    /**
     * Unique identifier for the section
     */
    private final String sectionId;

    /**
     * The subject to be taught in the class
     */
    private final Subject subject;

    /**
     * When this class will be held each week
     */
    private Schedule schedule;

    /**
     * Where this class will be held
     */
    private Room room;

    /**
     * The faculty member who will teaching this class
     */
    private Faculty faculty;

    /**
     * The students enlisted in this class
     */
    private final Collection<Student> students = new HashSet<>();

    public Section(String sectionId, Subject subject, Schedule schedule, Room room, Faculty faculty) {
        this.sectionId = sectionId;
        this.schedule = schedule;
        this.subject = subject;
        this.room = room;
        this.faculty = faculty;
    }

    /**
     * Checks whether this class has an overlap of schedule with another class.
     *
     * @param other the other Section to compare against
     * @return      true if there is an overlap
     */
    public boolean hasScheduleOverlapWith(Section other) {
        return this.schedule.isOverlappingWith(other.schedule);
    }

    public Collection<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Subject getSubject() {
        return subject;
    }

    public String getSectionId() {
        return sectionId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    /**
     * Adds a student to the class list.
     *
     * @param student   the student to add
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    @Override
    public String toString() {
        return sectionId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sectionId == null) ? 0 : sectionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Section other))
            return false;
        if (sectionId == null) {
            return other.sectionId == null;
        } else {
            return sectionId.equals(other.sectionId);
        }
    }

}
