package com.comtrade.praksa.shopping.dao;

import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.comtrade.praksa.shopping.R;
import com.comtrade.praksa.shopping.model.Product;
import com.comtrade.praksa.shopping.util.DataUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class ShoppingDataProvider.
 */
public class ShoppingDataProvider extends ContentProvider {

	/** The Constant DB_NAME. */
	private static final String DB_NAME = "shopping.db";
	
	/** The Constant DB_VERSION. */
	private static final int DB_VERSION = 1;
	
	/** The Constant T_PRODUCTS. Products table */
	private static final String T_PRODUCTS = "products";
	
	/** The Constant V_PRODUCTS. Products view (joined with category) */
	private static final String V_PRODUCTS = "products_view";
	
	/** The Constant T_CATEGORIES. Categories table */
	private static final String T_CATEGORIES = "categories";
	
	/** The Constant uriMatcher. */
	private static final UriMatcher uriMatcher;
	
	/** The Constant PRODUCT_DIR. */
	private static final int PRODUCT_DIR = 1;
	
	/** The Constant PRODUCT_ITEM. */
	private static final int PRODUCT_ITEM = 2;
	
	/** The Constant CATEGORY_DIR. */
	private static final int CATEGORY_DIR = 3;
	
	/** The Constant CATEGORY_ITEM. */
	private static final int CATEGORY_ITEM = 4;
	
	/** The db helper. */
	private ProductHelper dbHelper;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(ProductContract.AUTHORITY, "product", PRODUCT_DIR);
		uriMatcher.addURI(ProductContract.AUTHORITY, "product/#", PRODUCT_ITEM);
		uriMatcher.addURI(CategoryContract.AUTHORITY, "category", CATEGORY_DIR);
		uriMatcher.addURI(CategoryContract.AUTHORITY, "category/#",
				CATEGORY_ITEM);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		Context context = getContext();
		dbHelper = new ProductHelper(context);
		return dbHelper != null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		String uriType;
		switch (uriMatcher.match(uri)) {
		case PRODUCT_DIR:
			uriType = ProductContract.CONTENT_TYPE;
			break;
		case PRODUCT_ITEM:
			uriType = ProductContract.CONTENT_ITEM_TYPE;
			break;
		case CATEGORY_DIR:
			uriType = CategoryContract.CONTENT_TYPE;
			break;
		case CATEGORY_ITEM:
			uriType = CategoryContract.CONTENT_ITEM_TYPE;
			break;
		default:
			uriType = null;
		}
		return uriType;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor results;
		switch (uriMatcher.match(uri)) {
		case PRODUCT_DIR:
			results = queryProducts(projection, selection, selectionArgs,
					sortOrder);
			break;
		case PRODUCT_ITEM:
			results = queryProduct(uri.getLastPathSegment(), projection);
			break;
		case CATEGORY_DIR:
			results = queryCategories(projection, selection, selectionArgs,
					sortOrder);
			break;
		case CATEGORY_ITEM:
			results = queryCategory(uri.getLastPathSegment(), projection);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		results.setNotificationUri(getContext().getContentResolver(), uri);
		return results;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri result;
		switch (uriMatcher.match(uri)) {
		case PRODUCT_DIR:
			result = insertProduct(values);
			break;
		case CATEGORY_DIR:
			result = insertCategory(values);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count;
		switch (uriMatcher.match(uri)) {
		case PRODUCT_DIR:
			count = updateProducts(values, selection, selectionArgs);
			break;
		case PRODUCT_ITEM:
			count = updateProduct(uri.getLastPathSegment(), values);
			break;
		case CATEGORY_DIR:
			count = updateCategories(values, selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			count = updateCategory(uri.getLastPathSegment(), values);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count;
		switch (uriMatcher.match(uri)) {
		case PRODUCT_DIR:
			count = deleteProducts(selection, selectionArgs);
			break;
		case PRODUCT_ITEM:
			count = deleteProduct(uri.getLastPathSegment());
			break;
		case CATEGORY_DIR:
			count = deleteCategories(selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			count = deleteCategory(uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	/**
	 * Query for single product. Calls SQL view 
	 *
	 * @param id the id
	 * @param projection the projection
	 * @return the cursor
	 */
	private Cursor queryProduct(String id, String[] projection) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(V_PRODUCTS);
		queryBuilder.appendWhere(ProductContract.Columns._ID + "=" + id);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, null, null, null,
				null, null);
		return cursor;
	}

	/**
	 * Query for many products. Calls SQL view 
	 *
	 * @param projection the projection
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param sortOrder the sort order
	 * @return the cursor
	 */
	private Cursor queryProducts(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(V_PRODUCTS);
		String orderBy = TextUtils.isEmpty(sortOrder) ? ProductContract.DEFAULT_SORT_ORDER
				: sortOrder;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, orderBy);
		return cursor;
	}

	/**
	 * Query for single category.
	 *
	 * @param id the id
	 * @param projection the projection
	 * @return the cursor
	 */
	private Cursor queryCategory(String id, String[] projection) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(T_CATEGORIES);
		queryBuilder.appendWhere(CategoryContract.Columns._ID + "=" + id);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, null, null, null,
				null, null);
		return cursor;
	}

	/**
	 * Query for many categories.
	 *
	 * @param projection the projection
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param sortOrder the sort order
	 * @return the cursor
	 */
	private Cursor queryCategories(String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(T_CATEGORIES);
		String orderBy = TextUtils.isEmpty(sortOrder) ? CategoryContract.DEFAULT_SORT_ORDER
				: sortOrder;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, orderBy);
		return cursor;
	}

	/**
	 * Insert product into database.
	 *
	 * @param values the values
	 * @return the uri
	 */
	private Uri insertProduct(ContentValues values) {
		values.put(ProductContract.Columns.IMPORTANCE_LEVEL, Product
				.calcProductImportance(
						values.getAsInteger(ProductContract.Columns.PRIORITY),
						values.getAsInteger(ProductContract.Columns.WISH)));
		values.put(ProductContract.Columns.CREATED_TIMESTAMP,
				System.currentTimeMillis());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(T_PRODUCTS, null, values);
		Uri uri = null;
		if (rowId > 0) {
			uri = ContentUris
					.withAppendedId(ProductContract.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return uri;
	}

	/**
	 * Insert category into database.
	 *
	 * @param values the values
	 * @return the uri
	 */
	private Uri insertCategory(ContentValues values) {
		values.put(CategoryContract.Columns.CREATED_TIMESTAMP,
				System.currentTimeMillis());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(T_CATEGORIES, null, values);
		Uri uri = null;
		if (rowId > 0) {
			uri = ContentUris.withAppendedId(CategoryContract.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return uri;
	}

	/**
	 * Update product.
	 *
	 * @param id the id
	 * @param values the values
	 * @return the int
	 */
	private int updateProduct(String id, ContentValues values) {
		String whereClause = ProductContract.Columns._ID + "=" + id;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.update(T_PRODUCTS, values, whereClause, null);
		return count;
	}

	/**
	 * Update products.
	 *
	 * @param values the values
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @return the int
	 */
	private int updateProducts(ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.update(T_PRODUCTS, values, selection, selectionArgs);
		return count;
	}

	/**
	 * Update category.
	 *
	 * @param id the id
	 * @param values the values
	 * @return the int
	 */
	private int updateCategory(String id, ContentValues values) {
		String whereClause = CategoryContract.Columns._ID + "=" + id;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.update(T_CATEGORIES, values, whereClause, null);
		return count;
	}

	/**
	 * Update categories.
	 *
	 * @param values the values
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @return the int
	 */
	private int updateCategories(ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.update(T_CATEGORIES, values, selection, selectionArgs);
		return count;
	}

	/**
	 * Delete product.
	 *
	 * @param id the id
	 * @return the int
	 */
	private int deleteProduct(String id) {
		String whereClause = ProductContract.Columns._ID + "=" + id;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.delete(T_PRODUCTS, whereClause, null);
		return count;
	}

	/**
	 * Delete products.
	 *
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @return the int
	 */
	private int deleteProducts(String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.delete(T_PRODUCTS, selection, selectionArgs);
		return count;
	}

	/**
	 * Delete category.
	 *
	 * @param id the id
	 * @return the int
	 */
	private int deleteCategory(String id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String whereClause = CategoryContract.Columns._ID + "=" + id;
		int count = db.delete(T_CATEGORIES, whereClause, null);
		return count;
	}

	/**
	 * Delete categories.
	 *
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @return the int
	 */
	private int deleteCategories(String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.delete(T_CATEGORIES, selection, selectionArgs);
		return count;
	}

	/**
	 * The Class ProductHelper. onCreate reads DDL queries from resource file
	 */
	private class ProductHelper extends SQLiteOpenHelper {

		/**
		 * Instantiates a new product helper.
		 *
		 * @param context the context
		 */
		public ProductHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				insertSqlFromResource(db, R.raw.db_shopping_create);
			} catch (IOException ex) {
				Log.d("ProductHelper", "onCreate exception", ex);
			}
		}

		/* (non-Javadoc)
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				insertSqlFromResource(db, R.raw.db_shopping_drop);
			} catch (IOException ex) {
				Log.d("ProductHelper", "onUpgrade exception", ex);
			}
			onCreate(db);
		}

		/**
		 * Insert sql from resource.
		 *
		 * @param db the db
		 * @param resourceId the resource id
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		private void insertSqlFromResource(SQLiteDatabase db, int resourceId)
				throws IOException {
			String dbCreate = DataUtil.getInstance().readFileToString(
					getContext(), resourceId);
			for (String sqlCommand : dbCreate.split(";")) {
				db.execSQL(sqlCommand);
			}
		}

	}

}
