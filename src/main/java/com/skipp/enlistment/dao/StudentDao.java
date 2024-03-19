package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Student;

import java.util.Collection;

/**
 * DAO class for saving and retrieving records in the <code>students</code> table.
 */
public interface StudentDao {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring DAO classes?
    // 2. You may use either JdbcTemplate or JdbcClient for your database operations.
    // 3. Read the docs for your choice from #2. Google for examples on how to use them.

    /**
     * Returns ALL the records from the <code>students</code> table.
     *
     * @return  a Collection of all Student records
     */
    Collection<Student> findAllStudents();

    /**
     * Returns a single Section record from the <code>students</code> table that matches the given studentNumber.
     *
     * @param studentNumber the studentNumber to match
     * @return              the Student record that matches the given studentNumber
     */
    Student findByNumber(int studentNumber);

    /**
     * Saves a record to the <code>students</code> table.
     *
     * @param student   the Student to save
     * @return          the Student that was saved
     */
    Student create(Student student);

    /**
     * Saves changes in a record to the <code>students</code> table.
     *
     * @param student   the Student (with changes) to save
     * @return          the Student that was saved
     */
    Student update(Student student);

    /**
     * Deletes a record from the <code>students</code> table that matches the given studentNumber.
     *
     * @param studentNumber the studentNumber to match
     */
    void delete(int studentNumber);

}
