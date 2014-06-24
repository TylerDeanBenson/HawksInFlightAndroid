package edu.hartford.mobile.hawksinflight;

//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;

import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
import android.os.Bundle;
//import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity
{
	//private static int CHOSEN_IMAGE = 1;

	Button browseButton;
	Button cameraButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		browseButton = (Button) findViewById(R.id.browse);
		cameraButton = (Button) findViewById(R.id.camera);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void openGallery(View v)
	{
		Intent openGal = new Intent(this, OpenGallery.class);
		startActivity(openGal);
		/*Intent i = new Intent();
		i.setType("image/*");
		i.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(i, "Select Picture"), CHOSEN_IMAGE);*/
	}
	
	public void openCamera(View v)
	{
		Intent openCam = new Intent(this, OpenCamera.class);
		//Intent openCam = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivity(openCam);		
	}
}

