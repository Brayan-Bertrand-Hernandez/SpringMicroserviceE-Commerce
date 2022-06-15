package com.commerce.item.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.item.model.Item;
import com.commerce.item.service.ItemService;
import com.commerce.commons.model.Product;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RefreshScope
@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Value("${spring.profiles.active}")
	private String profile;

	@Value("${configuration.text}")
	private String text;

	@Value("${server.port}")
	private String port;

	private static final String ERR = "Error";
	private static final String ERR_NOTE = "Nota";
	private static final String USER_MSG = "Mensaje para el usuario";
	private static final String SERVER_MSG = "Mensaje interno";
	private static final String OBJ = "Ticket";
	private static final String PROFILE = "Perfil Activo";
	private String user_header = "";
	private String internal_header = "";
	private String internal_err = "";
	private String note_err = "";
	private Map<String, Object> response;

	@GetMapping("/index")
	public ResponseEntity<?> index(@RequestParam(name = "name", required = false) String name,
			@RequestHeader(name = "token-request", required = false) String token) {
		List<Item> products = itemService.findAll();
		response = new HashMap<>();

		if (products.isEmpty()) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "No hay productos en la base de datos";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@CircuitBreaker(name = "items", fallbackMethod = "detailOPtionalMethod")
	@GetMapping("/search/{id}/quantity/{quantity}")
	public ResponseEntity<?> details(@PathVariable Long id, @PathVariable Integer quantity) {
		Item currentItem;
		response = new HashMap<>();

		try {
			currentItem = itemService.findById(id, quantity);
		} catch (Exception e) {
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

		if (currentItem == null) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "El registro del producto con ID ".concat(id.toString().concat(" no existe."));
			note_err = "Verifique que el ID solicitado exista en la base de datos.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(currentItem, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> create(@RequestBody Product product, BindingResult bindingResult) throws IOException {
		response = new HashMap<>();

		if (bindingResult.hasErrors()) {
			return binding(bindingResult);
		}

		try {
			itemService.save(product);
		} catch (Exception e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar la inserción en la base de datos.";
			internal_err = "Datos restantes.";
			note_err = "Verifique el objeto enviado.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		internal_header = "El producto, ha sido cargado con éxito en la base de datos.";

		response.put(SERVER_MSG, internal_header);
		response.put(OBJ, product);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody Product product, BindingResult bindingResult, @PathVariable Long id) {
		Product currentProduct = itemService.findProductById(id);

		response = new HashMap<>();

		if (bindingResult.hasErrors()) {
			return binding(bindingResult);
		}

		try {
			if (currentProduct == null) {
				user_header = "Lo sentimos, el recurso ha actualizar no existe, le pedimos que hable con un asesor de soporte.";
				internal_header = "El registro del producto con ID ".concat(id.toString().concat(" no existe."));
				note_err = "Verifique que el ID solicitado exista en la base de datos.";

				response.put(USER_MSG, user_header);
				response.put(SERVER_MSG, internal_header);
				response.put(ERR_NOTE, note_err);

				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			
			currentProduct.setCreatedAt(product.getCreatedAt());
			currentProduct.setName(product.getName());
			currentProduct.setPrice(product.getPrice());
			currentProduct.setPort(product.getPort());

			currentProduct = itemService.update(currentProduct, id);
			
			product.setId(id);
		} catch (Exception e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar una actualización en la base de datos.";
			internal_err = "Datos restantes.";
			note_err = "Verifique el objeto enviado.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		internal_header = "El producto, ha sido actualizado con éxito en la base de datos.";

		response.put(SERVER_MSG, internal_header);
		response.put(OBJ, product);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		response = new HashMap<>();

		try {
			itemService.delete(id);
		} catch (Exception e) {
			user_header = "Lo sentimos, hemos tenido problemas para procesar su solicitud, contacte con un asesor de la página.";
			internal_header = "Error al realizar la eliminación del registro en la base de datos.";
			internal_err = "Producto ligado.";
			note_err = "Verifique los campos a los que se encuentra ligado el ID.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR, internal_err);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		internal_header = "El producto, ha sido eliminado con éxito en la base de datos.";

		response.put(SERVER_MSG, internal_header);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/configs")
	public ResponseEntity<?> getConfig() {
		response = new HashMap<>();

		response.put(ERR_NOTE, text);
		response.put(PROFILE, profile);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<?> detailOPtionalMethod(Long id, Integer quantity, Throwable err) {
		Item currentItem;
		response = new HashMap<>();

		try {
			currentItem = itemService.findById(id, quantity);
		} catch (Exception e) {
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

		if (currentItem == null) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "El registro del producto con ID ".concat(id.toString().concat(" no existe."));
			note_err = "Verifique que el ID solicitado exista en la base de datos.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		internal_header = "El metodo principal tiene sobrecarga, se ha utilizado el metodo alternativo.";

		response.put(OBJ, currentItem);
		response.put(SERVER_MSG, internal_header);
		response.put(ERR, err);

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
