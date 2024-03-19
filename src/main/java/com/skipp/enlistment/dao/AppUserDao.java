package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.AppUser;

/**
 *  DAO class for saving and retrieving records from the <code>users</code> table.
 */
public interface AppUserDao {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring DAO classes?
    // 2. You may use either JdbcTemplate or JdbcClient for your database operations.
    // 3. Read the docs for your choice from #2. Google for examples on how to use them.

    /**
     * Saves a record to the <code>users</code> table.
     *
     * @param user the AppUser to be saved
     * @return  the AppUser that was saved
     */
    AppUser create(AppUser user);

    /**
     * Returns a record from the <code>users</code> table with the <code>username</code> value
     * matching the given <code>username</code>.
     *
     * @param username  the username to find
     * @return  the AppUser record of the user with the given username
     */
    AppUser findByUsername(String username);

    /**
     * Saves changes to a record in the <code>users</code> table.
     *
     * @param user  the AppUser (with changes) to be saved
     * @return      the AppUser that was saved
     */
    AppUser update(AppUser user);

}
