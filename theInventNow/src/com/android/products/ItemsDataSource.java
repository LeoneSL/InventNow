package com.android.products;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
//import android.util.Log;

public class ItemsDataSource {

	private SQLiteDatabase database;
	private DatabaseHelper dbHandler;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_UPC,
			DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_AMOUNT, DatabaseHelper.COLUMN_DESC,
			DatabaseHelper.COLUMN_DEL };
	
	public ItemsDataSource(Context context){
		dbHandler = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHandler.getWritableDatabase();
	}
	
	public void close() {
		dbHandler.close();
	}
	
	//add the created item to the database
	public Item addItem(Item item){
		ContentValues values = new ContentValues();
		//insert
		values.put(DatabaseHelper.COLUMN_UPC, item.getUPC());
		values.put(DatabaseHelper.COLUMN_NAME, item.getName());
		values.put(DatabaseHelper.COLUMN_AMOUNT, item.getAmount());
		//values.put(DatabaseHelper.COLUMN_USER, item.getUsername());
		values.put(DatabaseHelper.COLUMN_DESC, item.getDescription());
		values.put(DatabaseHelper.COLUMN_DEL, item.getDelFlag());
		
		long insertID = database.insert(DatabaseHelper.TABLE_ITEMS, null, values);
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, allColumns, 
				DatabaseHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
		
		cursor.moveToFirst();
		Item newItem = cursorToItem(cursor);
		cursor.close();
		return newItem;
	}
	//get attributes for adding item from UI
	public Item createItem(Item item){
		Item newItem = new Item();
		newItem.setUPC(item.getUPC());
		newItem.setName(item.getName());
		newItem.setAmount(item.getAmount());
		newItem.setUsername("UserName");
		newItem.setDescription("Description");
		newItem.setDelFlag(0);
		//add it
		return addItem(newItem);
	}
	
	//search for single item
	public boolean doesItemExistAlready(String UPC){
		
		String[] colUPC = new String[] { "UPC" };
		//String sql = "SELECT UPC from Items WHERE UPC = " + String.valueOf(UPC);
		Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, colUPC, DatabaseHelper.COLUMN_UPC + "=?", new String[] { String.valueOf(UPC) }, null, null, null);
		//Cursor cursor = database.rawQuery(sql, null);
		if(cursor.getCount() == 0)
			return false;
		else
			return true;
	}
	
	//list all items
	public List<Item> getAllItems(boolean getLowItems){
		List<Item> items = new ArrayList<Item>();
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, allColumns, null, null, null, null, null);
		//Cursor cursor = database.rawQuery("SELECT * From " + DatabaseHelper.TABLE_ITEMS, null);
		
		cursor.moveToFirst();
		//get only greater than zero items
		if(getLowItems == false){
			while(!cursor.isAfterLast()){
				Item item = cursorToItem(cursor);
				if(item.getAmount() > 0)
				{
					if(item.getDelFlag() == 0){
						items.add(item);
						cursor.moveToNext();
					}
					else{
						cursor.moveToNext();
					}
				
				}else{
					cursor.moveToNext();
				}
			}
		}
		else{
			while(!cursor.isAfterLast()){
				Item item = cursorToItem(cursor);
				//populate with low item values
				if(item.getAmount() == 0 || item.getAmount() == 1)
				{
					if(item.getDelFlag() == 0){
						items.add(item);
						cursor.moveToNext();
					}
					else{
						cursor.moveToNext();
					}
				}else{
					cursor.moveToNext();
				}
			}
		}
		//close it
		cursor.close();
		return items;
	}
	
	public Item getSingleItem(String UPC){
		Item item = new Item();
		Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, allColumns, "UPC = ?", new String[] { String.valueOf(UPC)}, null, null, null);
		cursor.moveToLast();
		item = cursorToItem(cursor);
		cursor.close();
		return item;
	}
	
	public void updateItem(Item item){
		ContentValues values = new ContentValues();
		if(item.getDelFlag() == 1){
			item.setDelFlag(0);
			values.put(DatabaseHelper.COLUMN_DEL, item.getDelFlag());
		}
		values.put(DatabaseHelper.COLUMN_AMOUNT, item.getAmount());
		database.update(DatabaseHelper.TABLE_ITEMS, values, "UPC =?", new String[] { item.getUPC() });
	}
	
	public void fakeDelete(Item item){
		ContentValues values = new ContentValues();
		item.setDelFlag(1);
		values.put(DatabaseHelper.COLUMN_DEL, item.getDelFlag());
		database.update(DatabaseHelper.TABLE_ITEMS, values, "UPC =?", new String[] {item.getUPC()});
	}
	
	public void deleteItem(Item item){
		String UPC = item.getUPC();
		database.delete(DatabaseHelper.TABLE_ITEMS, UPC, new String[] { item.getUPC()});
	}
	
	public void deleteAll(){
		database.delete(DatabaseHelper.TABLE_ITEMS, null, null);
	}
	
	public boolean isEmpty(){
		String sql = "SELECT COUNT(*) From " + DatabaseHelper.TABLE_ITEMS;
		SQLiteStatement s = database.compileStatement(sql);
		//long count = s.simpleQueryForLong();
		if(s == null){
		return true;
		}else {
			return false;
		}
		
	}
	
	public long getItemCount(){
		String sql = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_ITEMS; //need to get user login info for this
		SQLiteStatement s = database.compileStatement(sql);
		long count = s.simpleQueryForLong();
		return count;
	}
	
	
	private Item cursorToItem(Cursor cursor){
		Item item = new Item();
		item.setID(cursor.getLong(0));
		item.setUPC(cursor.getString(1));
		item.setName(cursor.getString(2));
		item.setAmount(cursor.getInt(3));
		//item.setUsername(cursor.getString(4));
		item.setDescription(cursor.getString(4));
		item.setDelFlag(cursor.getInt(5));
		
		return item;
	}
	
}
