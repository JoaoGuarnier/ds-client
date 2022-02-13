package com.devsuperior.dsclient.repository;

import com.devsuperior.dsclient.model.Client;
import com.devsuperior.dsclient.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long totalClients;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1l;
        nonExistingId = 999l;
        totalClients =4l;
    }

    @Test
    public void findByIdShouldReturnClientWhenIdExists() {
        Optional<Client> optionalClient = clientRepository.findById(existingId);
        Assertions.assertTrue(optionalClient.isPresent());
    }

    @Test
    public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
        Optional<Client> optionalClient = clientRepository.findById(nonExistingId);
        Assertions.assertFalse(optionalClient.isPresent());
    }

    @Test
    public void deleteShouldDeleteEntityWhenIdExists() {
        clientRepository.deleteById(existingId);
        Optional<Client> optionalClient = clientRepository.findById(existingId);
        Assertions.assertFalse(optionalClient.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
           clientRepository.deleteById(nonExistingId);
        });
    }

    @Test
    public void saveShouldPersistEntityWhenIdIsNull() {
        Client client = Factory.createClient();
        client.setId(null);

        Client persistedClient = clientRepository.save(client);
        Assertions.assertNotNull(persistedClient.getId());
    }

    @Test
    public void saveShouldAutoIncrementWhenPersistAnEntity() {
        Client client = Factory.createClient();
        client.setId(null);

        Client persistedClient = clientRepository.save(client);
        Assertions.assertEquals(totalClients + 1, persistedClient.getId());
    }

}
