package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.service.SubjectService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

// TODO What stereotype annotation should be put here?
@RequestMapping("/subjects")
@RestController
public class SubjectController {

    // TODO What bean should be wired here?
    private final SubjectService subjectServiceImpl;
    private final Validation validation;

    public SubjectController(SubjectService subjectServiceImpl, Validation validation) {
        this.subjectServiceImpl = subjectServiceImpl;
        this.validation = validation;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Subject> getSubjects() {
        // TODO implement this handler
        Collection<Subject> subjects = subjectServiceImpl.findAllSubjects();
        return subjects;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subject createSubject(@RequestBody Subject subject, Authentication auth) {
        // TODO implement this handler
        Subject newSubject;
        validation.validateIfRoleIsNotFaculty(auth);

        try{
            newSubject = subjectServiceImpl.create(subject);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistsException("Subject with subjectId " + subject.getSubjectId() + " already exists.");
        }

        return newSubject;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @DeleteMapping("/{subjectId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSubject(@PathVariable String subjectId, Authentication auth) {
        // TODO implement this handler
        validation.validateIfRoleIsNotFaculty(auth);

        try {
            subjectServiceImpl.delete(subjectId);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {

            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Subject SubjectId: %s not found", subjectId));
            }

            if (e instanceof DataIntegrityViolationException) {
                throw new ReferentialIntegrityViolationException("Subject " + subjectId + " is still being used.");
            }
        }

    }

}
