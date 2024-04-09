package com.skipp.enlistment.repository.jdbc;

import com.skipp.enlistment.dao.SubjectDao;
import com.skipp.enlistment.domain.Room;
import com.skipp.enlistment.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class JdbcSubjectRepository implements SubjectDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcSubjectRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Subject> findAllSubjects() {
        String sql = "SELECT * FROM subjects";
        Collection<Subject> subjects = jdbcTemplate.query(sql, new SubjectMapper());
        return subjects;
    }

    @Override
    public Subject findSubject(String subjectId) {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        Subject subject = jdbcTemplate.queryForObject(sql, new SubjectMapper(), subjectId);
        return subject;
    }

    @Override
    public Subject create(Subject subject) {
        String sql = "INSERT INTO subjects (subject_id) VALUES (?)";
        jdbcTemplate.update(sql, subject.getSubjectId());
        return new Subject(subject.getSubjectId());
    }

    @Override
    public void delete(String subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        jdbcTemplate.update(sql, subjectId);
    }
}

class SubjectMapper implements RowMapper<Subject> {
    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Subject(
                rs.getString("subject_id"));
    }
}
