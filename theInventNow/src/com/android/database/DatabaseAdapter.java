package com.android.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
/**
 * Adapts the database to deal with the front end.
 * 
 * @author Gil
 *
 */
public class DatabaseAdapter {
	//Table name
	private static final String LOGIN_TABLE = "user";
	//Table unique id
	public static final String COL_ID = "id";
	//Table username, password and email columns 
	public static final String COL_USERNAME = "username";
	public static final String COL_PASSWORD = "password";
	public static final String COL_EMAIL = "email";

	private Context context;
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	/**
	 * The adapter constructor. 
	 * @param context
	 */
	public DatabaseAdapter(Context context) {
		this.context = context;
	}

	/**
	 * Creates the database helper and gets the database. 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DatabaseAdapter open() throws SQLException {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Creates the user name, password and email.
	 * 
	 * @param username The username.
	 * @param password The password.
	 * @param email  The email.
	 * @return
	 */
	public long createUser(String username, String password, String email) {
		ContentValues initialValues = createUserTableContentValues(username, password, email);
		return database.insert(LOGIN_TABLE, null, initialValues);
	}

	/**
	 * Removes a user's details given an id.
	 * 
	 * @param rowId Column id. 
	 * @return
	 */
	public boolean deleteUser(long rowId) {
		return database.delete(LOGIN_TABLE, COL_ID + "=" + rowId, null) > 0;
	}

	public boolean updateUserTable(long rowId, String username, String password, String email) {
		ContentValues updateValues = createUserTableContentValues(username, password, email);
		return database.update(LOGIN_TABLE, updateValues, COL_ID + "=" + rowId, null) > 0;
	}

	/**
	 * Retrieves the details of all the users stored in the login table.
	 * 
	 * @return
	 */
	public Cursor fetchAllUsers() {
		return database.query(LOGIN_TABLE, new String[] { COL_ID, COL_USERNAME, 
				COL_PASSWORD, COL_EMAIL }, null, null, null, null, null);
	}

	/**
	 * Retrieves the details of a specific user, given a username and password .
	 * 
	 * @return
	 */
	public Cursor fetchUser(String username, String password) {
		Cursor myCursor = database.query(LOGIN_TABLE, 
				new String[] { COL_ID, COL_USERNAME, COL_PASSWORD, COL_EMAIL }, 
				COL_USERNAME + "='" + username + "' AND " + 
				COL_PASSWORD + "='" + password + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	/**
	 * Retrieves the email of a specific user, given a username .
	 * 
	 * @return
	 */
	public Cursor fetchUserEmail(String username) {
		Cursor myCursor = database.query(LOGIN_TABLE, 
				new String[] { COL_EMAIL }, 
				COL_USERNAME + "='" + username + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	/**
	 * Retrieves the detailts of user if email, given a email .
	 * 
	 * @return
	 */
	public Cursor fetchEmail(String email) {
		Cursor myCursor = database.query(LOGIN_TABLE, 
				new String[] { COL_EMAIL }, 
				COL_EMAIL + "='" + email + "'", null, null, null, null);

		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}
	
	

	/**
	 * Returns the table details given a row id.
	 * @param rowId The table row id. 
	 * @return
	 * @throws SQLException
	 */
	public Cursor fetchUserById(long rowId) throws SQLException {
		Cursor myCursor = database.query(LOGIN_TABLE, 
				new String[] { COL_ID, COL_USERNAME, COL_PASSWORD, COL_EMAIL }, 
				COL_ID + "=" + rowId, null, null, null, null);
		if (myCursor != null) {
			myCursor.moveToFirst();
		}
		return myCursor;
	}

	/**
	 * Stores the username, password and email upon creation of new login details.
	 * @param username The user name.
	 * @param password The password.
	 * @param email The email.
	 * @return The entered values. 
	 */
	private ContentValues createUserTableContentValues(String username, String password, String email) {
		ContentValues values = new ContentValues();
		values.put(COL_USERNAME, username);
		values.put(COL_PASSWORD, password);
		values.put(COL_EMAIL, email);
		
		return values;
	}
}