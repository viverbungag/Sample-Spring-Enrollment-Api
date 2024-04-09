package com.skipp.enlistment.repository.jdbc;

import com.skipp.enlistment.dao.EnlistmentDao;
import com.skipp.enlistment.domain.AppUser;
import com.skipp.enlistment.domain.Enlistment;
import com.skipp.enlistment.domain.Section;
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
public class JdbcEnlistmentRepository implements EnlistmentDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcEnlistmentRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Enlistment create(Student student, Section section) {
        return null;
    }

    @Override
    public void delete(int studentNumber, String sectionId) {
        String sql = "DELETE FROM enlistments WHERE student_number = ? AND section_id = ?";
        jdbcTemplate.update(sql, studentNumber, sectionId);
    }

    @Override
    public Collection<Enlistment> findAllStudentsEnlisted(String sectionId) {
        String sql = "SELECT * FROM enlistments WHERE section_id = ?";
        Collection<Enlistment> enlistment = jdbcTemplate.query(sql, new EnlistmentMapper(), sectionId);
        return enlistment;
    }

    @Override
    public Collection<Enlistment> findAllEnlistedClasses(int studentNumber) {
        String sql = "SELECT * FROM enlistments WHERE student_number = ?";
        Collection<Enlistment> enlistment = jdbcTemplate.query(sql, new EnlistmentMapper(), studentNumber);
        return enlistment;
    }
}

class EnlistmentMapper implements RowMapper<Enlistment> {
    @Override
    public Enlistment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Enlistment(
                rs.getInt("student_number"),
                rs.getString("section_id"));
    }
}
