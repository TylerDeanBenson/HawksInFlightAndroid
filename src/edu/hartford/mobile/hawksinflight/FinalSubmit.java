package edu.hartford.mobile.hawksinflight;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class FinalSubmit extends Activity
{
	private EditText title;
	private EditText name;
	private EditText description;
	private EditText email;
	
	private TextView text;
	private Button reset;
	private Button submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final_submit);
		
		//Uncompressing and setting image from intent
		byte[] byteArray = getIntent().getByteArrayExtra("image");
		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView view = (ImageView) findViewById(R.id.finalImage);
        view.setImageBitmap(bmp);
        
        title = (EditText) findViewById(R.id.title);
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        email = (EditText) findViewById(R.id.email);
        
        reset = (Button) findViewById(R.id.reset);
        submit = (Button) findViewById(R.id.upload);
        
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
		String titleText = title.getText().toString();
		String nameText = name.getText().toString();
		String descriptionText = description.getText().toString();
		String emailText = email.getText().toString();
		
		if(titleText.equals(""))
		{
			text.setText(R.string.incorrectTitle);
		}
		else if(nameText.equals(""))
		{
			text.setText(R.string.incorrectTitle);
		}
		else if(descriptionText.equals(""))
		{
			text.setText(R.string.incorrectTitle);
		}
		else if(emailText.equals(""))
		{
			text.setText(R.string.incorrectTitle);
		}
		else if(titleText.equals("") && nameText.equals("") &&
			descriptionText.equals("") && emailText.equals(""))
		{
			text.setText(R.string.incorrectEverything);
		}
		else
		{
			text.setText(R.string.correct);
		}

	}
	
	private void onReset()
	{
		title.setText("");
		name.setText("");
		description.setText("");
		email.setText("");
	}
}
