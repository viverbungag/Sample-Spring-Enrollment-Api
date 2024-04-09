package com.skipp.enlistment.repository.jdbc;

import com.skipp.enlistment.dao.SubjectDao;
import com.skipp.enlistment.domain.Room;
import com.skipp.enlistment.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class JdbcSubjectRepository implements SubjectDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcSubjectRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Subject> findAllSubjects() {
        return null;
    }

    @Override
    public Subject findSubject(String subjectId) {
        return null;
    }

    @Override
    public Subject create(Subject subject) {
        return null;
    }

    @Override
    public void delete(String subjectId) {

    }
}

class SubjectMapper implements RowMapper<Subject> {
    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Subject(
                rs.getString("subject_id"));
    }
}
