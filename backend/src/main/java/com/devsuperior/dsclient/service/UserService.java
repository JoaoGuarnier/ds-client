package com.devsuperior.dsclient.service;


import com.devsuperior.dsclient.dto.RoleDto;
import com.devsuperior.dsclient.dto.UserDto;
import com.devsuperior.dsclient.dto.UserInsertDto;
import com.devsuperior.dsclient.dto.UserUpdateDto;
import com.devsuperior.dsclient.model.Role;
import com.devsuperior.dsclient.model.User;
import com.devsuperior.dsclient.repository.RoleRepository;
import com.devsuperior.dsclient.repository.UserRepository;
import com.devsuperior.dsclient.service.exceptions.DatabaseException;
import com.devsuperior.dsclient.service.exceptions.RoleNotFoundException;
import com.devsuperior.dsclient.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        Page<UserDto> userDto = userRepository.findAll(pageable).map(UserDto::new);
        return userDto;
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        Optional<User> userRole = userRepository.findById(id);
        User entity = userRole.orElseThrow(() -> new UserNotFoundException("Id não encontrado"));
        UserDto roleDto = new UserDto(entity);
        return roleDto;
    }

    @Transactional
    public UserDto save(UserInsertDto userInsertDto) {
        User entity = new User();
        convertDtoToEntity(userInsertDto, entity);
        User entitySaved = userRepository.save(entity);
        return new UserDto(entitySaved);
    }
    
    @Transactional
    public UserDto update(Long id, UserUpdateDto dto) {
        try {
            User entity = userRepository.getById(id);
            convertDtoToEntity(dto,entity);
            User userSaved = userRepository.save(entity);
            return new UserDto(userSaved);
        } catch (EntityNotFoundException e) {
            throw new RoleNotFoundException("Id not found " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        }  catch (EmptyResultDataAccessException e) {
            throw new RoleNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }

    }



    private void convertDtoToEntity(UserDto userDto, User entity) {
        entity.setFirstName(userDto.getFirstName());
        entity.setLastName(userDto.getLastName());
        entity.setEmail(userDto.getEmail());

        entity.getRoles().clear();

        for (RoleDto roleDto : userDto.getRoles()) {
            Role role = roleRepository.getById(roleDto.getId());
            entity.getRoles().add(role);
        }


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw  new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
