package com.commerce.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.commons.email.factory.EmailFactory;
import com.commerce.commons.email.model.Email;
import com.commerce.users.restclient.EmailRestClient;

@Service
public class EmailService implements EmailFactory {

	@Autowired
	private EmailRestClient feignClient;

	@Override
	public void sendSimpleMessage(Email mail) {
		feignClient.simpleEmail(mail);
	}

	@Override
	public void sendMessageWithAttachment(Email mail) {
		feignClient.fileEmail(mail);
	}

}
