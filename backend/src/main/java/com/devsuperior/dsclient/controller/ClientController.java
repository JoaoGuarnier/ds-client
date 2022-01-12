package com.devsuperior.dsclient.controller;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    private ResponseEntity<Page<ClientDto>> findAll(Pageable pageable) {
        Page<ClientDto> clients = clientService.findAll(pageable);
        return ResponseEntity.ok().body(clients);
    }


    @GetMapping("/{id}")
    private ResponseEntity<ClientDto> findById(@PathVariable Long id) {
        ClientDto clientDto = clientService.findById(id);
        return ResponseEntity.ok().body(clientDto);
    }


}
