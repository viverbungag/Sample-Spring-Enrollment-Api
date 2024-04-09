package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.dao.StudentDao;
import com.skipp.enlistment.domain.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SectionServiceImpl implements SectionService{

    private final SectionDao sectionRepository;

    @Autowired
    public SectionServiceImpl(SectionDao sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Section findById(String sectionId, boolean includeStudents) {
        return sectionRepository.findById(sectionId);
    }

    @Override
    public Collection<Section> findAllSections() {
        return sectionRepository.findAllSections();
    }

    @Override
    public Section create(Section section) {
        return null;
    }

    @Override
    public Section update(Section section) {
        return null;
    }

    @Override
    public void delete(String sectionId) {

    }
}
