package com.example.demo_park_api;

import com.example.demo_park_api.jwt.JwtToken;
import com.example.demo_park_api.web.dto.UserSignInDto;
import com.example.demo_park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationIt {

    @Autowired
    WebTestClient testClient;

    @Test
    public void  authenticateWithValidCredentials_ShouldReturnTokenAndStatus200() {
        JwtToken responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSignInDto("rldias@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).as("JWT token should not be null")
                                                                .isNotNull();
    }

    @Test
    public void  authenticateWithInvalidCredentials_ShouldReturnErrorMessageAndStatus400() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSignInDto("wrong_email@gmail.com", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).as("Error message should not be null")
                .isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error message should be 400")
                .isEqualTo(400);

        responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSignInDto("rldias@gmail.com", "000000"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).as("Error message should not be null")
                .isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error message should be 400")
                .isEqualTo(400);

    }

    @Test
    public void  authenticateWithInvalidUsername_ShouldReturnErrorMessageAndStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSignInDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).as("Error response body should not be null")
                .isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error message should be 422")
                .isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSignInDto("@gmail.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).as("Error response body should not be null")
                .isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error message should be 422")
                .isEqualTo(422);
    }

    @Test
    public void  authenticateWithInvalidPassword_ShouldReturnErrorMessageAndStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSignInDto("rldias@gmail.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).as("Error response body should not be null")
                .isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error message should be 422")
                .isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSignInDto("rldias@gmail.com", "123"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).as("Error response body should not be null")
                .isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error message should be 422")
                .isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSignInDto("rldias@gmail.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).as("Error response body should not be null")
                .isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus())
                .as("Error message should be 422")
                .isEqualTo(422);
    }
}
