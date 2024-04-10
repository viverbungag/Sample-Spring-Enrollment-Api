package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.EnlistmentDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.dao.StudentDao;
import com.skipp.enlistment.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SectionServiceImpl implements SectionService{

    private final SectionDao sectionRepository;
    private final EnlistmentDao enlistmentRepository;

    @Autowired
    public SectionServiceImpl(SectionDao sectionRepository, EnlistmentDao enlistmentRepository) {
        this.sectionRepository = sectionRepository;
        this.enlistmentRepository = enlistmentRepository;
    }

    @Override
    public Section findById(String sectionId, boolean includeStudents) {
        Section section = sectionRepository.findById(sectionId);
        return section;
    }

    @Override
    public Collection<Section> findAllSections() {
        Collection<Section> sections = sectionRepository.findAllSections();
        return sections;
    }

    @Override
    public Section create(Section section) {
        validateIfSectionIdIsAlphanumeric(section.getSectionId());
        validateScheduleOverlaps(section);

        Section newSection = sectionRepository.create(section);
        return newSection;
    }

    @Override
    public Section update(Section section) {
        validateScheduleOverlaps(section);

        // Will check if section exists, else will throw an exception
        sectionRepository.findById(section.getSectionId());

        Section updatedSection = sectionRepository.update(section);
        return updatedSection;
    }

    @Override
    public void delete(String sectionId) {

        // Will check if section exists, else will throw an exception
        sectionRepository.findById(sectionId);

        sectionRepository.delete(sectionId);
    }

    private void validateIfSectionIdIsAlphanumeric(String sectionId) {
        if (!sectionId.matches("^[a-zA-Z0-9]*$")) {
            throw new IllegalArgumentException("sectionId should be alphanumeric, was " + sectionId);
        }
    }

    private void validateScheduleOverlaps(Section section) {
        Collection<Section> allSections = sectionRepository.findAllSections();
        allSections.forEach(currentSection -> {
            if (section.hasScheduleOverlapWith(currentSection)) {
                if (section.getFaculty().getFacultyNumber().equals(currentSection.getFaculty().getFacultyNumber())) {
                    throw new ScheduleConflictException("Faculty " + section.getFaculty().getFirstName() + " " + section.getFaculty().getLastName() +
                            " FN#" + section.getFaculty().getFacultyNumber() + " has a schedule overlap between new section " +
                            section.getSectionId() + " with schedule " + section.getSchedule() + " and current section " +
                            currentSection.getSectionId() + " with schedule " + currentSection.getSchedule() + ".");
                }

                if (section.getRoom().equals(currentSection.getRoom())) {
                    throw new ScheduleConflictException("Room " + section.getRoom() + " has a schedule overlap between new section " +
                            section.getSectionId() + " with schedule " + section.getSchedule() + " and current section " +
                            currentSection.getSectionId() + " with schedule " + currentSection.getSchedule() + ".");
                }
            }
        });
    }
}
