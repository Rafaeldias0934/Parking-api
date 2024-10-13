package com.example.demo_park_api.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPasswordDto {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
