package com.comtrade.praksa.shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.comtrade.praksa.shopping.dao.CategoryContract;
import com.comtrade.praksa.shopping.dao.ProductContract;
import com.comtrade.praksa.shopping.model.Category;
import com.comtrade.praksa.shopping.model.Product;
import com.comtrade.praksa.shopping.model.ValidationErrorCode;
import com.comtrade.praksa.shopping.model.ValidationException;

// TODO: Auto-generated Javadoc
/**
 * The Class AddProductActivity.
 */
public class AddProductActivity extends Activity implements OnClickListener {

	/** The add btn. */
	private Button addBtn;

	/** The add category button. */
	private Button addCategoryButton;

	/** The name edit text. */
	private EditText nameEditText;

	/** The location edit text. */
	private EditText locationEditText;

	/** The description edit text. */
	private EditText descriptionEditText;

	/** The price edit text. */
	private EditText priceEditText;

	/** The category spinner. */
	private Spinner categorySpinner;

	/** The currency spinner. */
	private Spinner currencySpinner;

	/** The priority level rating bar. */
	private RatingBar priorityLevelRatingBar;

	/** The wish level rating bar. */
	private RatingBar wishLevelRatingBar;

	/** The category adapter. */
	private SimpleCursorAdapter categoryAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);
		bindUIFields();
		loadCategories();
		addBtn.setOnClickListener(this);
		addCategoryButton.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_product_save_button:
			addProduct();
			break;
		case R.id.add_product_new_category_button:
			showNewCategoryDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * Adds the product in database.
	 */
	private void addProduct() {
		Product product;
		try {
			String name = nameEditText.getText().toString();
			Integer price;
			try {
				price = Integer.parseInt(priceEditText.getText().toString());
			} catch (NumberFormatException ex) {
				throw new ValidationException(ex.getMessage(),
						ValidationErrorCode.PRODUCT_PRICE_OUT_OF_BOUNDS);
			}
			String currency = (String) currencySpinner.getSelectedItem();
			String description = descriptionEditText.getText().toString();
			Cursor categoryCursor = (Cursor) categorySpinner.getSelectedItem();
			Category category = Category.fromCursor(categoryCursor);
			String location = locationEditText.getText().toString();
			int priorityLevel = (int) priorityLevelRatingBar.getRating();
			int wishLevel = (int) wishLevelRatingBar.getRating();
			product = new Product(name, price, currency, description, category, location, priorityLevel, wishLevel);
			getContentResolver().insert(ProductContract.CONTENT_URI,
					product.toContentValues());
			Toast.makeText(
					AddProductActivity.this,
					getString(R.string.add_product_saved_message,
							product.getName()), Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
			finish();
		} catch (ValidationException ex) {
			Log.d("AddProductActivity", "addProduct validationex", ex);
			setWidgetError(ex);
		} catch (Exception ex) {
			Log.d("AddProductActivity", "addProduct", ex);
			Toast.makeText(AddProductActivity.this,
					getString(R.string.add_product_not_saved_message),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * Show new category dialog.
	 */
	private void showNewCategoryDialog() {
		AlertDialog.Builder categoryPromptBuilder = new AlertDialog.Builder(
				AddProductActivity.this);
		LayoutInflater li = LayoutInflater.from(AddProductActivity.this);
		View promptsView = li.inflate(R.layout.add_category, null);
		categoryPromptBuilder.setView(promptsView);
		categoryPromptBuilder
				.setMessage(R.string.add_product_new_category_name_title);
		final EditText categoryEditText = (EditText) promptsView
				.findViewById(R.id.add_product_new_category_dialog_edittext);
		categoryPromptBuilder
				.setTitle(R.string.add_product_new_category_dialog_title);
		categoryPromptBuilder.setPositiveButton(getString(R.string.ok_text),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						addCategory(categoryEditText);
					}
				}).setNegativeButton(getString(R.string.cancel_text),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		categoryPromptBuilder.create().show();

	}

	/**
	 * Adds the category in database.
	 *
	 * @param categoryEditText the category edit text
	 */
	private void addCategory(final EditText categoryEditText) {
		final String categoryName = categoryEditText.getText().toString();
		try {
			if (TextUtils.isEmpty(categoryName)) {
				throw new ValidationException("categoryName.empty");
			}
			ContentValues values = new ContentValues();
			values.put(CategoryContract.Columns.NAME, categoryName);
			Uri uri = getContentResolver().insert(CategoryContract.CONTENT_URI, values);
			if (uri == null) {
				throw new Exception("Category insertion failed. Uri = null");
			}
			Cursor newCursor = getContentResolver().query(CategoryContract.CONTENT_URI, null, null, null, null);
			categoryAdapter.changeCursor(newCursor);
			for (int i = 0; i < categorySpinner.getCount(); i++) {
				Cursor cursor = (Cursor) categorySpinner.getItemAtPosition(i);
				if (categoryName.equals(cursor.getString(cursor.getColumnIndex(CategoryContract.Columns.NAME)))) {
					categorySpinner.setSelection(i);
					break;
				}
			}
		} catch (ValidationException ex) {
			Log.d("addCategory", "validation exception", ex);
			Toast.makeText(AddProductActivity.this,
					getString(R.string.validation_product_category_empty),
					Toast.LENGTH_SHORT).show();
		} catch (Exception ex) {
			Log.d("addCategory", "exception", ex);
			Toast.makeText(AddProductActivity.this,
					getString(R.string.add_category_failed), Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Loads available categories into spinner.
	 */
	private void loadCategories() {
		Cursor cursor = getContentResolver().query(
				CategoryContract.CONTENT_URI, null, null, null, null);
		String[] from = new String[] { CategoryContract.Columns.NAME };
		int[] to = new int[] { android.R.id.text1 };
		categoryAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cursor, from, to, 0);
		categoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySpinner.setAdapter(categoryAdapter);
	}

	/**
	 * Sets the validation error on appropriate widget.
	 * 
	 * @param ex
	 *            the new widget error
	 */
	private void setWidgetError(final ValidationException ex) {
		Toast.makeText(AddProductActivity.this,
				getString(R.string.add_product_validation_failed_message),
				Toast.LENGTH_SHORT).show();
		switch (ex.getErrorCode()) {
		case CATEGORY_EMPTY:
			Toast.makeText(AddProductActivity.this,
					getString(R.string.validation_product_category_empty),
					Toast.LENGTH_SHORT).show();
			break;
		case PRODUCT_DESC_EMPTY:
			descriptionEditText
					.setError(getString(R.string.validation_product_desc_empty));
			break;
		case PRODUCT_NAME_EMTPY:
			nameEditText
					.setError(getString(R.string.validation_product_name_empty));
			break;
		case PRODUCT_PRICE_OUT_OF_BOUNDS:
			priceEditText
					.setError(getString(R.string.validation_product_price_out_of_bounds));
			break;
		case PRODUCT_CURRENCY_EMPTY:
			Toast.makeText(AddProductActivity.this,
					getString(R.string.validation_product_currency_empty),
					Toast.LENGTH_SHORT).show();
			break;
		case PRODUCT_PRIORITY_OUT_OF_BOUNDS:
			Toast.makeText(
					AddProductActivity.this,
					getString(R.string.validation_product_priority_out_of_bounds),
					Toast.LENGTH_SHORT).show();
			break;
		case PRODUCT_WISH_OUT_OF_BOUNDS:
			Toast.makeText(AddProductActivity.this,
					getString(R.string.validation_product_wish_out_of_bounds),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	/**
	 * Bind ui fields.
	 */
	private void bindUIFields() {
		addBtn = (Button) findViewById(R.id.add_product_save_button);
		nameEditText = (EditText) findViewById(R.id.add_product_name_edittext);
		locationEditText = (EditText) findViewById(R.id.add_product_location_edittext);
		descriptionEditText = (EditText) findViewById(R.id.add_product_desc_edittext);
		priceEditText = (EditText) findViewById(R.id.add_product_price_edittext);
		currencySpinner = (Spinner) findViewById(R.id.add_product_currency_spinner);
		categorySpinner = (Spinner) findViewById(R.id.add_product_category_spinner);
		priorityLevelRatingBar = (RatingBar) findViewById(R.id.add_product_prioritylevel_ratingbar);
		wishLevelRatingBar = (RatingBar) findViewById(R.id.add_product_wishlevel_ratingbar);
		addCategoryButton = (Button) findViewById(R.id.add_product_new_category_button);
	}
}
