package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.Student;
import com.skipp.enlistment.dto.StudentDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

// TODO What stereotype annotation should be put here?
@RequestMapping("/students")
public class StudentController {

    // TODO What bean should be wired here?

    // TODO What @XXXMapping annotation should be put here?
    public Collection<Student> getStudents() {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    public StudentDto getStudent(@PathVariable Integer studentNumber, Authentication auth) {
        // TODO implement this handler
        // Hint: 'auth' is where you can get the username of the user accessing the API
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    public Student createStudent(@RequestBody Student student) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    public Student updateStudent(@RequestBody Student student) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    public void deleteStudent(@PathVariable Integer studentNumber) {
        // TODO implement this handler
    }

}
