package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.dto.FacultyDto;
import com.skipp.enlistment.service.FacultyService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

// TODO What stereotype annotation should be put here?
@RequestMapping("/faculty")
@RestController
public class FacultyController {

    // TODO What bean should be wired here?
    private final FacultyService facultyServiceImpl;
    private final Verification verification;

    public FacultyController(FacultyService facultyServiceImpl, Verification verification) {
        this.facultyServiceImpl = facultyServiceImpl;
        this.verification = verification;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Faculty> getAllFaculty() {
        // TODO implement this handler
        Collection<Faculty> faculties = facultyServiceImpl.findAllFaculty();
        return faculties;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    @GetMapping("/{facultyNumber}")
    @ResponseStatus(HttpStatus.OK)
    public FacultyDto getFaculty(@PathVariable Integer facultyNumber, Authentication auth) {
        // TODO implement this handler
        Faculty faculty;
        if (verification.isRoleNotFaculty(auth)){
            throw new AccessDeniedException("Access Denied");
        }
        try{
            faculty = facultyServiceImpl.findByNumber(facultyNumber, true);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Faculty Number: %s not found", facultyNumber));
        }

        FacultyDto facultyDto = new FacultyDto(faculty, true);
        return facultyDto;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty createFaculty(@RequestBody Faculty faculty, Authentication auth) {
        // TODO implement this handler
        Faculty newFaculty;

        if (verification.isRoleNotFaculty(auth)){
            throw new AccessDeniedException("Access Denied");
        }

        try{
            newFaculty = facultyServiceImpl.create(faculty);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistsException(String.format("Faculty Number: %s already exists", faculty.getFacultyNumber()));
        }

        return newFaculty;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Faculty updateFaculty(@RequestBody Faculty faculty, Authentication auth) {
        // TODO implement this handler
        Faculty updatedFaculty;
        if (verification.isRoleNotFaculty(auth)){
            throw new AccessDeniedException("Access Denied");
        }

        try {
            updatedFaculty = facultyServiceImpl.update(faculty);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Faculty Number: %s not found", faculty.getFacultyNumber()));
        }

        return updatedFaculty;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @DeleteMapping("/{facultyNumber}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFaculty(@PathVariable Integer facultyNumber, Authentication auth) {
        // TODO implement this handler
        if (verification.isRoleNotFaculty(auth)) {
            throw new AccessDeniedException("Access Denied");
        }

        try {
            facultyServiceImpl.delete(facultyNumber);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {

            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Faculty Number: %s not found", facultyNumber));
            }

            if (e instanceof DataIntegrityViolationException) {
                throw new ReferentialIntegrityViolationException("Faculty " + facultyNumber + " is still teaching a section");
            }
        }
    }

}
