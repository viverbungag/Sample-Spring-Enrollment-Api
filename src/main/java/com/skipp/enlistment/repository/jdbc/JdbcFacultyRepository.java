package com.skipp.enlistment.repository.jdbc;

import com.skipp.enlistment.dao.FacultyDao;
import com.skipp.enlistment.domain.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class JdbcFacultyRepository implements FacultyDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcFacultyRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Faculty> findAllFaculty() {
        String sql = "SELECT * FROM faculty";
        Collection<Faculty> faculty = jdbcTemplate.query(sql, new FacultyMapper());
        return faculty;
    }

    @Override
    public Faculty findByNumber(int facultyNumber) {
        String sql = "SELECT * FROM faculty WHERE faculty_number = ?";
        Faculty faculty = jdbcTemplate.queryForObject(sql, new FacultyMapper(), facultyNumber);
        return faculty;
    }

    @Override
    public Faculty create(Faculty faculty) {
        String sql = "INSERT INTO faculty (faculty_number, first_name, last_name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, faculty.getFacultyNumber(), faculty.getFirstName(), faculty.getLastName());
        return new Faculty(faculty.getFacultyNumber(), faculty.getFirstName(), faculty.getLastName());
    }

    @Override
    public Faculty update(Faculty faculty) {
        String sql = "UPDATE faculty SET first_name = ?, last_name = ? WHERE faculty_number = ?";
        jdbcTemplate.update(sql, faculty.getFirstName(), faculty.getLastName(), faculty.getFacultyNumber());
        return new Faculty(faculty.getFacultyNumber(), faculty.getFirstName(), faculty.getLastName());
    }

    @Override
    public void delete(int facultyNumber) {
        String sql = "DELETE FROM faculty WHERE faculty_number = ?";
        jdbcTemplate.update(sql, facultyNumber);
    }
}

class FacultyMapper implements RowMapper<Faculty> {
    @Override
    public Faculty mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Faculty(
                rs.getInt("faculty_number"),
                rs.getString("first_name"),
                rs.getString("last_name"));
    }
}


