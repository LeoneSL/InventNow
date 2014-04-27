package com.android.inventNow;

import java.util.List;

import com.android.products.*;

import android.app.Activity;
//import android.app.ListActivity;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;

public class ProductList extends Activity implements OnClickListener {

	//private List<Item> productsList;
	private ItemsDataSource database;
	private ListView lv;
	private ListViewAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
    
        database = new ItemsDataSource(this);
        database.open();
        //get all database items for display
        List<Item> productsList = database.getAllItems(false);
        
        //call custom adapter and display it
        adapter = new ListViewAdapter(this, productsList);
        lv = (ListView) findViewById(R.id.list_view_products);
        lv.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {

		
	}
	
	//refresh the data on hitting back
	@Override
	public void onResume(){
		super.onResume();
		List<Item> productsList = database.getAllItems(false);
        
        adapter = new ListViewAdapter(this, productsList);
        lv = (ListView) findViewById(R.id.list_view_products);
        //ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, productsList);
        lv.setAdapter(adapter);
	}


}
