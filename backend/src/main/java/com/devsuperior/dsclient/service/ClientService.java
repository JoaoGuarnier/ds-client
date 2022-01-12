package com.devsuperior.dsclient.service;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.model.Client;
import com.devsuperior.dsclient.repository.ClientRepository;
import com.devsuperior.dsclient.service.exceptions.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;


    @Transactional(readOnly = true)
    public Page<ClientDto> findAll(Pageable pageable) {
        Page<ClientDto> clientsDto = clientRepository.findAll(pageable).map(ClientDto::new);
        return clientsDto;
    }

    @Transactional(readOnly = true)
    public ClientDto findById(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        Client entity = optionalClient.orElseThrow(() -> new ClientNotFoundException("Id não encontrado"));
        ClientDto clientDto = new ClientDto(entity);
        return clientDto;
    }

}
