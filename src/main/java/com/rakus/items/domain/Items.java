package com.rakus.items.domain;

/**
 * 商品を表すエンティティ.
 * 
 * @author yu.terauchi
 *
 */
public class Items {
	/** ID */
	private Integer id;
	/** 商品名 */
	private String name;
	/** 状態 */
	private Integer conditionId;
	/** カテゴリ */
	private String categoryName;
	/** ブランド */
	private String brand;
	/** 価格 */
	private Integer price;
	/** よくわからん */
	private Integer shopping;
	/** 説明文 */
	private String description;

	public Items() {}

	public Items(Integer id, String name, Integer conditionId, String categoryName, String brand, Integer price,
			Integer shopping, String description) {
		super();
		this.id = id;
		this.name = name;
		this.conditionId = conditionId;
		this.categoryName = categoryName;
		this.brand = brand;
		this.price = price;
		this.shopping = shopping;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Items [id=" + id + ", name=" + name + ", conditionId=" + conditionId + ", categoryName=" + categoryName
				+ ", brand=" + brand + ", price=" + price + ", shopping=" + shopping + ", description=" + description
				+ "]";
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

	public Integer getConditionId() {
		return conditionId;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getShopping() {
		return shopping;
	}

	public void setShopping(Integer shopping) {
		this.shopping = shopping;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
