package com.commerce.item.restclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.commerce.item.model.Product;

@FeignClient(name = "products")
public interface ProductRestClient {
	
	@GetMapping("/index")
	public List<Product> index();
	
	@GetMapping("/{id}")
	public Product details(@PathVariable Long id);
	
}
