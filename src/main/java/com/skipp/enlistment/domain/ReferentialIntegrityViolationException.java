package com.skipp.enlistment.domain;

/**
 * Should be thrown when a non-existent record is used as a reference from another entity.
 * Or when there is an attempt to delete a record that is still being referenced by another table.
 * <p>
 * Examples:
 * <ul>
 * <li> When trying to create a section with the room field referring to a record that does not exist in the database.
 * <li> When trying to delete a subject that is used in a section.
 * </ul>
 */
public class ReferentialIntegrityViolationException extends RuntimeException {

    public ReferentialIntegrityViolationException(String message) {
        super(message);
    }

}
