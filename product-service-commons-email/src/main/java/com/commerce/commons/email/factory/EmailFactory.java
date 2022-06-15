package com.commerce.commons.email.factory;

import com.commerce.commons.email.model.Email;

public interface EmailFactory {
	
	public void sendSimpleMessage(Email mail);
	
	public void sendMessageWithAttachment(Email mail);

}
