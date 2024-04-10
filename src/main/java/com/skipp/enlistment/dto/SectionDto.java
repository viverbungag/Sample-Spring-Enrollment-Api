package com.skipp.enlistment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skipp.enlistment.domain.Section;
import com.skipp.enlistment.domain.Student;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.HashSet;

/**
 * Value object or DTO (data transfer object) for the Section object to be used in the web layer.
 */
public class SectionDto {

    @Schema(description = "Unique identifier for a section")
    private String sectionId;

    @Schema(description = "Unique identifier for the subject")
    private String subjectId;

    @Schema(description = "The schedule of the section")
    private String schedule;

    @Schema(description = "The room name where the section is held")
    private String roomName;

    @Schema(description = "The faculty number of the faculty member teaching the section")
    private int facultyNumber;

    @Schema(description = "The students enrolled in the section")
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
