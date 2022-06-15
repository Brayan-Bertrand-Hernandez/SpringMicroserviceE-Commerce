package com.commerce.email.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commerce.commons.email.model.Email;
import com.commerce.email.service.EmailService;

@Controller
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@Value("${spring.profiles.active}")
	private String profile;

	@Value("${configuration.text}")
	private String text;
	
	private static final String OBJ = "Correo";
	private static final String ERR = "Error";
	private static final String ERR_NOTE = "Nota";
	private static final String USER_MSG = "Mensaje para el usuario";
	private static final String SERVER_MSG = "Mensaje interno";
	private static final String PROFILE = "Perfil Activo";
	
	private String user_header = "";
	private String internal_header = "";
	private String internal_err = "";
	private String note_err = "";
	
	private Map<String, Object> response;
	
	@PostMapping("/simple")
	public ResponseEntity<?> simpleEmail(@RequestBody Email mail) {	
		response = new HashMap<>();
		
		try {
			emailService.sendSimpleMessage(mail);
		} catch(Exception e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar el envio del correo.";
			internal_err = "Servidor SMTP sin servicio.";
			note_err = "Verifique el estatus del servidor SMTP.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		internal_header = "El email, ha sido enviado con exito.";

		response.put(SERVER_MSG, internal_header);
		response.put(OBJ, mail);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/files")
	public ResponseEntity<?> fileEmail(@RequestBody Email mail) {
		response = new HashMap<>();
		
		try {
			emailService.sendMessageWithAttachment(mail);
		} catch(Exception e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar el envio del correo.";
			internal_err = "Servidor SMTP sin servicio.";
			note_err = "Verifique el estatus del servidor SMTP.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		internal_header = "El email, ha sido enviado con exito.";

		response.put(SERVER_MSG, internal_header);
		response.put(OBJ, mail);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/configs")
	public ResponseEntity<?> getConfig() {
		response = new HashMap<>();

		response.put(ERR_NOTE, text);
		response.put(PROFILE, profile);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
