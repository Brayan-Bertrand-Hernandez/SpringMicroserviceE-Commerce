package com.commerce.commons.email.factory;

import com.commerce.commons.email.model.Email;

public interface EmailObjFactory {
	
	public Email sendSimpleMessage(Email mail);
	
	public Email sendMessageWithAttachment(Email mail);
	
}
