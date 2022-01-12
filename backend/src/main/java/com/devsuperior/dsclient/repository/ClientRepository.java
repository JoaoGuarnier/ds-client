package com.devsuperior.dsclient.repository;

import com.devsuperior.dsclient.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
