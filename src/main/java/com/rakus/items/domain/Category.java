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
	private Integer parentId;
	/** カテゴリ名 */
	private String categoryName;
	/** 分解前データ */
	private String nameAll;

	public Category() {
	}

	public Category(Integer id, Integer parentId, String categoryName, String nameAll) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.categoryName = categoryName;
		this.nameAll = nameAll;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", parentId=" + parentId + ", categoryName=" + categoryName + ", nameAll="
				+ nameAll + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
