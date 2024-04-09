package com.skipp.enlistment.repository.jdbc;

import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

@Repository
public class JdbcSectionRepository implements SectionDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcSectionRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Section findById(String sectionId) {
        String sql = "SELECT * FROM sections " +
                "INNER JOIN subjects ON sections.subject_id = subjects.subject_id " +
                "INNER JOIN rooms ON sections.room_name = rooms.name " +
                "INNER JOIN faculty ON sections.faculty_number = faculty.faculty_number " +
                "WHERE section_id = ?";
        Section section = jdbcTemplate.queryForObject(sql, new SectionMapper(), sectionId);
        return section;
    }

    @Override
    public Collection<Section> findAllSections() {
        String sql = "SELECT * FROM sections " +
                "INNER JOIN subjects ON sections.subject_id = subjects.subject_id " +
                "INNER JOIN rooms ON sections.room_name = rooms.name " +
                "INNER JOIN faculty ON sections.faculty_number = faculty.faculty_number";
        Collection<Section> section = jdbcTemplate.query(sql, new SectionMapper());
        return section;
    }

    @Override
    public Collection<Section> findByFaculty(int facultyNumber) {
        String sql = "SELECT * FROM sections " +
                "INNER JOIN subjects ON sections.subject_id = subjects.subject_id " +
                "INNER JOIN rooms ON sections.room_name = rooms.name " +
                "INNER JOIN faculty ON sections.faculty_number = faculty.faculty_number " +
                "WHERE sections.faculty_number = ?";
        Collection<Section> section = jdbcTemplate.query(sql, new SectionMapper(), facultyNumber);
        return section;
    }

    @Override
    public Section create(Section section) {
        return null;
    }

    @Override
    public Section update(Section section) {
        return null;
    }

    @Override
    public void delete(String sectionId) {

    }
}

class SectionMapper implements RowMapper<Section> {
    @Override
    public Section mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subject subject = new Subject(
                rs.getString("subject_id"));

        Room room = new Room(
                rs.getString("room_name"),
                rs.getInt("capacity"));

        Faculty faculty = new Faculty(
                rs.getInt("faculty_number"),
                rs.getString("first_name"),
                rs.getString("last_name"));

        return new Section(
                rs.getString("section_id"),
                subject,
                Schedule.valueOf(rs.getString("schedule")),
                room,
                faculty);
    }
}
