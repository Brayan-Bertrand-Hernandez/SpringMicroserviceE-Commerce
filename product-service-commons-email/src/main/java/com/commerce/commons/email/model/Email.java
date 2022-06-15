package com.commerce.commons.email.model;

public class Email {

	private String to;
	private String subject;
	private String body;
	private String pathToAttachment;

	public Email(String to, String subject, String body, String pathToAttachment) {
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.pathToAttachment = pathToAttachment;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPathToAttachment() {
		return pathToAttachment;
	}

	public void setPathToAttachment(String pathToAttachment) {
		this.pathToAttachment = pathToAttachment;
	}

}
