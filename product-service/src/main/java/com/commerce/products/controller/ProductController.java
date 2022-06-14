package com.commerce.products.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.products.model.Product;
import com.commerce.products.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	private Environment env;

	@Autowired
	private ProductService productService;

	private static final String ERR = "Error";
	private static final String ERR_NOTE = "Nota";
	private static final String USER_MSG = "Mensaje para el usuario";
	private static final String SERVER_MSG = "Mensaje interno";
	private static final String OBJ = "Producto";
	private String user_header = "";
	private String internal_header = "";
	private String internal_err = "";
	private String note_err = "";
	private Map<String, Object> response;

	@GetMapping("/index")
	public ResponseEntity<?> index() {
		List<Product> products = productService.findAll();
		response = new HashMap<>();

		if (products.isEmpty()) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "No hay productos en la base de datos";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		products = productService.findAll().stream().map(producto -> {
			producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
			return producto;
		}).collect(Collectors.toList());

		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> details(@PathVariable Long id) throws InterruptedException {
		Product currentProduct;
		response = new HashMap<>();

		try {
			currentProduct = productService.findById(id);
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

		if (currentProduct == null) {
			user_header = "Lo sentimos, el recurso solicitado no existe.";
			internal_header = "El registro del producto con ID ".concat(id.toString().concat(" no existe."));
			note_err = "Verifique que el ID solicitado exista en la base de datos.";

			response.put(USER_MSG, user_header);
			response.put(SERVER_MSG, internal_header);
			response.put(ERR_NOTE, note_err);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

		currentProduct.setPort(Integer.parseInt(env.getProperty("local.server.port")));

		return new ResponseEntity<>(currentProduct, HttpStatus.OK);
	}

	@PostMapping("/new")
	public ResponseEntity<?> create(@RequestBody Product product, BindingResult bindingResult) throws IOException {
		Product newProduct;
		response = new HashMap<>();

		if (bindingResult.hasErrors()) {
			return binding(bindingResult);
		}

		try {
			newProduct = productService.save(product);
		} catch (DataAccessException e) {
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
		response.put(OBJ, newProduct);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@RequestBody Product product, BindingResult bindingResult, @PathVariable Long id) {
		Product currentProduct = productService.findById(id);
		Product updatedProduct;

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

			currentProduct.setCreateAt(product.getCreateAt());
			currentProduct.setName(product.getName());
			currentProduct.setPrice(product.getPrice());

			updatedProduct = productService.save(currentProduct);
		} catch (DataAccessException e) {
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
		response.put(OBJ, updatedProduct);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		response = new HashMap<>();

		try {
			productService.deleteById(id);
		} catch (DataAccessException e) {
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

	public ResponseEntity<?> binding(BindingResult bindingResult) {
		String errs = "Errores";
		List<String> errors = bindingResult.getFieldErrors().stream()
				.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
				.collect(Collectors.toList());

		response.put(errs, errors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
