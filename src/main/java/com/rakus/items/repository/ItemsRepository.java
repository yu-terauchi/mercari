package com.rakus.items.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rakus.items.domain.Items;

/**
 * 商品情報を読み込むリポジトリ.
 * 
 * @author yu.terauchi
 *
 */
@RequestMapping("/items")
@Repository
public class ItemsRepository {

	private final static RowMapper<Items> ITEMS_ROW_MAPPER = (rs, i) -> {
		Items items = new Items();
		items.getId();
		items.getCategoryName();
		return items;
	};
	// private final static

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<Items> findAll() {
		String sql = "SELECT id,name,condition_id,category_name,brand,price,shopping,description FROM items ORDER BY id;";
		List<Items> itemsList = jdbcTemplate.query(sql, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	public void insertItems(Items items) {

		String sql = "INSERT INTO order_toppings(topping_id, order_item_id) VALUES(:toppingId, :orderItemId)";

		SqlParameterSource param = new BeanPropertySqlParameterSource(items);

		jdbcTemplate.update(sql, param);

	}

}
