package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.dao.EnlistmentDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.dao.StudentDao;
import com.skipp.enlistment.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    @Autowired
    public StudentServiceImpl(StudentDao studentRepository, EnlistmentDao enlistmentRepository, SectionDao sectionRepository, AppUserDao appUserRepository) {
        this.studentRepository = studentRepository;
        this.enlistmentRepository = enlistmentRepository;
        this.sectionRepository = sectionRepository;
        this.appUserRepository = appUserRepository;
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

        AppUser appUser = new AppUser(String.format("ST-%s", student.getStudentNumber()), "", "STUDENT");
        appUserRepository.create(appUser);
        return newStudent;
    };

    @Transactional
    public Student update(Student student) {
        Student updatedStudent = studentRepository.update(student);
        AppUser appUser = appUserRepository.findByUsername(String.format("ST-%s", student.getStudentNumber()));
        appUserRepository.update(appUser);
        return updatedStudent;
    };

    @Transactional
    public void delete(int studentNumber) {
        studentRepository.findByNumber(studentNumber);
        studentRepository.delete(studentNumber);
    };
}
