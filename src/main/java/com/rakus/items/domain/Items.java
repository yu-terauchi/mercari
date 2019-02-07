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
	/** 孫カテゴリID */
	private Integer categoryId;
	/** ブランド */
	private String brand;
	/** 価格 */
	private Integer price;
	/** 商品運送状況 */ 
	//本来shippingだが、DBもshoppingにしてしまっているので統一
	private Integer shopping;
	/** 説明文 */
	private String description;
	/**親カテゴリ*/
	private Category parent;
	/**子カテゴリ*/
	private Category child;
	/**孫カテゴリ*/
	private Category grandson;

	public Items() {
	}

	public Items(Integer id, String name, Integer conditionId, Integer categoryId, String brand, Integer price,
			Integer shopping, String description, Category parent, Category child, Category grandson) {
		super();
		this.id = id;
		this.name = name;
		this.conditionId = conditionId;
		this.categoryId = categoryId;
		this.brand = brand;
		this.price = price;
		this.shopping = shopping;
		this.description = description;
		this.parent = parent;
		this.child = child;
		this.grandson = grandson;
	}

	@Override
	public String toString() {
		return "Items [id=" + id + ", name=" + name + ", conditionId=" + conditionId + ", categoryId=" + categoryId
				+ ", brand=" + brand + ", price=" + price + ", shopping=" + shopping + ", description=" + description
				+ ", parent=" + parent + ", child=" + child + ", grandson=" + grandson + "]";
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

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
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

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Category getChild() {
		return child;
	}

	public void setChild(Category child) {
		this.child = child;
	}

	public Category getGrandson() {
		return grandson;
	}

	public void setGrandson(Category grandson) {
		this.grandson = grandson;
	}

}
