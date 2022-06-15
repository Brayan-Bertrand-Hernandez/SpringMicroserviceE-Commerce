package com.commerce.users.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commerce.commons.email.model.Email;
import com.commerce.commons.users.model.Role;
import com.commerce.commons.users.model.User;
import com.commerce.users.service.EmailService;
import com.commerce.users.service.RoleService;
import com.commerce.users.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private EmailService emailService;

	@Value("${spring.profiles.active}")
	private String profile;

	@Value("${configuration.text}")
	private String text;

	@Value("${server.port}")
	private String port;

	private static final String ERR = "Error";
	private static final String PORT = "Puerto";
	private static final String ERR_NOTE = "Nota";
	private static final String USER_MSG = "Mensaje para el usuario";
	private static final String SERVER_MSG = "Mensaje interno";
	private static final String OBJ_USER = "Usuario";
	private static final String OBJ_ROL = "Roles";
	private static final String OBJ_PAGES = "Paginas";
	private static final String PROFILE = "Perfil Activo";

	private String user_header = "";
	private String internal_header = "";
	private String internal_err = "";
	private String note_err = "";

	private Map<String, Object> response;
	private Email mail;

	@GetMapping("/index")
	public ResponseEntity<?> index() {
		List<User> users = userService.findAll();
		response = new HashMap<>();

		if (users.isEmpty()) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "No hay usuarios en la base de datos";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		users = userService.findAll().stream().map(u -> {
			u.setPort(Integer.parseInt(port));
			return u;
		}).collect(Collectors.toList());

		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/index/enabled")
	public ResponseEntity<?> indexEnabledUsers() {
		List<User> enabled_users = userService.findAllEnabledUsers();
		response = new HashMap<>();

		if (enabled_users.isEmpty()) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "No hay usuarios activos en la base de datos";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		enabled_users = userService.findAllEnabledUsers().stream().map(user -> {
			user.setPort(Integer.parseInt(port));
			return user;
		}).collect(Collectors.toList());

		return new ResponseEntity<>(enabled_users, HttpStatus.OK);
	}

	@GetMapping("/page/{page}")
	public ResponseEntity<?> indexPage(@PathVariable Integer page) {
		Page<User> page_users = userService.findAll(PageRequest.of(page, 5));
		response = new HashMap<>();

		if (page_users.isEmpty()) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "No hay usuarios en la base de datos";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put(OBJ_PAGES, page_users);
		response.put(PORT, port);

		return new ResponseEntity<>(page_users, HttpStatus.OK);
	}

	@GetMapping("/enabled/page/{page}")
	public ResponseEntity<?> indexEnabledUsersPage(@PathVariable Integer page) {
		Page<User> page_enabled_users = userService.findAllEnabledUsers(PageRequest.of(page, 5));
		response = new HashMap<>();

		if (page_enabled_users.isEmpty()) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "No hay usuarios activos en la base de datos";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put(OBJ_PAGES, page_enabled_users);
		response.put(PORT, port);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/search/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		User currentUser;
		response = new HashMap<>();

		try {
			currentUser = userService.findById(id);
		} catch (DataAccessException e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar la consulta en la base de datos.";
			internal_err = "Dato invalido.";
			note_err = "Verifique que el dato enviado sea Long.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (currentUser == null) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "El registro del usuario con ID ".concat(id.toString().concat(" no existe."));
			note_err = "Verifique que el ID solicitado exista en la base de datos.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		currentUser.setPort(Integer.parseInt(port));

		return new ResponseEntity<>(currentUser, HttpStatus.OK);
	}

	@GetMapping("/enabled/email/{email}")
	public ResponseEntity<?> enabled(@PathVariable String email) {
		User currentUser;
		User enabledUser;
		response = new HashMap<>();
		mail = new Email();

		try {
			currentUser = userService.findByEmail(email);
			currentUser.setIsEnabled(Boolean.TRUE);

			enabledUser = userService.save(currentUser);
		} catch (DataAccessException e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar la consulta en la base de datos.";
			internal_err = "Dato invalido.";
			note_err = "Verifique que el dato enviado sea String.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (enabledUser == null) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "El registro del usuario con Email ".concat(email.concat(" no existe."));
			note_err = "Verifique que el email solicitado exista en la base de datos.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		user_header = "Verificación realizada con exito";
		internal_header = "El usuario ha sido verificado con éxito.";

		mail.setTo(email);
		mail.setSubject(user_header);
		mail.setBody("Estimado ".concat(enabledUser.getName().concat(" ")
				.concat(enabledUser.getLastname().concat(" se ha verificado su cuenta con éxito!"))));

		emailService.sendSimpleMessage(mail);

		response.put(SERVER_MSG, internal_header);
		response.put(OBJ_USER, enabledUser);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<?> createClient(@RequestBody User user, BindingResult bindingResult) {
		User newClient;
		response = new HashMap<>();
		mail = new Email();

		if (bindingResult.hasErrors()) {
			return binding(bindingResult);
		}

		User passiveClient = userService.findByEmail(user.getEmail());

		try {
			// String passwordEncoded = bCryptPasswordEncoder.encode(user.getPassword());

			// user.setPassword(passwordEncoded);

			newClient = userService.save(user);
		} catch (DataAccessException e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar la inserción en la base de datos.";
			internal_err = "Email en uso.";
			note_err = "Verifique que el email utilizado, no se encuentre en uso.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (passiveClient != null) {
			user_header = "Confirmación de reactivación";
		} else {
			user_header = "Confirmación de creación";
		}

		mail.setTo(passiveClient.getEmail());
		mail.setSubject(user_header);
		mail.setBody("Estimado ".concat(passiveClient.getName().concat(" ".concat(passiveClient.getLastname().concat(
				" se ha registrado con éxito en nuestra plataforma!\n\nComo último paso verifique su cuente en: http://localhost:8090/api/v1/users/enabled/email/"
						.concat(passiveClient.getEmail()))))));

		emailService.sendSimpleMessage(mail);

		internal_header = "El usuario ha sido creado con éxito.";

		response.put(SERVER_MSG, internal_header);
		response.put(OBJ_USER, newClient);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody User user, BindingResult bindingResult, @PathVariable Long id) {
		User currentUser = userService.findById(id);
		User updatedUser;
		mail = new Email();

		response = new HashMap<>();

		if (bindingResult.hasErrors()) {
			return binding(bindingResult);
		}

		try {
			if (currentUser == null) {
				user_header = "Lo sentimos, el recurso solicitado no existe.";
				internal_header = "El registro del usuario con ID ".concat(id.toString().concat(" no existe."));
				note_err = "Verifique que el ID solicitado exista en la base de datos.";

				response.put(USER_MSG, user_header);
				response.put(SERVER_MSG, internal_header);
				response.put(ERR_NOTE, note_err);

				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

			currentUser.setName(user.getName());
			currentUser.setLastname(user.getLastname());
			currentUser.setCellphone(user.getCellphone());
			currentUser.setEmail(user.getEmail());
			currentUser.setAge(user.getAge());
			currentUser.setAuthorities(user.getAuthorities());
			currentUser.setAge(user.getAge());
			currentUser.setIsEnabled(currentUser.getIsEnabled());

			// String passwordEncoded = bCryptPasswordEncoder.encode(password);

			// user.setPassword(passwordEncoded);

			currentUser.setPassword(user.getPassword());

			updatedUser = userService.save(currentUser);
		} catch (DataAccessException e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar la inserción en la base de datos.";
			internal_err = "Email en uso.";
			note_err = "Verifique que el email utilizado, no se encuentre en uso.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (currentUser.getEmail().equals(user.getEmail())) {
			mail.setBody("Estimado ".concat(currentUser.getName().concat(" ".concat(
					currentUser.getLastname().concat(" los datos de de su cuenta han sido actualizados con exito!")))));
		} else {
			mail.setBody("Estimado ".concat(user.getName().concat(" ".concat(user.getLastname().concat(
					" los datos de de su cuenta han sido actualizados con exito!\n\nComo último paso verifique su cuente en: https://localhost:8090/api/v1/users/enabled/email/"
							.concat(user.getEmail()))))));
		}

		user_header = "Confirmación de actualización";

		mail.setTo(currentUser.getEmail());
		mail.setSubject(user_header);

		emailService.sendSimpleMessage(mail);

		internal_header = "El usuario ha sido actualizado con éxito.";

		response.put(SERVER_MSG, internal_header);
		response.put(OBJ_USER, updatedUser);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		User deleteUser = userService.findById(id);
		response = new HashMap<>();
		mail = new Email();

		try {
			userService.delete(id);
		} catch (DataAccessException e) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "El registro del usuario con id ".concat(id.toString().concat(" no existe."));
			note_err = "Verifique que el ID solicitado exista en la base de datos.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		user_header = "Confirmación de eliminación.";
		internal_header = "El usuario ha sido eliminado con éxito.";

		mail.setTo(deleteUser.getEmail());
		mail.setSubject(user_header);
		mail.setBody("Estimado ".concat(deleteUser.getName().concat(" ").concat(deleteUser.getLastname().concat(
				" su cuenta ha sido eliminada de nuestro sistema, esperemos que su instancia aquí le haya servido de algo, le agradecemos su tiempo."))));

		emailService.sendSimpleMessage(mail);

		response.put(SERVER_MSG, internal_header);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/recovery-account")
	public ResponseEntity<?> recoverAccount(@RequestBody User user) {
		User recoveryUser;
		response = new HashMap<>();
		mail = new Email();

		try {
			recoveryUser = userService.findByEmail(user.getEmail());
		} catch (DataAccessException e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar la consulta en la base de datos.";
			internal_err = "Dato invalido.";
			note_err = "Verifique que el dato enviado sea Long.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (recoveryUser == null) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "El registro del usuario con Email ".concat(user.getEmail().concat(" no existe."));
			note_err = "Verifique que el email solicitado exista en la base de datos.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		// String passwordEncoded = bCryptPasswordEncoder.encode(password);

		recoveryUser.setPassword(user.getPassword());

		recoveryUser = userService.save(recoveryUser);

		user_header = "Contraseña cambiada con exito.";
		internal_header = "El usuario ha sido modificado con éxito.";

		mail.setTo(user.getEmail());
		mail.setSubject(user_header);
		mail.setBody("Estimado ".concat(recoveryUser.getName().concat(" ").concat(recoveryUser.getLastname()
				.concat(" su conntraseña ha sido modificada con exito, esperamos poder seguir apoyandolo."))));

		emailService.sendSimpleMessage(mail);

		response.put(SERVER_MSG, internal_header);
		response.put(OBJ_USER, recoveryUser);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/roles")
	public ResponseEntity<?> roleList() {
		List<Role> authorities = roleService.findAllAuthorities();
		response = new HashMap<>();

		if (authorities.isEmpty()) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "No hay roles en la base de datos";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		response.put(OBJ_ROL, authorities);
		response.put(PORT, port);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/configs")
	public ResponseEntity<?> getConfig() {
		response = new HashMap<>();

		response.put(ERR_NOTE, text);
		response.put(PROFILE, profile);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<?> binding(BindingResult bindingResult) {
		String errs = "Errores";

		List<String> errors = bindingResult.getFieldErrors().stream()
				.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
				.collect(Collectors.toList());

		response.put(errs, errors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
