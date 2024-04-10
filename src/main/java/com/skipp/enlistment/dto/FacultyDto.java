package com.skipp.enlistment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.domain.Section;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.HashSet;

/**
 * Value object or DTO (data transfer object) for the Faculty object to be used in the web layer.
 */
public final class FacultyDto {

    @Schema(description = "Unique identifier for a faculty member")
    private Integer facultyNumber;

    @Schema(description = "The first name of the faculty member")
    private String firstName;

    @Schema(description = "The last name of the faculty member")
    private String lastName;

    @Schema(description = "The sections that this faculty member will be teaching")
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
