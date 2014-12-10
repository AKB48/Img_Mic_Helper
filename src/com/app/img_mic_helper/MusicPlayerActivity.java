/**
 * 
 */
package com.app.img_mic_helper;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author William
 *
 */
public class MusicPlayerActivity extends Activity {
	
	private ImageView front_cover_iv = null;
	private TextView music_name_tv = null;
	private TextView player_name_tv = null;
	private ProgressBar music_progress = null;
	private ProgressBar sound_progress = null;
	private Button play = null;
	private Button next = null;
	private Boolean isPlay = false;
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);
        
        front_cover_iv = (ImageView)this.findViewById(R.id.front_cover);
        music_name_tv = (TextView)this.findViewById(R.id.music_name);
        player_name_tv = (TextView)this.findViewById(R.id.player_name);
        music_progress = (ProgressBar)this.findViewById(R.id.music_progress);
        sound_progress = (ProgressBar)this.findViewById(R.id.sound_progress);
        play = (Button)this.findViewById(R.id.play);
        next = (Button)this.findViewById(R.id.next);
        
	}
	
}
