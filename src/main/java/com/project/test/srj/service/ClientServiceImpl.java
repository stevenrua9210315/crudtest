package com.project.test.srj.service;

import com.project.test.srj.dto.ClientRequest;
import com.project.test.srj.exception.ClienteNoEncontradoException;
import com.project.test.srj.exception.ErrorDbException;
import com.project.test.srj.model.Client;
import com.project.test.srj.repository.ClientRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client create(ClientRequest request) {
        try {
            Client client = new Client();
            client.setNombre(request.getNombre().trim());
            client.setApellido(request.getApellido().trim());
            client.setDireccion(request.getDireccion().trim());
            return clientRepository.save(client);
        } catch (DataAccessException ex) {
            log.error("Fallo al conectar o escribir en SQL Server al crear cliente", ex);
            throw new ErrorDbException(
                    "DB_001",
                    "No se pudo registrar el cliente porque no hay conexión con la base de datos.",
                    "ClientServiceImpl.create",
                    ex
            );
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!clientRepository.existsById(id)) {
                throw new ClienteNoEncontradoException(
                        "CLI_404",
                        "No se encontró un cliente con el id " + id,
                        "ClientServiceImpl.deleteById",
                        null
                );
            }
            clientRepository.deleteById(id);
        } catch (DataAccessException ex) {
            log.error("Error al eliminar el cliente con id={}", id, ex);
            throw new ErrorDbException(
                    "DB_002",
                    "No se pudo eliminar el cliente por un problema con la base de datos.",
                    "ClientServiceImpl.deleteById",
                    ex
            );
        }
    }
}
