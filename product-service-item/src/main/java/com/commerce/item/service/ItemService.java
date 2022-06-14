package com.commerce.item.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.item.factory.ItemFactory;
import com.commerce.item.model.Item;
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

}
