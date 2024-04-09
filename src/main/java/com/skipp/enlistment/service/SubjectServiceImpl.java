package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.SubjectDao;
import com.skipp.enlistment.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SubjectServiceImpl implements SubjectService{

    private final SubjectDao subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectDao subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Collection<Subject> findAllSubjects() {
        Collection<Subject> subjects = subjectRepository.findAllSubjects();
        return subjects;
    }

    @Override
    public Subject findSubject(String subjectId) {
        Subject subject = subjectRepository.findSubject(subjectId);
        return subject;
    }

    @Override
    public Subject create(Subject subject) {
        Subject newSubject = subjectRepository.create(subject);
        return newSubject;
    }

    @Override
    public void delete(String subjectId) {
        subjectRepository.findSubject(subjectId);
        subjectRepository.delete(subjectId);
    }
}
