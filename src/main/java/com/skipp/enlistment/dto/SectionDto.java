package com.skipp.enlistment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skipp.enlistment.domain.Section;
import com.skipp.enlistment.domain.Student;

import java.util.Collection;
import java.util.HashSet;

/**
 * Value object or DTO (data transfer object) for the Section object to be used in the web layer.
 */
public class SectionDto {

    private String sectionId;
    private String subjectId;
    private String schedule;
    private String roomName;
    private int facultyNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<StudentDto> students;

    public SectionDto() {
    }

    public SectionDto(Section section, boolean includeStudents) {
        this.sectionId = section.getSectionId();
        this.subjectId = section.getSubject().getSubjectId();
        this.schedule = section.getSchedule().toString();
        this.roomName = section.getRoom().getName();
        this.facultyNumber = section.getFaculty().getFacultyNumber();
        if (includeStudents) {
            students = new HashSet<>();
            for (Student student : section.getStudents()) {
                students.add(new StudentDto(student, false));
            }
        }
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getFacultyNumber() {
        return facultyNumber;
    }

    public void setFacultyNumber(int facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public Collection<StudentDto> getStudents() {
        return students;
    }

}
