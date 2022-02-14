package com.devsuperior.dsclient.service;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.repository.ClientRepository;
import com.devsuperior.dsclient.service.exceptions.ClientNotFoundException;
import com.devsuperior.dsclient.service.exceptions.DatabaseException;
import com.devsuperior.dsclient.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ClientServiceIT {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private ClientDto clientDto;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1l;
        nonExistingId = 999l;
        countTotalProducts = 4l;
        clientDto = Factory.createClientDto();
    }

    @Test
    public void findByIdShouldReturnClientDtoWhenIdExists() {
        ClientDto clientDto = clientService.findById(existingId);
        Assertions.assertNotNull(clientDto);
    }

    @Test
    public void findByIdShouldThrowClientNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.findById(nonExistingId);
        });
    }

    @Test
    public void findAllShouldReturnPage() {
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<ClientDto> result = clientService.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("JOAO CARLOS", result.getContent().get(0).getName());
        Assertions.assertEquals("LEBRON JAMES", result.getContent().get(1).getName());
    }

    @Test
    public void saveShouldReturnClientDto() {
        ClientDto persistedClientDto = clientService.save(this.clientDto);
        Assertions.assertNotNull(persistedClientDto);
    }

    @Test
    public void updateShouldReturnClientDtoWhenIdExists() {
        ClientDto updatedClientDto = clientService.update(existingId, clientDto);
        Assertions.assertNotNull(updatedClientDto);
    }

    @Test
    public void updateShouldThrowClientNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.update(nonExistingId,clientDto);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            clientService.delete(existingId);
        });
    }

    @Test
    public void deleteShouldThrowClientNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
           clientService.delete(nonExistingId);
        });
    }


}
