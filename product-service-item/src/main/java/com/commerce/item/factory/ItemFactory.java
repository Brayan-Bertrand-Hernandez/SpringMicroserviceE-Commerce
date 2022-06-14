package com.commerce.item.factory;

import java.util.List;

import com.commerce.item.model.Item;
import com.commerce.commons.model.Product;

public interface ItemFactory {
	
	public List<Item> findAll();

	public Item findById(Long id, Integer quantity);
	
	public Product findProductById(Long id);
	
	public Product save(Product product);
	
	public Product update(Product product, Long id);
	
	public void delete(Long id);
	
}

