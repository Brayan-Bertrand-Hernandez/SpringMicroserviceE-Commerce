package com.commerce.products.repository;

import org.springframework.data.repository.CrudRepository;

import com.commerce.products.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{
	
}
