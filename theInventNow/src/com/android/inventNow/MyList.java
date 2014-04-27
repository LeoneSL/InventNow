package com.android.inventNow;

import java.util.List;

import com.android.products.*;

import android.app.Activity;
//import android.app.ListActivity;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;

public class MyList extends Activity implements OnClickListener {

	//private List<Item> productsList;
	private ItemsDataSource database;
	private ListViewAdapter adapter;
	private ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_list);
        
        database = new ItemsDataSource(this);
        database.open();
        //get all database items for display
        List<Item> productsList = database.getAllItems(true);
        
        adapter = new ListViewAdapter(this, productsList);
        lv = (ListView) findViewById(R.id.products_low_view);
        lv.setAdapter(adapter);
        
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResume(){
		super.onResume();
		 List<Item> productsList = database.getAllItems(true);
	        
	     adapter = new ListViewAdapter(this, productsList);
	     lv = (ListView) findViewById(R.id.products_low_view);
	     lv.setAdapter(adapter);
	        
	}


}
