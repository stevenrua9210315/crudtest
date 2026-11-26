package com.project.test.srj.service;

import com.project.test.srj.dto.ClientRequest;
import com.project.test.srj.model.Client;

public interface ClientService {

    Client create(ClientRequest request);
}
