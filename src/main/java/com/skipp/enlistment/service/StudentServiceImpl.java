package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.dao.EnlistmentDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.dao.StudentDao;
import com.skipp.enlistment.domain.*;
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

        if (student.getStudentNumber() < 0) {
            throw new IllegalArgumentException("studentNumber must be non-negative, was " + student.getStudentNumber());
        }


        if(student.getFirstName().isBlank()){
            throw new IllegalArgumentException("firstName should not be blank");
        }

        if(student.getLastName().isBlank()){
            throw new IllegalArgumentException("lastName should not be blank");
        }

        String password = passwordEncoder.encode(student.getFirstName().replace(" ", "") + student.getLastName().replace(" ", ""));
        AppUser appUser = new AppUser(String.format("ST-%s", student.getStudentNumber()), password, "STUDENT");
        appUserRepository.create(appUser);
        return newStudent;
    };

    @Transactional
    public Student update(Student student) {

        if(student.getFirstName().isBlank()){
            throw new IllegalArgumentException("firstName should not be blank");
        }

        if(student.getLastName().isBlank()){
            throw new IllegalArgumentException("lastName should not be blank");
        }

        studentRepository.findByNumber(student.getStudentNumber());
        Student updatedStudent = studentRepository.update(student);
        String password = passwordEncoder.encode(student.getFirstName().replace(" ", "") + student.getLastName().replace(" ", ""));
        AppUser appUser = new AppUser(String.format("ST-%s", student.getStudentNumber()), password, "STUDENT");
        appUserRepository.update(appUser);
        return updatedStudent;
    };

    @Transactional
    public void delete(int studentNumber) {
        studentRepository.findByNumber(studentNumber);
        studentRepository.delete(studentNumber);
    };
}
