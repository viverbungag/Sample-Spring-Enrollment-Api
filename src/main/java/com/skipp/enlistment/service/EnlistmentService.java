package com.skipp.enlistment.service;

import com.skipp.enlistment.domain.Enlistment;

/**
 * Service layer class for Enlistment.
 * <p>
 * This is where business validations should take place.
 */
public interface EnlistmentService {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring service layer classes?
    // 2. Make all operations use a transaction. (Hint: what annotation do you use?)
    // 3. Make read operations use a read-only transaction.
    // 4. Wire any other service class or DAO class that you need to use.
    // 5. Throw appropriate exceptions. (Hint: read the description of the exceptions included in this project.)

    /**
     * Enlists a student into a section.
     * <p>
     * Validations:
     * <ul>
     * <li> Student with studentNumber should exist.
     * <li> Section with sectionId should exist.
     * <li> The student should not be enrolled in the same class more than once.
     * <li> The student should not enlist in more than one section with the same subject.
     * <li> There must be no conflict in schedule between this section and the other sections that the student is already enlisted in.
     * <li> The number of students enlisted in a section must not exceed the capacity of the room assigned to it.
     * <li> The student can only enlist himself/herself; he/she cannot enroll another student.
     * </ul>
     *
     * @param studentNumber the studentNumber of the Student who's trying to enlist
     * @param sectionId     the sectionId of the Section that the student intends to enlist in
     * @return the Enlistment record that was saved
     */
    Enlistment enlist(int studentNumber, String sectionId);

    /**
     * Removes a student from the list of students enrolled in a section.
     *
     * @param studentNumber the studentNumber of the Student to be removed
     * @param sectionId     the sectionId of the Section
     */
    void cancel(int studentNumber, String sectionId);

}
