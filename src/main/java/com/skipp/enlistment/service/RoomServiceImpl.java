package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.RoomDao;
import com.skipp.enlistment.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoomServiceImpl implements RoomService{

    private final RoomDao roomRepository;

    @Autowired
    public RoomServiceImpl(RoomDao roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Collection<Room> findAllRooms() {
        Collection<Room> rooms = roomRepository.findAllRooms();
        return rooms;
    }

    @Override
    public Room findByName(String name) {
        Room room = roomRepository.findByName(name);
        return room;
    }

    @Override
    public Room create(Room room) {
        Room newRoom = roomRepository.create(room);
        return newRoom;
    }

    @Override
    public Room update(Room room) {
        roomRepository.findByName(room.getName());
        Room updatedRoom = roomRepository.update(room);
        return updatedRoom;
    }

    @Override
    public void delete(String name) {
        roomRepository.findByName(name);
        roomRepository.delete(name);
    }
}
