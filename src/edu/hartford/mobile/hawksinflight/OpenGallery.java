package edu.hartford.mobile.hawksinflight;

import java.io.ByteArrayOutputStream;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;;

public class OpenGallery extends ActionBarActivity {

	private static int CHOSEN_IMAGE = 1;
	//private ImageView image;// ImageView
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		//image = (ImageView)findViewById(R.id.confirmImage);	
		
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CHOSEN_IMAGE);
        setContentView(R.layout.activity_confirm_image);
        //this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_gallery, menu);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// Here we need to check if the activity that was triggers was the Image Gallery.
		// If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
		if (requestCode == CHOSEN_IMAGE && resultCode == RESULT_OK && null != data) {
	        Uri selectedImage = data.getData();
	        String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	        Cursor cursor = getContentResolver().query(selectedImage,
	                 filePathColumn, null, null, null);
	        cursor.moveToFirst();
	 
	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        String picturePath = cursor.getString(columnIndex);
	        Bitmap picture = BitmapFactory.decodeFile(picturePath);
	        cursor.close();
	        
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        byte[] byteArray = stream.toByteArray();
	        
			Intent i = new Intent(this, FinalSubmit.class);
            i.putExtra("image", byteArray);
            startActivity(i);
			// Let's read picked image data - its URI
			/*Uri pickedImage = data.getData();
			// Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            //String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            
            // Now we need to set the GUI ImageView data with data read from the picked file.
            //image.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            
            Intent i = new Intent(this, FinalSubmit.class);
            i.putExtra("capture", pickedImage);
            startActivity(i);
            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();*/
		}
	}
}
