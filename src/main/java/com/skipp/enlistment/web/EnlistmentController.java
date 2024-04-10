package com.skipp.enlistment.web;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.service.EnlistmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

// TODO What stereotype annotation should be put here?
@RequestMapping("/enlist")
@RestController
public class EnlistmentController {

    private final Validation validation;
    private final EnlistmentService enlistmentServiceImpl;

    // TODO What bean should be wired here?
    @Autowired
    public EnlistmentController(Validation validation, EnlistmentService enlistmentServiceImpl) {
        this.validation = validation;
        this.enlistmentServiceImpl = enlistmentServiceImpl;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by students. Apply the appropriate annotation.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enlistment enlist(@RequestBody Enlistment enlistment, Authentication auth) {
        // TODO implement this handler
        // Hint: 'auth' is where you can get the username of the user accessing the API
        Enlistment newEnlistment;
        validation.validateIfRoleIsNotStudent(auth);

        validateIfTheStudentAccessingTheInformationIsTheUserAuthorized(auth, enlistment.studentNumber());

        try {
            newEnlistment = enlistmentServiceImpl.enlist(enlistment.studentNumber(), enlistment.sectionId());
        } catch (DuplicateEnlistmentException | SameSubjectException | ScheduleConflictException | RoomCapacityReachedException e) {
            throw new EnlistmentException(e.getMessage());
        }

        return newEnlistment;
    }

    // TODO What @XXXMapping annotation should be put here?
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void cancel(@RequestBody Enlistment enlistment) {
        // TODO implement this handler
        try {
            enlistmentServiceImpl.cancel(enlistment.studentNumber(), enlistment.sectionId());
        } catch (EmptyResultDataAccessException | RecordNotFoundException e) {

            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Enlistment not found, studentNumber: %s, sectionId: %s", enlistment.studentNumber(), enlistment.sectionId()));
            }

            if (e instanceof RecordNotFoundException) {
                throw new ReferentialIntegrityViolationException(e.getMessage());
            }
        }
    }

    private void validateIfTheStudentAccessingTheInformationIsTheUserAuthorized(Authentication auth, Integer studentNumber) {
        String authId = auth.getName().split("-")[1];
        if (!authId.equals(String.valueOf(studentNumber))) {
            throw new AccessDeniedException("You cannot enlist for another student");
        }
    }

}
