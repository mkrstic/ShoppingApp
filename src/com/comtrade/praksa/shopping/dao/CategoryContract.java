package com.comtrade.praksa.shopping.dao;

import android.net.Uri;
import android.provider.BaseColumns;

// TODO: Auto-generated Javadoc
/**
 * The Class CategoryContract, for ContentProvider purpose
 */
public final class CategoryContract {
	
	/** The Constant AUTHORITY. */
	public static final String AUTHORITY = "com.comtrade.praksa.shoppingapp.provider";
	
	/** The Constant CONTENT_URI - base uri. */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/category");
	
	/** The Constant CONTENT_TYPE. */
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.shoppingapp.category";
	
	/** The Constant CONTENT_ITEM_TYPE. */
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.shoppingapp.category";
	
	/** The Constant DEFAULT_SORT_ORDER. */
	public static final String DEFAULT_SORT_ORDER = Columns.NAME + " ASC";

	/**
	 * The Class Columns.
	 */
	public static final class Columns implements BaseColumns {
		
		/** The Constant NAME column. */
		public static final String NAME = "name";
		
		/** The Constant CREATED_TIMESTAMP column.  */
		public static final String CREATED_TIMESTAMP = "created_timestamp";

		/**
		 * Instantiates a new columns.
		 */
		private Columns() {
		}
	}

	/**
	 * Instantiates a new category contract.
	 */
	private CategoryContract() {

	}
}
