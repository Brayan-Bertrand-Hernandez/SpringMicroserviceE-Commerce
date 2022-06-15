package com.commerce.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.commons.users.model.Role;
import com.commerce.users.factory.RoleFactory;
import com.commerce.users.repository.RoleRepository;

@Service
public class RoleService implements RoleFactory {
	
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllAuthorities() {
        return roleRepository.findAll();
    }
    
}
