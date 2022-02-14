package com.devsuperior.dsclient.controller;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.model.Client;
import com.devsuperior.dsclient.service.ClientService;
import com.devsuperior.dsclient.service.exceptions.ClientNotFoundException;
import com.devsuperior.dsclient.service.exceptions.DatabaseException;
import com.devsuperior.dsclient.tests.Factory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    private Long existingId;
    private Long nonExistingId;
    private Long dependingId;
    private Client client;
    private ClientDto clientDto;
    private PageImpl page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1l;
        nonExistingId = 999l;
        dependingId = 666l;
        client = Factory.createClient();
        clientDto = Factory.createClientDto();
        page = new PageImpl(List.of(client));

        doReturn(clientDto).when(clientService).findById(existingId);
        doThrow(ClientNotFoundException.class).when(clientService).findById(nonExistingId);

        doReturn(page).when(clientService).findAll(any());

        doReturn(clientDto).when(clientService).save(any());

        doNothing().when(clientService).delete(existingId);
        doThrow(ClientNotFoundException.class).when(clientService).delete(nonExistingId);
        doThrow(DatabaseException.class).when(clientService).delete(dependingId);

        doReturn(clientDto).when(clientService).update(eq(existingId), any());
        doThrow(ClientNotFoundException.class).when(clientService).update(eq(nonExistingId), any());

    }

    @Test
    public void findByIdShouldReturnIsOkWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/clients/{id}", existingId));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void findByIdShouldReturnIsNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/clients/{id}", nonExistingId));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnIsOk() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/clients"));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void saveShouldReturnIsCreated() throws Exception {
        String clientDtoJson = objectMapper.writeValueAsString(clientDto);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/clients").content(clientDtoJson).contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void deleteShouldReturnIsNoContentWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", existingId));
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnIsNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", nonExistingId));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnIsOkWhenIdExists() throws Exception {
        String clientDtoJson = objectMapper.writeValueAsString(clientDto);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/clients/{id}", existingId)
                .content(clientDtoJson).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void updateShouldReturnIsNotFoundWhenIdDoesNotExist() throws Exception {
        String clientDtoJson = objectMapper.writeValueAsString(clientDto);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/clients/{id}", nonExistingId)
                .content(clientDtoJson).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }



}
