package com.skipp.enlistment.web;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.dto.StudentDto;
import com.skipp.enlistment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO What stereotype annotation should be put here?
@RequestMapping("/students")
@RestController
public class StudentController {

    private final StudentService studentServiceImpl;
    private final Validation validation;

    // TODO What bean should be wired here?
    @Autowired
    public StudentController(StudentService studentServiceImpl, Validation validation) {
        this.studentServiceImpl = studentServiceImpl;
        this.validation = validation;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Student> getStudents() {
        // TODO implement this handler
        return studentServiceImpl.findAllStudents();
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    @GetMapping("/{studentNumber}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDto getStudent(@PathVariable Integer studentNumber, Authentication auth) {
        // TODO implement this handler
        // Hint: 'auth' is where you can get the username of the user accessing the API
        Student student;
        validation.validateIfRoleIsNotFaculty(auth);

        try{
            student = studentServiceImpl.findByNumber(studentNumber, true);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Student Number: %s not found", studentNumber));
        }

        StudentDto studentDto = new StudentDto(student, true);
        return studentDto;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student, Authentication auth) {
        // TODO implement this handler
        Student newStudent;
        validation.validateIfRoleIsNotFaculty(auth);

        try{
            newStudent = studentServiceImpl.create(student);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistsException("Student with studentNumber " + student.getStudentNumber() + " already exists.");
        }

        return newStudent;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Student updateStudent(@RequestBody Student student, Authentication auth) {
        // TODO implement this handler
        Student updatedStudent;
        validation.validateIfRoleIsNotFaculty(auth);

        try{
            updatedStudent = studentServiceImpl.update(student);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Student Number: %s not found", student.getStudentNumber()));
        }

        return updatedStudent;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @DeleteMapping("/{studentNumber}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(@PathVariable Integer studentNumber, Authentication auth) {
        // TODO implement this handler
        validation.validateIfRoleIsNotFaculty(auth);

        try {
            studentServiceImpl.delete(studentNumber);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {

            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Student Number: %s not found", studentNumber));
            }

            if (e instanceof DataIntegrityViolationException) {
                throw new ReferentialIntegrityViolationException("Student " + studentNumber + " is still enrolled in a section.");
            }
        }
    }
}
