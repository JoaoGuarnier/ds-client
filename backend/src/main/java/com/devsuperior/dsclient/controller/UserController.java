package com.devsuperior.dsclient.controller;

import com.devsuperior.dsclient.dto.RoleDto;
import com.devsuperior.dsclient.dto.UserDto;
import com.devsuperior.dsclient.dto.UserInsertDto;
import com.devsuperior.dsclient.dto.UserUpdateDto;
import com.devsuperior.dsclient.service.RoleService;
import com.devsuperior.dsclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    private ResponseEntity<Page<UserDto>> findAll(Pageable pageable) {
        Page<UserDto> roles = userService.findAll(pageable);
        return ResponseEntity.ok().body(roles);
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserDto> findById(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping
    private ResponseEntity<UserDto> save(@RequestBody UserInsertDto userInsertDto) {
        UserDto dto = userService.save(userInsertDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    private ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        UserDto userDto = userService.update(id, dto);
        return ResponseEntity.ok().body(userDto);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<UserDto> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
