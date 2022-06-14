package com.commerce.item.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.item.factory.ItemFactory;
import com.commerce.item.model.Item;
import com.commerce.commons.model.Product;
import com.commerce.item.restclient.ProductRestClient;

@Service
public class ItemService implements ItemFactory{
	
	@Autowired
	private ProductRestClient feignClient;

	@Override
	public List<Item> findAll() {	
		return feignClient.index().stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer quantity) {
		return new Item(feignClient.details(id), quantity);
	}
	
	@Override
	public Product findProductById(Long id) {
		return feignClient.details(id);
	}

	@Override
	public Product save(Product product) {
		return feignClient.create(product);
	}

	@Override
	public Product update(Product product, Long id) {
		return feignClient.update(product, id);
	}

	@Override
	public void delete(Long id) {
		feignClient.deleteById(id);
	}

}
