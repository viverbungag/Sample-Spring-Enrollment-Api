package com.skipp.enlistment.it;

import com.skipp.enlistment.domain.Room;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RoomIntegrationTests {

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
        headers.setContentType(MediaType.APPLICATION_JSON);
        endpointUrl = "http://localhost:" + port + "/rooms";
    }

    @Test
    public void displayAllRooms_unauthorized() {
        ResponseEntity<List> response = restTemplate.getForEntity(endpointUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void displayAllRooms() {
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, request, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Room> subjects = response.getBody();
        assertThat(subjects.size()).isEqualTo(3);
    }

    @Test
    public void createRoom_success() {
        final String ROOM_NAME = "Theater";
        final int CAPACITY = 100;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<Room> response = restTemplate.postForEntity(endpointUrl, request, Room.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Room _room = response.getBody();
        assertThat(_room.getName()).isEqualTo(ROOM_NAME);
        assertThat(_room.getCapacity()).isEqualTo(CAPACITY);
    }

    @Test
    public void createRoom_accessDenied() {
        final String ROOM_NAME = "Theater";
        final int CAPACITY = 100;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void createRoom_blankName_fail() {
        final String ROOM_NAME = "";
        final int CAPACITY = -1;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "name should not be blank");
    }

    @Test
    public void createRoom_invalidCapacity_fail() {
        final String ROOM_NAME = "PH101";
        final int CAPACITY = -1;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(endpointUrl, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "capacity must be non-negative, was " + CAPACITY);
    }

    @Test
    public void createRoom_conflict() {
        final String ROOM_NAME = "PH102";
        final int CAPACITY = 50;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<Room> response = restTemplate.postForEntity(endpointUrl, request, Room.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void updateRoom_success() {
        final String ROOM_NAME = "MH204";
        final int CAPACITY = 20;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<Room> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, Room.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Room _room = response.getBody();
        assertThat(_room.getName()).isEqualTo(ROOM_NAME);
        assertThat(_room.getCapacity()).isEqualTo(CAPACITY);
    }

    @Test
    public void updateRoom_accessDenied() {
        final String ROOM_NAME = "MH204";
        final int CAPACITY = 20;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void updateRoom_nonExistent_fail() {
        final String ROOM_NAME = "AS101";
        final int CAPACITY = 20;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<Room> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, Room.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateRoom_invalidCapacity_fail() {
        final String ROOM_NAME = "NEC310";
        final int CAPACITY = -1;

        Room room = new Room(ROOM_NAME, CAPACITY);
        HttpEntity request = new HttpEntity<>(room, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl, HttpMethod.PUT, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "capacity must be non-negative, was " + CAPACITY);
    }

    @Test
    public void deleteRoom_success() {
        final String ROOM_NAME = "NEC310";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + ROOM_NAME, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteRoom_accessDenied() {
        final String ROOM_NAME = "NEC310";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("ST-20240001", "JessePinkman", StandardCharsets.UTF_8);
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl + "/" + ROOM_NAME, HttpMethod.DELETE, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message", "Access Denied");
    }

    @Test
    public void deleteRoom_stillUsedInSection_fail() {
        final String ROOM_NAME = "PH102";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(endpointUrl + "/" + ROOM_NAME, HttpMethod.DELETE, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).hasFieldOrPropertyWithValue("message",
                "Room " + ROOM_NAME + " is still being used.");
    }

    @Test
    public void deleteRoom_notFound() {
        final String ROOM_NAME = "AS101";

        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(endpointUrl + "/" + ROOM_NAME, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
