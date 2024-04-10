package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.dto.SectionDto;
import com.skipp.enlistment.service.FacultyService;
import com.skipp.enlistment.service.RoomService;
import com.skipp.enlistment.service.SectionService;
import com.skipp.enlistment.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

// TODO What stereotype annotation should be put here?
@RequestMapping("/sections")
@RestController
public class SectionController {

    private final SectionService sectionServiceImpl;
    private final SubjectService subjectServiceImpl;
    private final RoomService roomServiceImpl;
    private final FacultyService facultyServiceImpl;
    private final Verification verification;

    // TODO What bean/s should be wired here?
    @Autowired
    public SectionController(SectionService sectionServiceImpl, Verification verification, SubjectService subjectServiceImpl, RoomService roomServiceImpl, FacultyService facultyServiceImpl) {
        this.sectionServiceImpl = sectionServiceImpl;
        this.verification = verification;
        this.subjectServiceImpl = subjectServiceImpl;
        this.roomServiceImpl = roomServiceImpl;
        this.facultyServiceImpl = facultyServiceImpl;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<SectionDto> getAllSections() {
        // TODO implement this handler
        List<SectionDto> sections = sectionServiceImpl.findAllSections().stream()
                .map((Section section) -> new SectionDto(section, false))
                .toList();
        return sections;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    @GetMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public SectionDto getSection(@PathVariable String sectionId) {
        // TODO implement this handler
        Section section;

        try{
            section = sectionServiceImpl.findById(sectionId, true);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Section ID: %s not found", sectionId));
        }



        SectionDto sectionDto = new SectionDto(section, true);
        return sectionDto;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SectionDto createSection(@RequestBody SectionDto section, Authentication auth) {
        // TODO implement this handler
        Section newSection;
        Subject subject;
        Room room;
        Faculty faculty;
        Schedule schedule = Schedule.valueOf(section.getSchedule());

        if (verification.isRoleNotFaculty(auth)) {
            throw new AccessDeniedException("Access Denied");
        }

        try{
            subject = subjectServiceImpl.findSubject(section.getSubjectId());
        } catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException("Subject ID: " + section.getSubjectId() + " not found");
        }

        try{
            room = roomServiceImpl.findByName(section.getRoomName());
        } catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException("Room Name: " + section.getRoomName() + " not found");
        }

        try{
            faculty = facultyServiceImpl.findByNumber(section.getFacultyNumber(), false);
        } catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException("Faculty Number: " + section.getFacultyNumber() + " not found");
        }

        Section sectionConverted = new Section(section.getSectionId(), subject, schedule, room, faculty);

        try{

            newSection = sectionServiceImpl.create(sectionConverted);

        } catch (DuplicateKeyException | ScheduleConflictException e) {

            if (e instanceof ScheduleConflictException) {
                System.out.println(e.getMessage());
                throw new SectionCreationException(e.getMessage());
            }

            throw new DuplicateKeyException("Section ID: " + section.getSectionId() + " already exists");

        }

        SectionDto sectionDto = new SectionDto(newSection, true);
        return sectionDto;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public SectionDto updateSection(@RequestBody SectionDto section, Authentication auth) {
        // TODO implement this handler
        Section updatedSection;
        Subject subject;
        Room room;
        Faculty faculty;
        Schedule schedule = Schedule.valueOf(section.getSchedule());

        if (verification.isRoleNotFaculty(auth)) {
            throw new AccessDeniedException("Access Denied");
        }

        try{
            subject = subjectServiceImpl.findSubject(section.getSubjectId());
        } catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException("Subject ID: " + section.getSubjectId() + " not found");
        }

        try{
            room = roomServiceImpl.findByName(section.getRoomName());
        } catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException("Room Name: " + section.getRoomName() + " not found");
        }

        try{
            faculty = facultyServiceImpl.findByNumber(section.getFacultyNumber(), false);
        } catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException("Faculty Number: " + section.getFacultyNumber() + " not found");
        }

        Section sectionConverted = new Section(section.getSectionId(), subject, schedule, room, faculty);
        if (verification.isRoleNotFaculty(auth)) {
            throw new AccessDeniedException("Access Denied");
        }

        try{
            updatedSection = sectionServiceImpl.update(sectionConverted);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException("Section ID: " + section.getSectionId() + " not found");
        }

        SectionDto sectionDto = new SectionDto(updatedSection, true);
        return sectionDto;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @DeleteMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSection(@PathVariable String sectionId, Authentication auth) {
        // TODO implement this handler
        if (verification.isRoleNotFaculty(auth)) {
            throw new AccessDeniedException("Access Denied");
        }

        try{
            sectionServiceImpl.delete(sectionId);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {
            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException("Section ID: " + sectionId + " not found");
            }

            if (e instanceof DataIntegrityViolationException) {
                throw new ReferentialIntegrityViolationException("Section ID: " + sectionId + " is being referenced by other entities");
            }
        }
    }

}
