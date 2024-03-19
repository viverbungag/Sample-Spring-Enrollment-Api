package com.skipp.enlistment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.domain.Section;

import java.util.Collection;
import java.util.HashSet;

/**
 * Value object or DTO (data transfer object) for the Faculty object to be used in the web layer.
 */
public final class FacultyDto {

    private Integer facultyNumber;
    private String firstName;
    private String lastName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<SectionDto> sections;

    public FacultyDto() {
    }

    public FacultyDto(Faculty faculty) {
        this(faculty, false);
    }

    public FacultyDto(Faculty faculty, boolean includeSections) {
        this.facultyNumber = faculty.getFacultyNumber();
        this.firstName = faculty.getFirstName();
        this.lastName = faculty.getLastName();
        if (includeSections) {
            sections = new HashSet<>();
            for (Section section : faculty.getSections()) {
                sections.add(new SectionDto(section, false));
            }
        }
    }

    public Integer getFacultyNumber() {
        return facultyNumber;
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
