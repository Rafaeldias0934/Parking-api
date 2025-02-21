package com.example.demo_park_api.service;

import com.example.demo_park_api.entity.Client;
import com.example.demo_park_api.exception.CpfUniqueViolationException;
import com.example.demo_park_api.exception.EntityNotFoundException;
import com.example.demo_park_api.repositories.ClientRepository;
import com.example.demo_park_api.repositories.projection.ClientProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client toSave(Client client) {
        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                    String.format("CPF '%s' can't be registered, already exists on the system", client.getCpf())
            );
        }
    }

    @Transactional(readOnly = true)
    public Client getById(Long id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Client id=%s not found in the system", id)
                ));
    }

    @Transactional(readOnly = true)
    public Page<ClientProjection> getAll(Pageable pageable) {
        return clientRepository.findAllPageable(pageable);

    }

    @Transactional(readOnly = true)
    public Client getByUserId(Long id) {
        return clientRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public Client getByCpf(String cpf) {
        return clientRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException(String.format("Client with '%s', not found", cpf))
        );
    }
}
