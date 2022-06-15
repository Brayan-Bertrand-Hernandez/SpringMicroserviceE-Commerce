package com.commerce.users.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commerce.users.factory.UserFactory;
import com.commerce.users.model.User;
import com.commerce.users.repository.UserRepository;

@Service
public class UserService implements UserFactory {

	@Autowired
	private UserRepository userRepository;

	@Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

	@Override
    @Transactional(readOnly = true)
    public List<User> findAllEnabledUsers() {
        return userRepository.findAllByIsEnabled(Boolean.TRUE);
    }

	@Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

	@Override
    @Transactional(readOnly = true)
    public Page<User> findAllEnabledUsers(Pageable pageable) {
        return userRepository.findAllByIsEnabled(true, pageable);
    }
	
	@Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

	@Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

	@Override
	@Transactional
	public void delete(Long id) {
		User user = userRepository.findById(id).orElse(null);

		if (user != null) {
			user.setIsEnabled(Boolean.FALSE);

			userRepository.save(user);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
