package com.commerce.products.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	private String user_header = "";
	private String internal_header = "";
	private String internal_err = "";
	private String note_err = "";
	private Map<String, Object> response;
	
	@GetMapping("/index")
	public ResponseEntity<?> index() {
		List<Product> products = productService.findAll();
		response = new HashMap<>();
		
		if(products.isEmpty()) {
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
		
		if(currentProduct == null) {
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
	
}
