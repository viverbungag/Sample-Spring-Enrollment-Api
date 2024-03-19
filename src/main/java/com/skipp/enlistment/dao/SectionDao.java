package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Section;

import java.util.Collection;

/**
 * DAO class for saving and retrieving records in the <code>sections</code> table.
 */
public interface SectionDao {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring DAO classes?
    // 2. You may use either JdbcTemplate or JdbcClient for your database operations.
    // 3. Read the docs for your choice from #2. Google for examples on how to use them.

    /**
     * Returns a single Section record from the <code>sections</code> table that matches the given sectionId.
     *
     * @param sectionId the sectionId to match
     * @return          the Section record that matches the given sectionId
     */
    Section findById(String sectionId);

    /**
     * Returns ALL the records from the <code>sections</code> table.
     *
     * @return  a Collection of all Section records
     */
    Collection<Section> findAllSections();

    /**
     * Returns all records from the <code>sections</code> table that match the given facultyNumber.
     *
     * @param facultyNumber the facultyNumber to match
     * @return              a Collection of Section records that match the given facultyNumber
     */
    Collection<Section> findByFaculty(int facultyNumber);

    /**
     * Saves a record to the <code>sections</code> table.
     *
     * @param section   the Section to save
     * @return          the Section that was saved
     */
    Section create(Section section);

    /**
     * Saves changes in a record to the <code>sections</code> table.
     *
     * @param section   the Section (with changes) to save
     * @return          the Section that was saved
     */
    Section update(Section section);

    /**
     * Deletes a record from the <code>sections</code> table that matches the given sectionId.
     *
     * @param sectionId the sectionId to match
     */
    void delete(String sectionId);

}

