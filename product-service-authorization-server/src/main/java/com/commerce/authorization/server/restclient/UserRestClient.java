package com.commerce.authorization.server.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.commerce.commons.users.model.Client;

@FeignClient(name = "clients")
public interface UserRestClient {
	
	@GetMapping("/search/email/{email}")
	public Client findByEmail(@PathVariable String email);

}

