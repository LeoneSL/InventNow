package com.android.inventNow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.database.DatabaseAdapter;
import com.android.database.ProductAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProductUpdate extends Activity implements OnClickListener {
    
  

    	private EditText mPriceEdit;
	private EditText mTitleEdit;
    	private EditText mQuantityEdit;
	private EditText mBarcodeEdit;
	private Button mAddButton;
	private Button mScanButton;
	private Button mClearButton;
	private ProductAdapter pdHelper;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        mBarcodeEdit = (EditText) findViewById(R.id.barcodeEdit);
        mQuantityEdit = (EditText) findViewById(R.id.quantityEdit);
        mTitleEdit = (EditText) findViewById(R.id.titleEdit);
        mPriceEdit = (EditText) findViewById(R.id.priceEdit);
        mScanButton = (Button) findViewById(R.id.scanButton);        
        mScanButton.setOnClickListener(this);
        mAddButton = (Button) findViewById(R.id.addButton);
        mAddButton.setOnClickListener(this);
        pdHelper = new ProductAdapter(this);
        pdHelper.open();
    }
    /**
     * Allows user to scan barcode  
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.scanButton:
        	Intent intent = new Intent("com.google.zxing.client.android.SCAN"); 
        	intent.putExtra("SCAN_MODE", "SCAN_MODE"); 
        	startActivityForResult(intent, 0);
        	
        	Log.d("test", "button works!"); 
        	
        case R.id.addButton:
            String barcode = mBarcodeEdit.getText().toString();
            String quantity = mQuantityEdit.getText().toString();
            String title = mTitleEdit.getText().toString();
            String price = mPriceEdit.getText().toString();

            String errors = validateFields(barcode, quantity, title, price);
            if (errors.length() > 0) {
                showInfoDialog(this, "Please 'Type In' these fields", errors);
            } else {
               //place create 

          // insert barcode quantity title price
                showInfoDialog(this, "Success", "Product saved successfully");
                resetForm();
            }	
        	
        	
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
    private void resetForm() {
    // TODO Auto-generated method stub
      mBarcodeEdit.getText().clear();
      mQuantityEdit.getText().clear();
      mTitleEdit.getText().clear();
      mPriceEdit.getText().clear();
}
    
    
   
    	

    public void onActivityResult(int requestCode, int resultCode, Intent intent){ 
    	if(requestCode == 0)     { 
    		if(resultCode == RESULT_OK)         {       
    			String contents = intent.getStringExtra("SCAN_RESULT");  
    			String format = intent.getStringExtra("SCAN_RESULT_FORMAT"); 
    			mBarcodeEdit.setText(contents);
    			
    			 String URL = "http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=272C987E-1F4B-4599-9C03-7F03623DD24F&upc="+contents;
    			new HttpAsyncTask().execute(URL);
    			Log.i("xZing", "contents: "+contents+" format: "+format);              
    			// Handle successful scan 
    			}         else if(resultCode == RESULT_CANCELED)  {              
    				// Handle cancel            
    				Log.i("xZing", "Cancelled");         
    			}    
    		} 
    	}
    
    @Override
    public void onConfigurationChanged(Configuration newConfig){        
        super.onConfigurationChanged(newConfig);
    }
    
    
    private static String validateFields(String barcode, String quantity, 
    	    String title, String price) {
    	    StringBuilder errors = new StringBuilder();

    	    if (barcode.matches("^\\s*$")) {
    	        errors.append("Barcode required\n");
    	    }

    	    if (quantity.matches("^\\s*$")) {
    	        errors.append("Quantity required\n");
    	    }

    	    if (title.matches("^\\s*$")) {
    	        errors.append("Title required\n");
    	    }

    	    if (!price.matches("^-?\\d+(.\\d+)?$")) {
    	        errors.append("Need numeric price\n");
    	    }

    	    return errors.toString();
    	}
    // GET(string url) this function gets response from http
    public static String GETproductInfo(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }
    
 // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
    // Use Async to execute Http Get result
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
            return GETproductInfo(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "loading!", Toast.LENGTH_LONG).show();
         
            
			try {
				JSONObject proInfo = new JSONObject(result);            
	            JSONObject infoData =  proInfo.getJSONObject("0");
				String price = infoData.getString("price");
	            		String title = infoData.getString("productname");
	            
	            mTitleEdit.setText(title);
	            if (price != "0.00") {
	            	mPriceEdit.setText(price);
	            }
			} catch (JSONException e) {
				  Toast.makeText(getBaseContext(), "no product info found, please enter in manually!", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
          
           
            
       }
    }
 
}
