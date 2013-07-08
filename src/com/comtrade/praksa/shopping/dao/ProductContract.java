package com.comtrade.praksa.shopping.dao;

import android.net.Uri;
import android.provider.BaseColumns;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductContract for ContentProvider.
 */
public final class ProductContract {
	
	/** The Constant AUTHORITY. */
	public static final String AUTHORITY = "com.comtrade.praksa.shoppingapp.provider";
	
	/** The Constant CONTENT_URI. */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/product");
	
	/** The Constant CONTENT_TYPE. */
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.shoppingapp.product";
	
	/** The Constant CONTENT_ITEM_TYPE. */
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.shoppingapp.product";
	
	/** The Constant DEFAULT_SORT_ORDER. */
	public static final String DEFAULT_SORT_ORDER = Columns.CREATED_TIMESTAMP + " DESC";
	
	/** The Constant IMPORTANCE_SORT_ORDER. */
	public static final String IMPORTANCE_SORT_ORDER = Columns.IMPORTANCE_LEVEL + " DESC";

	/**
	 * The Class Columns.
	 */
	public static final class Columns implements BaseColumns {
		
		/** The Constant NAME. */
		public static final String NAME = "name";
		
		/** The Constant DESCRIPTION. */
		public static final String DESCRIPTION = "description";
		
		/** The Constant PRICE. */
		public static final String PRICE = "price";
		
		/** The Constant CURRENCY. */
		public static final String CURRENCY = "currency";
		
		/** The Constant CATEGORY_ID. */
		public static final String CATEGORY_ID = "category_id";
		
		/** The Constant CATEGORY_NAME. Not actual column, but used in SQL view */
		public static final String CATEGORY_NAME = "category_name";
		
		/** The Constant LOCATION. */
		public static final String LOCATION = "location";
		
		/** The Constant PRIORITY. */
		public static final String PRIORITY = "priority_level";
		
		/** The Constant WISH. */
		public static final String WISH = "wish_level";
		
		/** The Constant CREATED_TIMESTAMP. */
		public static final String CREATED_TIMESTAMP = "created_timestamp";
		
		/** The Constant IMPORTANCE_LEVEL. */
		public static final String IMPORTANCE_LEVEL = "importance_rate";

		/**
		 * Instantiates a new columns.
		 */
		private Columns() {
		}
	}

	/**
	 * Instantiates a new product contract.
	 */
	private ProductContract() {

	}
}
