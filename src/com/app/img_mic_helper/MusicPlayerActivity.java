/**
 * 
 */
package com.app.img_mic_helper;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.img_mic_helper.share.WXShare;
import com.config.Config;
import com.utils.CantoneseRadio;
import com.utils.CartoonRadio;
import com.utils.ChineseRadio;
import com.utils.DiscShapeProcessor;
import com.utils.EightyRadio;
import com.utils.FreshRadio;
import com.utils.HttpSender;
import com.utils.LightMusicRadio;
import com.utils.Music;
import com.utils.NewMusicRadio;
import com.utils.NinetyRadio;
import com.utils.Radio;
import com.utils.RockRadio;
import com.utils.WesternRadio;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;


/**
 * The activity class provides the function to listen to music.
 * @author William
 *
 */
public class MusicPlayerActivity extends Activity implements MediaPlayer.OnPreparedListener,
		MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener{
	
	private MediaPlayer musicPlayer = null;
	private Radio radio = null;
	private ArrayList<HashMap<String, Object>> radio_name_list = null;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private Bitmap music_cover = null;
	private int currentChannel = 0;
	private Music currentMusic = null;
	private int current_song_id = 0;
	private MusicHandler musicHandler = null;
	private Timer musicProgressTimer = null;
	private Animation rotateAnimation;
	private Menu menu;
	private ActionBar actionBar;
	
	private ImageView front_cover_iv = null;
	private TextView music_name_tv = null;
	private TextView player_name_tv = null;
	private TextView preparing_tv = null;
	private SeekBar music_progress = null;
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
        musicPlayer.setOnPreparedListener(this);
        musicPlayer.setOnBufferingUpdateListener(this);
        musicPlayer.setOnCompletionListener(this);

        front_cover_iv = (ImageView)this.findViewById(R.id.front_cover);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);
        music_name_tv = (TextView)this.findViewById(R.id.music_name);
        player_name_tv = (TextView)this.findViewById(R.id.player_name);
        preparing_tv = (TextView)this.findViewById(R.id.preparing);
        preparing_tv.setVisibility(View.GONE);
        music_progress = (SeekBar)this.findViewById(R.id.music_progress);
        music_progress.setEnabled(false);
        radio_list = (GridView)this.findViewById(R.id.radio_list);
        play = (Button)this.findViewById(R.id.play);
        next = (Button)this.findViewById(R.id.next);
        ban = (Button)this.findViewById(R.id.ban);
        
        play.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (isPrepare && musicPlayer != null && isPlay && musicPlayer.isPlaying())
                {
                    musicPlayer.pause();
                    isPlay = false;
                    front_cover_iv.clearAnimation();
                    v.setBackgroundResource(R.drawable.start);
                }
                else if (isPrepare && musicPlayer != null && !isPlay && !musicPlayer.isPlaying())
                {
                    musicPlayer.start();
                    isPlay = true;
                    front_cover_iv.startAnimation(rotateAnimation);
                    v.setBackgroundResource(R.drawable.stop);
                }
            }
        });
        
        next.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                if (musicPlayer != null)
                {
                    if (musicPlayer.isPlaying())
                    {
                        play.setBackgroundResource(R.drawable.buffering);
                        musicPlayer.stop();
                        front_cover_iv.clearAnimation();
                    }
                    
                    isPrepare = false;
                    isPlay = false;
                    radio.setPlayType('s');
                    freshMenu();
                    prepareMusic();
                }
                
            }
        });
        
        ban.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (musicPlayer != null)
                {
                    if (musicPlayer.isPlaying())
                    {
                        play.setBackgroundResource(R.drawable.buffering);
                        musicPlayer.stop();
                        front_cover_iv.clearAnimation();
                    }
                    
                    isPrepare = false;
                    isPlay = false;
                    radio.setPlayType('b');
                    freshMenu();
                    prepareMusic();
                }
                
            }
        });
        
        setButtonGroupState(false);
        
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
        
        musicProgressTimer = new Timer();
        musicProgressTimer.schedule(musicProgressTimerTask, 0, 500);
 
        actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
            
	}
	
	
	private void prepareMusic()
	{
		if (radio != null)
		{
		    play.setBackgroundResource(R.drawable.buffering);
		    preparing_tv.setVisibility(View.VISIBLE);
			currentMusic = radio.getMusic();
			if (currentMusic != null)
			{
			    GetBitmapThread getBitmapThread = new GetBitmapThread(currentMusic.getCover());
			    getBitmapThread.start();
			    music_name_tv.setText(currentMusic.getName());
			    player_name_tv.setText(currentMusic.getPlayer());
			
			    musicPlayer.reset();
			    try 
			    {
			        musicPlayer.setDataSource(currentMusic.getUrl());
			        current_song_id = currentMusic.getId();
			        musicPlayer.prepare();
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
			else
			{
                String requestUrl = radio.constructRadioUrl(current_song_id);
                HttpSender.getInstance().Httpget(requestUrl, currentChannel, musicHandler);
            }

		}
	}
	
	
	class OnRadioChoose implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			String requestUrl = null;
			Radio newRadio = null;
			
			switch (position) {
			case 0:
			    newRadio = new ChineseRadio('s');			
				break;
			case 1:
			    newRadio = new WesternRadio('s');
			    break;
			case 2:
			    newRadio = new CantoneseRadio('s');
			    break;
			case 3:
			    newRadio = new EightyRadio('s');
			    break;
			case 4:
			    newRadio = new NinetyRadio('s');
			    break;
			case 5:
			    newRadio = new NewMusicRadio('s');
			    break;
			case 6:
			    newRadio = new FreshRadio('s');
			    break;
			case 7:
			    newRadio = new CartoonRadio('s');
			    break;
			case 8:
			    newRadio = new LightMusicRadio('s');
			    break;
			case 9:
			    newRadio = new RockRadio('s');
			    break;
			default:
				break;
			}
			
			
			if (newRadio != null && currentChannel != newRadio.getChannel())
			{
			    play.setBackgroundResource(R.drawable.buffering);
			    isPrepare = false;
			    radio = newRadio;
			    requestUrl = radio.constructRadioUrl();
                currentChannel = radio.getChannel();
                newRadio = null;
                freshMenu();
                preparing_tv.setVisibility(View.VISIBLE);
                HttpSender.getInstance().Httpget(requestUrl, currentChannel, musicHandler);
			}
			
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
			else if (msg.what == -2)
			{
			    int position = musicPlayer.getCurrentPosition();  
	            int duration = musicPlayer.getDuration();  
	              
	            if (duration > 0) {  
	                long pos = music_progress.getMax() * position / duration;  
	                music_progress.setProgress((int)(pos));  
	            }  
			}
			
		}
		
	}
	
	
	class GetBitmapThread extends Thread
	{
		private String httpUrl = null;
		
		public GetBitmapThread(String url)
		{
		    this.httpUrl = url;
		}
		
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
			front_cover_iv.clearAnimation();
		}
		if (musicProgressTimer != null)
		{
			musicProgressTimer.cancel();
		}
		if (musicProgressTimerTask != null)
		{
			musicProgressTimerTask.cancel();
		}
		
		super.onDestroy();
	}


	@Override
	public void onCompletion(MediaPlayer mp) 
	{	    
		isPrepare = false;
		isPlay = false;
		front_cover_iv.clearAnimation();
		radio.setPlayType('s');
		freshMenu();
		prepareMusic();
	}


	@Override
	public void onPrepared(MediaPlayer mp) 
	{
	    isPrepare = true;
	    freshMenu();
	    preparing_tv.setVisibility(View.GONE);
		if (isPrepare && mp != null)
		{
			mp.start();		
			isPlay = true;
			play.setBackgroundResource(R.drawable.stop);
			setButtonGroupState(true);
			front_cover_iv.startAnimation(rotateAnimation);
			
		}
	}


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent)
    {
        music_progress.setSecondaryProgress(percent);   
    }
    
    
    private void setButtonGroupState(Boolean clickable)
    {
        play.setClickable(clickable);
        next.setClickable(clickable);
        ban.setClickable(clickable);
    }
    
    
    TimerTask musicProgressTimerTask = new TimerTask() 
    {  
        @Override  
        public void run() 
        {  
            if (musicPlayer != null && isPrepare && isPlay && musicPlayer.isPlaying()) 
            {  
                Message message = Message.obtain();
                message.what = -2;
                musicHandler.sendMessage(message);
            }  
        }  
    };  
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	setIconEnable(menu, true);
        getMenuInflater().inflate(R.menu.music_player, menu);
        this.menu = menu;
        freshMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.share)
        {
        	try 
        	{
        		String text = getString(R.string.i_am_listening) + " " + currentMusic.getName() + "-" + currentMusic.getPlayer();
				WXShare.getInstance().shareMusictoWX(this.getApplicationContext(), currentMusic.getUrl(), text);
			} 
        	catch (Exception e) 
        	{
				e.printStackTrace();
			}
        	
        }
        else if (id == android.R.id.home)
        {
        	onBackPressed();
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
        
    }
    
    
    private void freshMenu()
    {
    	MenuItem menuItem = null;
    	menuItem = menu.getItem(0);
    	menuItem.setEnabled(isPrepare); 	
    }
    
    
    private void setIconEnable(Menu menu, boolean enable)  
    {  
        try   
        {  
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");  
            Method method = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);  
            method.setAccessible(true);  
               
            method.invoke(menu, enable);  
              
        }
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        
    }  
    
	
}
