package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Subject;

import java.util.Collection;

/**
 * DAO class for saving and retrieving records in the <code>subjects</code> table.
 */
public interface SubjectDao {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring DAO classes?
    // 2. You may use either JdbcTemplate or JdbcClient for your database operations.
    // 3. Read the docs for your choice from #2. Google for examples on how to use them.

    /**
     * Returns ALL the records from the <code>subjects</code> table.
     * 
     * @return  a Collection of all Subject records
     */
    Collection<Subject> findAllSubjects();

    /**
     * Returns a single Subject record from the <code>subjects</code> table that matches the given subjectId.
     * 
     * @param subjectId the subjectId to match
     * @return          the Subject record that matches the given subjectId
     */
    Subject findSubject(String subjectId);

    /**
     * Saves a record to the <code>subjects</code> table.
     * 
     * @param subject   the Subject to save
     * @return          the Subject that was saved
     */
    Subject create(Subject subject);

    /**
     * Deletes a record from the <code>students</code> table that matches the given subjectId.
     * 
     * @param subjectId the subjectId to match
     */
    void delete(String subjectId);

}

