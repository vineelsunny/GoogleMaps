package com.example.allplacesnearu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DummyActivityTest  extends Activity{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy_test);
        Button btn678=(Button)findViewById(R.id.button178);
        btn678.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i=new Intent(DummyActivityTest.this,DirectionIntro.class);
				startActivity(i);				
				
				
			}
        	
        });
	}
}
