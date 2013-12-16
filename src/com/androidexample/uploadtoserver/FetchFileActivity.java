package com.androidexample.uploadtoserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FetchFileActivity extends Activity {

	
	TextView messageText;
	//Button checkBackUpStatusButton;
    Button fetchButton;
	int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String fetchUri = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fetch_file);
		      
		   // checkBackUpStatusButton = (Button)findViewById(R.id.uploadButton);
		    fetchButton = (Button)findViewById(R.id.uploadButton);
		    messageText  = (TextView)findViewById(R.id.messageText);
		     
		    messageText.setText("checking backuped data :- ");
		     
		    /************* Php script path ****************/
		  fetchUri = "http://160.39.205.142:8080/gppcloudbackend/download_files?user_id=jiatian&path=data/local/tmp/haha.txt";
		    Log.i("fetchUri", fetchUri);
		    fetchButton.setOnClickListener(new OnClickListener() {            
		        @Override
		        public void onClick(View v) {
		             
		            dialog = ProgressDialog.show(FetchFileActivity.this, "", "Fetch File...", true);
		             
		            new Thread(new Runnable() {
		                    public void run() {
		                         runOnUiThread(new Runnable() {
		                                public void run() {
		                                    messageText.setText("File file starting.....");
		                                }
		                            });                      
		                       
		                         fetchFile();
		                                                  
		                    }
		                  }).start();        
		            }
		        });
	}
	public int fetchFile() {
	    Log.i("enter fetch file", "success");
	    

	   // HttpURLConnection conn = null;
	 //   DataOutputStream dos = null;  
	    
	     try { 
	              
	               
	    	 DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
	    	 HttpPost httppost = new HttpPost(fetchUri);
	    	 // Depends on your web service
	    	 Log.i("url:",fetchUri);
	    	 httppost.setHeader("Content-type", "application/json");

	    	 InputStream inputStream = null;
	    	 String result = null;
	    	 try {
	    	     HttpResponse response = httpclient.execute(httppost);           
	    	     HttpEntity entity = response.getEntity();
	    	     if (response.getStatusLine().getStatusCode() == 200) {
	    	    	 runOnUiThread(new Runnable() {
	                     public void run() {
	                          
	                         String msg = "fetch file finish: \n\n";
	                          
	                         messageText.setText(msg);
	                         Toast.makeText(FetchFileActivity.this, "fetch file finished.", 
	                                      Toast.LENGTH_SHORT).show();
	                     }
	                 });     
	             }

	    	     inputStream = entity.getContent();
	    	     // json is UTF-8 by default
	    	     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
	    	     StringBuilder sb = new StringBuilder();

	    	     String line = null;
	    	     while ((line = reader.readLine()) != null)
	    	     {
	    	         sb.append(line + "\n");
	    	     }
	    	     result = sb.toString();
	    	 } catch (Exception e) { 
	    	     // Oops
	    	 }
	    	 finally {
	    	     try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
	    	 }   
	    	 JSONObject jObject = new JSONObject(result);
	    	 Log.i("aJsonString", jObject.toString());
	    	// {"UserID":"Mickey","Paths":[{"TimeStamp":"2013\/12\/15 17:54:04","Path":"data\/local\/tmp\/haha.txt"}]}
	    	 String user_id = jObject.getString("UserID");
	    	 String path = jObject.getString("Paths");
	    	 Log.i("user_id",user_id);
	    	 Log.i("path",path);
	          
	        } catch (Exception e) {
	             
	            dialog.dismiss();  
	            e.printStackTrace();
	             
	            runOnUiThread(new Runnable() {
	                public void run() {
	                    messageText.setText("Got Exception : see logcat ");
	                    Toast.makeText(FetchFileActivity.this, "Got Exception : see logcat ", 
	                            Toast.LENGTH_SHORT).show();
	                }
	            });
	            Log.e("Upload file to server Exception", "Exception : "
	                                             + e.getMessage(), e);  
	        }
	        dialog.dismiss();       
	        return serverResponseCode; 
	         
	     } // End else block 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fetch_file, menu);
		return true;
	}

}
