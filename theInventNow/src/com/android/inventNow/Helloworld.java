package com.android.inventNow;

import java.math.BigDecimal;

import com.android.database.DatabaseAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/***
 * Simple Helloworld welcome to InventNow. 
 * @author gil
 *
 */
public class Helloworld extends Activity {
	
	public static final String MY_PREFS = "SharedPreferences";
	private DatabaseAdapter dbHelper;
	public TextView name;
	public TextView displayname;
	private Button logoutButton;
	private Button inventNowButton;
	private Button settingsButton;
	private Button productsButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences mySharedPreferences = getSharedPreferences(MY_PREFS, 0);
        Editor editor = mySharedPreferences.edit();
        editor.putLong("uid", 0);
        editor.commit();
        
        dbHelper = new DatabaseAdapter(this);
        dbHelper.open();
        
       
          
        
        
        
        setContentView(R.layout.hello);
        initControls();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	
	    
	public void initControls(){
		name = (TextView)findViewById(R.id.nameValue);
		displayname = (TextView)findViewById(R.id.displayName);
		
		logoutButton = (Button) findViewById(R.id.Logout);
		inventNowButton = (Button) findViewById(R.id.inventNow);
		settingsButton = (Button) findViewById(R.id.hSettings);
		productsButton = (Button) findViewById(R.id.Products);
		
		
		
		
		logoutButton.setOnClickListener(new Button.OnClickListener(){
    		public void onClick (View v){
    			Exit();
    		}
    	});
		
		/**
	     * Sends user to menu for Add or removing Products.
	     */
	    
		inventNowButton.setOnClickListener(new Button.OnClickListener(){
    		public void onClick (View v){
    			Intent i = new Intent(Helloworld.this, ProductUpdate.class);
    	    	startActivity(i);
    		}
    	});
		
		
		
		//display user name in Hello, Welcome 'user' 
    	SharedPreferences prefs = getSharedPreferences(MY_PREFS, 0);
    	String thisUsername = prefs.getString("username", "");
    	displayname.setText(thisUsername);
    	
    		
		
	}
	
	
	 /**
     * Deals with Exit option - exits the application.
     */
    private void Exit()
    {
    	finish();
    }
    
    //need private void products
    //need private void settings
    
    

    
	
}


