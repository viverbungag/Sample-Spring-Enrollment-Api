package com.skipp.enlistment.domain;

/**
 * This represents a user of the system; may be a STUDENT or FACULTY.
 */
public class AppUser {

    /**
     * The username to use for authentication
     */
    private String username;

    /**
     * The hash of the user's password
     */
    private String passwordHash;

    /**
     * The role of the user in the system; may be STUDENT or FACULTY
     */
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;

    }

}
