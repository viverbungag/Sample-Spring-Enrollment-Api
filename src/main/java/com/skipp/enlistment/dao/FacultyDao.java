package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Faculty;

import java.util.Collection;

/**
 * DAO class for saving and retrieving records in the <code>faculty</code> table.
 */
public interface FacultyDao {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring DAO classes?
    // 2. You may use either JdbcTemplate or JdbcClient for your database operations.
    // 3. Read the docs for your choice from #2. Google for examples on how to use them.

    /**
     * Returns ALL records from the <code>faculty</code> table.
     *
     * @return  a Collection of all Faculty records
     */
    Collection<Faculty> findAllFaculty();

    /**
     * Returns a single Faculty record that matches the given facultyNumber.
     *
     * @param facultyNumber the facultyNumber to match
     * @return              the Faculty record that matches the given facultyNumber
     */
    Faculty findByNumber(int facultyNumber);

    /**
     * Saves a record to the <code>faculty</code> table.
     *
     * @param faculty   the Faculty to save
     * @return          the Faculty that was saved
     */
    Faculty create(Faculty faculty);

    /**
     * Saves changes in a record to the <code>faculty</code> table.
     *
     * @param faculty   the Faculty (with changes) to save
     * @return          the Faculty that was saved
     */
    Faculty update(Faculty faculty);

    /**
     * Deletes a record that matches the given facultyNumber from the <code>faculty</code> table.
     *
     * @param facultyNumber the facultyNumber to match
     */
    void delete(int facultyNumber);

}

