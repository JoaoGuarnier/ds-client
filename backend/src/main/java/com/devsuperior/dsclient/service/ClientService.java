package com.devsuperior.dsclient.service;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.model.Client;
import com.devsuperior.dsclient.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;


    @Transactional(readOnly = true)
    public Page<ClientDto> findAll(Pageable pageable) {
        Page<ClientDto> clientsDto = clientRepository.findAll(pageable).map(ClientDto::new);
        return clientsDto;
    }

}
