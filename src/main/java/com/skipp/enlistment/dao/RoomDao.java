package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Room;

import java.util.Collection;

/**
 * DAO class for saving and retrieving records in the <code>rooms</code> table.
 */
public interface RoomDao {

    // TODO for the implementation class
    // 1. What stereotype annotation do you use to mark Spring DAO classes?
    // 2. You may use either JdbcTemplate or JdbcClient for your database operations.
    // 3. Read the docs for your choice from #2. Google for examples on how to use them.

    /**
     * Returns ALL the records from the <code>rooms</code> table.
     *
     * @return  a Collection of all Room records
     */
    Collection<Room> findAllRooms();

    /**
     * Returns a single Room record that matches the given name.
     *
     * @param name  the name to match
     * @return  the Room record that matches the given name
     */
    Room findByName(String name);

    /**
     * Saves a record to the <code>rooms</code> table.
     *
     * @param room  the Room to save
     * @return      the Room that was saved
     */
    Room create(Room room);

    /**
     * Saves changes in a record to the <code>rooms</code> table.
     *
     * @param room  the Room (with changes) to save
     * @return      the Room that was saved
     */
    Room update(Room room);

    /**
     * Deletes the record from the <code>rooms</code> table that matches the given name.
     *
     * @param name  the name to match
     */
    void delete(String name);

}

