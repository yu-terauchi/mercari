package com.rakus.items.repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.rakus.items.domain.Category;
import com.rakus.items.domain.CategoryFamily;
import com.rakus.items.domain.Original;

/**
 * 商品情報を読み込むリポジトリ.
 * 
 * @author yu.terauchi
 *
 */
@Repository
public class CategoryRepository {

	/**
	 * オリジナルテーブルからカテゴリ情報を取得する際のマッパー.
	 */
	private final static RowMapper<Original> ORIGINAL_ROW_MAPPER = (rs, i) -> {
		Original original = new Original();
		original.setCategoryName(rs.getString("category_name"));
		return original;
	};
	private final static RowMapper<Category> CATEGORY_ROW_MAPPER = (rs, i) -> {
		Category category = new Category();
		category.setId(rs.getInt("id"));
		category.setParentId(rs.getInt("parent_id"));
		category.setCategoryName(rs.getString("category_name"));
		category.setNameAll(rs.getString("name_all"));
		return category;
	};
	private final static RowMapper<Category> MIN_CATEGORY_ROW_MAPPER = (rs, i) -> {
		Category category = new Category();
		category.setId(rs.getInt("id"));
		category.setParentId(rs.getInt("parent_id"));
		category.setCategoryName(rs.getString("category_name"));
		return category;
	};

	private static final RowMapper<CategoryFamily> CATEGORY_FAMILY_ROW_MAPPER = (rs, i) -> {
		CategoryFamily categoryFamily = new CategoryFamily();
		categoryFamily.setParentId(rs.getInt("parent_id"));
		categoryFamily.setParentCategoryName(rs.getString("p_category_name"));
		categoryFamily.setChildId(rs.getInt("child_id"));
		categoryFamily.setChildCategoryName(rs.getString("c_category_name"));
		categoryFamily.setGrandsonId(rs.getInt("grandson_id"));
		categoryFamily.setGrandsonCategoryName(rs.getString("g_category_name"));
		categoryFamily.setNameAll(rs.getString("name_all"));
		return categoryFamily;
	};

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	/**
	 * オリジナルテーブルからカテゴリ情報を取得.
	 * 
	 * @return カテゴリ情報一覧
	 */
	public List<Original> findCategoryAll() {
		String sql = "SELECT id,category_name FROM original ORDER BY id;";
		List<Original> originalList = jdbcTemplate.query(sql, ORIGINAL_ROW_MAPPER);
		return originalList;
	}

	/**
	 * 一件のカテゴリ情報を取得する.
	 * 
	 * @param id
	 *            カテゴリID
	 * @return カテゴリ情報
	 */
	public Category findById(Integer itemId) {
		String sql = "SELECT id,parent_id,category_name,name_all FROM category WHERE id = :id ORDER BY id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", itemId);
		Category category = jdbcTemplate.queryForObject(sql, param, CATEGORY_ROW_MAPPER);
		return category;
	}

	/**
	 * 親カテゴリの最小情報取得.
	 * 
	 * @return 親カテゴリ情報一覧
	 */
	public List<Category> loadParent() {
		String sql = "SELECT id ,parent_id,category_name "
					+ "FROM category "
					+ "WHERE parent_id is NULL "
					+ "AND name_all is NULL "
					+ "ORDER BY category_name;";
		List<Category> parentList = jdbcTemplate.query(sql, MIN_CATEGORY_ROW_MAPPER);
		return parentList;
	}

	/**
	 * 子カテゴリの最小情報取得.
	 * 
	 * @return 子カテゴリ情報一覧
	 */
	public List<Category> loadChild() {
		//DISTINCT on () で特定のカラムの重複のみを排除することができる
		String sql = "SELECT id,parent_id,category_name  " 
					+ "FROM category " 
					+ "WHERE parent_id is not NULL "
					+ "AND name_all is NULL " 
					+ "ORDER BY category_name;";
		List<Category> childList = jdbcTemplate.query(sql, MIN_CATEGORY_ROW_MAPPER);
		return childList;
	}

	/**
	 * 孫カテゴリの最小情報取得.
	 * 
	 * @return 孫カテゴリ情報一覧
	 */
	public List<Category> loadGrandson() {
		//DISTINCT on () で特定のカラムの重複のみを排除することができる
		String sql = "SELECT id , parent_id,category_name "
				+ "FROM category "
				+ "WHERE parent_id is not NULL "
				+ "AND name_all is not NULL "
				+ "ORDER BY category_name;";
		List<Category> grandsonList = jdbcTemplate.query(sql, MIN_CATEGORY_ROW_MAPPER);
		return grandsonList;
	}

	/**
	 * カテゴリの親子孫関係を取得
	 * 
	 * @return 親子孫カテゴリ情報
	 */
	public List<CategoryFamily> load() {
		String sql = "SELECT p.id AS parent_id , p.category_name AS p_category_name "
				+ ", c.id AS child_id , c.category_name AS c_category_name "
				+ ", g.id AS grandson_id , g.category_name AS g_category_name,g.name_all AS name_all "
				+ "FROM category AS p " + "LEFT OUTER JOIN category AS c ON p.id = c.parent_id "
				+ "LEFT OUTER JOIN category AS g ON c.id = g.parent_id "
				+ "WHERE p.parent_id is NULL AND p.name_all is NULL "
				+ "AND c.parent_id is not NULL AND c.name_all is NULL "
				+ "AND g.parent_id is not NULL AND g.name_all is not NULL " + "ORDER BY p.id;";
		List<CategoryFamily> categoryFamilyList = jdbcTemplate.query(sql, CATEGORY_FAMILY_ROW_MAPPER);
		return categoryFamilyList;
	}

	/**
	 * カテゴリー名で検索する.
	 * 
	 * @param categoryName
	 *            カテゴリ名
	 * @return カテゴリ情報
	 */
	public Category findByCategoryName(String categoryName) {
		String sql = "SELECT id,parent,category_name,name_all FROM category WHERE category_name = :category_name  ORDER BY id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("category_name", categoryName);
		Category category = jdbcTemplate.queryForObject(sql, param, CATEGORY_ROW_MAPPER);
		return category;
	}

	/**
	 * 孫カテゴリを1件検索する.
	 * 
	 * @param categoryId 商品カテゴリID
	 * @return カテゴリー
	 */
	public Category findGrandsonByItemsCategoryId(Integer categoryId) {
		String sql = "SELECT * FROM category WHERE category_id = :categoryId  ORDER BY id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("categoryId", categoryId);
			Category grandson = jdbcTemplate.queryForObject(sql, param, CATEGORY_ROW_MAPPER);
			return grandson;
	}
//
//	/**
//	 * カテゴリ情報を整理して保存.
//	 */
//	public void saveCategory() {
//		// オリジナルテーブルからカテゴリ情報一覧を取得
//		List<Original> originalList = findCategoryAll();
//		// 重複しているname_allの行を省く
//		List<Original> omitOriginalList = new ArrayList<>(new LinkedHashSet<>(originalList));
//		// category_nameを分解した配列をリストに詰める
//		String sql = null;
//		SqlParameterSource param = null;
//		// -------------------------------------------------------------------------------------------------------------------
//		// 重複を省いたカテゴリ情報一覧を「/」で分解して配列にする
//		for (Original original : omitOriginalList) {
//			// nameAllはaaaa/bbbb/ccccのように連なった状態のカテゴリ名
//			String nameAll = original.getCategoryName();
//			if (nameAll != null) {
//				String[] categoryName = nameAll.split("/");
//				// ---------------------------------------------------------------------------------------------------------------
//				// 大カテゴリ
//				Category parentCategory = findParent(categoryName[0]);
//				// 大カテゴリがなかった場合
//				if (parentCategory == null) {
//					sql = "INSERT INTO category(parent_id,category_name,name_all) VALUES(:parent_id,:category_name,:name_all)";
//					param = new MapSqlParameterSource().addValue("parent_id", null)
//							.addValue("category_name", categoryName[0]).addValue("name_all", null);
//					jdbcTemplate.update(sql, param);
//					// 大カテゴリが存在した場合
//				} else {
//					// 中カテゴリ
//					Category childCategory = findChild(categoryName[1], parentCategory.getId());
//					// 中カテゴリが存在しない場合
//					if (childCategory == null) {
//						sql = "INSERT INTO category(parent_id,category_name,name_all) VALUES(:parent_id,:category_name,:name_all)";
//						param = new MapSqlParameterSource().addValue("parent_id", parentCategory.getId())
//								.addValue("category_name", categoryName[1]).addValue("name_all", null);
//						jdbcTemplate.update(sql, param);
//						// 中カテゴリが存在する場合
//					} else {
//						// 小カテゴリ
//						Category grandsonCategory = findGrandson(categoryName[2], childCategory.getId());
//						// 小カテゴリが存在しない場合
//						if (grandsonCategory == null) {
//							sql = "INSERT INTO category(parent_id,category_name,name_all) VALUES(:parent_id,:category_name,:name_all)";
//							param = new MapSqlParameterSource().addValue("parent_id", childCategory.getId())
//									.addValue("category_name", categoryName[2]).addValue("name_all", nameAll);
//							jdbcTemplate.update(sql, param);
//							// 小カテゴリが存在する場合
//						} else {
//							// 何もしないぞ！☆
//						}
//					}
//				}
//			}
//		}
//	}
}
