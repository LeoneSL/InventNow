package com.android.inventNow;

import com.android.products.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.ContentValues;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ProductsDetailedView extends Activity implements OnClickListener {
	
	int amt;
	ItemsDataSource database;
	Item item;
	TextView upc;
	TextView name;
	TextView amount;
	TextView desc;
	ListView lv;
	int id;
	ListViewAdapter lvd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        database = new ItemsDataSource(this);
        database.open();
        
        final ListView lv1 = (ListView) findViewById(R.id.list_view_products);
        lv = lv1;
        
        setContentView(R.layout.product_detail);
        
        
        Bundle data = getIntent().getExtras();
        item = (Item) data.getParcelable("Item");
        upc = (TextView) findViewById(R.id.productTextUPC);
        name = (TextView) findViewById(R.id.productTextName);
        amount = (TextView) findViewById(R.id.productTextAmount);
        desc = (TextView) findViewById(R.id.productTextDesc);
        
        amt = item.getAmount();
        
        Button plus = (Button) findViewById(R.id.buttonplus);
        plus.setOnClickListener(this);
        Button minus = (Button) findViewById(R.id.buttonminus);
        minus.setOnClickListener(this);
        Button update = (Button) findViewById(R.id.buttonConfirm);
        update.setOnClickListener(this);
        Button delete = (Button) findViewById(R.id.buttonDelete);
        delete.setOnClickListener(this);
        
        upc.setText(item.getUPC());
        name.setText(item.getName());
        amount.setText(String.valueOf(amt));
        desc.setText("$"+ String.valueOf(item.getDescription()));
        
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.buttonplus:
			amt+=1;
			amount.setText(String.valueOf(amt));
			break;
		case R.id.buttonminus:
			if(amt == 0)
				break;
			else
				amt -= 1;
			amount.setText(String.valueOf(amt));
			break;
		case R.id.buttonConfirm:
			//Log.d("Called", "confirm button pressed");
			//Log.d("b4db", "amount = " + String.valueOf(amt));
			item.setAmount(amt);
			database.updateItem(item);
			//Intent i = new Intent(this, ProductList.class);
			//this.startActivity(i);
			showInfoDialog(this, "Updated", "Item Quality Successfully Updated To " + String.valueOf(item.getAmount()));
			break;
			
		case R.id.buttonDelete:
			showInfoDialog(this, "Deleting...", "Deleting Item...");
			database.fakeDelete(item);
			Intent i = new Intent(this, Helloworld.class);
			this.startActivity(i);
			break;
			
		}

		
	}
	
	private void showInfoDialog(Context context, String title, String information) {
		new AlertDialog.Builder (context)
		.setMessage(information)
		.setTitle(title)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {


			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		}).show();  
		
	}

}
