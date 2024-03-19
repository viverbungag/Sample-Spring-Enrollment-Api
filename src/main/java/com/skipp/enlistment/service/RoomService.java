package com.skipp.enlistment.service;

import com.skipp.enlistment.domain.Room;

import java.util.Collection;

/**
 * Service layer class for Room.
 * <p>
 * This is where business validations should take place.
 */
public interface RoomService {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring service layer classes?
    // 2. Make all operations use a transaction. (Hint: what annotation do you use?)
    // 3. Make read operations use a read-only transaction.
    // 4. Wire any other service class or DAO class that you need to use.
    // 5. Throw appropriate exceptions. (Hint: read the description of the exceptions included in this project.)

    /**
     * Returns a Collection of ALL Faculty records.
     *
     * @return a Collection of all Faculty records
     */
    Collection<Room> findAllRooms();

    /**
     * Returns a single Room record matching the given name.
     *
     * @param name the name of the Room to match
     * @return the Room record with the matching name
     */
    Room findByName(String name);

    /**
     * Creates a Room record in the database.
     * <p>
     * Validations:
     * <ul>
     * <li> name should not be blank
     * <li> capacity should be non-negative
     * </ul>
     *
     * @param room the Room record to be created
     * @return the Room record that was created
     */
    Room create(Room room);

    /**
     * Updates a Room record in the database.
     * <p>
     * Validations:
     * <ul>
     * <li> capacity should be non-negative
     * </ul>
     *
     * @param room the Room record to be updated
     * @return the Room record that was updated
     */
    Room update(Room room);

    /**
     * Deletes a Room record from the database that matches the given name.
     *
     * @param name the name to match
     */
    void delete(String name);

}
