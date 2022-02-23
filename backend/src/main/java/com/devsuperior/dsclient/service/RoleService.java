package com.devsuperior.dsclient.service;


import com.devsuperior.dsclient.dto.RoleDto;
import com.devsuperior.dsclient.model.Role;
import com.devsuperior.dsclient.repository.RoleRepository;
import com.devsuperior.dsclient.service.exceptions.DatabaseException;
import com.devsuperior.dsclient.service.exceptions.RoleNotFoundException;
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
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Transactional(readOnly = true)
    public Page<RoleDto> findAll(Pageable pageable) {
        Page<RoleDto> rolesDto = roleRepository.findAll(pageable).map(RoleDto::new);
        return rolesDto;
    }

    @Transactional(readOnly = true)
    public RoleDto findById(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        Role entity = optionalRole.orElseThrow(() -> new RoleNotFoundException("Id não encontrado"));
        RoleDto roleDto = new RoleDto(entity);
        return roleDto;
    }

    @Transactional
    public RoleDto save(RoleDto roleDto) {
        Role entity = new Role();
        convertDtoToEntity(roleDto,entity);
        Role entitySaved = roleRepository.save(entity);
        return new RoleDto(entitySaved);
    }
    
    @Transactional
    public RoleDto update(Long id, RoleDto dto) {
        try {
            Role entity = roleRepository.getById(id);
            convertDtoToEntity(dto,entity);
            Role roleSaved = roleRepository.save(entity);
            return new RoleDto(roleSaved);
        } catch (EntityNotFoundException e) {
            throw new RoleNotFoundException("Id not found " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            roleRepository.deleteById(id);
        }  catch (EmptyResultDataAccessException e) {
            throw new RoleNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }

    }



    private void convertDtoToEntity(RoleDto roleDto, Role entity) {
        entity.setAuthority(roleDto.getAuthority());
    }
}
