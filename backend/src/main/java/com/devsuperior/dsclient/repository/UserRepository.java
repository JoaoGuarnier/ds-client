package com.devsuperior.dsclient.repository;

import com.devsuperior.dsclient.model.Client;
import com.devsuperior.dsclient.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
