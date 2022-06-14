package com.commerce.products.factory;

import java.util.List;

import com.commerce.commons.model.Product;

public interface ProductFactory {
	
	public List<Product> findAll();

	public Product findById(Long id);
	
	public Product save(Product product);
	
	public void deleteById(Long id);
	
}
