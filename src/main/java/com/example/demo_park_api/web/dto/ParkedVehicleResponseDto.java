package com.example.demo_park_api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParkedVehicleResponseDto {

    private String plate;
    private String brand;
    private String model;
    private String color;
    private String clientCpf;
    private String receipt;
    private LocalDate entryDate;
    private LocalDateTime exitDate;
    private BigDecimal value;
    private BigDecimal discount;
}
