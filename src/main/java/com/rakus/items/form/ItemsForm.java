package com.rakus.items.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * パラメータを受け取るフォーム.
 * 
 * @author yu.terauchi
 *
 */
public class ItemsForm {
	/** 商品ID */
	private Integer id;
	/** 商品名 */
	@NotBlank(message = "商品名を入力してください")
	private String name;
	/** 親カテゴリ名 */
	private Integer parentCategoryId;
	/** 子カテゴリ名 */
	private Integer childCategoryId;
	/** 孫カテゴリ名 */
	private Integer grandsonCategoryId;
	/** ブランド */
	@NotBlank(message = "ブランド名を入力してください")
	private String brand;
	/** 状態 */
	@NotNull(message = "商品状態を選択してください")
	private Integer conditionId;
	/** 価格 */
	@NotNull(message = "商品の価格を入力してください")
	@Pattern(regexp = "^\\d{1,10}$", message = "10桁まで数字で入力してください")
	private String price;
	/** 説明文 */
	@NotBlank(message = "商品説明を入力してください")
	private String description;

	public ItemsForm() {
	}

	public ItemsForm(Integer id, @NotBlank(message = "商品名を入力してください") String name, Integer parentCategoryId,
			Integer childCategoryId, Integer grandsonCategoryId, @NotBlank(message = "ブランド名を入力してください") String brand,
			@NotNull(message = "商品状態を選択してください") Integer conditionId,
			@NotNull(message = "商品の価格を入力してください") @Pattern(regexp = "^\\d{1,10}$", message = "10桁まで数字で入力してください") String price,
			@NotBlank(message = "商品説明を入力してください") String description) {
		super();
		this.id = id;
		this.name = name;
		this.parentCategoryId = parentCategoryId;
		this.childCategoryId = childCategoryId;
		this.grandsonCategoryId = grandsonCategoryId;
		this.brand = brand;
		this.conditionId = conditionId;
		this.price = price;
		this.description = description;
	}

	@Override
	public String toString() {
		return "ItemsForm [id=" + id + ", name=" + name + ", parentCategoryId=" + parentCategoryId
				+ ", childCategoryId=" + childCategoryId + ", grandsonCategoryId=" + grandsonCategoryId + ", brand="
				+ brand + ", conditionId=" + conditionId + ", price=" + price + ", description=" + description + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getConditionId() {
		return conditionId;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
