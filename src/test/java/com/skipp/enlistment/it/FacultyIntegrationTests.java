package com.skipp.enlistment.it;

import com.skipp.enlistment.dao.AppUserDao;
import com.skipp.enlistment.domain.AppUser;
import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.dto.FacultyDto;
import com.skipp.enlistment.dto.SectionDto;
import com.skipp.enlistment.dto.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
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
public class FacultyIntegrationTests {

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
        endpointUrl = "http://localhost:" + port + "/faculty";
    }

    @Test
    public void displayAllFaculty_unauthorized() {
        ResponseEntity<List> response = restTemplate.getForEntity(endpointUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void displayAllFaculty() {
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, request, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Faculty> faculty = response.getBody();
        assertThat(faculty.size()).isEqualTo(2);
    }

    @Test
    public void displayOneFaculty() {
        final int FACULTY_NUMBER = 20160916;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<FacultyDto> response = restTemplate.exchange(endpointUrl + "/" + FACULTY_NUMBER, HttpMethod.GET, request, FacultyDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        FacultyDto faculty = response.getBody();
        assertThat(faculty.getFacultyNumber()).isEqualTo(FACULTY_NUMBER);
        assertThat(faculty.getFirstName()).isEqualTo("Walter");
        assertThat(faculty.getLastName()).isEqualTo("White");
        assertThat(faculty.getSections()).extracting(SectionDto::getSectionId)
                .containsExactlyInAnyOrder("ES1TFXY", "KAS1MHR1", "ES1MHXY", "PI100TFWX");
    }

    @Test
    public void displayNonExistentFaculty() {
        final int FACULTY_NUMBER = 999;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<FacultyDto> response = restTemplate.exchange(endpointUrl + "/" + FACULTY_NUMBER, HttpMethod.GET, request, FacultyDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createFaculty_success() {
        final int FACULTY_NUMBER = 20140613;
        final String FIRST_NAME = "Mike";
        final String LAST_NAME = "Ehrmantraut";

        Faculty faculty = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(faculty, headers);

        ResponseEntity<Faculty> response = restTemplate.postForEntity(endpointUrl, request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Faculty _faculty = response.getBody();
        assertThat(_faculty.getFacultyNumber()).isEqualTo(FACULTY_NUMBER);
        assertThat(_faculty.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(_faculty.getLastName()).isEqualTo(LAST_NAME);

        final String USERNAME = "FC-" + FACULTY_NUMBER;
//        String rawPassword = StringUtils.replaceChars(student.getFirstName() + student.getLastName(), " ", "");
//        final String PASSWORD = passwordEncoder.encode(passwordEncoder.encode(rawPassword));
        final String ROLE = "FACULTY";
        AppUser user = userDao.findByUsername(USERNAME);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
//        assertThat(user.getPasswordHash()).isEqualTo(PASSWORD);
        assertThat(user.getRole()).isEqualTo(ROLE);
    }

    @Test
    public void createFaculty_accessDenied() {
        final int FACULTY_NUMBER = 20140613;
        final String FIRST_NAME = "Mike";
        final String LAST_NAME = "Ehrmantraut";

        Faculty faculty = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(faculty, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void createFaculty_duplicateFacultyNumber_fail() {
        final int FACULTY_NUMBER = 20180354;
        final String FIRST_NAME = "Hank";
        final String LAST_NAME = "Schrader";

        Faculty faculty = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(faculty, headers);

        ResponseEntity<Faculty> response = restTemplate.postForEntity(endpointUrl, request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void createFaculty_invalidFacultyNumber_fail() {
        final int FACULTY_NUMBER = -1;
        final String FIRST_NAME = "Hank";
        final String LAST_NAME = "Schrader";

        Faculty faculty = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(faculty, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "facultyNumber must be non-negative, was " + FACULTY_NUMBER);
    }

    @Test
    public void createFaculty_blankFirstName_fail() {
        final int FACULTY_NUMBER = 20170101;
        final String FIRST_NAME = "";
        final String LAST_NAME = "Fring";

        Faculty faculty = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(faculty, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "firstName should not be blank");
    }

    @Test
    public void createFaculty_blankLastName_fail() {
        final int FACULTY_NUMBER = 20170101;
        final String FIRST_NAME = "Gus";
        final String LAST_NAME = "";

        Faculty faculty = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(faculty, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "lastName should not be blank");
    }

    @Test
    public void updateFaculty_success() {
        final int FACULTY_NUMBER = 20180354;
        final String FIRST_NAME = "Mike";
        final String LAST_NAME = "Ehrmantraut";

        Faculty student = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty _student = response.getBody();
        assertThat(_student.getFacultyNumber()).isEqualTo(FACULTY_NUMBER);
        assertThat(_student.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(_student.getLastName()).isEqualTo(LAST_NAME);

        final String USERNAME = "FC-" + FACULTY_NUMBER;
//        String rawPassword = StringUtils.replaceChars(student.getFirstName() + student.getLastName(), " ", "");
//        final String PASSWORD = passwordEncoder.encode(passwordEncoder.encode(rawPassword));
        final String ROLE = "FACULTY";
        AppUser user = userDao.findByUsername(USERNAME);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
//        assertThat(user.getPasswordHash()).isEqualTo(PASSWORD);
        assertThat(user.getRole()).isEqualTo(ROLE);
    }

    @Test
    public void updateFaculty_accessDenied() {
        final int FACULTY_NUMBER = 20180354;
        final String FIRST_NAME = "Mike";
        final String LAST_NAME = "Ehrmantraut";

        Faculty student = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void updateFaculty_nonExistent_fail() {
        final int FACULTY_NUMBER = 20240005;
        final String FIRST_NAME = "Hank";
        final String LAST_NAME = "Schrader";

        Faculty faculty = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(faculty, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateFaculty_blankFirstName_fail() {
        final int FACULTY_NUMBER = 20180354;
        final String FIRST_NAME = "";
        final String LAST_NAME = "Fring";

        Faculty student = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "firstName should not be blank");
    }

    @Test
    public void updateFaculty_blankLastName_fail() {
        final int FACULTY_NUMBER = 20180354;
        final String FIRST_NAME = "Gus";
        final String LAST_NAME = "";

        Faculty student = new Faculty(FACULTY_NUMBER, FIRST_NAME, LAST_NAME);
        HttpEntity request = new HttpEntity<>(student, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "lastName should not be blank");
    }

    @Test
    public void deleteFaculty_success() {
        final int FACULTY_NUMBER = 20180354;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + FACULTY_NUMBER, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteFaculty_accessDenied() {
        final int FACULTY_NUMBER = 20180354;

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl + "/" + FACULTY_NUMBER, HttpMethod.DELETE, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void deleteFaculty_stillTeaching_fail() {
        final int FACULTY_NUMBER = 20160916;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + FACULTY_NUMBER, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void deleteFaculty_notFound() {
        final int FACULTY_NUMBER = 20241999;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + FACULTY_NUMBER, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
