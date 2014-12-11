/**
 * 
 */
package com.app.img_mic_helper;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.config.Config;
import com.utils.ChineseRadio;
import com.utils.DiscShapeProcessor;
import com.utils.HttpSender;
import com.utils.Music;
import com.utils.Radio;

import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;


/**
 * @author William
 *
 */
public class MusicPlayerActivity extends Activity implements MediaPlayer.OnPreparedListener,
		MediaPlayer.OnCompletionListener{
	
	private MediaPlayer musicPlayer = null;
	private Radio radio = null;
	private ArrayList<HashMap<String, Object>> radio_name_list = null;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private Bitmap music_cover = null;
	private int currentChannel = 0;
	private Music currentMusic = null;
	private MusicHandler musicHandler = null;
	
	private ImageView front_cover_iv = null;
	private TextView music_name_tv = null;
	private TextView player_name_tv = null;
	private SeekBar music_progress = null;
	private SeekBar sound_progress = null;
	private GridView radio_list = null; 
	private Button play = null;
	private Button next = null;
	private Button  ban = null;
	
	private Boolean isPlay = false;
	private Boolean isPrepare = false;
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);
        
        musicPlayer = new MediaPlayer();
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        front_cover_iv = (ImageView)this.findViewById(R.id.front_cover);
        music_name_tv = (TextView)this.findViewById(R.id.music_name);
        player_name_tv = (TextView)this.findViewById(R.id.player_name);
        music_progress = (SeekBar)this.findViewById(R.id.music_progress);
        sound_progress = (SeekBar)this.findViewById(R.id.sound_progress);
        radio_list = (GridView)this.findViewById(R.id.radio_list);
        play = (Button)this.findViewById(R.id.play);
        next = (Button)this.findViewById(R.id.next);
        ban = (Button)this.findViewById(R.id.ban);
        
        musicHandler = new MusicHandler( Looper.myLooper());
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.screenWidth = dm.widthPixels;
        this.screenHeight = dm.heightPixels ;
            
        music_cover = BitmapFactory.decodeResource(getResources(), R.drawable.radio);
        DiscShapeProcessor.getInstance().setResultWidth((int) (this.screenWidth*0.65f));
        DiscShapeProcessor.getInstance().setResultHeight((int)(this.screenWidth*0.65f));
        Bitmap tempBitmap = DiscShapeProcessor.getInstance().process(music_cover);
        music_cover.recycle();
        music_cover = tempBitmap;
        tempBitmap = null;
        front_cover_iv.setImageBitmap(music_cover);
        
        
        radio_name_list = new ArrayList<HashMap<String, Object>>();  
        for(int i = 0; i < Config.radios_list.length; i++)  
        {  
        	HashMap<String, Object> map = new HashMap<String, Object>();  
        	map.put("itemName", getString(Config.radios_list[i]));
        	radio_name_list.add(map);  
        }  

        SimpleAdapter radioListAdapter = new SimpleAdapter(this,
        										  radio_name_list, 
                                                  R.layout.function_bar_content,        
                                                  new String[] {"itemName"},    
                                                  new int[] {R.id.item_name});  
        
        radio_list.setAdapter(radioListAdapter);  
        radio_list.setOnItemClickListener(new OnRadioChoose());  
        
	}
	
	
	private void prepareMusic()
	{
		if (radio != null)
		{
			currentMusic = radio.getMusic();
			GetBitmapThread getBitmapThread = new GetBitmapThread();
			getBitmapThread.httpUrl = currentMusic.getCover();
			getBitmapThread.start();
			music_name_tv.setText(currentMusic.getName());
			player_name_tv.setText(currentMusic.getPlayer());
			musicPlayer.stop();
			musicPlayer.reset();
			try 
			{
				Log.i("aa", currentMusic.getUrl());
				musicPlayer.setDataSource(currentMusic.getUrl());
				isPrepare = true;
				musicPlayer.prepare();
				musicPlayer.start();
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			}
			catch (SecurityException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalStateException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}

		}
	}
	
	
	class OnRadioChoose implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			String requestUrl = null;
			
			switch (position) {
			case 0:
				radio = new ChineseRadio();
				isPrepare = false;
				requestUrl = radio.constructRadioUrl();
				currentChannel = radio.getChannel();
				break;
			default:
				break;
			}
			
			HttpSender.getInstance().Httpget(requestUrl, currentChannel, musicHandler);
		}
		
	}
	
	
	class MusicHandler extends Handler
	{
		public MusicHandler(Looper looper)
		{
			super(looper);
		}
		
		public void handleMessage(Message msg)
		{
			if (msg.what == currentChannel)
			{
				JSONObject json_object;
				try 
				{
					json_object = new JSONObject(msg.obj.toString());
					radio.generateAlbum(json_object.getJSONArray("song"));
					prepareMusic();
				} 
				catch (JSONException e) 
				{
					e.printStackTrace();
				}					
			}
			else if (msg.what == -1)
			{
				Bitmap tempBitmap = music_cover;
				music_cover = (Bitmap)msg.obj;
				front_cover_iv.setImageBitmap(music_cover);
				tempBitmap.recycle();
				tempBitmap = null;
			}
			
		}
		
	}
	
	
	class GetBitmapThread extends Thread
	{
		private String httpUrl = null;
		
		public void run() 
		{
    		getBitmap(httpUrl);
    	}
		
		private void getBitmap(String httpUrl)  
	    {  
	        Bitmap bitmap = null;  
	        try  
	        {  
	            URL url = new URL(httpUrl);  
	            bitmap = BitmapFactory.decodeStream(url.openStream());
	            Bitmap temBitmap = DiscShapeProcessor.getInstance().process(bitmap);
	            bitmap.recycle();
	            bitmap = temBitmap;
	            temBitmap = null;
	            Message message = Message.obtain();
				message.what = -1;
				message.obj = bitmap;
				musicHandler.sendMessage(message);
	        } 
	        catch (Exception e)  
	        {   
	            e.printStackTrace();  
	        }  
	          
	        
	    }  
	}
	
	
	@Override
	protected void onDestroy()
	{
		if (musicPlayer != null)
		{
			musicPlayer.release();
			musicPlayer = null;
		}
		super.onDestroy();
	}


	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		Log.i("AA", "outer");
		if (isPrepare && mp != null)
		{
			Log.i("AA", "inner");
			mp.start();		
			isPlay = true;
			play.setBackgroundResource(R.drawable.stop);
		}
	}
	
}
