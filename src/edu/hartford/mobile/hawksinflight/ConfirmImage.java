package edu.hartford.mobile.hawksinflight;

import java.io.ByteArrayOutputStream;
//import java.io.File;

import android.support.v7.app.ActionBarActivity;
//import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class ConfirmImage extends ActionBarActivity {

	//private Bitmap bitmap;
	private ImageView mImageView = null;
	private Bitmap bmp;
	//private String mCurrentPhotoPath;
	//private ImageView view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_image);
		mImageView = (ImageView) findViewById(R.id.confirmImage);
		byte[] byteArray = getIntent().getByteArrayExtra("capture");
		bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		mImageView.setImageBitmap(bmp);
		
		//Intent newIntent = getIntent();
		//mCurrentPhotoPath = newIntent.getExtras().getString("capture");
		//File f = new File(mCurrentPhotoPath);
	    //Uri contentUri = Uri.fromFile(f);
	    //mImageView.setImageURI(contentUri);
		//mCurrentPhotoPath = getIntent().getExtras().getParcelable("capture");
		//setPic();
		//grabImage(view);
        //ImageView view = (ImageView) findViewById(R.id.confirmImage);
       //view.setImageURI(mImageUri); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirm_image, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onSelectionYes(View v)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        
		Intent openFinal = new Intent(this, FinalSubmit.class);
		openFinal.putExtra("image", byteArray);
		startActivity(openFinal);	
	}
	
	public void onSelectionNo(View v)
	{       
		Intent openCam = new Intent(this, OpenCamera.class);
		startActivity(openCam);	
	}
	
	/*private void setPic() {
	    // Get the dimensions of the View
	    //int targetW = mImageView.getWidth();
	    //int targetH = mImageView.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    //int photoW = bmOptions.outWidth;
	   // int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	   // int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	   // bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    mImageView.setImageBitmap(bitmap);
	}*/
}
