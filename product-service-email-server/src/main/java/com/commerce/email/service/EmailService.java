package com.commerce.email.service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.commerce.commons.email.factory.EmailFactory;
import com.commerce.commons.email.model.Email;

@Service
public class EmailService implements EmailFactory {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${email.userId}")
	private String email;

	@Override
	public void sendSimpleMessage(Email mail) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(email);
        mailMessage.setTo(mail.getTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getBody());

        javaMailSender.send(mailMessage);
	}

	@Override
	public void sendMessageWithAttachment(Email mail) {
	    try {
	    	MimeMessage message = javaMailSender.createMimeMessage();
		     
		    MimeMessageHelper helper = new MimeMessageHelper(message, true);
		    
		    helper.setFrom(email);
		    helper.setTo(mail.getTo());
		    helper.setSubject(mail.getSubject());
		    helper.setText(mail.getBody());
		    
		    File file = new File(mail.getPathToAttachment());
		        
		    FileSystemResource fileResource = new FileSystemResource(file);
		    
			helper.addAttachment(file.getName(), fileResource);
			
			javaMailSender.send(message);
	    } catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
