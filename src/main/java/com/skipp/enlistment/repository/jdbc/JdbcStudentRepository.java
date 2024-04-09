package com.skipp.enlistment.repository.jdbc;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.dao.StudentDao;
import com.skipp.enlistment.domain.AppUser;
import com.skipp.enlistment.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class JdbcStudentRepository implements StudentDao {

    JdbcTemplate jdbcTemplate;


    @Autowired
    public JdbcStudentRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Student> findAllStudents() {
        String sql = "SELECT * FROM students";
        Collection<Student> students = jdbcTemplate.query(sql, new StudentMapper());
        return students;
    }

    @Override
    public Student findByNumber(int studentNumber) {
        String sql = "SELECT * FROM students WHERE student_number = ?";
        Student student = jdbcTemplate.queryForObject(sql, new StudentMapper(), studentNumber);
        return student;
    }

    @Override
    public Student create(Student student) {
        String sql = "INSERT INTO students (student_number, first_name, last_name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, student.getStudentNumber(), student.getFirstName(), student.getLastName());
        return new Student(student.getStudentNumber(), student.getFirstName(), student.getLastName());
    }

    @Override
    public Student update(Student student) {
        String sql = "UPDATE students SET first_name = ?, last_name = ? WHERE student_number = ?";
        jdbcTemplate.update(sql, student.getFirstName(), student.getLastName(), student.getStudentNumber());
        return new Student(student.getStudentNumber(), student.getFirstName(), student.getLastName());
    }

    @Override
    public void delete(int studentNumber) {
        String sql = "DELETE FROM students WHERE student_number = ?";
        jdbcTemplate.update(sql, studentNumber);
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
