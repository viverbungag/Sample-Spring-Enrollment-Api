package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Enlistment;
import com.skipp.enlistment.domain.Section;
import com.skipp.enlistment.domain.Student;

import java.util.Collection;

/**
 * DAO class for saving and retrieving records in the <code>enlistments</code> table.
 */
public interface EnlistmentDao {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring DAO classes?
    // 2. You may use either JdbcTemplate or JdbcClient for your database operations.
    // 3. Read the docs for your choice from #2. Google for examples on how to use them.

    /**
     * Saves a record to the <code>enlistments</code> table.
     *
     * @param student   holds the studentNumber to save
     * @param section   holds the sectionId to save
     * @return          the Enlistment record that was saved
     */
    Enlistment create(Student student, Section section);

    /**
     * Deletes a record that matches the given studentNumber and sectionId
     * from the <code>enlistments</code> table.
     *
     * @param studentNumber the studentNumber to match
     * @param sectionId     the sectionId to match
     */
    void delete(int studentNumber, String sectionId);

    /**
     *
     * @param sectionId the sectionId to match
     * @return          a Collection of Enlistments that match the given sectionId
     */
    Collection<Enlistment> findAllStudentsEnlisted(String sectionId);

    /**
     *
     * @param studentNumber the studentNumber to match
     * @return              a Collection of Enlistments that match the given sectionId
     */
    Collection<Enlistment> findAllEnlistedClasses(int studentNumber);

}
