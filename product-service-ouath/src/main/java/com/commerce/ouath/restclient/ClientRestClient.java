package com.commerce.ouath.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.commerce.commons.users.model.Client;

@FeignClient(name = "users")
public interface ClientRestClient {
	@GetMapping("/search/email/{email}")
	public Client findByEmail(@PathVariable String email);
}
