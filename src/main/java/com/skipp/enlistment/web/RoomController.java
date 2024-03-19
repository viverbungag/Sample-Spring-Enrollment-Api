package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.Room;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

// TODO What stereotype annotation should be put here?
@RequestMapping("/rooms")
public class RoomController {

    // TODO What bean should be wired here?

    // TODO What @XXXMapping annotation should be put here?
    public Collection<Room> getRooms() {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    public Room createRoom(@RequestBody Room room) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    public Room updateRoom(@RequestBody Room room) {
        // TODO implement this handler
        return null;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    public void deleteRoom(@PathVariable String roomName) {
        // TODO implement this handler
    }

}
