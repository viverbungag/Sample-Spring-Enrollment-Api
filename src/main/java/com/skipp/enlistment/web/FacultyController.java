package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.dto.FacultyDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

// TODO What stereotype annotation should be put here?
@RequestMapping("/faculty")
public class FacultyController {

    // TODO What bean should be wired here?

    // TODO What @XXXMapping annotation should be put here?
    public Collection<Faculty> getAllFaculty() {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    public FacultyDto getFaculty(@PathVariable Integer facultyNumber) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    public void deleteFaculty(@PathVariable Integer facultyNumber) {
        // TODO implement this handler
    }

}
