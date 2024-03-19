package com.skipp.enlistment.service;

import com.skipp.enlistment.domain.Student;

import java.util.Collection;

/**
 * Service layer class for Student.
 * <p>
 * This is where business validations should take place.
 */
public interface StudentService {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring service layer classes?
    // 2. Make all operations use a transaction. (Hint: what annotation do you use?)
    // 3. Make read operations use a read-only transaction.
    // 4. Wire any other service class or DAO class that you need to use. (Hint: You will need a PasswordEncoder for password operations.)
    // 5. Throw appropriate exceptions. (Hint: read the description of the exceptions included in this project.)

    /**
     * Returns a Collection of ALL StudentDto records.
     *
     * @return a Collection of all StudentDto records
     */
    Collection<Student> findAllStudents();

    /**
     * Returns a single Student record matching the given studentNumber.
     * <p>
     * If includeSections is <code>true</code>, then the sections that the student
     * is associated with should also be included in the returned Student.
     *
     * @param studentNumber   the studentNumber to match
     * @param includeSections indicates if associated classes are to be included
     * @return a Student record with the details of the student, with or without sections enlisted
     */
    Student findByNumber(int studentNumber, boolean includeSections);

    /**
     * Creates a Student record in the database. This should also create a corresponding User record in the database
     * with the following attributes:
     * <ul>
     * <li> username should be 'FC-' + facultyNumber
     * <li> passwordHash should be the hash of (firstName + lastName); remove any spaces in between first before hashing
     * <li> role should be 'FACULTY'
     * <p>
     * Validations:
     * <ul>
     * <li> studentNumber must be non-negative
     * <li> firstName should not be blank
     * <li> lastName should not be blank
     * </ul>
     *
     * @param student the Student record to be created
     * @return the Student record that was created
     */
    Student create(Student student);

    /**
     * Updates a Student record in the database. This should also update the password of the corresponding User record.
     * <p>
     * Validations:
     * <ul>
     * <li> firstName should not be blank
     * <li> lastName should not be blank
     * </ul>
     *
     * @param student the Student record (with changes) to be updated
     * @return the Student record that was saved
     */
    Student update(Student student);

    /**
     * Deletes a Student record from the database that matches the given studentNumber.
     *
     * @param studentNumber the studentNumber to match
     */
    void delete(int studentNumber);

}
