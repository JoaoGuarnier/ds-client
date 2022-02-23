package com.devsuperior.dsclient.dto;

import com.devsuperior.dsclient.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;
    private String authority;

    public RoleDto(Role role) {
        this.id = role.getId();
        this.authority = role.getAuthority();
    }

}
