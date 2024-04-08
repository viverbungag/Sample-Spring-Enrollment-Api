package com.skipp.enlistment.dao;

import com.skipp.enlistment.dao.StudentDao;
import com.skipp.enlistment.domain.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class StudentImpl implements StudentDao {

    JdbcTemplate jdbcTemplate;

    public Collection<Student> findAllStudents() {
        String sql = "SELECT * FROM student";
        return jdbcTemplate.query(sql, new StudentMapper());
    }

    public Student findByNumber(int studentNumber) {
        return null;
    }

    public Student create(Student student) {
        return null;
    }

    public Student update(Student student) {
        return null;
    }

    public void delete(int studentNumber) {
    }
}

class StudentMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(
                rs.getInt("student_number"),
                rs.getString("first_name"),
                rs.getString("last_name"));
    }
}
