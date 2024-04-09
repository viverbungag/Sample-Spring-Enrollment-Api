package com.skipp.enlistment.domain;

import com.skipp.enlistment.dao.AppUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class Verification {

    private final AppUserDao appUserRepository;

    @Autowired
    public Verification(AppUserDao appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public boolean isRoleNotFaculty(Authentication auth){
        AppUser appUser = appUserRepository.findByUsername(auth.getName());
        if (!appUser.getRole().equals("FACULTY")) {
            return true;
        }
        return false;
    }
}
