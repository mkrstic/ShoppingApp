package com.comtrade.praksa.shopping.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.comtrade.praksa.shopping.dao.CategoryContract;
import com.comtrade.praksa.shopping.dao.ProductContract;

// TODO: Auto-generated Javadoc
/**
 * The Class Category.
 */
public class Category {

	/**
	 * Builds Category instance from cursor.
	 *
	 * @param cursor the cursor
	 * @param useProductContract use product contract (true) or category contract (false)
	 * @return the category
	 */
	public static Category fromCursor(Cursor cursor, boolean useProductContract) {
		if (cursor.isBeforeFirst()) {
			cursor.moveToFirst();
		}
		String columnId = useProductContract ? ProductContract.Columns.CATEGORY_ID
				: CategoryContract.Columns._ID;
		String columnName = useProductContract ? ProductContract.Columns.CATEGORY_NAME
				: CategoryContract.Columns.NAME;
		int id = cursor.getInt(cursor.getColumnIndex(columnId));
		String name = cursor.getString(cursor.getColumnIndex(columnName));
		Category category;
		try {
			category = new Category(name);
			category.id = id;
		} catch (ValidationException ex) {
			Log.e("Category", "fromCategory", ex);
			category = null;
		}
		return category;
	}
	
	/**
	 * Builds category instance from cursor. Set useProductContract parameter to false
	 *
	 * @param cursor the cursor
	 * @return the category
	 */
	public static Category fromCursor(Cursor cursor) {
		return fromCursor(cursor, false);
	}

	/** The id. */
	private Integer id;
	
	/** The name. */
	private String name;

	/**
	 * Instantiates a new category.
	 *
	 * @param name the name
	 * @throws ValidationException the validation exception
	 */
	public Category(String name) throws ValidationException {
		setName(name);
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
	 * @param name the new name
	 * @throws ValidationException the validation exception
	 */
	public void setName(String name) throws ValidationException {
		if (TextUtils.isEmpty(name)) {
			throw new ValidationException("Empty category",
					ValidationErrorCode.CATEGORY_EMPTY);
		}
		this.name = name;
	}
	
	/**
	 * Converts category to content values object.
	 *
	 * @return the content values
	 */
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		if (getId() != null) {
			values.put(CategoryContract.Columns._ID, getId());
		}
		values.put(CategoryContract.Columns.NAME, getName());
		return values;
	}

}
