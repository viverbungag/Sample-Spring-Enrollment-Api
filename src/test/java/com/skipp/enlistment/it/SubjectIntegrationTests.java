package com.skipp.enlistment.it;

import com.skipp.enlistment.domain.Subject;
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
public class SubjectIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();
    private String endpointUrl;

    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setBasicAuth("FC-20160916", "WalterWhite", StandardCharsets.UTF_8);
        endpointUrl = "http://localhost:" + port + "/subjects";
    }

    @Test
    public void displayAllSubjects_unauthorized() {
        ResponseEntity<List> response = restTemplate.getForEntity(endpointUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void displayAllSubjects() {
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, request, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Subject> subjects = response.getBody();
        assertThat(subjects.size()).isEqualTo(4);
    }

    @Test
    public void createSubject_success() {
        final String SUBJECT_ID = "IT 100";

        Subject subject = new Subject(SUBJECT_ID);
        HttpEntity request = new HttpEntity<>(subject, headers);

        ResponseEntity<Subject> response = restTemplate.postForEntity(endpointUrl, request, Subject.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Subject _subject = response.getBody();
        assertThat(_subject.getSubjectId()).isEqualTo(SUBJECT_ID);
    }

    @Test
    public void createSubject_accessDenied() {
        final String SUBJECT_ID = "IT 100";

        Subject subject = new Subject(SUBJECT_ID);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(subject, headers);

        ResponseEntity<Subject> response = restTemplate.postForEntity(endpointUrl, request, Subject.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void createSubject_conflict() {
        final String SUBJECT_ID = "ES 1";

        Subject subject = new Subject(SUBJECT_ID);
        HttpEntity request = new HttpEntity<>(subject, headers);

        ResponseEntity<Subject> response = restTemplate.postForEntity(endpointUrl, request, Subject.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void deleteSubject_success() {
        final String SUBJECT_ID = "COMM 2";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + SUBJECT_ID, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteSubject_accessDenied() {
        final String SUBJECT_ID = "COMM 2";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + SUBJECT_ID, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void deleteSubject_stillUsedInSection_fail() {
        final String SUBJECT_ID = "ES 1";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + SUBJECT_ID, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void deleteSubject_notFound() {
        final String SUBJECT_ID = "STS";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + SUBJECT_ID, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
