package com.devsuperior.dsclient.repository;

import com.devsuperior.dsclient.model.Client;
import com.devsuperior.dsclient.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
