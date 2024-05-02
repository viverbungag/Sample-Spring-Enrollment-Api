package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.dao.EnlistmentDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.dao.StudentDao;
import com.skipp.enlistment.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentDao studentRepository;
    private final EnlistmentDao enlistmentRepository;
    private final SectionDao sectionRepository;
    private final AppUserDao appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(StudentDao studentRepository, EnlistmentDao enlistmentRepository, SectionDao sectionRepository, AppUserDao appUserRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.enlistmentRepository = enlistmentRepository;
        this.sectionRepository = sectionRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Collection<Student> findAllStudents() {
        return studentRepository.findAllStudents();
    };

    public Student findByNumber(int studentNumber, boolean includeSections) {
        Student student = studentRepository.findByNumber(studentNumber);
        if (includeSections) {
            Collection<Enlistment> enlistments = enlistmentRepository.findAllEnlistedClasses(studentNumber);
            enlistments.stream().forEach(enlistment -> {
                Section section = sectionRepository.findById(enlistment.sectionId());
                student.addSection(section);
            });
        }
        return student;
    };

    @Transactional
    public Student create(Student student) {
        Student newStudent = studentRepository.create(student);

        validateIfStudentNumberIsNonNegative(student.getStudentNumber());
        validateIfFirstNameIsNotBlank(student.getFirstName());
        validateIfLastNameIsNotBlank(student.getLastName());

        AppUser appUser = constructAppUserBasedOnStudent(newStudent);
        appUserRepository.create(appUser);

        return newStudent;
    };

    @Transactional
    public Student update(Student student) {
        validateIfFirstNameIsNotBlank(student.getFirstName());
        validateIfLastNameIsNotBlank(student.getLastName());

        // Will check if student exists, else will throw an exception
        studentRepository.findByNumber(student.getStudentNumber());

        Student updatedStudent = studentRepository.update(student);

        AppUser appUser = constructAppUserBasedOnStudent(updatedStudent);
        appUserRepository.update(appUser);

        return updatedStudent;
    };

    @Transactional
    public void delete(int studentNumber) {

        // Will check if student exists, else will throw an exception
        studentRepository.findByNumber(studentNumber);

        studentRepository.delete(studentNumber);
    };

    private void validateIfStudentNumberIsNonNegative(int studentNumber) {
        if (studentNumber < 0) {
            throw new IllegalArgumentException("studentNumber must be non-negative, was " + studentNumber);
        }
    }

    private void validateIfFirstNameIsNotBlank(String firstName) {
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("firstName should not be blank");
        }
    }

    private void validateIfLastNameIsNotBlank(String lastName) {
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("lastName should not be blank");
        }
    }

    private AppUser constructAppUserBasedOnStudent(Student student) {
        String rawPassword = StringUtils.replaceChars(student.getFirstName() + student.getLastName(), " ", "");
        String password = passwordEncoder.encode(rawPassword);
        AppUser appUser = new AppUser(String.format("ST-%s", student.getStudentNumber()), password, "STUDENT");
        return appUser;
    }


}
