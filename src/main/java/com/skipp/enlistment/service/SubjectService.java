package com.skipp.enlistment.service;

import com.skipp.enlistment.domain.Subject;

import java.util.Collection;

/**
 * Service layer class for Student.
 *
 * This is where business validations should take place.
 */
public interface SubjectService {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring service layer classes?
    // 2. Make all operations use a transaction. (Hint: what annotation do you use?)
    // 3. Make read operations use a read-only transaction.
    // 4. Wire any other service class or DAO class that you need to use.
    // 5. Throw appropriate exceptions. (Hint: read the description of the exceptions included in this project.)

    /**
     * Returns a Collection of ALL Subject records.
     *
     * @return  a Collection of all Subject records
     */
    Collection<Subject> findAllSubjects();

    /**
     * Returns a single Subject record matching the given subjectId.
     *
     * @param subjectId the subjectId of the Subject to match
     * @return          the Subject record with the matching subjectId
     */
    Subject findSubject(String subjectId);

    /**
     * Creates a Room record in the database.
     * <p>
     * Validations:
     * <ul>
     * <li> name should not be blank
     * </ul>
     * @param subject   the Subject record to be created
     * @return          the Subject record that was created
     */
    Subject create(Subject subject);

    /**
     * Deletes a Subject record from the database that matches the given subjectId.
     *
     * @param subjectId  the subjectId to match
     */
    void delete(String subjectId);

}
