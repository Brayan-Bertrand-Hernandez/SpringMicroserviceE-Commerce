package com.commerce.products.repository;

import org.springframework.data.repository.CrudRepository;

import com.commerce.commons.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{
	
}
