package com.VerticalCodes.tabs;

import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
public class ActivityAbout extends AppCompatActivity{
	ImageView backBtn;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_about);
		backBtn=findViewById(R.id.about_btn_back);
		backBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				onBackPressed();
				}
			});
	}
	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		
		super.onBackPressed();
	}
}