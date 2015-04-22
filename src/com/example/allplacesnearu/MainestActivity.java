package com.example.allplacesnearu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainestActivity extends Activity {
	Button imb1;
	Button imb2;
	Button imb3;
	Button imb4,imb5,imb6;
	 Animation myAnimation;
	TextView myText1,myText2,myText3,myText4,myText5,myText6;
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mainest);
	imb1=(Button)findViewById(R.id.imageButton1);
	imb2=(Button)findViewById(R.id.imageButton2);
	imb3=(Button)findViewById(R.id.imageButton3);
	imb4=(Button)findViewById(R.id.imageButton4);
	imb5=(Button)findViewById(R.id.imageButton5);
	imb6=(Button)findViewById(R.id.imageButton6);
	
	final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scaleanim);
    final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotateanim);
	
	 myText1 = (TextView)findViewById(R.id.tv1);
	myText2 = (TextView)findViewById(R.id.tv2);
	 myText3 = (TextView)findViewById(R.id.tv3);
	 myText4 = (TextView)findViewById(R.id.tv4);
	 myText5=(TextView)findViewById(R.id.tv5);
	 myText6=(TextView)findViewById(R.id.tv6);
	 
	  myAnimation = AnimationUtils.loadAnimation(this, R.anim.textauim);
	  
	  myText1.setOnClickListener(new OnClickListener(){

		   @Override
		   public void onClick(View arg0) {
		    myText1.startAnimation(animScale);
		   }});
	  
	  myText2.setOnClickListener(new OnClickListener(){

		   @Override
		   public void onClick(View arg0) {
		    myText2.startAnimation(animRotate);
		   }});
	  
	  myText3.setOnClickListener(new OnClickListener(){

		   @Override
		   public void onClick(View arg0) {
		    myText3.startAnimation(animScale);
		   }});
	  
	  myText4.setOnClickListener(new OnClickListener(){

		   @Override
		   public void onClick(View arg0) {
		    myText4.startAnimation(animRotate);
		   }});
	  myText5.setOnClickListener(new OnClickListener(){

		   @Override
		   public void onClick(View arg0) {
		    myText5.startAnimation(animScale);
		   }});
	  myText6.setOnClickListener(new OnClickListener(){

		   @Override
		   public void onClick(View arg0) {
		    myText5.startAnimation(animRotate);
		   }});
	  
	  
	  
	  
	imb1.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			 arg0.startAnimation(animRotate);
			Toast.makeText(MainestActivity.this,"Geocoding",Toast.LENGTH_SHORT).show();
			Intent intt=new Intent(MainestActivity.this,Geocoding.class);
			startActivity(intt);
			
			
		}
		
	});
	imb2.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			arg0.startAnimation(animScale);
			Toast.makeText(MainestActivity.this,"reverse geocoding",Toast.LENGTH_SHORT).show();
			Intent intt=new Intent(MainestActivity.this,ReverseGeocoding.class);
			startActivity(intt);
			
		}
		
	});
	imb3.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			 arg0.startAnimation(animRotate);
			Toast.makeText(MainestActivity.this,"Places near to you",Toast.LENGTH_SHORT).show();
			Intent intt=new Intent(MainestActivity.this,MainActivityNew.class);
			startActivity(intt);
			
		}
		
	});
	imb4.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			arg0.startAnimation(animScale);
			Toast.makeText(MainestActivity.this,"As List",Toast.LENGTH_SHORT).show();
			Intent intt=new Intent(MainestActivity.this,NearPlacesActivity.class);
			startActivity(intt);
			
		}
		
	});
	imb5.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			arg0.startAnimation(animRotate);
			Toast.makeText(MainestActivity.this,"Street-View",Toast.LENGTH_SHORT).show();
			Intent intt=new Intent(MainestActivity.this,StreetViewActivity.class);
			startActivity(intt);
			
		}
		
	});
	imb6.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			arg0.startAnimation(animRotate);
			Toast.makeText(MainestActivity.this,"Get-Directions",Toast.LENGTH_SHORT).show();
			Intent intt=new Intent(MainestActivity.this,DirectionActivity.class);
			startActivity(intt);
			
		}
		
	});

}
	
}
