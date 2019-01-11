package com.rakus.items.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakus.items.domain.Items;
import com.rakus.items.repository.ItemsRepository;

@Service
public class ItemsService {

	@Autowired
	public ItemsRepository itemsRepository;

	public List<Items> findAll() {
		List<Items> itemList = itemsRepository.findAll();
		return itemList;
	}
}
