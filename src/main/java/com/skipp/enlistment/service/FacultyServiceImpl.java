package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.dao.FacultyDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.domain.AppUser;
import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.domain.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyServiceImpl implements FacultyService{

    private final FacultyDao facultyRepository;
    private final SectionDao sectionRepository;
    private final AppUserDao appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FacultyServiceImpl(FacultyDao facultyRepository, SectionDao sectionRepository, AppUserDao appUserRepository, PasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.sectionRepository = sectionRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Collection<Faculty> findAllFaculty() {
        Collection<Faculty> faculties = facultyRepository.findAllFaculty();
        return faculties;
    }

    @Override
    public Faculty findByNumber(int facultyNumber, boolean includeSections) {
        Faculty faculty = facultyRepository.findByNumber(facultyNumber);
        if (includeSections) {
            Collection<Section> sections = sectionRepository.findByFaculty(facultyNumber);
            sections.stream().forEach(section -> faculty.addSection(section));
        }

        return faculty;
    }

    @Override
    public Faculty create(Faculty faculty) {
        if (faculty.getFacultyNumber() < 0) {
            throw new IllegalArgumentException("facultyNumber must be non-negative, was " + faculty.getFacultyNumber());
        }

        if (faculty.getFirstName().isBlank()){
            throw new IllegalArgumentException("firstName should not be blank");
        }
        if (faculty.getLastName().isBlank()){
            throw new IllegalArgumentException("lastName should not be blank");
        }

        Faculty newFaculty = facultyRepository.create(faculty);
        String password = passwordEncoder.encode(faculty.getFirstName().replace(" ", "")+faculty.getLastName().replace(" ", ""));
        AppUser appUser = new AppUser(String.format("FC-%s", faculty.getFacultyNumber()), password, "FACULTY");
        appUserRepository.create(appUser);
        return newFaculty;
    }

    @Override
    public Faculty update(Faculty faculty) {

        if (faculty.getFacultyNumber() < 0) {
            throw new IllegalArgumentException("facultyNumber must be non-negative, was " + faculty.getFacultyNumber());
        }

        if (faculty.getFirstName().isBlank()){
            throw new IllegalArgumentException("firstName should not be blank");
        }
        if (faculty.getLastName().isBlank()){
            throw new IllegalArgumentException("lastName should not be blank");
        }

        facultyRepository.findByNumber(faculty.getFacultyNumber());
        Faculty updatedFaculty = facultyRepository.update(faculty);
        return updatedFaculty;
    }

    @Override
    public void delete(int facultyNumber) {
        facultyRepository.findByNumber(facultyNumber);
        facultyRepository.delete(facultyNumber);
    }
}
