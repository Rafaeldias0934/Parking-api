package com.example.demo_park_api.service;

import com.example.demo_park_api.entity.Client;
import com.example.demo_park_api.exception.CpfUniqueViolationException;
import com.example.demo_park_api.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client toSave(Client client) {
        try {
            return  clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                    String.format("CPF '%s' can't be registered, already exists on the system", client.getCpf())
            );
        }
    }
}
