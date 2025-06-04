package com.example.demo_park_api.service;

import com.example.demo_park_api.entity.ClientSpot;
import com.example.demo_park_api.exception.EntityNotFoundException;
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

    @Transactional(readOnly = true)
    public ClientSpot getByReceipt(String receipt) {
        return clientSpotRepository.findByReceiptAndExitDateIsNull(receipt).orElseThrow(
                ()-> new EntityNotFoundException(
                        String.format("Receipt '%s', not found in the system or check-out already done", receipt)
                )
        );
    }
}
