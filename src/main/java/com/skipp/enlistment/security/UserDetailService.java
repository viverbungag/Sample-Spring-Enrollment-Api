package com.skipp.enlistment.security;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Used by Spring Security for retrieving user details. DO NOT MODIFY!
 */
@Component
public class UserDetailService implements UserDetailsService {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private AppUserDao appUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AppUser user = appUserDao.findByUsername(username);
            return User.withUsername(user.getUsername()).password(user.getPasswordHash()).roles(user.getRole()).build();
        } catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
    }

}
