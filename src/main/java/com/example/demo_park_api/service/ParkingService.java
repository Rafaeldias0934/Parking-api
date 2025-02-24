package com.example.demo_park_api.service;

import com.example.demo_park_api.Utils.ParkingUtils;
import com.example.demo_park_api.entity.Client;
import com.example.demo_park_api.entity.ClientSpot;
import com.example.demo_park_api.entity.ParkingSpots;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ClientSpotService clientSpotService;
    private final ClientService clientService;
    private final SpotService spotService;

    @Transactional
    public ClientSpot checkIn(ClientSpot clientSpot) {
        Client client = clientService.getByCpf(clientSpot.getClient().getCpf());
        clientSpot.setClient(client);

        ParkingSpots parkingSpots = spotService.getBySpotAvailable();
        parkingSpots.setSpotStatus(ParkingSpots.SpotStatus.OCCUPIED);
        clientSpot.setCodeParkingSpots(parkingSpots);

        clientSpot.setEntryDate(LocalDateTime.now());
        clientSpot.setReceipt(ParkingUtils.generateReceipt());

        return clientSpotService.save(clientSpot);
    }
}
