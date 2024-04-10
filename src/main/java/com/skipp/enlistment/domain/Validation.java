package com.skipp.enlistment.domain;

import com.skipp.enlistment.dao.AppUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class Validation {
    private final AppUserDao appUserRepository;

    @Autowired
    public Validation(AppUserDao appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public void validateIfRoleIsNotStudent(Authentication auth){
        AppUser appUser = appUserRepository.findByUsername(auth.getName());
        if (!appUser.getRole().equals("STUDENT")) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    public void validateIfRoleIsNotFaculty(Authentication auth){
        AppUser appUser = appUserRepository.findByUsername(auth.getName());
        if (!appUser.getRole().equals("FACULTY")) {
            throw new AccessDeniedException("Access Denied");
        }
    }
}
