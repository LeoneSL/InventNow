package com.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
/**
 * Adapts the product database to deal with the front end.
 * 
 * @author Gil
 *
 */
public class ProductAdapter {
	//Table name
	private static final String PRODUCT_TABLE = "products";
	//Table unique id
	public static final String COL_ID = "id";
	
	//Table barcode, title, price, quantity 
	public static final String COL_USERID = "userId";
	public static final String COL_BARCODE = "barcode";
	public static final String COL_TITLE = "title";
	public static final String COL_PRICE = "price";
	public static final String COL_QUANTITY = "quantity";

	private Context context;
	private SQLiteDatabase database;
	private ProductHelper pdHelper;

	/**
	 * The adapter constructor. 
	 * @param context
	 */
	public ProductAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Creates the database helper and gets the database. 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ProductAdapter open() throws SQLException {
		pdHelper = new ProductHelper(context);
		database = pdHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		pdHelper.close();
	}

	/**
	 * Creates the barcode, title, price and quantity.
	 * 
	 * @param barcode.
	 * @param title.
	 * @param price.
	 * @param quantity.
	 * @return
	 */
	public long createProduct(String id, String userId, String barcode, String title, String price, String quantity) {
		ContentValues initialValues = createProductTableContentValues(id, userId, barcode, title, price, quantity);
		return database.insert(PRODUCT_TABLE, null, initialValues);
	}

	/**
	 * Removes a user's details given an id.
	 * 
	 * @param rowId Column id. 
	 * @return
	 */
	public boolean deleteUser(String userId, String barcode) {
		return database.delete(PRODUCT_TABLE, COL_USERID + "=" + userId+ "' AND " + 
				COL_BARCODE + "='" + barcode + "'" , null) > 0;
	}

	public boolean updateUserTable(long rowId, String id, String userId, String barcode, String title, String price, String quantity) {
		ContentValues updateValues = createProductTableContentValues(id, userId, barcode, title, price, quantity);
		return database.update(PRODUCT_TABLE, updateValues, COL_ID + "=" + rowId, null) > 0;
		
		
		/**
		 * Update user item in the table by user id and item barcode table.
		 * 
		 * @return
		 */
		
		
	}public boolean updateUserItemTable(long rowId, String id, String userId, String barcode, String title, String price, String quantity) {
		ContentValues updateValues = createProductTableContentValues(id, userId, barcode, title, price, quantity);
		return database.update(PRODUCT_TABLE, updateValues, COL_ID + "=" + rowId, null) > 0;
	}
	
	
	

	/**
	 * Retrieves the details of all the products stored in the products table.
	 * 
	 * @return
	 */
	public Cursor fetchAllProducts() {
		return database.query(PRODUCT_TABLE, new String[] { COL_ID, COL_BARCODE, 
				COL_TITLE, COL_PRICE, COL_QUANTITY }, null, null, null, null, null);
	}
	
	/**
	 * Retrieves the details of all the  user products stored in the products table.
	 * 
	 * @return
	 */
	public Cursor fetchAllUserProducts(String userId) {
		return database.query(PRODUCT_TABLE, new String[] { COL_ID, COL_USERID, COL_BARCODE, 
				COL_TITLE, COL_PRICE, COL_QUANTITY }, null, null, null, null, null);
	}

	/**
	 * Retrieves the details of a specific user, given a barcode and title .
	 * 
	 * @return
	 */
	public Cursor fetchProduct(String barcode, String title) {
		Cursor myCursor = database.query(PRODUCT_TABLE, 
				new String[] { COL_ID, COL_BARCODE, COL_TITLE }, 
				COL_BARCODE + "='" + barcode + "' AND " + 
				COL_TITLE + "='" + title + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	/**
	 * Retrieves the details of a specific products, given a barcode, title, price and quantity.
	 * 
	 * @return
	 */
	public Cursor fetchUserProduct(String barcode, String userId, String title, String price, String quantity) {
		Cursor myCursor = database.query(PRODUCT_TABLE, 
				new String[] { COL_ID, COL_USERID, COL_BARCODE, COL_TITLE, COL_PRICE, COL_QUANTITY }, 
				COL_BARCODE + "='" + barcode + "' AND " + 
				COL_TITLE + "='" + title + "' AND " + 
				COL_PRICE + "='" + price + "' AND " +
				COL_QUANTITY + "='" + quantity + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	

	/**
	 * Returns the table details given a user id.
	 * @param userId The table user id. 
	 * @return
	 * @throws SQLException
	 */
	public Cursor fetchAllProductByUserId(long userId) throws SQLException {
		Cursor myCursor = database.query(PRODUCT_TABLE, 
				new String[] { COL_ID, COL_USERID, COL_BARCODE, COL_TITLE, COL_PRICE, COL_QUANTITY }, 
				COL_USERID + "=" + userId, null, null, null, null);
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	/**
	 * Stores the barcode, title,price, and quantity upon creation of new login details.
	 * @param barcode The barcode.
	 * @param title The Title.
	 * @param price The Price.
	 * @param quantity The quantity.
	 * @return The entered values. 
	 */
	private ContentValues createProductTableContentValues(String id, String userId, String barcode, String title, String price, String quantity) {
		ContentValues values = new ContentValues();
		values.put(COL_ID, id);
		values.put(COL_USERID, userId);
		values.put(COL_BARCODE, barcode);
		values.put(COL_TITLE, title);
		values.put(COL_PRICE, price);
		values.put(COL_QUANTITY, quantity);
		
		return values;
	}
}