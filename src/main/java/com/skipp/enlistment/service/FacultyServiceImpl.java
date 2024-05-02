package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.dao.FacultyDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.domain.AppUser;
import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.domain.Section;
import org.apache.commons.lang3.StringUtils;
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
        validateIfFacultyNumberIsNonNegative(faculty.getFacultyNumber());

        validateIfFirstNameIsNotBlank(faculty.getFirstName());
        validateIfLastNameIsNotBlank(faculty.getLastName());

        Faculty newFaculty = facultyRepository.create(faculty);

        AppUser appUser = constructAppUserBasedOnFaculty(newFaculty);
        appUserRepository.create(appUser);
        return newFaculty;
    }

    @Override
    public Faculty update(Faculty faculty) {

        validateIfFirstNameIsNotBlank(faculty.getFirstName());
        validateIfLastNameIsNotBlank(faculty.getLastName());

        // Will check if faculty exists, else will throw an exception
        facultyRepository.findByNumber(faculty.getFacultyNumber());

        AppUser appUser = constructAppUserBasedOnFaculty(faculty);

        Faculty updatedFaculty = facultyRepository.update(faculty);
        appUserRepository.update(appUser);
        return updatedFaculty;
    }

    @Override
    public void delete(int facultyNumber) {

        // Will check if faculty exists, else will throw an exception
        facultyRepository.findByNumber(facultyNumber);

        facultyRepository.delete(facultyNumber);
    }

    private void validateIfFacultyNumberIsNonNegative(int facultyNumber) {
        if (facultyNumber < 0) {
            throw new IllegalArgumentException("facultyNumber must be non-negative, was " + facultyNumber);
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

    private AppUser constructAppUserBasedOnFaculty(Faculty faculty) {
        String rawPassword = StringUtils.replaceChars(faculty.getFirstName() + faculty.getLastName(), " ", "");
        String password = passwordEncoder.encode(rawPassword);
        AppUser appUser = new AppUser(String.format("FC-%s", faculty.getFacultyNumber()), password, "FACULTY");
        return appUser;
    }
}
