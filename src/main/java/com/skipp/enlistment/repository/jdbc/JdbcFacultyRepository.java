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
        return null;
    }

    @Override
    public Faculty findByNumber(int facultyNumber) {
        return null;
    }

    @Override
    public Faculty create(Faculty faculty) {
        return null;
    }

    @Override
    public Faculty update(Faculty faculty) {
        return null;
    }

    @Override
    public void delete(int facultyNumber) {

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


