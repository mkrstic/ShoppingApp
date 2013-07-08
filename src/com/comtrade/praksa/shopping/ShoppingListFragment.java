package com.comtrade.praksa.shopping;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.comtrade.praksa.shopping.dao.ProductContract;
import com.comtrade.praksa.shopping.dao.ShoppingCursorAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class ShoppingListActivity.
 */
public class ShoppingListFragment extends ListFragment implements
		OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	/** The Constant REQUEST_NEW_PRODUCT. */
	private static final int REQUEST_NEW_PRODUCT = 1;

	/** The add product btn. */
	private Button addProductBtn;

	/** The adapter. */
	private ShoppingCursorAdapter adapter;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		adapter = new ShoppingCursorAdapter(this, null, 0);
		getLoaderManager().initLoader(0, null, this);
		setListAdapter(adapter);
		addProductBtn.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.list_content, container, false);
		addProductBtn = (Button) root.findViewById(R.id.add_product);
		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
	 * android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(getActivity(),
				ProductContract.CONTENT_URI,
				ShoppingCursorAdapter.DEFAULT_PRODUCTS_PROJECTION, null, null,
				null);
		return loader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
	 * .support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
	 * .support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ShoppingListFragment.REQUEST_NEW_PRODUCT:
			if (resultCode == Activity.RESULT_OK) {
				onCreate(null);
			}
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.activity_shoping_list, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean consumed = true;
		switch (item.getItemId()) {
		case R.id.menu_sort:
			sortProducts();
			break;
		default:
			consumed = false;
		}
		return consumed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_product:
			startAddProductActivity();
			break;
		default:
			break;
		}
	}

	/**
	 * Start add product activity.
	 */
	private void startAddProductActivity() {
		Intent intent = new Intent(getActivity(), AddProductActivity.class);
		startActivityForResult(intent, ShoppingListFragment.REQUEST_NEW_PRODUCT);
	}

	/**
	 * Sort products.
	 */
	private void sortProducts() {
		adapter.sortProductsByImportance();
		getListView().setSelectionAfterHeaderView();
	}


}
