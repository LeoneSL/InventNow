package com.android.inventNow;

//import java.util.ArrayList;
import java.util.List;

//import android.app.AlertDialog;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
//import android.os.Parcelable;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.products.Item;
import com.android.products.ItemsDataSource;

public class ListViewAdapter extends BaseAdapter {
	private Context context;
	LayoutInflater inflater;
	ItemsDataSource database;
	private List<Item> products;
	private ViewGroup p;
	
	public ListViewAdapter(Context context, List<Item> items){
		this.context = context;
		this.products = items;
	}
	
	public void updateItem(List<Item> item, int pos){
		//ThreadPreconditions.checkOnMainThread();
		this.products = item;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount(){
		return products.size();
	}

	@Override
	public Item getItem(int position){
		return products.get(position);
	}
	
	@Override
	public long getItemId(int position){
		return position;
	}
	
	//create custom listview ui
	//add whatever else we feel like
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		View v = convertView;
	    p = parent;

		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.listview_text, p, false);
		}
		
		//find text variables
		TextView productName = (TextView) v.findViewById(R.id.product_name);
		TextView productAmount = (TextView) v.findViewById(R.id.product_amount);
		TextView productUPC = (TextView) v.findViewById(R.id.product_upc);
		
		final Item tempItem = getItem(position);
		productName.setText(tempItem.getName());
		productAmount.setText("Count: "+ Integer.toString(tempItem.getAmount()));
		productUPC.setText("UPC = " + tempItem.getUPC());
		
		v.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Log.d("Pressed", "Item at " + position);
				//showInfoDialog(context, "Pressed", "Item Pressed at " + String.valueOf(position));
				Intent intent = new Intent(context, ProductsDetailedView.class);
				intent.putExtra("Item", new Item(tempItem.getID(), tempItem.getUPC(), tempItem.getName(), tempItem.getAmount(), "default", tempItem.getDescription(), tempItem.getDelFlag()));
				intent.putExtra("id", String.valueOf(position));
				//intent.putExtra("Item", new Item(100, "001", "Name", 1, "username", "desc", 0));
				context.startActivity(intent);
			}
			
		});
	    
		return v;
	}

	public void notifyDataSetChanged() {
	    this.notifyDataSetChanged();
	}
/*
	private void showInfoDialog(Context context, String title, String information) {
		new AlertDialog.Builder (context)
		.setMessage(information)
		.setTitle(title)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {


			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		}).show();  
		
	}*/
}