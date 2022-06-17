package com.commerce.authorization.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.commerce.authorization.server.restclient.UserRestClient;
import com.commerce.commons.users.model.Client;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRestClient feignClient;

	private static final String ERR = "Lo sentimos, no hemos podido encontrar el email que ha ingresado en el sistema, verifique que el siguiente correo sea suyo: ";
	private static int ENCODER_STRENGTH = 11;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(ENCODER_STRENGTH);
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Client currentUsername = feignClient.findByEmail(email);

		if (currentUsername == null) {
			throw new UsernameNotFoundException(ERR.concat(email));
		}

		List<GrantedAuthority> authorities = currentUsername.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());

		return new User(currentUsername.getEmail(), currentUsername.getPassword(), currentUsername.getIsEnabled(), true,
				true, true, authorities);
	}

}