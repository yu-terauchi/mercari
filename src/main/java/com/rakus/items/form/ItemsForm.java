package com.rakus.items.form;

/**
 * パラメータを受け取るフォーム.
 * 
 * @author yu.terauchi
 *
 */
public class ItemsForm {
	/** 商品名 */

	private String name;
	/** 親カテゴリ名 */
	private Integer parentCategoryId;
	/** 子カテゴリ名 */
	private Integer childCategoryId;
	/** 孫カテゴリ名 */
	private Integer grandsonCategoryId;
	/** ブランド */
	private String brand;

	public ItemsForm() {
	}

	public ItemsForm(String name, Integer parentCategoryId, Integer childCategoryId, Integer grandsonCategoryId,
			String brand) {
		super();
		this.name = name;
		this.parentCategoryId = parentCategoryId;
		this.childCategoryId = childCategoryId;
		this.grandsonCategoryId = grandsonCategoryId;
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "ItemsForm [name=" + name + ", parentCategoryId=" + parentCategoryId + ", childCategoryId="
				+ childCategoryId + ", grandsonCategoryId=" + grandsonCategoryId + ", brand=" + brand + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Integer parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public Integer getChildCategoryId() {
		return childCategoryId;
	}

	public void setChildCategoryId(Integer childCategoryId) {
		this.childCategoryId = childCategoryId;
	}

	public Integer getGrandsonCategoryId() {
		return grandsonCategoryId;
	}

	public void setGrandsonCategoryId(Integer grandsonCategoryId) {
		this.grandsonCategoryId = grandsonCategoryId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

}
