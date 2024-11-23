package com.example.demo_park_api;


import com.example.demo_park_api.web.dto.ClientCreateDto;
import com.example.demo_park_api.web.dto.ClientResponseDto;
import com.example.demo_park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/clients/clients-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/clients/clients-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIt {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createClient_WithValidData_ReturnClientsAndStatus201() {
        ClientResponseDto responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rnferreira@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("Renan Ferreira", "81911895001"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Renan Ferreira");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("81911895001");
    }

    @Test
    public void createClient_WithCpfAlreadyRegistered_ReturnErrorMessageAndStatus409() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rnferreira@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("Renan Ferreira", "99106729010"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error status code should be 409")
                .isEqualTo(409);
    }

    @Test
    public void createClient_WithInvalidData_ReturnErrorMessageAndStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rnferreira@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error status code should be 422")
                .isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rnferreira@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("rafa", "00000000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error status code should be 422")
                .isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rnferreira@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("rafa", "000.000.000-00"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error status code should be 422")
                .isEqualTo(422);

    }

    @Test
    public void createClient_WithUserNoPermission_ReturnErrorMessageAndStatus403() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("Roberto Santos", "99106729010"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error status code should be 403")
                .isEqualTo(403);
    }

    @Test
    public void getById_WithValidIDByADMIN_ReturnClientAndStatus200() {
        ClientResponseDto responseBody = webTestClient
                .get()
                .uri("/api/v1/clients/5")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(5);
    }

    @Test
    public void getById_WithInvalidIDByADMIN_ReturnErrorMessageAndStatus404() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clients/0")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "rldias@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error status code should be 404")
                .isEqualTo(404);
    }

    @Test
    public void getById_WithValidIDByCLIENT_ReturnErrorMessageAndStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("/api/v1/clients/0")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "leafar@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error status code should be 403")
                .isEqualTo(403);
    }
}
