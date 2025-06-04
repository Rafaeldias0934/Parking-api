package com.example.demo_park_api;


import com.example.demo_park_api.web.dto.ParkedVehicleCreateDto;

import io.jsonwebtoken.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/parking/parking-delete.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/parking/parking-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

public class ParkingIt {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCheckIn_WithInvalidsData_ReturnCreatedAndLocation() {
        ParkedVehicleCreateDto createDto = ParkedVehicleCreateDto.builder()
                .plate("WAR-1111").brand("FIAT").model("PALIO 2.0")
                .color("BLUE").clientCpf("00992999090")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("plate").isEqualTo("WAR-1111")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO 2.0")
                .jsonPath("color").isEqualTo("BLUE")
                .jsonPath("clientCpf").isEqualTo("00992999090")
                .jsonPath("receipt").exists()
                .jsonPath("entryDate").exists()
                .jsonPath("spotCode").exists();
    }

    @Test
    public void createCheckIn_WithClientProfile_ReturnErrorStatus403() {
        ParkedVehicleCreateDto createDto = ParkedVehicleCreateDto.builder()
                .plate("WAR-1111").brand("FIAT").model("PALIO 2.0")
                .color("BLUE").clientCpf("00992999090")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "leafar@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_WithInvalidsData_ReturnErrorStatus422() {
        ParkedVehicleCreateDto createDto = ParkedVehicleCreateDto.builder()
                .plate("").brand("FIAT").model("")
                .color("").clientCpf("")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "leafar@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_WithCPFNotFound_ReturnErrorStatus404() {
        ParkedVehicleCreateDto createDto = ParkedVehicleCreateDto.builder()
                .plate("WAR-1111").brand("FIAT").model("PALIO 2.0")
                .color("BLUE").clientCpf("36175707044")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Sql(scripts = "/sql/users/parking/parking-delete-spots-occupieds.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/users/parking/parking-insert-spots-occupieds.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void createCheckIn_WithSpotsOccupieds_ReturnErrorStatus404() {
        ParkedVehicleCreateDto createDto = ParkedVehicleCreateDto.builder()
                .plate("WAR-1111").brand("FIAT").model("PALIO 2.0")
                .color("BLUE").clientCpf("00992999090")
                .build();

        testClient.post().uri("/api/v1/parking/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@gmail.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void getCheckIn_WithProfileAdmin_ReturnDataStatus200() {

        testClient.get()
                .uri("/api/v1/parking/check-in/{receipt}", "20250311-100000")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "admin@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-8021")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("YAMAHA")
                .jsonPath("color").isEqualTo("RED")
                .jsonPath("clientCpf").isEqualTo("99106729010")
                .jsonPath("receipt").isEqualTo("20250311-100000")
                .jsonPath("entryDate").isEqualTo("2025-03-11 09:57:00")
                .jsonPath("spotCode").isEqualTo("A-01");
    }

    @Test
    public void getCheckIn_WithProfileClient_ReturnDataStatus200() {

        testClient.get()
                .uri("/api/v1/parking/check-in/{receipt}", "20250311-100000")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "rralmeida@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-8021")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("YAMAHA")
                .jsonPath("color").isEqualTo("RED")
                .jsonPath("clientCpf").isEqualTo("99106729010")
                .jsonPath("receipt").isEqualTo("20250311-100000")
                .jsonPath("entryDate").isEqualTo("2025-03-11 09:57:00")
                .jsonPath("spotCode").isEqualTo("A-01");
    }

    @Test
    public void getCheckIn_WithNonExistentReceipt_ReturnErrorStatus404() {

        testClient.get()
                .uri("/api/v1/parking/check-in/{receipt}", "20250311-999999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "rralmeida@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parking/check-in/20250311-999999")
                .jsonPath("method").isEqualTo("GET");
    }
}
