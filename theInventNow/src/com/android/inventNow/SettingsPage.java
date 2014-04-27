package com.android.inventNow;

import com.android.products.ItemsDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsPage extends Activity implements OnClickListener {

	private Button mDeleteButton;
	private Button mAboutButton;
	private ItemsDataSource database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        mDeleteButton = (Button) findViewById(R.id.del_button);
        mAboutButton = (Button) findViewById(R.id.about_button);
        mDeleteButton.setOnClickListener(this);
        database = new ItemsDataSource(this);
        database.open();
        
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.del_button:
			database.deleteAll();
			//boolean test = database.isEmpty();
			//String str = String.valueOf(test);
			//Log.d("d", "database is empty : " + str);
			showInfoDialog(this, "Success!", "Database was cleared");
			break;
		
		case R.id.about_button:
			Intent i = new Intent(v.getContext(),About.class);
	           startActivity(i);       	
	       	
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
