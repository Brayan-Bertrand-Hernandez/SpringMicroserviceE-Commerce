package com.commerce.users.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commerce.commons.email.model.Email;

@FeignClient(name = "emails")
public interface EmailRestClient {

	@PostMapping("/simple")
	public Email simpleEmail(@RequestBody Email mail);
	
	@PostMapping("/files")
	public Email fileEmail(@RequestBody Email mail);
	
}
