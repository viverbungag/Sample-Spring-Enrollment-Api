package com.skipp.enlistment.repository.jdbc;

import com.skipp.enlistment.dao.RoomDao;
import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.domain.Room;
import com.skipp.enlistment.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class JdbcRoomRepository implements RoomDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRoomRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Room> findAllRooms() {
        String sql = "SELECT * FROM rooms";
        Collection<Room> rooms = jdbcTemplate.query(sql, new RoomMapper());
        return rooms;
    }

    @Override
    public Room findByName(String name) {
        String sql = "SELECT * FROM rooms WHERE name = ?";
        Room room = jdbcTemplate.queryForObject(sql, new RoomMapper(), name);
        return room;
    }

    @Override
    public Room create(Room room) {
        String sql = "INSERT INTO rooms (name, capacity) VALUES (?, ?)";
        jdbcTemplate.update(sql, room.getName(), room.getCapacity());
        return new Room(room.getName(), room.getCapacity());
    }

    @Override
    public Room update(Room room) {
        String sql = "UPDATE rooms SET name = ?, capacity = ? WHERE name = ?";
        jdbcTemplate.update(sql, room.getName(), room.getCapacity(), room.getName());
        return new Room(room.getName(), room.getCapacity());
    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM rooms WHERE name = ?";
        jdbcTemplate.update(sql, name);
    }
}

class RoomMapper implements RowMapper<Room> {
    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Room(
                rs.getString("name"),
                rs.getInt("capacity"));
    }
}
