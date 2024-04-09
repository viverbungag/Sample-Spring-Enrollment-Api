package com.skipp.enlistment.repository.jdbc;

import com.skipp.enlistment.dao.RoomDao;
import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class JdbcRoomRepository implements RoomDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRoomRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Room> findAllRooms() {
        return null;
    }

    @Override
    public Room findByName(String name) {
        return null;
    }

    @Override
    public Room create(Room room) {
        return null;
    }

    @Override
    public Room update(Room room) {
        return null;
    }

    @Override
    public void delete(String name) {

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
