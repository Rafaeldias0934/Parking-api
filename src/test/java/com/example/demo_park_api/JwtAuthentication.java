package com.example.demo_park_api;

import com.example.demo_park_api.jwt.JwtToken;
import com.example.demo_park_api.web.dto.UserSignInDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient webTestClient, String username, String password){
        String token = webTestClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UserSignInDto(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();
        return httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
