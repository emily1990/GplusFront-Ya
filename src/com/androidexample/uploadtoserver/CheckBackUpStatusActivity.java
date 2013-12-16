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

public class CheckBackUpStatusActivity extends Activity {
	TextView messageText;
	//Button checkBackUpStatusButton;
    Button checkstatusButton;
	int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String checkBackUpstatusUri = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_back_up_status);
		      
		   // checkBackUpStatusButton = (Button)findViewById(R.id.uploadButton);
		    checkstatusButton = (Button)findViewById(R.id.uploadButton);
		    messageText  = (TextView)findViewById(R.id.messageText);
		     
		    messageText.setText("checking backuped data :- ");
		     
		    /************* Php script path ****************/
		  checkBackUpstatusUri = "http://160.39.205.142:8080/gppcloudbackend/get_backup_info?user_id=Mickey";
		    Log.i("checkBackUpstatusUri", checkBackUpstatusUri);
		    checkstatusButton.setOnClickListener(new OnClickListener() {            
		        @Override
		        public void onClick(View v) {
		             
		            dialog = ProgressDialog.show(CheckBackUpStatusActivity.this, "", "Check backed up...", true);
		             
		            new Thread(new Runnable() {
		                    public void run() {
		                         runOnUiThread(new Runnable() {
		                                public void run() {
		                                    messageText.setText("Checking status starting.....");
		                                }
		                            });                      
		                       
		                         checkBackUpStatus();
		                                                  
		                    }
		                  }).start();        
		            }
		        });
	}
	public int checkBackUpStatus() {
	    Log.i("enter checkbackup status", "success");
	    

	   // HttpURLConnection conn = null;
	 //   DataOutputStream dos = null;  
	    
	     try { 
	              
	               
	    	 DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
	    	 HttpPost httppost = new HttpPost(checkBackUpstatusUri);
	    	 // Depends on your web service
	    	 Log.i("url:",checkBackUpstatusUri);
	    	 httppost.setHeader("Content-type", "application/json");

	    	 InputStream inputStream = null;
	    	 String result = null;
	    	 try {
	    	     HttpResponse response = httpclient.execute(httppost);           
	    	     HttpEntity entity = response.getEntity();
	    	     if (response.getStatusLine().getStatusCode() == 200) {
	    	    	 runOnUiThread(new Runnable() {
	                     public void run() {
	                          
	                         String msg = "check status finish: \n\n";
	                          
	                         messageText.setText(msg);
	                         Toast.makeText(CheckBackUpStatusActivity.this, "check status finished.", 
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
	    	// String aJsonString = jObject.getString("Content");
	    	 Log.i("aJsonString", jObject.toString());
	    	// {"UserID":"Mickey","Paths":[{"TimeStamp":"2013\/12\/15 17:54:04","Path":"data\/local\/tmp\/haha.txt"}]}
	    	 String user_id = jObject.getString("UserID");
	    	 String path = jObject.getString("Paths");
	    	 Log.i("user_id",user_id);
	    	 Log.i("path",path);
	             //close the streams //
	       /*      fileInputStream.close();
	             dos.flush();
	             dos.close();
	         */      
	        } catch (Exception e) {
	             
	            dialog.dismiss();  
	            e.printStackTrace();
	             
	            runOnUiThread(new Runnable() {
	                public void run() {
	                    messageText.setText("Got Exception : see logcat ");
	                    Toast.makeText(CheckBackUpStatusActivity.this, "Got Exception : see logcat ", 
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
		getMenuInflater().inflate(R.menu.check_back_up_status, menu);
		return true;
	}

}
