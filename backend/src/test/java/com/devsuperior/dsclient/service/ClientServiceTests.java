package com.devsuperior.dsclient.service;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.model.Client;
import com.devsuperior.dsclient.repository.ClientRepository;
import com.devsuperior.dsclient.service.exceptions.ClientNotFoundException;
import com.devsuperior.dsclient.service.exceptions.DatabaseException;
import com.devsuperior.dsclient.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ClientServiceTests {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependingId;
    private Client client;
    private ClientDto clientDto;
    private PageImpl<Client> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1l;
        nonExistingId = 999l;
        dependingId = 3l;
        client = Factory.createClient();
        clientDto = Factory.createClientDto();
        page = new PageImpl<>(List.of(client));

        doReturn(Optional.of(client)).when(clientRepository).findById(existingId);
        doReturn(Optional.empty()).when(clientRepository).findById(nonExistingId);

        doReturn(page).when(clientRepository).findAll((Pageable) any());

        doReturn(client).when(clientRepository).save(any());

        doReturn(client).when(clientRepository).getById(existingId);
        doThrow(EntityNotFoundException.class).when(clientRepository).getById(nonExistingId);

        doNothing().when(clientRepository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(clientRepository).deleteById(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(clientRepository).deleteById(dependingId);

    }

    @Test
    public void findByIdShouldReturnClientDtoWhenIdExists() {
        ClientDto clientDto = clientService.findById(existingId);
        Assertions.assertNotNull(clientDto);
        verify(clientRepository,times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowClientNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
           clientService.findById(nonExistingId);
        });
        verify(clientRepository,times(1)).findById(nonExistingId);
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0,10);
        Page<ClientDto> result = clientService.findAll(pageable);
        Assertions.assertNotNull(result);
        verify(clientRepository, times(1)).findAll(pageable);
    }

    @Test
    public void saveShouldReturnClientDto() {
        ClientDto persistedClientDto = clientService.save(clientDto);
        Assertions.assertNotNull(persistedClientDto);
    }

    @Test
    public void updateShouldReturnClientDtoWhenIdExist() {
        ClientDto persistedClientDto = clientService.update(existingId,  clientDto);
        Assertions.assertNotNull(persistedClientDto);
    }

    @Test
    public void updateShouldThrowClientNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
           clientService.update(nonExistingId, clientDto);
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

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependingId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            clientService.delete(dependingId);
        });
    }

}
