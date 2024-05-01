package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

// TODO What stereotype annotation should be put here?
@RequestMapping("/rooms")
@RestController
public class RoomController {

    // TODO What bean should be wired here?
    private final RoomService roomServiceImpl;

    @Autowired
    public RoomController(RoomService roomServiceImpl) {
        this.roomServiceImpl = roomServiceImpl;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Room> getRooms() {
        // TODO implement this handler
        Collection<Room> rooms = roomServiceImpl.findAllRooms();
        return rooms;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('FACULTY')")
    public Room createRoom(@RequestBody Room room, Authentication auth) {
        // TODO implement this handler
        Room newRoom;

        try{
            newRoom = roomServiceImpl.create(room);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistsException("Room with name " + room.getName() + " already exists.");
        }

        return newRoom;
    }

    // TODO What @XXXMapping annotation should be put here?
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('FACULTY')")
    public Room updateRoom(@RequestBody Room room, Authentication auth) {
        // TODO implement this handler
        Room updatedRoom;

        try{
            updatedRoom = roomServiceImpl.update(room);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Room Name: %s not found", room.getName()));
        }

        return updatedRoom;
    }

    // TODO What @XXXMapping annotation should be put here?
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @DeleteMapping("/{roomName}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('FACULTY')")
    public void deleteRoom(@PathVariable String roomName, Authentication auth) {
        // TODO implement this handler

        try {
            roomServiceImpl.delete(roomName);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {

            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Room Name: %s not found", roomName));
            }

            if (e instanceof DataIntegrityViolationException) {
                throw new ReferentialIntegrityViolationException("Room " + roomName + " is still being used.");
            }
        }


    }

}
