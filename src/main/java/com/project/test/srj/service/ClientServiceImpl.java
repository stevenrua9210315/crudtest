package com.project.test.srj.service;

import com.project.test.srj.dto.ClientRequest;
import com.project.test.srj.model.Client;
import com.project.test.srj.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client create(ClientRequest request) {
        Client client = new Client();
        client.setNombre(request.getNombre());
        client.setApellido(request.getApellido());
        client.setDireccion(request.getDireccion());

        return clientRepository.save(client);
    }
}
