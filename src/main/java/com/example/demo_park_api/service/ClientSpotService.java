package com.example.demo_park_api.service;

import com.example.demo_park_api.entity.ClientSpot;
import com.example.demo_park_api.repositories.ClientSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientSpotService {

    private final ClientSpotRepository clientSpotRepository;

    @Transactional
    public ClientSpot save(ClientSpot clientSpot) {
        return clientSpotRepository.save(clientSpot);
    }
}
