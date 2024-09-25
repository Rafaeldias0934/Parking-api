package com.example.demo_park_api.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto {

    private String id;
    private String username;
    private String role;
}
