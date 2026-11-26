package com.project.test.srj.controller;

import com.project.test.srj.dto.ClientRequest;
import com.project.test.srj.model.Client;
import com.project.test.srj.service.ClientServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClientController {
    private final ClientServiceImpl clientService;

    public ClientController(ClientServiceImpl clientservice) {
        this.clientService = clientservice;
    }

    @PostMapping
    public ResponseEntity<Client> crear(@Valid @RequestBody ClientRequest clientRequest) {
        Client creado = clientService.create(clientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}
