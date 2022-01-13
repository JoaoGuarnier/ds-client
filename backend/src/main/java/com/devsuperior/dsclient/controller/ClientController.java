package com.devsuperior.dsclient.controller;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

    @PostMapping
    private ResponseEntity<ClientDto> save(@RequestBody ClientDto clientDto) {
        ClientDto dto = clientService.save(clientDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    private ResponseEntity<ClientDto> update(@PathVariable Long id, @RequestBody ClientDto dto) {
        ClientDto clientDto = clientService.update(id, dto);
        return ResponseEntity.ok().body(clientDto);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<ClientDto> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
