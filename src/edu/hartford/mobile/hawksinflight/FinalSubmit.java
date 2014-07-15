package edu.hartford.mobile.hawksinflight;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FinalSubmit extends Activity
{	
	private EditText title;
	private EditText name;
	private EditText description;
	private EditText email;
	
	private TextView text;
	private Button reset;
	private Button submit;
	Bitmap bmp = null;
	String finalImagePath = null;
	ProgressDialog dialog = null;
	int serverResponseCode = 0;
	Uri tempUri = null;
	Context con = getBaseContext();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final_submit);
		
		//Uncompressing and setting image from intent
		byte[] byteArray = getIntent().getByteArrayExtra("image");
		bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView view = (ImageView) findViewById(R.id.finalImage);
        view.setImageBitmap(bmp);
        
        title = (EditText) findViewById(R.id.title);
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        email = (EditText) findViewById(R.id.email);
        
        reset = (Button) findViewById(R.id.resetForm);
        submit = (Button) findViewById(R.id.flickrUpload);
        
        text = (TextView) findViewById(R.id.errorTextField);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReset();
            }
        });
        
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
	}
	
	private void onSubmit()
	{
		if(errorCheck() == false)
		{
			//Do Nothing
		}
		else
		{
			tempUri = getImageUri(getApplicationContext(), bmp);
			finalImagePath = getRealPathFromURI(tempUri);
			
			dialog = ProgressDialog.show(FinalSubmit.this, "", finalImagePath/*"Uploading file..."*/, true);
	        new Thread(new Runnable() {
	               public void run() {
	                    runOnUiThread(new Runnable() {
	                           public void run() {
	                               //tv.setText("uploading started.....");
	                           }
	                       });                      
	                int response= uploadFile(finalImagePath);
	                getContentResolver().delete(tempUri, null, null);
	                postData();
	                revertePage();
	                System.out.println("RES : " + response);  
	                //tv.setText("" + response);
	               }
	             }).start(); 
		}
    }
	
	public int uploadFile(String sourceFileUri) 
	{
        String upLoadServerUri = "http://137.49.139.33/Flickr/upload_test2.php";
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri); 
        if (!sourceFile.isFile()) {
        	Log.e("uploadFile", "Source File Does not exist");
        	return 0;
        }
        try { // open a URL connection to the Servlet
             FileInputStream fileInputStream = new FileInputStream(sourceFile);
             URL url = new URL(upLoadServerUri);
             conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
             conn.setDoInput(true); // Allow Inputs
             conn.setDoOutput(true); // Allow Outputs
             conn.setUseCaches(false); // Don't use a Cached Copy
             conn.setRequestMethod("POST");
             conn.setRequestProperty("Connection", "Keep-Alive");
             conn.setRequestProperty("ENCTYPE", "multipart/form-data");
             conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
             conn.setRequestProperty("uploaded_file", fileName); 
             dos = new DataOutputStream(conn.getOutputStream());
   
             dos.writeBytes(twoHyphens + boundary + lineEnd); 
             dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
             dos.writeBytes(lineEnd);
   
             bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size
   
             bufferSize = Math.min(bytesAvailable, maxBufferSize);
             buffer = new byte[bufferSize];
   
             // read file and write it into form...
             bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
               
             while (bytesRead > 0) {
               dos.write(buffer, 0, bufferSize);
               bytesAvailable = fileInputStream.available();
               bufferSize = Math.min(bytesAvailable, maxBufferSize);
               bytesRead = fileInputStream.read(buffer, 0, bufferSize);               
              }
   
             // send multipart form data necesssary after file data...
             dos.writeBytes(lineEnd);
             dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
   
             // Responses from the server (code and message)
             serverResponseCode = conn.getResponseCode();
             String serverResponseMessage = conn.getResponseMessage();
              
             Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
             if(serverResponseCode == 200){
                 runOnUiThread(new Runnable() {
                      public void run() {
                          //tv.setText("File Upload Completed.");
                          Toast.makeText(FinalSubmit.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                      }
                  });                
             }    
             
             //close the streams //
             fileInputStream.close();
             dos.flush();
             dos.close();
              
        } catch (MalformedURLException ex) {  
            dialog.dismiss();  
            ex.printStackTrace();
            Toast.makeText(FinalSubmit.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
        } catch (Exception e) {
            dialog.dismiss();  
            e.printStackTrace();
            Toast.makeText(FinalSubmit.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);  
        }
        dialog.dismiss();       
        return serverResponseCode;  
    } 
	
	private void onReset()
	{
		title.setText("");
		name.setText("");
		description.setText("");
		email.setText("");
	}
	
	public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null); 
        cursor.moveToFirst(); 
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
        return cursor.getString(idx); 
    }
    
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    
    public void postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://137.49.139.33/Flickr/saveInput.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("title", title.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("description", description.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            @SuppressWarnings("unused")
			HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    } 
    
    public boolean errorCheck()
    {
    	String titleText = title.getText().toString();
		String nameText = name.getText().toString();
		String descriptionText = description.getText().toString();
		String emailText = email.getText().toString();
		
		titleText.toLowerCase();
		nameText.toLowerCase();
		descriptionText.toLowerCase();
		emailText.toLowerCase();
		
		if(titleText.equals(""))
		{
			text.setText(R.string.incorrectTitle);
			return false;
		}
		else if(descriptionText.equals(""))
		{
			text.setText(R.string.incorrectDescription);
			return false;
		}
		else if(nameText.equals(""))
		{
			text.setText(R.string.incorrectName);
			return false;
		}
		else if(emailText.contains("@hartford.edu"))
		{
			text.setText(R.string.incorrectNonHartfordEmail);
			return false;
		}
		else if(emailText.equals(""))
		{
			text.setText(R.string.incorrectEmail);
			return false;
		}
		else if(titleText.equals("") && nameText.equals("") &&
			descriptionText.equals("") && emailText.equals(""))
		{
			text.setText(R.string.incorrectEverything);
			return false;
		}
		else
		{
			text.setText(R.string.correct);
			return true;
		}
    }
    
    private void revertePage()
    {
    	Intent goHome = new Intent(this, MainActivity.class);
		startActivity(goHome);	
    }
}
