package com.rakus.items.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.rakus.items.domain.Category;
import com.rakus.items.domain.Items;

/**
 * itemsテーブルの情報を操作するリポジトリ.
 * 
 * @author yu.terauchi
 *
 */
@Repository
public class ItemsRepository {

	private final static RowMapper<Items> ITEMS_ROW_MAPPER = (rs, i) -> {
		//このインスタンス化は効率が悪いか？
		Items items = new Items();
		Category parent = new Category();
		Category child = new Category();
		Category grandson = new Category();

		items.setId(rs.getInt("id"));
		items.setName(rs.getString("name"));
		items.setConditionId(rs.getInt("condition_id"));
		items.setCategoryId(rs.getInt("category_id"));
		items.setBrand(rs.getString("brand"));
		items.setPrice(rs.getInt("price"));
		items.setShopping(rs.getInt("shopping"));
		items.setDescription(rs.getString("description"));

		parent.setParentId(rs.getInt("parent_id"));
		parent.setCategoryName(rs.getString("p_category_name"));
		child.setId(rs.getInt("child_id"));
		child.setCategoryName(rs.getString("c_category_name"));
		grandson.setId(rs.getInt("grandson_id"));
		grandson.setCategoryName(rs.getString("g_category_name"));
		grandson.setNameAll(rs.getString("name_all"));

		items.setParent(parent);
		items.setChild(child);
		items.setGrandson(grandson);
		return items;
	};

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	// 最大表示件数
	final int MAX_COUNT = 30;
	// 表示開始の行数 〇〇行目から表示
	int count = 0;

	/**
	 * items情報を取得する .
	 * @return items一覧
	 */
	public List<Items> load() {
		// データ量が多いので仮に50個までで読み込む のちにページング機能追加で対応
		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
				+ ",p.id AS parent_id , p.category_name AS p_category_name "
				+ ", c.id AS child_id , c.category_name AS c_category_name "
				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
				+ "FROM items AS i "
				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id "
				+ "WHERE p.parent_id is NULL AND p.name_all is NULL "
				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
				+ "AND g.parent_id is not NULL AND g.name_all is not NULL "
				+ "ORDER BY id "
				+ "LIMIT 10;";
		List<Items> itemsList = jdbcTemplate.query(sql, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * ページング機能.
	 * @param id ページ番号
	 * @return 商品一覧
	 */
	public List<Items> loadPage(Integer pageId) {
		// 2ページ目以降
		if (pageId >= 2) {
			count = (MAX_COUNT * pageId) - 1;
		}
		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
				+ ",p.id AS parent_id , p.category_name AS p_category_name "
				+ ", c.id AS child_id , c.category_name AS c_category_name "
				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
				+ "FROM items AS i " 
				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id "
				+ "WHERE p.parent_id is NULL AND p.name_all is NULL "
				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
				+ "ORDER BY id "
				+ "LIMIT :max_count OFFSET :count";
		SqlParameterSource param = new MapSqlParameterSource().addValue("max_count", MAX_COUNT).addValue("count",count);
		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * 商品詳細を読み込む.
	 * @param id 商品id
	 * @return 商品詳細情報
	 */
	public Items findItemsDetail(Integer id) {
		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
				+ ",p.id AS parent_id , p.category_name AS p_category_name "
				+ ", c.id AS child_id , c.category_name AS c_category_name "
				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
				+ "FROM items AS i " 
				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id "
				+ "WHERE i.id = :id "
				+ "AND p.parent_id is NULL AND p.name_all is NULL "
				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
				+ "AND g.parent_id is not NULL AND g.name_all is not NULL "
				+ "ORDER BY id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Items itemDetail = jdbcTemplate.queryForObject(sql, param, ITEMS_ROW_MAPPER);
		return itemDetail;
	}

	/**
	 * 商品検索.
	 * @param name 商品名
	 * @param parentCategoryName 親カテゴリ名
	 * @param childCategoryName 子カテゴリ名
	 * @param grandsonCategoryName 孫カテゴリ名
	 * @param brand グランド名
	 * @return 商品リスト
	 */
	public List<Items> findItems(String name,Integer grandsonCategoryId,String brand) {
		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
					+ ",p.id AS parent_id , p.category_name AS p_category_name "
					+ ", c.id AS child_id , c.category_name AS c_category_name "
					+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
					+ "FROM items AS i "
					+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
					+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
					+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
					+ "WHERE i.name ILIKE :name "
					+ "AND g.id = :grandsonCategoryId "
					+ "AND i.brand ILIKE :brand "
					+ "AND p.parent_id is NULL AND p.name_all is NULL "
					+ "AND c.parent_id is not NULL AND c.name_all is NULL "
					+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
					+ "ORDER BY id "
					+ "Limit 30";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'")
				.addValue("granedsonCategoryName", grandsonCategoryId).addValue("brand", "'%" + brand + "%'");
		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 商品名検索.
	 * @param name 商品名
	 * @return 商品一覧
	 */
	public List<Items> findByName(String name,Integer pageId){
		// 2ページ目以降
		try {
			if (pageId >= 2) {
				count = (MAX_COUNT * pageId) - 1;
			}
//			String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//						+ ",p.id AS parent_id , p.category_name AS p_category_name "
//						+ ", c.id AS child_id , c.category_name AS c_category_name "
//						+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//						+ "FROM items AS i "
//						+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//						+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//						+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//						+ "WHERE i.name ILIKE :name "
//						+ "AND p.parent_id is NULL AND p.name_all is NULL "
//						+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//						+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//						+ "ORDER BY id "
//						+ "LIMIT :max_count OFFSET :count";
			String sql = "SELECT sub.id,sub.name,sub.condition_id,sub.category_id,sub.brand,sub.price,sub.shopping,sub.description, "
					+ "sub.parent_id , sub.p_category_name, "
					+ "sub.child_id , sub.c_category_name, "
					+ "sub.child_id ,sub.c_category_name, "
					+ "sub.grandson_id , sub.g_category_name,sub.name_all "
					+ "FROM "
					+ "("
					+ "SELECT "
					+ "i.id,i.name,i.condition_id,i.category_id,"
					+ "i.brand,i.price,i.shopping,i.description,"
					+ "p.id AS parent_id , p.category_name AS p_category_name,"
					+ "c.id AS child_id , c.category_name AS c_category_name,"
					+ "g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
					+ "FROM items AS i "
					+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
					+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
					+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id "
					+ "WHERE "
					+ "p.parent_id is NULL AND p.name_all is NULL "
					+ "AND c.parent_id is not NULL AND c.name_all is NULL "
					+ "AND g.parent_id is not NULL AND g.name_all is not NULL "
					+ ")"
					+ " sub "
					+ "where sub.name ilike :name "
					+ "order by sub.id "
					+ "limit :max_count offset :count";
			
			System.out.println("クエリ前");
			SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'").addValue("max_count", MAX_COUNT).addValue("count",count);
			System.out.println("パラメータ処理");
			List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
			System.out.println("クエリ終了");
			return itemsList;
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return null;
	}
//	
//	/**
//	 * ブランド名検索.
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByBrand(String brand){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.brand ILIKE :brand  "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "'%" + brand + "%'");
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 商品名・ブランド名検索
//	 * @param name 商品名
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNameBrand(String name,String brand){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.name ILIKE :name "
//				+ "AND i.name ILIKE :brand "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'").addValue("brand", "'%" + brand + "%'");
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 商品名・親カテゴリID検索
//	 * @param name 商品名
//	 * @param parentCategoryId 親カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNameParentCategoryId(String name,Integer parentCategoryId){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.name ILIKE :name "
//				+ "AND p.id = :parentCategoryId "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'").addValue("parentCategoryId",parentCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 商品名・子カテゴリID検索
//	 * @param name 商品名
//	 * @param childCategoryId 子カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNameChildCategoryId(String name,Integer childCategoryId){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.name ILIKE :name "
//				+ "AND c.id = :childCategoryId "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'").addValue("childCategoryId",childCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 商品名・孫カテゴリ
//	 * @param name 商品名
//	 * @param grandsonCategoryId 孫カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNameGrandsonCategoryId(String name,Integer grandsonCategoryId){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.name ILIKE :name "
//				+ "AND g.id = :grandsonCategoryId "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'").addValue("grandsonCategoryId",grandsonCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 商品名・親カテゴリID・ブランド名検索
//	 * @param name 商品名
//	 * @param parentCategoryId 親カテゴリ名
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNameParentCategoryIdBrand(String name,Integer parentCategoryId,String brand){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.name ILIKE :name "
//				+ "AND i.brand ILIKE :brand "
//				+ "AND p.id = :parentCategoryId "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'").addValue("brand", "'%" + brand + "%'").addValue("parentCategoryId",parentCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 商品名・子カテゴリID.
//	 * @param name 商品名
//	 * @param childCategoryId 子カテゴリID
//	 * @param brand ブランド名
//	 * @return 商品リスト
//	 */
//	public List<Items> findByNameChildCategoryIdBrand(String name,Integer childCategoryId,String brand){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.name ILIKE :name "
//				+ "AND i.brand ILIKE :brand "
//				+ "AND c.id = :childCategoryId "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'").addValue("brand", "'%" + brand + "%'").addValue("childCategoryId",childCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 商品名・孫カテゴリID・ブランド名
//	 * @param name 商品名
//	 * @param grandsonCategoryId 孫カテゴリID
//	 * @param brand ブランド名
//	 * @return 所品リスト
//	 */
//	public List<Items> findByNameGrandsonCategoryIdBrand(String name,Integer grandsonCategoryId,String brand){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.name ILIKE :name "
//				+ "AND i.brand ILIKE :brand "
//				+ "AND g.id = :grandsonCategoryId "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "'%" + name + "%'").addValue("brand", "'%" + brand + "%'").addValue("grandsonCategoryId",grandsonCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 親カテゴリID検索.
//	 * @param parentCategoryId 親カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByParentCategoryId(Integer parentCategoryId){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ "FROM items AS i "
//				+ "WHERE category_id IN ("
//				+ "SELECT id FROM category AS g "
//				+ "WHERE g.parent_id is not NULL AND g.name_all is not NULL "
//				+ ")";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("parentCategoryId",parentCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 子カテゴリID検索
//	 * @param childCategoryId 子カテゴリID
//	 * @return 商品リスト
//	 */
//	public List<Items> findByChildCategoryId(Integer childCategoryId){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE c.id = :childCategoryId "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30 ";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("childCategoryId",childCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
//	/**
//	 * 孫カテゴリID検索
//	 * @param grandsonCategoryId 孫カテゴリ
//	 * @return 商品リスト
//	 */
//	public List<Items> findByGrandsonCategoryId(Integer grandsonCategoryId){
//		String sql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
//				+ ",p.id AS parent_id , p.category_name AS p_category_name "
//				+ ", c.id AS child_id , c.category_name AS c_category_name "
//				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
//				+ "FROM items AS i "
//				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
//				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
//				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id " 
//				+ "WHERE i.id = :grandsonCategoryId "
//				+ "AND p.parent_id is NULL AND p.name_all is NULL "
//				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
//				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " 
//				+ "ORDER BY id "
//				+ "limit 30";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("grandsonCategoryId",grandsonCategoryId);
//		List<Items> itemsList = jdbcTemplate.query(sql, param, ITEMS_ROW_MAPPER);
//		return itemsList;
//	}
}
