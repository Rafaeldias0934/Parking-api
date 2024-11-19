package com.example.demo_park_api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class ClientResponseDto {
    private Long id;
    private String name;
    private String cpf;
}
