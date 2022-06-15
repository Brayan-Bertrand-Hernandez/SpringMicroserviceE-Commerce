package com.commerce.users.factory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.commerce.commons.users.model.User;

public interface UserFactory {

	List<User> findAll();

    List<User> findAllEnabledUsers();

    Page<User> findAll(Pageable pageable);

    Page<User> findAllEnabledUsers(Pageable pageable);

    User findById(Long id);

    User save(User user);

    void delete(Long id);

    User findByEmail(String email);
	
}
