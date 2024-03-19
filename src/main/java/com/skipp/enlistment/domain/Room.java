package com.skipp.enlistment.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents a room where a class will be held.
 */
public class Room {

    /**
     * Unique identifier for the room
     */
    private final String name;

    /**
     * Maximum capacity of the room
     */
    private final Integer capacity;

    public Room() {
        this.name = "TBA";
        this.capacity = 0;
    }

    @JsonCreator
    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        if (name == null) {
            return other.name == null;
        } else return name.equals(other.name);
    }

}

