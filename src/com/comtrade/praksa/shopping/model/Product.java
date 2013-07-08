package com.comtrade.praksa.shopping.model;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.comtrade.praksa.shopping.dao.ProductContract;

// TODO: Auto-generated Javadoc
/**
 * The Class Product.
 */
public class Product implements Serializable {
	
	/**
	 * Calculates product importance.
	 *
	 * @param priorityLevel the priority level
	 * @param wishLevel the wish level
	 * @return the int
	 */
	public static int calcProductImportance(int priorityLevel, int wishLevel) {
		return priorityLevel * 3 + wishLevel * 2;
	}
	
	/**
	 * Builds Product instance from cursor.
	 *
	 * @param cursor the cursor
	 * @return the product
	 */
	public static Product fromCursor(Cursor cursor) {
		if (cursor.isBeforeFirst()) {
			cursor.moveToFirst();
		}
		Product product;
		try {
			Category category = Category.fromCursor(cursor, true);
			product = new Product(
					cursor.getString(cursor
							.getColumnIndex(ProductContract.Columns.NAME)),
					cursor.getInt(cursor
							.getColumnIndex(ProductContract.Columns.PRICE)),
					cursor.getString(cursor
							.getColumnIndex(ProductContract.Columns.CURRENCY)),
					cursor.getString(cursor
							.getColumnIndex(ProductContract.Columns.DESCRIPTION)),
					category, 
					cursor.getString(cursor
							.getColumnIndex(ProductContract.Columns.LOCATION)),
					cursor.getInt(cursor
							.getColumnIndex(ProductContract.Columns.PRIORITY)),
					cursor.getInt(cursor
							.getColumnIndex(ProductContract.Columns.WISH)));
			product.id = cursor.getInt(cursor.getColumnIndex(ProductContract.Columns._ID));
		} catch (ValidationException ex) {
			Log.e("Product", "fromCursor", ex);
			product = null;
		}
		return product;
	}
	

	/** The Constant NORMAL_PRIORITY_WISH_LEVEL. */
	public static final int NORMAL_PRIORITY_WISH_LEVEL = 3;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1480295124157021189L;

	/** The id. */
	private Integer id;
	
	/** The name. */
	private String name;

	/** The description. */
	private String description;

	/** The currency. */
	private String currency;

	/** The category. */
	private Category category;

	/** The location. */
	private String location;

	/** The price. */
	private int price;

	/** The priority level. */
	private int priorityLevel;

	/** The wish level. */
	private int wishLevel;

	/**
	 * Instantiates a new product.
	 * 
	 * @param name
	 *            the name
	 * @param price
	 *            the price
	 * @param currency
	 *            the currency
	 * @param description
	 *            the description
	 * @param category
	 *            the category
	 * @param location
	 *            the location
	 * @param priorityLevel
	 *            the priority level
	 * @param wishLevel
	 *            the wish level
	 * @throws ValidationException
	 *             the validation exception
	 */
	public Product(String name, int price, String currency, String description,
			Category category, String location, int priorityLevel, int wishLevel)
			throws ValidationException {
		setName(name);
		setPrice(price);
		setCurrency(currency);
		setDescription(description);
		setCategory(category);
		setLocation(location);
		setPriorityLevel(priorityLevel);
		setWishLevel(wishLevel);
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 * @throws ValidationException
	 *             the validation exception
	 */
	public void setName(String name) throws ValidationException {
		if (TextUtils.isEmpty(name)) {
			throw new ValidationException("Empty name",
					ValidationErrorCode.PRODUCT_NAME_EMTPY);
		}
		this.name = name;
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Sets the price.
	 * 
	 * @param price
	 *            the new price
	 * @throws ValidationException
	 *             the validation exception
	 */
	public void setPrice(int price) throws ValidationException {
		if (price < 0) {
			throw new ValidationException("Negative price",
					ValidationErrorCode.PRODUCT_PRICE_OUT_OF_BOUNDS);
		}
		this.price = price;
	}

	/**
	 * Gets the currency.
	 * 
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency.
	 * 
	 * @param currency
	 *            the new currency
	 * @throws ValidationException
	 *             the validation exception
	 */
	public void setCurrency(String currency) throws ValidationException {
		if (TextUtils.isEmpty(currency)) {
			throw new ValidationException("Empty currency",
					ValidationErrorCode.PRODUCT_CURRENCY_EMPTY);
		}
		this.currency = currency;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 * @throws ValidationException
	 *             the validation exception
	 */
	public void setDescription(String description) throws ValidationException {
		if (TextUtils.isEmpty(description)) {
			throw new ValidationException("Empty description",
					ValidationErrorCode.PRODUCT_DESC_EMPTY);
		}
		this.description = description;
	}

	/**
	 * Gets the category.
	 * 
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category
	 *            the new category
	 * @throws ValidationException
	 *             the validation exception
	 */
	public void setCategory(Category category) throws ValidationException {
		if (category == null || category.getId() == null) {
			throw new ValidationException("Invalid category",
					ValidationErrorCode.CATEGORY_EMPTY);
		}
		this.category = category;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location) {
		if (TextUtils.isEmpty(location)) {
			location = "";
		}
		this.location = location;
	}

	/**
	 * Gets the priority level.
	 * 
	 * @return the priority level
	 */
	public int getPriorityLevel() {
		return priorityLevel;
	}

	/**
	 * Sets the priority level.
	 * 
	 * @param priorityLevel
	 *            the new priority level
	 * @throws ValidationException
	 *             the validation exception
	 */
	public void setPriorityLevel(int priorityLevel) throws ValidationException {
		if (priorityLevel < 1 || priorityLevel > 5) {
			throw new ValidationException("Priority out of bounds 1 to 5",
					ValidationErrorCode.PRODUCT_PRIORITY_OUT_OF_BOUNDS);
		}
		this.priorityLevel = priorityLevel;
	}

	/**
	 * Gets the wish level.
	 * 
	 * @return the wish level
	 */
	public int getWishLevel() {
		return wishLevel;
	}

	/**
	 * Sets the wish level.
	 * 
	 * @param wishLevel
	 *            the new wish level
	 * @throws ValidationException
	 *             the validation exception
	 */
	public void setWishLevel(int wishLevel) throws ValidationException {
		if (wishLevel < 1 || wishLevel > 5) {
			throw new ValidationException("Wish out of bounds 1 to 5",
					ValidationErrorCode.PRODUCT_WISH_OUT_OF_BOUNDS);
		}
		this.wishLevel = wishLevel;
	}
	
	/**
	 * Converts product to content values object.
	 *
	 * @return the content values
	 */
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		if (getId() != null) {
			values.put(ProductContract.Columns._ID, getId());
		}
		values.put(ProductContract.Columns.NAME, getName());
		values.put(ProductContract.Columns.PRICE, getPrice());
		values.put(ProductContract.Columns.CURRENCY, getCurrency());
		values.put(ProductContract.Columns.DESCRIPTION, getDescription());
		values.put(ProductContract.Columns.CATEGORY_ID, getCategory().getId());
		values.put(ProductContract.Columns.LOCATION, getLocation());
		values.put(ProductContract.Columns.PRIORITY, getPriorityLevel());
		values.put(ProductContract.Columns.WISH, getWishLevel());
		return values;
	}

	
	

}
