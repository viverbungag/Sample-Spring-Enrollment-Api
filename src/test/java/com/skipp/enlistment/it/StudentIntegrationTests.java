package com.skipp.enlistment.it;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.domain.AppUser;
import com.skipp.enlistment.domain.Student;
import com.skipp.enlistment.dto.ErrorResponse;
import com.skipp.enlistment.dto.SectionDto;
import com.skipp.enlistment.dto.StudentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class StudentIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AppUserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private HttpHeaders headers = new HttpHeaders();
    private String endpointUrl;

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setBasicAuth("FC-20160916", "WalterWhite", StandardCharsets.UTF_8);
        endpointUrl = "http://localhost:" + port + "/students";
    }

    @Test
    public void displayAllStudents_unauthorized() {
        ResponseEntity<List> response = restTemplate.getForEntity(endpointUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void displayAllStudents() {
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, request, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Student> students = response.getBody();
        assertThat(students.size()).isEqualTo(3);
    }

    @Test
    public void displayOneStudent() {
        final int STUDENT_NUMBER = 20240001;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<StudentDto> response = restTemplate.exchange(endpointUrl + "/" + STUDENT_NUMBER, HttpMethod.GET, request, StudentDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        StudentDto student = response.getBody();
        assertThat(student.getStudentNumber()).isEqualTo(STUDENT_NUMBER);
        assertThat(student.getFirstName()).isEqualTo("Jesse");
        assertThat(student.getLastName()).isEqualTo("Pinkman");
        assertThat(student.getSections()).extracting(SectionDto::getSectionId).containsExactly("ES1TFXY");
    }

    @Test
    public void displayOneStudent_denied() {
        final int STUDENT_NUMBER = 20240001;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240002", "HectorSalamanca", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<StudentDto> response = restTemplate.exchange(endpointUrl + "/" + STUDENT_NUMBER, HttpMethod.GET, request, StudentDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void displayNonExistentStudent() {
        final int STUDENT_NUMBER = 999;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl + "/" + STUDENT_NUMBER, HttpMethod.GET, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createStudent_success() {
        final int STUDENT_NUMBER = 20240004;
        final String FIRST_NAME = "Nacho";
        final String LAST_NAME = "Varga";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<Student> response = restTemplate.postForEntity(endpointUrl, request, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Student _student = response.getBody();
        assertThat(_student.getStudentNumber()).isEqualTo(STUDENT_NUMBER);
        assertThat(_student.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(_student.getLastName()).isEqualTo(LAST_NAME);

        final String USERNAME = "ST-" + STUDENT_NUMBER;
//        String rawPassword = StringUtils.replaceChars(student.getFirstName() + student.getLastName(), " ", "");
//        final String PASSWORD = passwordEncoder.encode(passwordEncoder.encode(rawPassword));
        final String ROLE = "STUDENT";
        AppUser user = userDao.findByUsername(USERNAME);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
//        assertThat(user.getPasswordHash()).isEqualTo(PASSWORD);
        assertThat(user.getRole()).isEqualTo(ROLE);
    }

    @Test
    public void createStudent_accessDenied() {
        final int STUDENT_NUMBER = 20240004;
        final String FIRST_NAME = "Nacho";
        final String LAST_NAME = "Varga";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void createStudent_duplicateStudentNumber_fail() {
        final int STUDENT_NUMBER = 20240003;
        final String FIRST_NAME = "Nacho";
        final String LAST_NAME = "Varga";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Student with studentNumber " + STUDENT_NUMBER + " already exists.");
    }

    @Test
    public void createStudent_blankFirstName_fail() {
        final int STUDENT_NUMBER = 20240004;
        final String FIRST_NAME = "";
        final String LAST_NAME = "Varga";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "firstName should not be blank");
    }

    @Test
    public void createStudent_blankLastName_fail() {
        final int STUDENT_NUMBER = 20240004;
        final String FIRST_NAME = "Nacho";
        final String LAST_NAME = "";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "lastName should not be blank");
    }

    @Test
    public void updateStudent_success() {
        final int STUDENT_NUMBER = 20240003;
        final String FIRST_NAME = "Nacho";
        final String LAST_NAME = "Varga";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<Student> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student _student = response.getBody();
        assertThat(_student.getStudentNumber()).isEqualTo(STUDENT_NUMBER);
        assertThat(_student.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(_student.getLastName()).isEqualTo(LAST_NAME);

        final String USERNAME = "ST-" + STUDENT_NUMBER;
//        String rawPassword = StringUtils.replaceChars(student.getFirstName() + student.getLastName(), " ", "");
//        final String PASSWORD = passwordEncoder.encode(passwordEncoder.encode(rawPassword));
        final String ROLE = "STUDENT";
        AppUser user = userDao.findByUsername(USERNAME);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
//        assertThat(user.getPasswordHash()).isEqualTo(PASSWORD);
        assertThat(user.getRole()).isEqualTo(ROLE);
    }

    @Test
    public void updateStudent_accessDenied() {
        final int STUDENT_NUMBER = 20240003;
        final String FIRST_NAME = "Nacho";
        final String LAST_NAME = "Varga";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void updateStudent_nonExistent_fail() {
        final int STUDENT_NUMBER = 20240005;
        final String FIRST_NAME = "Nacho";
        final String LAST_NAME = "Varga";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateStudent_blankFirstName_fail() {
        final int STUDENT_NUMBER = 20240003;
        final String FIRST_NAME = "";
        final String LAST_NAME = "Varga";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "firstName should not be blank");
    }

    @Test
    public void updateStudent_blankLastName_fail() {
        final int STUDENT_NUMBER = 20240003;
        final String FIRST_NAME = "Nacho";
        final String LAST_NAME = "";

        Student student = new Student(STUDENT_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "lastName should not be blank");
    }

    @Test
    public void deleteStudent_success() {
        final int STUDENT_NUMBER = 20240003;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + STUDENT_NUMBER, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteStudent_accessDenied() {
        final int STUDENT_NUMBER = 20240003;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl + "/" + STUDENT_NUMBER, HttpMethod.DELETE, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void deleteStudent_stillEnrolled_fail() {
        final int STUDENT_NUMBER = 20240001;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl + "/" + STUDENT_NUMBER, HttpMethod.DELETE, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Student " + STUDENT_NUMBER + " is still enrolled in a section.");
    }

    @Test
    public void deleteStudent_notFound() {
        final int STUDENT_NUMBER = 20241999;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + STUDENT_NUMBER, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
