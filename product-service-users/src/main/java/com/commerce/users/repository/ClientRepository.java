package com.commerce.users.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.commerce.commons.users.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

	Client findByEmail(String email);

	List<Client> findAllByIsEnabled(Boolean isEnabled);

	Page<Client> findAllByIsEnabled(Boolean isEnabled, Pageable pageable);

}
