package com.devsuperior.dsclient.controller;

import com.devsuperior.dsclient.dto.ClientDto;
import com.devsuperior.dsclient.dto.RoleDto;
import com.devsuperior.dsclient.service.ClientService;
import com.devsuperior.dsclient.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    private ResponseEntity<Page<RoleDto>> findAll(Pageable pageable) {
        Page<RoleDto> roles = roleService.findAll(pageable);
        return ResponseEntity.ok().body(roles);
    }

    @GetMapping("/{id}")
    private ResponseEntity<RoleDto> findById(@PathVariable Long id) {
        RoleDto roleDto = roleService.findById(id);
        return ResponseEntity.ok().body(roleDto);
    }

    @PostMapping
    private ResponseEntity<RoleDto> save(@RequestBody RoleDto roleDto) {
        RoleDto dto = roleService.save(roleDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    private ResponseEntity<RoleDto> update(@PathVariable Long id, @RequestBody RoleDto dto) {
        RoleDto roleDto = roleService.update(id, dto);
        return ResponseEntity.ok().body(roleDto);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<RoleDto> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
