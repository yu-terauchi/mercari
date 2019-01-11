package com.rakus.items.domain;

/**
 * カテゴリを表すエンティティ .
 * 
 * @author yu.terauchi
 *
 */
public class Category {
	/** ID */
	private Integer id;
	/** 親カテゴリ */
	private Integer parent_id;
	/** カテゴリ名 */
	private String categoryName;
	/** 分解前データ */
	private String nameAll;

	public Category() {
	}

	public Category(Integer id, Integer parent_id, String categoryName, String nameAll) {
		super();
		this.id = id;
		this.parent_id = parent_id;
		this.categoryName = categoryName;
		this.nameAll = nameAll;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", parent_id=" + parent_id + ", categoryName=" + categoryName + ", nameAll="
				+ nameAll + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getNameAll() {
		return nameAll;
	}

	public void setNameAll(String nameAll) {
		this.nameAll = nameAll;
	}

}
