package com.android.inventNow;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.database.DatabaseAdapter;



/**
 * Handles the user registration activity.
 * @author Gil
 */
public class Register extends Activity {
	private Mail m;
	private EditText newUsername;
	private EditText newPassword;
	private EditText newConfiPass;
	private EditText newEmail;
	private Button registerButton;
	private Button clearButton;
	private Button backButton;

	private DatabaseAdapter dbHelper;

	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

        SharedPreferences settings = getSharedPreferences(login.MY_PREFS, 0);
        Editor editor = settings.edit();
        editor.putLong("uid", 0);
        editor.commit();
        
        dbHelper = new DatabaseAdapter(this);
        dbHelper.open();
        setContentView(R.layout.register);
        initControls();
	    }

	 /**
	  * Handles interface controls.
	 */
	 private void initControls()
	    {
		 	newUsername = (EditText) findViewById(R.id.nUsername);
		 	newPassword = (EditText) findViewById(R.id.nPassword);
		 	newConfiPass = (EditText) findViewById(R.id.nConfiPass);
		 	newEmail = (EditText) findViewById(R.id.nEmail);
		 	registerButton = (Button) findViewById(R.id.nRegister);
		 	clearButton = (Button) findViewById(R.id.nClear);
		 	backButton = (Button) findViewById(R.id.nBack);

		 	registerButton.setOnClickListener(new Button.OnClickListener() { 
		 		public void onClick (View v){ 
		 			RegisterMe(v); }});

		 	clearButton.setOnClickListener(new Button.OnClickListener() { 
	    		public void onClick (View v){ 
	    			ClearForm(); }});

		 	backButton.setOnClickListener(new Button.OnClickListener() { 
	    		public void onClick (View v){ 
	    			BackToLogin(); }});
	    }

	 /**
     * Clears the registration fields.
     */
	 private void ClearForm()
	    {
	    	saveLoggedInUId(0, "", "", "");
	    	newUsername.setText("");
	    	newPassword.setText("");
	    	newConfiPass.setText("");
	    	newEmail.setText("");
	    }

	 /**
	  * Takes user back to login.
	  */
	  private void BackToLogin()
	    {	
	    	finish();
	    }

	  /**
	   * Handles the registration process.
	   * @param v
	   */
	 @SuppressWarnings("deprecation")
	private void RegisterMe(View v)
	    {
		 		 	
		 	
		 	//Get user details. 
	    	String username = newUsername.getText().toString();
	    	String password = newPassword.getText().toString();
	    	String confirmpassword = newConfiPass.getText().toString();
	    	String email = newEmail.getText().toString();

	    	//Check if all fields have been completed.
	    	if (username.equals("") || password.equals("") || email.equals("")){
	    		Toast.makeText(getApplicationContext(), 
	    				"Please ensure all fields have been completed.",
				          Toast.LENGTH_SHORT).show();
	  		return;
	    	}

	    	//Check password match. 
	    	if (!password.equals(confirmpassword)) {
	    		Toast.makeText(getApplicationContext(), 
	    				"The password does not match.",
				          	Toast.LENGTH_SHORT).show();
	    					newPassword.setText("");
	    					newConfiPass.setText("");
	    		return;
	    	}

	    	//Encrypt password with MD5.
	    	password = md5(password);

	    	//Check database for existing users.
	    	Cursor user = dbHelper.fetchUser(username, password);
	    	if (user == null) {
	    		Toast.makeText(getApplicationContext(), "Database query error",
				          Toast.LENGTH_SHORT).show();
	    	} else {
	    		startManagingCursor(user);

	    		//Check for duplicate usernames
	    		if (user.getCount() > 0) {
	    			Toast.makeText(getApplicationContext(), "The username is already registered",
	  			          Toast.LENGTH_SHORT).show();
	    			stopManagingCursor(user);
	        		user.close();
	    			return;
	    		}
	    		stopManagingCursor(user);
	    		user.close();
	    		user = dbHelper.fetchUser(username, password);
	        	if (user == null) {
	        		Toast.makeText(getApplicationContext(), "Database query error",
	  			          Toast.LENGTH_SHORT).show();
	        		return;
	        	} else {
	        		startManagingCursor(user);

	        		if (user.getCount() > 0) {
	        			Toast.makeText(getApplicationContext(), "The username is already registered",
	        			          Toast.LENGTH_SHORT).show();
	        			stopManagingCursor(user);
	            		user.close();
	        			return;
	        		}
	        		stopManagingCursor(user);
	        		user.close();
	        	}
	        	//Create the new username.
	    		long id = dbHelper.createUser(username, password, email);
	    		if (id > 0) {
	    			Toast.makeText(getApplicationContext(), "Your username was created",
	    			          Toast.LENGTH_SHORT).show();
	    			saveLoggedInUId(id, username, newPassword.getText().toString(),newEmail.getText().toString());
	    			Intent i = new Intent(v.getContext(),Helloworld.class);	    	        
	    	        
	    			
	    			
	    			startActivity(i);	
	    			
	    			//get user info for email
	    		 	final String eUsername = newUsername.getText().toString();
	    		 	final String ePassword = newPassword.getText().toString();
	    		 	final String eEmail = newEmail.getText().toString();
	    			final String body ="Thank you, "+eUsername
	    					+", for your registering with InventNow!\n"
	    					+"Please view your info below.\n\n"
	    					+"\n\n"
	    					+"Username: "+eUsername+"\n"
	    					+"Email registered with InventNow: "+eEmail+"\n"
	    					+"Password created with InventNow: "+ePassword+"\n"
	    					+"\n"
	    					+"Once again welcome to the new exciting age of Inventory!!!\n"
	    					+"Your registeration with Invent Now is greatly appreciated!\n\n"
	    					+"Sincerely,\n\n"
	    					+"The InventNow Team";
	    	        /*place email activity into this position */
	    			new Thread(){ 
	    				
	    				
	                	public void run(){ 
	                	    try {  
	                	    		
	                	    
	                	    	
	                	    Sender sender = new Sender("inventnow.app@gmail.com", "inventnow");
	                            sender.sendMail("Thank you for registering with InventNow",   
	                                    body,   
	                                    "inventNow.app@gmail.com",   
	                                    eEmail);
	                          
	                        } catch (Exception e) {   
	                            Log.e("SendMail error", e.getMessage(), e);   
	                        } 
	                	}
	                    }.start();
	    	        
	    	        
	    	    
	    			

		    		finish();
	    		} else {
	    			Toast.makeText(getApplicationContext(), "Fail to create new username",
	    			          Toast.LENGTH_SHORT).show();
	    		}
	    	}
	    }

	 private void saveLoggedInUId(long id, String username, String password, String email) {
			SharedPreferences settings = getSharedPreferences(login.MY_PREFS, 0);
			Editor editor = settings.edit();
			editor.putLong("uid", id);
			editor.putString("username", username);
			editor.putString("password", password);
			editor.putString("email", email);
			editor.commit();
	}
	/**
	 * Hashes the password with MD5.  
	 * @param s
	 * @return
	 */
	private String md5(String s) {
	    try {

	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();


	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        return s;
	    }
	}
}
