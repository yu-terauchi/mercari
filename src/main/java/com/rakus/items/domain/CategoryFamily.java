package com.rakus.items.domain;

/**
 * 親子孫の関連を保存するドメイン.
 * 
 * @author yu.terauchi
 *
 */
public class CategoryFamily {
	/** 親カテゴリID */
	private Integer parentId;
	/** 子カテゴリID */
	private Integer childId;
	/** 孫カテゴリID */
	private Integer grandsonId;
	/** 親カテゴリ名 */
	private String parentCategoryName;
	/** 子カテゴリ名 */
	private String childCategoryName;
	/** 孫カテゴリ名 */
	private String grandsonCategoryName;
	/** 元のカテゴリ名 */
	private String nameAll;

	public CategoryFamily() {
	}

	public CategoryFamily(Integer parentId, Integer childId, Integer grandsonId, String parentCategoryName,
			String childCategoryName, String grandsonCategoryName, String nameAll) {
		super();
		this.parentId = parentId;
		this.childId = childId;
		this.grandsonId = grandsonId;
		this.parentCategoryName = parentCategoryName;
		this.childCategoryName = childCategoryName;
		this.grandsonCategoryName = grandsonCategoryName;
		this.nameAll = nameAll;
	}

	@Override
	public String toString() {
		return "CategoryFamily [parentId=" + parentId + ", childId=" + childId + ", grandsonId=" + grandsonId
				+ ", parentCategoryName=" + parentCategoryName + ", childCategoryName=" + childCategoryName
				+ ", grandsonCategoryName=" + grandsonCategoryName + ", nameAll=" + nameAll + "]";
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

	public Integer getGrandsonId() {
		return grandsonId;
	}

	public void setGrandsonId(Integer grandsonId) {
		this.grandsonId = grandsonId;
	}

	public String getParentCategoryName() {
		return parentCategoryName;
	}

	public void setParentCategoryName(String parentCategoryName) {
		this.parentCategoryName = parentCategoryName;
	}

	public String getChildCategoryName() {
		return childCategoryName;
	}

	public void setChildCategoryName(String childCategoryName) {
		this.childCategoryName = childCategoryName;
	}

	public String getGrandsonCategoryName() {
		return grandsonCategoryName;
	}

	public void setGrandsonCategoryName(String grandsonCategoryName) {
		this.grandsonCategoryName = grandsonCategoryName;
	}

	public String getNameAll() {
		return nameAll;
	}

	public void setNameAll(String nameAll) {
		this.nameAll = nameAll;
	}

}
