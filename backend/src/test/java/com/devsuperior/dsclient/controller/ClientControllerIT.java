package com.devsuperior.dsclient.controller;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1l;
        nonExistingId = 999l;
        countTotalProducts = 4l;
    }

    @Test
    public void findAllShouldReturnIsOk() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/clients"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
    }

    @Test
    public void findByIdShouldReturnIsOkWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/clients/{id}", existingId));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.name").value("JOAO CARLOS"));
    }

    @Test
    public void findByIdShouldReturnIsNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/clients/{id}", nonExistingId));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void saveShouldReturnIsCreated() throws Exception{
        ClientDto clientDto = Factory.createClientDto();
        String clientDtoJson = objectMapper.writeValueAsString(clientDto);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/clients")
                .content(clientDtoJson).contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").value(5));
    }

    @Test
    public void updateShouldReturnIsOkWhenIdExists() throws Exception {
        ClientDto clientDto = Factory.createClientDto();

        Long expectedId = clientDto.getId();
        String expectedName = clientDto.getName();

        String clientDtoJson = objectMapper.writeValueAsString(clientDto);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/clients/{id}", existingId)
                .content(clientDtoJson).contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(expectedId));
        resultActions.andExpect(jsonPath("$.name").value(expectedName));

    }

    @Test
    public void updateShouldReturnIsNotFoundWhenIdDoesNotExist() throws Exception {
        ClientDto clientDto = Factory.createClientDto();
        String clientDtoJson = objectMapper.writeValueAsString(clientDto);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/clients/{id}", nonExistingId)
                .content(clientDtoJson).contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());

    }

    @Test
    public void deleteShouldReturnIsNotContentWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", existingId));
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnIsNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/clients/{id}", nonExistingId));
        resultActions.andExpect(status().isNotFound());
    }

}
