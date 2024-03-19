package com.skipp.enlistment.service;

import com.skipp.enlistment.domain.Faculty;

import java.util.Collection;

/**
 * Service layer class for Faculty.
 *
 * This is where business validations should take place.
 */
public interface FacultyService {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring service layer classes?
    // 2. Make all operations use a transaction. (Hint: what annotation do you use?)
    // 3. Make read operations use a read-only transaction.
    // 4. Wire any other service class or DAO class that you need to use. (Hint: You will need a PasswordEncoder for password operations.)
    // 5. Throw appropriate exceptions. (Hint: read the description of the exceptions included in this project.)

    /**
     * Returns a Collection of ALL Faculty records.
     *
     * @return a Collection of all Faculty records
     */
    Collection<Faculty> findAllFaculty();

    /**
     * Returns a single FacultyDto record matching the given facultyNumber.
     * <p>
     * If includeSections is <code>true</code>, then the sections that the faculty
     * is associated with should also be included in the returned FacultyDto.
     *
     * @param facultyNumber   the facultyNumber to match
     * @param includeSections indicates if associated classes are to be included
     * @return a FacultyDto record with the details of the faculty, with or without sections associated
     */
    Faculty findByNumber(int facultyNumber, boolean includeSections);

    /**
     * Creates a Faculty record in the database. This should also create a corresponding User record in the database
     * with the following attributes:
     * <ul>
     * <li> username should be 'FC-' + facultyNumber
     * <li> passwordHash should be the hash of (firstName + lastName); remove any spaces in between first before hashing
     * <li> role should be 'FACULTY'
     * </ul>
     * <p>
     * Validations:
     * <ul>
     * <li> facultyNumber must be non-negative
     * <li> firstName should not be blank
     * <li> lastName should not be blank
     * </ul>
     *
     * @param faculty   the Faculty record to be created
     * @return          the Faculty record that was created
     */
    Faculty create(Faculty faculty);

    /**
     * Updates a Faculty record in the database. This should also update the password of the corresponding User record.
     * <p>
     * Validations:
     * <ul>
     * <li> firstName should not be blank
     * <li> lastName should not be blank
     * </ul>
     *
     * @param faculty   the Faculty record (with changes) to be updated
     * @return          the Faculty record that was saved
     */
    Faculty update(Faculty faculty);

    /**
     * Deletes a Faculty record from the database that matches the given facultyNumber.
     *
     * @param facultyNumber the facultyNumber to match
     */
    void delete(int facultyNumber);

}
