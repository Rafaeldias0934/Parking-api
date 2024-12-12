package com.example.demo_park_api;


import com.example.demo_park_api.web.dto.ParkingSpotDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/spots/spots-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/spots/spots-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingSpotIt {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void CreateSpot_WithValidData_ReturnLocationAndStatus201() {
        webTestClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .bodyValue(new ParkingSpotDto("A-05", "AVAILABLE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void CreateSpot_WithCodeAlreadyExistent_ReturnErrorMessageAndStatus409() {
        webTestClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .bodyValue(new ParkingSpotDto("A-01", "AVAILABLE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spots")
                .jsonPath("message").isEqualTo("The parking spot with code 'A-01' is already in use");
    }

    @Test
    public void CreateSpot_WithInvalidsData_ReturnErrorMessageAndStatus422() {
        webTestClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .bodyValue(new ParkingSpotDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spots");

        webTestClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .bodyValue(new ParkingSpotDto("A-501", "OPEN"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spots");
    }

    @Test
    public void getSpot_WithCodeExistent_ReturnParkingSpotAndStatus200() {
        webTestClient
                .get()
                .uri("/api/v1/spots/{code}", "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("code").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("AVAILABLE");
    }

    @Test
    public void getSpot_WithCodeNonexistent_ReturnErrorMessageAndStatus404() {
        webTestClient
                .get()
                .uri("/api/v1/spots/{code}", "A-10")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/spots/A-10");
    }

    @Test
    public void CreateSpot_NoPermissionAccess_ReturnErrorMessageAndStatus403() {
        webTestClient
                .post()
                .uri("/api/v1/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "leafar@gmail.com", "123456"))
                .bodyValue(new ParkingSpotDto("A-05", "AVAILABLE"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spots");
    }

    @Test
    public void getSpot_NoPermissionAccess_ReturnErrorMessageAndStatus403() {
        webTestClient
                .get()
                .uri("/api/v1/spots/{code}", "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "leafar@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/spots/A-01");
    }

}

