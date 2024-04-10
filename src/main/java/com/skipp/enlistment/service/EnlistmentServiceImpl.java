package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.EnlistmentDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.dao.StudentDao;
import com.skipp.enlistment.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class EnlistmentServiceImpl implements EnlistmentService {

    private final EnlistmentDao enlistmentRepository;
    private final SectionDao sectionRepository;
    private final StudentDao studentRepository;

    @Autowired
    public EnlistmentServiceImpl(EnlistmentDao enlistmentRepository, SectionDao sectionRepository, StudentDao studentRepository) {
        this.enlistmentRepository = enlistmentRepository;
        this.sectionRepository = sectionRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional
    public Enlistment enlist(int studentNumber, String sectionId) {
        Student student;
        Section section;

        try {
            student = studentRepository.findByNumber(studentNumber);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException("Student with number " + studentNumber + " not found");
        }

        try {
            section = sectionRepository.findById(sectionId);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException("Section with id " + sectionId + " not found");
        }

        Collection<Enlistment> enlistmentsBasedOnStudentNumber = enlistmentRepository.findAllEnlistedClasses(studentNumber);

        enlistmentsBasedOnStudentNumber.forEach(enlistment -> {
            if (enlistment.studentNumber() == studentNumber && enlistment.sectionId().equals(sectionId)) {
                throw new DuplicateEnlistmentException("Enlisted more than once: " + sectionId);
            }

            Section enlistedSection = sectionRepository.findById(enlistment.sectionId());

            if (section.getSubject().getSubjectId().equals(enlistedSection.getSubject().getSubjectId())) {
                throw new SameSubjectException("Section " + sectionId + " with subject " + section.getSubject().getSubjectId() + " has same subject as currently enlisted section " + enlistment.sectionId());
            }

            enlistedSection.getSchedule().notOverlappingWith(section.getSchedule());
        });

        Collection<Enlistment> enlistmentsBasedOnSectionId = enlistmentRepository.findAllStudentsEnlisted(sectionId);
        if (enlistmentsBasedOnSectionId.size() >= section.getRoom().getCapacity()) {
            throw new RoomCapacityReachedException("Capacity Reached - enlistments: " + enlistmentsBasedOnSectionId.size() + "; capacity: " + section.getRoom().getCapacity());
        }

        Enlistment enlistment = enlistmentRepository.create(student, section);
        return enlistment;
    }

    @Override
    @Transactional
    public void cancel(int studentNumber, String sectionId) {
        try {
            studentRepository.findByNumber(studentNumber);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException("Student with number " + studentNumber + " not found");
        }

        try {
            sectionRepository.findById(sectionId);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException("Section with id " + sectionId + " not found");
        }

        enlistmentRepository.findAllStudentsEnlisted(sectionId);
        enlistmentRepository.findAllEnlistedClasses(studentNumber);
        enlistmentRepository.delete(studentNumber, sectionId);
    }
}
