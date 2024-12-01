package com.example.demo_park_api.web.dto.mapper;

import com.example.demo_park_api.entity.ParkingSpots;
import com.example.demo_park_api.web.dto.ParkingSpotDto;
import com.example.demo_park_api.web.dto.SpotResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpotMapper {

    public static ParkingSpots toSpot(ParkingSpotDto parkingSpotDto) {
        return new ModelMapper().map(parkingSpotDto, ParkingSpots.class);
    }

    public static SpotResponseDto spotResponseDto(ParkingSpots parkingSpots) {
        return new ModelMapper().map(parkingSpots, SpotResponseDto.class);
    }
}
