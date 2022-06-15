package com.commerce.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commerce.commons.users.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
