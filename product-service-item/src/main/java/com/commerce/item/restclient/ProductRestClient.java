package com.commerce.item.restclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commerce.commons.model.Product;

@FeignClient(name = "products")
public interface ProductRestClient {
	
	@GetMapping("/index")
	public List<Product> index();
	
	@GetMapping("/{id}")
	public Product details(@PathVariable Long id);
	
	@PostMapping("/new")
	public Product create(@RequestBody Product product);
	
	@PutMapping("/update/{id}")
	public Product update(@RequestBody Product product, @PathVariable Long id);
	
	@DeleteMapping("/delete/{id}")
	public void deleteById(@PathVariable Long id);
	
}
