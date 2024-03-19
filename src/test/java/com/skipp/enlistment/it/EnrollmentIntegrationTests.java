package com.skipp.enlistment.it;

import com.skipp.enlistment.domain.Enlistment;
import com.skipp.enlistment.dto.ErrorResponse;
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
import org.springframework.test.context.jdbc.Sql;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EnrollmentIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String endpointUrl;

    @BeforeEach
    public void setup() {
        endpointUrl = "http://localhost:" + port + "/enlist";
    }

    @Test
    public void enlist_unauthorized() {
        final int STUDENT_NUMBER = 20240003;
        final String SECTION_ID = "ES1MHXY";

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, e, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void faculty_accessDenied() {
        final int STUDENT_NUMBER = 20240003;
        final String SECTION_ID = "ES1MHXY";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("FC-20180354", "SaulGoodman", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void anotherStudent_accessDenied() {
        final int STUDENT_NUMBER = 20240003;
        final String SECTION_ID = "ES1MHXY";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "You cannot enlist for another student");
    }

    @Test
    public void shouldEnrollStudent() {
        final int STUDENT_NUMBER = 20240003;
        final String SECTION_ID = "ES1MHXY";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-" + STUDENT_NUMBER, "KimWexler", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<Enlistment> response = restTemplate.postForEntity(endpointUrl, request, Enlistment.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Enlistment _e = response.getBody();
        assertThat(_e.studentNumber()).isEqualTo(STUDENT_NUMBER);
        assertThat(_e.sectionId()).isEqualTo(SECTION_ID);
    }

    @Test
    public void alreadyEnlistedInSameSection() {
        final int STUDENT_NUMBER = 20240001;
        final String SECTION_ID = "ES1TFXY";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-" + STUDENT_NUMBER, "JessePinkman", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Enlisted more than once: " + SECTION_ID);
    }

    @Test
    public void alreadyEnlistedInSameSubject() {
        final int STUDENT_NUMBER = 20240001;
        final String SECTION_ID = "ES1MHXY";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-" + STUDENT_NUMBER, "JessePinkman", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Section " + SECTION_ID + " with subject ES 1 has same subject as currently enlisted section ES1TFXY");
    }

    @Test
    public void scheduleConflict() {
        final int STUDENT_NUMBER = 20240001;
        final String SECTION_ID = "PI100TFWX";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-" + STUDENT_NUMBER, "JessePinkman", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Schedule Conflict: -> This schedule: TF 14:30-17:30; Other schedule: TF 13:00-16:00");
    }

    @Test
    public void roomAtCapacity() {
        final int STUDENT_NUMBER = 20240002;
        final String SECTION_ID = "ES1TFXY";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-" + STUDENT_NUMBER, "HectorSalamanca", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Capacity Reached - enlistments: 1; capacity: 1");
    }

    @Test
    public void cancelEnlistment_success() {
        final int STUDENT_NUMBER = 20240001;
        final String SECTION_ID = "ES1TFXY";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-" + STUDENT_NUMBER, "JessePinkman", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void cancelEnlistment_nonExistentStudent() {
        final int STUDENT_NUMBER = 999;
        final String SECTION_ID = "ES1TFXY";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void cancelEnlistment_nonExistentSection() {
        final int STUDENT_NUMBER = 20240001;
        final String SECTION_ID = "INVALID";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-" + STUDENT_NUMBER, "JessePinkman", StandardCharsets.UTF_8);

        Enlistment e = new Enlistment(STUDENT_NUMBER, SECTION_ID);
        HttpEntity<Enlistment> request = new HttpEntity<>(e, headers);

        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
