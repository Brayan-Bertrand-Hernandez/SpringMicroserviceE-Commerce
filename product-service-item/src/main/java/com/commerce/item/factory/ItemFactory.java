package com.commerce.item.factory;

import java.util.List;

import com.commerce.item.model.Item;

public interface ItemFactory {
	public List<Item> findAll();

	public Item findById(Long id, Integer quantity);
}
