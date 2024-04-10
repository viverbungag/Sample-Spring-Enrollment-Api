package com.skipp.enlistment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skipp.enlistment.domain.Section;
import com.skipp.enlistment.domain.Student;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.HashSet;

/**
 * Value object or DTO (data transfer object) for the Student object to be used in the web layer.
 */
public final class StudentDto {

    @Schema(description = "Unique identifier for a student")
    private Integer studentNumber;

    @Schema(description = "The first name of the student")
    private String firstName;

    @Schema(description = "The last name of the student")
    private String lastName;

    @Schema(description = "The sections where the student has enlisted in")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<SectionDto> sections;

    public StudentDto() {
    }

    public StudentDto(Student student) {
        this(student, false);
    }

    public StudentDto(Student student, boolean includeSections) {
        this.studentNumber = student.getStudentNumber();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        if (includeSections) {
            sections = new HashSet<>();
            for (Section section : student.getSections()) {
                sections.add(new SectionDto(section, false));
            }
        }
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Collection<SectionDto> getSections() {
        return sections;
    }

}
