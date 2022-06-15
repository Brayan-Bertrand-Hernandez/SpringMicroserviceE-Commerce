package com.commerce.users.factory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.commerce.commons.users.model.Client;

public interface ClientFactory {

	List<Client> findAll();

    List<Client> findAllEnabledUsers();

    Page<Client> findAll(Pageable pageable);

    Page<Client> findAllEnabledUsers(Pageable pageable);

    Client findById(Long id);

    Client save(Client user);

    void delete(Long id);

    Client findByEmail(String email);
	
}
