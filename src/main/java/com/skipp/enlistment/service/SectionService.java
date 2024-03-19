package com.skipp.enlistment.service;

import com.skipp.enlistment.domain.Section;

import java.util.Collection;

/**
 * Service layer class for Section.
 *
 * This is where business validations should take place.
 */
public interface SectionService {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring service layer classes?
    // 2. Make all operations use a transaction. (Hint: what annotation do you use?)
    // 3. Make read operations use a read-only transaction.
    // 4. Wire any other service class or DAO class that you need to use.
    // 5. Throw appropriate exceptions. (Hint: read the description of the exceptions included in this project.)

    /**
     * Returns a single Section record matching the given sectionId.
     * <p>
     * If includeStudents is <code>true</code>, then the students that are enrolled
     * in the section should also be included in the returned FacultyDto.
     *
     * @param sectionId
     * @param includeStudents
     * @return
     */
    Section findById(String sectionId, boolean includeStudents);

    /**
     * Returns a Collection of ALL Section records.
     *
     * @return  a Collection of all Section records
     */
    Collection<Section> findAllSections();

    /**
     * Creates a Section record in the database.
     * <p>
     * Validations:
     * <ul>
     * <li> sectionId should be alphanumeric
     * <li> There must be no overlap in schedule of rooms.
     * <li> There must be no overlap in schedule of faculty.
     * </ul>
     *
     * @param section   the Section record to be created
     * @return          the Section record that was created
     */
    Section create(Section section);

    /**
     * Updates a Section record in the database. Only the room and schedule can be modified in a Section record.
     * <p>
     * Validations:
     * <ul>
     * <li> There must be no overlap in schedule of rooms.
     * <li> There must be no overlap in schedule of faculty.
     * </ul>
     *
     * @param section   the Section record to be updated
     * @return          the Section record that was updated
     */
    Section update(Section section);

    /**
     * Deletes a Section record from the database that matches the given sectionId.
     *
     * @param sectionId the sectionId to match
     */
    void delete(String sectionId);

}
