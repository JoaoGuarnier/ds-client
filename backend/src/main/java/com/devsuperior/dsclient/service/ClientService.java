package com.devsuperior.dsclient.service;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.model.Client;
import com.devsuperior.dsclient.repository.ClientRepository;
import com.devsuperior.dsclient.service.exceptions.ClientNotFoundException;
import com.devsuperior.dsclient.service.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    @Transactional
    public ClientDto save(ClientDto clientDto) {
        Client entity = new Client();
        convertDtoToEntity(clientDto,entity);
        Client entitySaved = clientRepository.save(entity);
        return new ClientDto(entitySaved);
    }
    
    @Transactional
    public ClientDto update(Long id, ClientDto dto) {
        try {
            Client entity = clientRepository.getById(id);
            convertDtoToEntity(dto,entity);
            Client clientSaved = clientRepository.save(entity);
            return new ClientDto(clientSaved);
        } catch (EntityNotFoundException e) {
            throw new ClientNotFoundException("Id not found " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            clientRepository.deleteById(id);
        }  catch (EmptyResultDataAccessException e) {
            throw new DatabaseException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }

    }



    private void convertDtoToEntity(ClientDto clientDto, Client entity) {
        entity.setCpf(clientDto.getCpf());
        entity.setBirthDate(clientDto.getBirthDate());
        entity.setIncome(clientDto.getIncome());
        entity.setName(clientDto.getName());
        entity.setChildren(clientDto.getChildren());
    }
}
