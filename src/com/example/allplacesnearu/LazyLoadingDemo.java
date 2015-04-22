package com.example.allplacesnearu;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LazyLoadingDemo extends Activity {
	
	 ImageView image;
	 //Drawable d;
	 LinearLayout ll;
	 protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.lazy_load);
	       //imageLoader=new ImageLoader(this);
	       //image=(ImageView)findViewById(R.id.imageLazy123);
	       //String url="http://www.w8themes.com/wp-content/uploads/2013/11/Blue-and-Purple-Wallpaper.png";
	       //imageLoader.DisplayImage(url, image);
	     
	       ll=(LinearLayout)findViewById(R.id.linear678);
	       //ll.setBackgroundDrawable(d);
	      // new GetResources().execute(url);
	       
	       
	       
	 }
	 
	 public class GetResources extends AsyncTask<String,Void,Drawable>
	 {
		 
	@Override
	protected Drawable doInBackground(String... params) {
		
		  Drawable drawable = LoadImageFromWebOperations(params[0]);
		  
		 return drawable;
		 
		 /*Bitmap bitmap=null;
		 try { 
		  	   
		  	   bitmap = BitmapFactory.decodeStream((InputStream)new URL(params[0]).getContent());
		  	   
		  	 } catch (MalformedURLException e) {
		  	   e.printStackTrace();
		  	 } catch (IOException e) {
		  	   e.printStackTrace();
		  	 }
		return bitmap;*/
	} 
	 @Override
	 protected void onPostExecute(Drawable bm)
	 {
		 //image.setImageBitmap(bm); 
		 //image.setImageDrawable(bm); 
		 ll.setBackgroundDrawable(bm);
		 
		 
	 }
	 
}
	 private Drawable LoadImageFromWebOperations(String url) {
		 
		    try { 
		        InputStream is = (InputStream) new URL(url).getContent();
		        Drawable d = Drawable.createFromStream(is, "src_name");
		        return d;
		    } catch (Exception e) {
		        System.out.println("Exc=" + e);
		        return null; 
		    } 
		 
		}
	 
	 
}
