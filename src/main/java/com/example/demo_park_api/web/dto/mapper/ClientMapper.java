package com.example.demo_park_api.web.dto.mapper;

import com.example.demo_park_api.entity.Client;
import com.example.demo_park_api.web.dto.ClientCreateDto;
import com.example.demo_park_api.web.dto.ClientResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {
    public static Client toClient(ClientCreateDto clientCreateDto) {
        return new ModelMapper().map(clientCreateDto, Client.class);
    }

    public static ClientResponseDto toDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }
}
