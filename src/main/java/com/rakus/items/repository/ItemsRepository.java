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

/**itemsテーブルの情報を操作するリポジトリ.
 * @author yu.terauchi
 */
@Repository
public class ItemsRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	// 最大表示件数
	private final int MAX_COUNT = 30;
	//商品件数
	private Integer countItems = 0;
	//ページ数
	private Integer maxPage = 1;

	/**
	 * 取得した値をセットするマッパー.
	 */
	private final static RowMapper<Items> ITEMS_ROW_MAPPER = (rs, i) -> {
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

		parent.setId(rs.getInt("parent_id"));
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

	
	//常に取得したいカラムを取得するためのSQL
	String prevSql = "SELECT i.id,i.name,i.condition_id,i.category_id,i.brand,i.price,i.shopping,i.description "
				+ ",p.id AS parent_id , p.category_name AS p_category_name "
				+ ", c.id AS child_id , c.category_name AS c_category_name "
				+ ",g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
				+ "FROM items AS i " 
				+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
				+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
				+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id ";
	//表示数・表示開始時点の条件設定をするSQL
	String behavSql = "LIMIT :max_count OFFSET :offSetCount ";
	//取得したitemsの総数を取得するSQL
	String countSql = "SELECT count(*) "
					+ "FROM items AS i "
					+ "LEFT OUTER JOIN category AS g ON g.id = i.category_id "
					+ "LEFT OUTER JOIN category AS c ON c.id = g.parent_id "
					+ "LEFT OUTER JOIN category AS p ON p.id = c.parent_id ";
//-------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 通常商品一覧ページング機能.
	 * @param id ページ番号
	 * @param count 表示開始件数
	 * @return 商品一覧
	 */
	public List<Items> loadPage(Integer pageId,Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("ORDER BY name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount",offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * 商品詳細を読み込む.
	 * 
	 * @param id 商品id
	 * @return 商品詳細情報
	 */
	public Items findItemsDetail(Integer id) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.id = :id ;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Items itemDetail = jdbcTemplate.queryForObject(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemDetail;
	}
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 完全商品検索.
	 * 
	 * 検索フォームのすべてに入力して検索
	 * 
	 * @param name 商品名
	 * @param parentCategoryName 親カテゴリ名
	 * @param childCategoryName 子カテゴリ名
	 * @param grandsonCategoryName 孫カテゴリ名
	 * @param brand ブランド名
	 * @return 商品リスト
	 */
	public List<Items> findItems(String name, Integer grandsonCategoryId, String brand,Integer pageId) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
					+ "AND g.id = :grandsonCategoryId " 
					+ "AND i.brand ILIKE :brand "
					+ "ORDER BY i.name " );
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("granedsonCategoryId", grandsonCategoryId)
															  .addValue("brand", "%" + brand + "%");
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 商品名検索.
	 * 
	 * @param name 商品名
	 * @return 商品一覧
	 */
	public List<Items> findByName(String name, Integer pageId, Integer offSetCount) {	
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
					+ "ORDER BY i.name " );
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * ブランド名(曖昧)検索.
	 * @param brand ブランド名
	 * @return 商品リスト
	 */
	 public List<Items> findByBrand(String brand, Integer pageId, Integer offSetCount){
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.brand ILIKE :brand "
					+ "ORDER BY i.name " );
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%")
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		if(itemsList.size() == 0) {
			System.out.println("商品リストが返ってきてない");
		}
		return itemsList;
	}
	
	/**
	 * ブランド名(完全一致)検索.
	 * @param brand ブランド名
	 * @return 商品リスト
	 */
	public List<Items> findByCompleteBrand(String brand, Integer pageId, Integer offSetCount){
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.brand = :brand "
				 + "ORDER BY i.name " );
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", brand)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
	
	/**
	 * 親カテゴリID検索.
	 * @param parentCategoryId 親カテゴリID
	 * @return 商品リスト
	 */
	public List<Items> findByParentCategoryId(Integer parentCategoryId,Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE p.id = :parentCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("parentCategoryId", parentCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
	
	/**
	 * 子カテゴリID検索
	 * 
	 * @param childCategoryId 子カテゴリID
	 * @return 商品リスト
	 */
	public List<Items> findByChildCategoryId(Integer childCategoryId,Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE c.id = :childCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("childCategoryId", childCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
	
	/**
	 * 孫カテゴリID検索
	 * 
	 * @param grandsonCategoryId 孫カテゴリ
	 * @return 商品リスト
	 */
	public List<Items> findByGrandsonCategoryId(Integer grandsonCategoryId,Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE g.id = :grandsonCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("grandsonCategoryId", grandsonCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * 商品名・ブランド名検索
	 * @param name 商品名
	 * @param brand ブランド名
	 * @return 商品リスト
	 */
	public List<Items> findByNameBrand(String name, String brand, Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND i.brand ILIKE :brand "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("brand","%" + brand + "%")
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * 商品名・親カテゴリID検索
	 * @param name 商品名
	 * @param parentCategoryId 親カテゴリID
	 * @return 商品リスト
	 */
	public List<Items> findByNameParentCategoryId(String name, Integer parentCategoryId, Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND p.id = :parentCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("parentCategoryId", parentCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * 商品名・子カテゴリID検索
	 * @param name 商品名
	 * @param childCategoryId 子カテゴリID
	 * @return 商品リスト
	 */
	public List<Items> findByNameChildCategoryId(String name, Integer childCategoryId, Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND c.id = :childCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("childCategoryId", childCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * 商品名・孫カテゴリ検索.
	 * @param name 商品名
	 * @param grandsonCategoryId 孫カテゴリID
	 * @return 商品リスト
	 */
	public List<Items> findByNameGrandsonCategoryId(String name, Integer grandsonCategoryId, Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND g.id = :grandsonCategoryId " 
				+ "ORDER BY i.name ");
		str.append(behavSql);
				
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("grandsonCategoryId", grandsonCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
	
	/**
	 * ブランド名・親カテゴリID検索
	 * @param brand ブランド名
	 * @param parentCategoryId 親カテゴリID
	 * @return 商品リスト
	 */
	public List<Items> findByParentCategoryIdBrand(String brand,Integer parentCategoryId, Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.brand ILIKE :brand "
				+ "AND p.id = :parentCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%")
															  .addValue("parentCategoryId", parentCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
	/**
	 * ブランド名・子カテゴリID検索
	 * @param brand ブランド名
	 * @param childCategoryId 子カテゴリID
	 * @return 商品リスト
	 */
	public List<Items> findByChildCategoryIdBrand(String brand,Integer childCategoryId, Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.brand ILIKE :brand "
				+ "AND c.id = :childCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%")
															  .addValue("childCategoryId", childCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
	/**
	 * ブランド名・孫カテゴリID検索
	 * @param brand ブランド名
	 * @param grandsonCategoryId 孫カテゴリID
	 * @return 商品リスト
	 */
	public List<Items> findByGrandsonCategoryIdBrand(String brand,Integer grandsonCategoryId, Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.brand ILIKE :brand "
				+ "AND g.id = :grandsonCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%")
															  .addValue("grandsonCategoryId", grandsonCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
	
	/**
	 * 商品名・親カテゴリID・ブランド名検索
	 * @param name 商品名
	 * @param parentCategoryId 親カテゴリ名
	 * @param brand ブランド名
	 * @return 商品リスト
	 */
	public List<Items> findByNameParentCategoryIdBrand(String name, Integer parentCategoryId, String brand,Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND i.brand ILIKE :brand "
				+ "AND p.id = :parentCategoryId "
				+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("brand", "%" + brand + "%")
															  .addValue("parentCategoryId", parentCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * 商品名・子カテゴリID・ブランド名検索.
	 * 
	 * @param name 商品名
	 * @param childCategoryId 子カテゴリID
	 * @param brand ブランド名
	 * @return 商品リスト
	 */
	public List<Items> findByNameChildCategoryIdBrand(String name, Integer childCategoryId, String brand,Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
					+ "AND i.brand ILIKE :brand " 
					+ "AND c.id = :childCategoryId  "
					+ "ORDER BY i.name ");
		str.append(behavSql);
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("brand", "%" + brand + "%")
															  .addValue("childCategoryId", childCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}

	/**
	 * 商品名・孫カテゴリID・ブランド名
	 * 
	 * @param name 商品名
	 * @param grandsonCategoryId 孫カテゴリID
	 * @param brand ブランド名
	 * @return 所品リスト
	 */
	public List<Items> findByNameGrandsonCategoryIdBrand(String name, Integer grandsonCategoryId, String brand,Integer pageId, Integer offSetCount) {
		StringBuilder str = new StringBuilder();
		str.append(prevSql);
		str.append("WHERE i.name ILIKE :name "
					+ "AND i.brand ILIKE :brand "
					+ "AND g.id = :grandsonCategoryId "
					+ "ORDER BY i.name ");
		str.append(behavSql);
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("brand", "%" + brand + "%")
															  .addValue("grandsonCategoryId", grandsonCategoryId)
															  .addValue("max_count", MAX_COUNT)
															  .addValue("offSetCount", offSetCount);
		List<Items> itemsList = jdbcTemplate.query(str.toString(), param, ITEMS_ROW_MAPPER);
		return itemsList;
	}
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	/**
	 * 帰ってきたリストの数に応じてページ数をカウントする.
	 * @param pageId ページ
	 * @return ページ数
	 */
	public Integer countPage() {
		String sql = "SELECT COUNT(*) FROM items;";
		SqlParameterSource param = new MapSqlParameterSource();
		countItems = jdbcTemplate.queryForObject(sql, param, Integer.class);
		//割った値が1.1~1.9の場合変数に入る値は1だが、2ページで表示したいから＋1
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	
	/**
	 * 商品名検索のページ数取得.
	 * @param name 商品名
	 * @return 商品一覧
	 */
	public Integer countByName(String name) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.name ILIKE :name ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	
	/**
	 * ブランド名検索のページ数取得.
	 * @param brand ブランド名
	 * @return ページ数
	 */
	public Integer countByBrand(String brand){
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.brand ILIKE :brand ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%");
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	
	/**親カテゴリID検索のページ数取得.
	 * @param parentCategoryId 親カテゴリID
	 * @return　ページ数
	 */
	public Integer countByParentCategoryId(Integer parentCategoryId){
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE p.id = :parentCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("parentCategoryId", parentCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**子カテゴリID検索のページ数取得.
	 * @param childCategoryId 子カテゴリID
	 * @return　ページ数
	 */
	public Integer countByChildCategoryId(Integer childCategoryId){
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE c.id = :childCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("childCategoryId", childCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**孫カテゴリID検索のページ数取得.
	 * @param grandsonCategoryId 孫カテゴリID
	 * @return　ページ数
	 */
	public Integer countByGrandsonCategoryId(Integer grandsonCategoryId){
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE g.id = :grandsonCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("grandsonCategoryId", grandsonCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	
	/**商品名・ブランド名検索のページ数取得.
	 * @param name　商品名
	 * @param brand　ブランド名
	 * @return　ページ数
	 */
	public Integer countByNameBrand(String name,String brand){
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.brand ILIKE :brand "
					+ "AND i.name ILIKE :name");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("brand", "%" + brand + "%");
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**商品名・親カテゴリID検索のページ数取得
	 * @param name　商品名
	 * @param parentCategoryId　親カテゴリID
	 * @return　ページ数
	 */
	public Integer countByNameParentCategoryId(String name, Integer parentCategoryId) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND p.id = :parentCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("parentCategoryId", parentCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**商品名・子カテゴリ検索のページ数取得.
	 * @param name　商品名
	 * @param childCategoryId　子カテゴリ
	 * @return　ページ数
	 */
	public Integer countByNameChildCategoryId(String name, Integer childCategoryId) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append(" WHERE i.name ILIKE :name "
					+ "AND c.id = :childCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("childCategoryId", childCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**商品名・孫カテゴリ検索のページ数取得.
	 * @param name　商品名
	 * @param grandsonCategoryId　孫カテゴリ
	 * @return　ページ数
	 */
	public Integer countByNameGrandsonCategoryId(String name, Integer grandsonCategoryId) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND g.id = :grandsonCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("grandsonCategoryId", grandsonCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	
	/**親カテゴリID・ブランドID検索のページ数取得
	 * @param brand　ブランド名
	 * @param parentCategoryId　親カテゴリID
	 * @return　ページ数
	 */
	public Integer countByParentCategoryIdBrand(String brand, Integer parentCategoryId) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.brand ILIKE :brand "
				+ "AND p.id = :parentCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%")
															  .addValue("parentCategoryId", parentCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**子カテゴリID・ブランド名検索のページ数取得
	 * @param brand　ブランド名
	 * @param childCategoryId　子カテゴリID
	 * @return　ページ数
	 */
	public Integer countByChildCategoryIdBrand(String brand, Integer childCategoryId) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.brand ILIKE :brand "
				+ "AND c.id = :childCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%")
															  .addValue("childCategoryId", childCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**ブランド名・孫カテゴリID検索のページ数取得.
	 * @param brand ブランド名
	 * @param grandsonCategoryId　孫カテゴリID
	 * @return　ページ数
	 */
	public Integer countByGrandsonCategoryIdBrand(String brand, Integer grandsonCategoryId) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.brand ILIKE :brand "
				+ "AND g.id = :grandsonCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("brand", "%" + brand + "%")
				.addValue("grandsonCategoryId", grandsonCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	
	
	
	/**商品名・親カテゴリID・ブランド名検索のページ取得
	 * @param name　商品名
	 * @param parentCategoryId　親カテゴリ
	 * @param brand　ブランド名
	 * @return　ページ数
	 */
	public Integer countByNameParentCategoryIdBrand(String name, Integer parentCategoryId, String brand) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND i.brand ILIKE :brand "
				+ "AND p.id = :parentCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("brand", "%" + brand + "%")
															  .addValue("parentCategoryId", parentCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**商品名・子カテゴリID・ブランド名検索のページ数取得.
	 * @param name　商品名
	 * @param childCategoryId　子カテゴリID
	 * @param brand　ブランド名
	 * @return　ページ数
	 */
	public Integer countByNameChildCategoryIdBrand(String name, Integer childCategoryId, String brand) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND i.brand ILIKE :brand "
				+ "AND c.id = :childCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("brand", "%" + brand + "%")
															  .addValue("childCategoryId", childCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
	/**商品名・孫カテゴリID・ブランド名検索ページ数を取得.
	 * @param name　商品名
	 * @param grandsonCategoryId　孫カテゴリID
	 * @param brand　ブランド名
	 * @return ページ数
	 */
	public Integer countByNameGrandsonCategoryIdBrand(String name, Integer grandsonCategoryId, String brand) {
		StringBuilder str = new StringBuilder();
		str.append(countSql);
		str.append("WHERE i.name ILIKE :name "
				+ "AND i.brand ILIKE :brand "
				+ "AND g.id = :grandsonCategoryId ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
															  .addValue("brand", "%" + brand + "%")
															  .addValue("parentCategoryId", grandsonCategoryId);
		countItems = jdbcTemplate.queryForObject(str.toString(), param, Integer.class);
		maxPage = countItems / MAX_COUNT + 1;
		return maxPage;
	}
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 商品を新しく追加.
	 * @param items 商品情報
	 */
	public void addItem(Items items) {
		String sql = "INSERT INTO items (name,price,category_id,brand,condition_id,shopping,description)"
					+ " VALUES(:name,:price,:categoryId,:brand,:conditionId,999,:description);";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", items.getName())
															  .addValue("price", items.getPrice())
															  .addValue("categoryId", items.getCategoryId())
															  .addValue("brand", items.getBrand())
															  .addValue("conditionId", items.getConditionId())
															  .addValue("description", items.getDescription());
		jdbcTemplate.update(sql, param);
	}
	/**
	 * 商品情報を更新.
	 * @param items 商品情報
	 */
	public void updateItem(Items items) {
		String sql = "UPDATE items "
					+ "SET name=:name,price=:price,category_id=:categoryId,"
					+ "brand=:brand,condition_id=:conditionId,description=:description "
					+ "WHERE id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", items.getId())
															  .addValue("name", items.getName())
															  .addValue("price", items.getPrice())
															  .addValue("categoryId", items.getCategoryId())
															  .addValue("brand", items.getBrand())
															  .addValue("conditionId", items.getConditionId())
															  .addValue("description", items.getDescription());
		jdbcTemplate.update(sql, param);
	}
}
