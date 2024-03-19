package com.skipp.enlistment.it;

import com.skipp.enlistment.domain.Section;
import com.skipp.enlistment.dto.SectionDto;
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
public class SectionIntegrationTests {

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
        endpointUrl = "http://localhost:" + port + "/sections";
    }

    @Test
    public void displayAllSections_unauthorized() {
        ResponseEntity<List> response = restTemplate.getForEntity(endpointUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void displayAllSections() {
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, request, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Section> faculty = response.getBody();
        assertThat(faculty.size()).isEqualTo(4);
    }

    @Test
    public void displayOneSection() {
        final String SECTION_ID = "ES1TFXY";
        final String SUBJECT_ID = "ES 1";
        final String SCHEDULE = "TF 14:30-17:30";
        final String ROOM_NAME = "MH204";
        final int FACULTY_NUMBER = 20160916;

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<SectionDto> response = restTemplate.exchange(endpointUrl + "/" + SECTION_ID, HttpMethod.GET, request, SectionDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        SectionDto section = response.getBody();
        assertThat(section.getSectionId()).isEqualTo(SECTION_ID);
        assertThat(section.getSubjectId()).isEqualTo(SUBJECT_ID);
        assertThat(section.getSchedule()).isEqualTo(SCHEDULE);
        assertThat(section.getRoomName()).isEqualTo(ROOM_NAME);
        assertThat(section.getFacultyNumber()).isEqualTo(FACULTY_NUMBER);
    }

    @Test
    public void displayNonExistentSection() {
        final String SECTION_ID = "INVALID";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<SectionDto> response = restTemplate.exchange(endpointUrl + "/" + SECTION_ID, HttpMethod.GET, request, SectionDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createSection_success() {
        final String SECTION_ID = "COMM2MHU1";
        final String SUBJECT_ID = "COMM 2";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<SectionDto> response = restTemplate.postForEntity(endpointUrl, request, SectionDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        SectionDto _section = response.getBody();
        assertThat(_section.getSectionId()).isEqualTo(SECTION_ID);
        assertThat(_section.getSubjectId()).isEqualTo(SUBJECT_ID);
        assertThat(_section.getSchedule()).isEqualTo(SCHEDULE);
        assertThat(_section.getRoomName()).isEqualTo(ROOM_NAME);
        assertThat(_section.getFacultyNumber()).isEqualTo(FACULTY_NUMBER);
    }

    @Test
    public void createSection_accessDenied() {
        final String SECTION_ID = "COMM2MHU1";
        final String SUBJECT_ID = "COMM 2";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void createSection_duplicateSectionId_fail() {
        final String SECTION_ID = "KAS1MHR1";
        final String SUBJECT_ID = "KAS 1";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Section " + SECTION_ID + " already exists.");
    }

    @Test
    public void createSection_invalidSectionId_fail() {
        final String SECTION_ID = "KAS1-MHR1";
        final String SUBJECT_ID = "KAS 1";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "sectionId should be alphanumeric, was " + SECTION_ID);
    }

    @Test
    public void createSection_nonExistentSubject_fail() {
        final String SECTION_ID = "STSMHU1";
        final String SUBJECT_ID = "STS";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void createSection_invalidScheduleDay_fail() {
        final String SECTION_ID = "COMM2MHU1";
        final String SUBJECT_ID = "COMM 2";
        final String SCHEDULE = "X 1-2";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Invalid format for schedule. Should be: [MTH|TF|WS] HH:mm-HH:mm");
    }

    @Test
    public void createSection_invalidScheduleTime_fail() {
        final String SECTION_ID = "COMM2MHU1";
        final String SUBJECT_ID = "COMM 2";
        final String SCHEDULE = "MTH 1-2";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Invalid format for schedule. Should be: [MTH|TF|WS] HH:mm-HH:mm");
    }

    @Test
    public void createSection_nonExistentRoom_fail() {
        final String SECTION_ID = "COMM2MHU1";
        final String SUBJECT_ID = "COMM 2";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "AS101";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void createSection_nonExistentFaculty_fail() {
        final String SECTION_ID = "COMM2MHU1";
        final String SUBJECT_ID = "COMM 2";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "NEC310";
        final int FACULTY_NUMBER = 999;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void createSection_facultyConflict_fail() {
        final String SECTION_ID = "COMM2MHU1";
        final String SUBJECT_ID = "COMM 2";
        final String SCHEDULE = "MTH 08:30-10:00"; // in conflict w/ KAS1MHR1
        final String ROOM_NAME = "NEC310";
        final int FACULTY_NUMBER = 20160916;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Faculty Walter White FN#20160916 has a schedule overlap between new section COMM2MHU1 with schedule " +
                        "MTH 08:30-10:00 and current section KAS1MHR1 with schedule MTH 08:30-10:00.");
    }

    @Test
    public void createSection_roomConflict_fail() {
        final String SECTION_ID = "COMM2MHU1";
        final String SUBJECT_ID = "COMM 2";
        final String SCHEDULE = "TF 14:30-17:30";
        final String ROOM_NAME = "MH204";
        final int FACULTY_NUMBER = 20180354;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Room MH204 has a schedule overlap between new section COMM2MHU1 with schedule " +
                        "TF 14:30-17:30 and current section ES1TFXY with schedule TF 14:30-17:30.");
    }

    @Test
    public void updateSection_success() {
        final String SECTION_ID = "KAS1MHR1";
        final String SUBJECT_ID = "KAS 1";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "NEC310";
        final int FACULTY_NUMBER = 20160916;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<SectionDto> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, SectionDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        SectionDto _section = response.getBody();
        assertThat(_section.getSectionId()).isEqualTo(SECTION_ID);
        assertThat(_section.getSubjectId()).isEqualTo(SUBJECT_ID);
        assertThat(_section.getSchedule()).isEqualTo(SCHEDULE);
        assertThat(_section.getRoomName()).isEqualTo(ROOM_NAME);
        assertThat(_section.getFacultyNumber()).isEqualTo(FACULTY_NUMBER);
    }

    @Test
    public void updateSection_accessDenied() {
        final String SECTION_ID = "KAS1MHR1";
        final String SUBJECT_ID = "KAS 1";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "NEC310";
        final int FACULTY_NUMBER = 20160916;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void updateSection_nonExistent_fail() {
        final String SECTION_ID = "INVALID";
        final String SUBJECT_ID = "KAS 1";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "NEC310";
        final int FACULTY_NUMBER = 20160916;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateSection_invalidScheduleDay_fail() {
        final String SECTION_ID = "KAS1MHR1";
        final String SUBJECT_ID = "KAS 1";
        final String SCHEDULE = "X 10:00-11:30";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20160916;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Invalid format for schedule. Should be: [MTH|TF|WS] HH:mm-HH:mm");
    }

    @Test
    public void updateSection_invalidScheduleTime_fail() {
        final String SECTION_ID = "KAS1MHR1";
        final String SUBJECT_ID = "KAS 1";
        final String SCHEDULE = "MTH 10-12";
        final String ROOM_NAME = "PH102";
        final int FACULTY_NUMBER = 20160916;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Invalid format for schedule. Should be: [MTH|TF|WS] HH:mm-HH:mm");
    }

    @Test
    public void updateSection_nonExistentRoom_fail() {
        final String SECTION_ID = "KAS1MHR1";
        final String SUBJECT_ID = "KAS 1";
        final String SCHEDULE = "MTH 10:00-11:30";
        final String ROOM_NAME = "AS101";
        final int FACULTY_NUMBER = 20160916;

        SectionDto section = new SectionDto();
        section.setSectionId(SECTION_ID);
        section.setSubjectId(SUBJECT_ID);
        section.setSchedule(SCHEDULE);
        section.setRoomName(ROOM_NAME);
        section.setFacultyNumber(FACULTY_NUMBER);
        HttpEntity request = new HttpEntity<>(section, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void deleteSection_success() {
        final String SECTION_ID = "KAS1MHR1";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + SECTION_ID, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteSection_accessDenied() {
        final String SECTION_ID = "KAS1MHR1";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl + "/" + SECTION_ID, HttpMethod.DELETE, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void deleteSection_studentsStillEnrolled_fail() {
        final String SECTION_ID = "ES1TFXY";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + SECTION_ID, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void deleteSection_notFound() {
        final String SECTION_ID = "INVALID";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + SECTION_ID, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
