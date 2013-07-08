package com.comtrade.praksa.shopping.dao;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comtrade.praksa.shopping.R;
import com.comtrade.praksa.shopping.model.Pair;
import com.comtrade.praksa.shopping.model.Product;

// TODO: Auto-generated Javadoc
/**
 * The Class ShoppingCursorAdapter.
 */
public class ShoppingCursorAdapter extends CursorAdapter implements
		OnClickListener {

	/** The Constant DEFAULT_PRODUCTS_PROJECTION. */
	public static final String[] DEFAULT_PRODUCTS_PROJECTION = new String[] {
			ProductContract.Columns._ID, ProductContract.Columns.NAME,
			ProductContract.Columns.DESCRIPTION, ProductContract.Columns.PRICE,
			ProductContract.Columns.CURRENCY, ProductContract.Columns.CATEGORY_ID, 
			ProductContract.Columns.CATEGORY_NAME, ProductContract.Columns.LOCATION, 
			ProductContract.Columns.PRIORITY, ProductContract.Columns.WISH };

	/** The Constant NORMAL_VIEW_TYPE. */
	private static final int NORMAL_VIEW_TYPE = 0;
	
	/** The Constant PRIORITY_VIEW_TYPE. */
	private static final int PRIORITY_VIEW_TYPE = 1;
	
	/** The Constant WISH_VIEW_TYPE. */
	private static final int WISH_VIEW_TYPE = 2;

	/** The context. */
	private final Fragment context;
	
	/**
	 * Instantiates a new shopping cursor adapter.
	 *
	 * @param context the context
	 * @param cursor the cursor
	 * @param flags the flags
	 */
	public <T extends Fragment & LoaderManager.LoaderCallbacks<?>> ShoppingCursorAdapter (T context, Cursor cursor, int flags) {
		super(context.getActivity(), cursor, flags);
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_normal_layout_delete_imagebutton:
		case R.id.product_priority_layout_delete_imagebutton:
		case R.id.product_wish_layout_delete_imagebutton:
			removeProduct((Long) v.getTag());
			break;
		case R.id.product_normal_layout_share_imagebutton:
		case R.id.product_wish_layout_share_imagebutton:
			shareProduct((Long) v.getTag());
			break;
		default:
			break;
		}

	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#getViewTypeCount()
	 */
	@Override
	public int getViewTypeCount() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#getItemViewType(int)
	 */
	@Override
	public int getItemViewType(int position) {
		Cursor cursor = (Cursor) getItem(position);
		return getItemViewType(cursor);
	}

	/**
	 * Gets the item view type.
	 *
	 * @param cursor the cursor
	 * @return the item view type
	 */
	private int getItemViewType(Cursor cursor) {
		int priorityLevel = cursor.getInt(cursor
				.getColumnIndex(ProductContract.Columns.PRIORITY));
		int wishLevel = cursor.getInt(cursor
				.getColumnIndex(ProductContract.Columns.WISH));
		int type = NORMAL_VIEW_TYPE;
		if (priorityLevel > Product.NORMAL_PRIORITY_WISH_LEVEL) {
			type = PRIORITY_VIEW_TYPE;
		} else if (wishLevel > Product.NORMAL_PRIORITY_WISH_LEVEL) {
			type = WISH_VIEW_TYPE;
		}
		return type;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ProductHolder holder = new ProductHolder();
		int itemType = getItemViewType(cursor);
		final Pair<Integer, Integer[]> layoutIds = getLayoutIds(itemType);
		int layoutResourceId = layoutIds.getFirst();
		Integer[] rowIds = layoutIds.getSecond();
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View row = inflater.inflate(layoutResourceId, parent, false);
		holder = new ProductHolder();
		holder.name = (TextView) row.findViewById(rowIds[0]);
		holder.price = (TextView) row.findViewById(rowIds[1]);
		holder.description = (TextView) row.findViewById(rowIds[2]);
		holder.category = (TextView) row.findViewById(rowIds[3]);
		holder.location = (TextView) row.findViewById(rowIds[4]);
		row.setTag(holder);
		ImageButton deleteButton = (ImageButton) row.findViewById(rowIds[5]);
		long productId = cursor.getLong(cursor
				.getColumnIndex(ProductContract.Columns._ID));
		deleteButton.setTag(productId);
		deleteButton.setOnClickListener(this);
		if (rowIds[6] != null) {
			ImageButton shareButton = (ImageButton) row.findViewById(rowIds[6]);
			shareButton.setTag(productId);
			shareButton.setOnClickListener(this);
		}
		return row;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ProductHolder holder = (ProductHolder) view.getTag();
		Product item = Product.fromCursor(cursor);
		holder.name.setText(item.getName());
		holder.price.setText(String.format("%d %s", item.getPrice(),
				item.getCurrency()));
		holder.description.setText(item.getDescription());
		holder.category.setText(item.getCategory().getName());
		if (TextUtils.isEmpty(item.getLocation())) {
			holder.location.setText(context
					.getString(R.string.product_no_location_placeholder));
		} else {
			holder.location.setText(item.getLocation());
		}
	}

	/**
	 * Removes the product by ID.
	 *
	 * @param id the id
	 */
	private void removeProduct(long id) {
		Uri productUri = ContentUris.withAppendedId(
				ProductContract.CONTENT_URI, id);
		int count = context.getActivity().getContentResolver().delete(productUri, null, null);
		if (count > 0) {
			context.getLoaderManager().restartLoader(0, null, (LoaderCallbacks<?>)context);
			Toast.makeText(context.getActivity(),
					context.getString(R.string.remove_product_success_message),
					Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(context.getActivity(),
					context.getString(R.string.remove_product_failed_message),
					Toast.LENGTH_SHORT).show();
		}
	}
	

	/**
	 * Share product.
	 *
	 * @param id the id
	 */
	private void shareProduct(long id) {
		Uri productUri = ContentUris.withAppendedId(
				ProductContract.CONTENT_URI, id);
		Cursor cursor = context.getActivity().getContentResolver().query(productUri,
				DEFAULT_PRODUCTS_PROJECTION, null, null, null);
		if (cursor.moveToFirst()) {
			Product product = Product.fromCursor(cursor);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(
					Intent.EXTRA_SUBJECT,
					context.getString(R.string.share_product_subject,
							product.getName(), product.getPrice(),
							product.getCurrency()));
			intent.putExtra(Intent.EXTRA_TEXT, context.getString(
					R.string.share_product_body, product.getName(),
					product.getDescription(), product.getLocation(),
					product.getPrice(), product.getCurrency()));
			context.startActivity(Intent.createChooser(intent,
					context.getString(R.string.share_chooser_text)));
		}
		cursor.close();
	}

	/**
	 * Sort products by importance.
	 */
	public void sortProductsByImportance() {
		Loader<Cursor> loader = context.getLoaderManager().getLoader(0);
		CursorLoader cursorLoader = (CursorLoader) loader;
		cursorLoader.setSortOrder(ProductContract.IMPORTANCE_SORT_ORDER);
	}

	/**
	 * Gets the layout ids.
	 *
	 * @param itemType the item type
	 * @return the layout ids
	 */
	private Pair<Integer, Integer[]> getLayoutIds(int itemType) {
		Integer[] rowIds;
		int layoutResourceId;
		switch (itemType) {
		case PRIORITY_VIEW_TYPE:
			layoutResourceId = R.layout.product_priority_layout;
			rowIds = new Integer[] {
					R.id.product_priority_layout_name_textview,
					R.id.product_priority_layout_price_textview,
					R.id.product_priority_layout_desc_textview,
					R.id.product_priority_layout_category_textview,
					R.id.product_priority_layout_location_textview,
					R.id.product_priority_layout_delete_imagebutton, null };
			break;
		case WISH_VIEW_TYPE:
			layoutResourceId = R.layout.product_wish_layout;
			rowIds = new Integer[] { R.id.product_wish_layout_name_textview,
					R.id.product_wish_layout_price_textview,
					R.id.product_wish_layout_desc_textview,
					R.id.product_wish_layout_category_textview,
					R.id.product_wish_layout_location_textview,
					R.id.product_wish_layout_delete_imagebutton,
					R.id.product_wish_layout_share_imagebutton };
			break;
		default: // NORMAL_VIEW_TYPE
			layoutResourceId = R.layout.product_normal_layout;
			rowIds = new Integer[] { R.id.product_normal_layout_name_textview,
					R.id.product_normal_layout_price_textview,
					R.id.product_normal_layout_desc_textview,
					R.id.product_normal_layout_category_textview,
					R.id.product_normal_layout_location_textview,
					R.id.product_normal_layout_delete_imagebutton,
					R.id.product_normal_layout_share_imagebutton };
			break;
		}
		return Pair.create(layoutResourceId, rowIds);
	}

	/**
	 * The Class ProductHolder.
	 */
	static class ProductHolder {

		/** The name. */
		TextView name;

		/** The price. */
		TextView price;

		/** The description. */
		TextView description;

		/** The category. */
		TextView category;

		/** The location. */
		TextView location;
	}

}
