package com.example.demo_park_api.web.dto.mapper;

import com.example.demo_park_api.entity.ClientSpot;
import com.example.demo_park_api.web.dto.ParkedVehicleCreateDto;
import com.example.demo_park_api.web.dto.ParkedVehicleResponseDto;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSpotMapper {
    public static ClientSpot toParkedVehicleCreateDto (ParkedVehicleCreateDto parkedVehicleCreateDto) {

        return new ModelMapper().map(parkedVehicleCreateDto, ClientSpot.class);
    }
    
    public static ParkedVehicleResponseDto toParkedVehicleResponseDto(ClientSpot clientSpot) {

        return new ModelMapper().map(clientSpot, ParkedVehicleResponseDto.class);
    }

}
