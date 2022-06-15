package com.commerce.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.commons.users.model.Client;
import com.commerce.users.factory.ClientFactory;
import com.commerce.users.repository.ClientRepository;

@Service
public class ClientService implements ClientFactory {

	@Autowired
	private ClientRepository clientRepository;

	@Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

	@Override
    @Transactional(readOnly = true)
    public List<Client> findAllEnabledUsers() {
        return clientRepository.findAllByIsEnabled(Boolean.TRUE);
    }

	@Override
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

	@Override
    @Transactional(readOnly = true)
    public Page<Client> findAllEnabledUsers(Pageable pageable) {
        return clientRepository.findAllByIsEnabled(true, pageable);
    }
	
	@Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

	@Override
    @Transactional
    public Client save(Client user) {
        return clientRepository.save(user);
    }

	@Override
	@Transactional
	public void delete(Long id) {
		Client user = clientRepository.findById(id).orElse(null);

		if (user != null) {
			user.setIsEnabled(Boolean.FALSE);

			clientRepository.save(user);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Client findByEmail(String email) {
		return clientRepository.findByEmail(email);
	}

}
