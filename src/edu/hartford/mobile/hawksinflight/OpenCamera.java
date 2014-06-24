package edu.hartford.mobile.hawksinflight;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
//import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class OpenCamera extends Activity
{
	    Button b;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        selectImage();
	    }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds options to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

	      private void selectImage() 
	      {
	          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	          File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
	          intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	          startActivityForResult(intent, 1);
	      }

	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (resultCode == RESULT_OK) 
	        {
	            if (requestCode == 1) 
	            {
	                File f = new File(Environment.getExternalStorageDirectory().toString());
	                for (File temp : f.listFiles()) {
	                    if (temp.getName().equals("temp.jpg")) {
	                        f = temp;
	                        break;
	                    }
	                }
	                Bitmap bitmap;
	                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

	                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
	                            bitmapOptions);
	                    
	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        	    byte[] byteArray = stream.toByteArray();
	        	        
	        		Intent i = new Intent(this, ConfirmImage.class);
	                i.putExtra("capture", byteArray);
	                startActivity(i);
	            }
	        }
	    }
	    
	}