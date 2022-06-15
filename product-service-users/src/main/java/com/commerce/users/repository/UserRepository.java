package com.commerce.users.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.commerce.commons.users.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	List<User> findAllByIsEnabled(Boolean isEnabled);

	Page<User> findAllByIsEnabled(Boolean isEnabled, Pageable pageable);

}
