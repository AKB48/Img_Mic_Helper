package com.app.img_mic_helper;

import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.config.ActivityFlag;

/**
 * The main page of this app.
 * It contains four buttons. 
 * Two of them are image processing. 
 * One of them is music player.
 * The last is information about the app.
 * @author William
 *
 */
public class MainActivity extends Activity {
	
	private Button from_album, from_camera, to_music, about; 
	private Bitmap bitmap = null;
	private String filePath = null;
	private ActionBar actionBar;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // from_album button is to choose images from album
        from_album = (Button)this.findViewById(R.id.from_album);
        from_album.setOnClickListener(click_into_album);
        
        // from_camera button is to achieve images from camera
        from_camera = (Button)this.findViewById(R.id.from_camera);
        from_camera.setOnClickListener(click_into_camera);
        
        // to_music button is to use the music player function
        to_music = (Button)this.findViewById(R.id.to_music);
        to_music.setOnClickListener(click_into_music);
        
        // about button is to show the information about this app
        about = (Button)this.findViewById(R.id.about);
        about.setOnClickListener(click_into_about);
        
        actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
    }

    // from_album button click event
    private OnClickListener click_into_album = new OnClickListener(){
    	/**
    	 * open the album to choose an image when this button is pressed.
    	 */
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);  
			intent.addCategory(Intent.CATEGORY_OPENABLE);
	        intent.setType("image/*");
	        try {
	        	startActivityForResult(intent, ActivityFlag.ALBUM_WITH_DATA);  
	        } catch (ActivityNotFoundException e) {  
	        	Toast.makeText(MainActivity.this, R.string.could_not_open_album,  Toast.LENGTH_LONG).show();
	        }   
	        
		}
    }; 

    // from_camera button click event
    OnClickListener click_into_camera = new OnClickListener(){

    	/**
    	 * open the camera to take a picture when this button is pressed.
    	 */
		@Override
		public void onClick(View v) {
			String state = Environment.getExternalStorageState();
			if( state.equals(Environment.MEDIA_MOUNTED))
			{
				try {
		            String SDCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		            File fileDir = new File(SDCardDir + "/Img_Mic_Helper" );
		            if(!fileDir.exists())
		                fileDir.mkdirs();
		          
		            filePath = SDCardDir + "/Img_Mic_Helper/temp.png";		       
		            File photoFile = new File(filePath);
		            photoFile.delete();
		            if( !photoFile.exists() )
		            {
		            	try
		            	{
		            		photoFile.createNewFile();
		            	}
		            	catch(IOException e)
		            	{
		            		e.printStackTrace();
		            		Toast.makeText(MainActivity.this, R.string.fail_photo_created, Toast.LENGTH_SHORT).show();
		            		return;
		            	}
		            }
		        
		            Intent intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		            intentCam.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
		            startActivityForResult(intentCam, ActivityFlag.CAMERA_WITH_DATA);		            
		        } catch (ActivityNotFoundException e) {
		        	Toast.makeText(MainActivity.this, R.string.could_not_open_camera,  Toast.LENGTH_LONG).show();
		        }
			}
			else
			{
				Toast.makeText(MainActivity.this, R.string.no_sdcard, Toast.LENGTH_SHORT).show();
			}
      		
		}	
    };
    
    
    // to_music button click event
    private OnClickListener click_into_music = new OnClickListener(){
    	/**
    	 * Enter the media player function when this button is pressed.
    	 */
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();  
			intent.setClass(MainActivity.this, MusicPlayerActivity.class);
	        try 
	        {
	        	startActivityForResult(intent, ActivityFlag.LISTEN_TO_MUSIC);  
	        }
	        catch (ActivityNotFoundException e) 
	        {  
	        	Toast.makeText(MainActivity.this, R.string.operation_error,  Toast.LENGTH_LONG).show();
	        }   
	        
		}
    }; 
    
    
    // about button click event
    private OnClickListener click_into_about = new OnClickListener(){
    	/**
    	 * Enter the application about page when this button is pressed.
    	 */
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();  
			intent.setClass(MainActivity.this, AboutActivity.class);
	        try 
	        {
	        	startActivityForResult(intent, ActivityFlag.APPLICATION_INFORMATION);  
	        }
	        catch (ActivityNotFoundException e) 
	        {  
	        	Toast.makeText(MainActivity.this, R.string.operation_error,  Toast.LENGTH_LONG).show();
	        }   
	        
		}
    }; 
    
    
    /**
     * call back function
     * deal with both pick images from album and camera.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        if(resultCode == RESULT_OK)
        {  
        	switch(requestCode) {
        	case ActivityFlag.ALBUM_WITH_DATA:
        		data.putExtra("ImageSource", ActivityFlag.ALBUM_WITH_DATA);
        		data.setClass(MainActivity.this, ImageProcessActivity.class);      		
        		MainActivity.this.startActivity(data);
        		break;
        	case ActivityFlag.CAMERA_WITH_DATA:
        	    if (data == null)
        	    {
        	        data = new Intent();
        	    }
        		data.putExtra("ImageSource", ActivityFlag.CAMERA_WITH_DATA);
        		data.putExtra("ImagePath", filePath);
                data.setClass(MainActivity.this, ImageProcessActivity.class);
                MainActivity.this.startActivity(data);
        		break;
        	default:
        		break;
        	}
        }
        else if (requestCode != ActivityFlag.LISTEN_TO_MUSIC && requestCode != ActivityFlag.APPLICATION_INFORMATION)
        {  
            Toast.makeText(MainActivity.this, R.string.choose_image_again, Toast.LENGTH_SHORT).show();  
        }  
              
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
        if (id == R.id.action_settings)
        {
            return true;
        }
        else if (id == android.R.id.home)
        {
        	onBackPressed();
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    
}
