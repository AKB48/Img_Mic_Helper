/**
 * 
 */
package com.utils.radio;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.R.integer;
import android.util.Log;

import com.app.img_mic_helper.R.string;

/**
 * This is a basic object class.
 * This class is used to describe a series of music that belongs to a same type.
 * The radio is a collection of the same type music.
 * @author William
 *
 */
public class Radio {
	
	private String basic_url = "http://douban.fm/j/mine/playlist?pb=64&from=mainsite";
	private String url = null;
	private int channel = 0;
	private char play_type = 's';
	private ArrayList<Music> album = null;
	private int currentPlayIndex = 0;
	
	
	/**
	 * The default constructor.
	 */
	public Radio()
	{
		
	}
	
	
	/**
	 * The constructor with two parameters -- channel and play type.
	 * @param channel the id of channel.
	 * @param play_type the character which represents the play type.
	 */
	public Radio(int channel, char play_type)
	{
		this.channel = channel;
		this.play_type = play_type;
	}
	
	
	/**
	 * The constructor with only one parameter -- channel.
	 * The play type will be set to the default value 's'.
	 * @param channel the id of channel.
	 */
	public Radio(int channel)
	{
		this(channel, 's');
	}
	
	
	/**
	 * Set the id of radio channel.
	 * @param channel the id of channel.
	 * @return true if the parameter channel is successfully set.
	 *            false if the parameter is not larger than zero, which is illegal.
	 */
	public Boolean setChannel(int channel)
	{
		if (channel <= 0)
		{
			return false;
		}
		else
		{
			this.channel = channel;
		}
		
		return true;
	}
	
	
	/**
	 * Get the id of radio channel.
	 * @return the id of channel.
	 */
	public int getChannel()
	{
		return this.channel;
	}
	
	
	/**
	 * Set the play type of the radio
	 * @param play_type the character that represents the play type.
	 *            It would be 's', 'b' or 'r'.
	 * @return true if the parameter is successfully set.
	 *            false if the parameter is illegal.
	 */
	public Boolean setPlayType(char play_type)
	{
		if (play_type == 's')
		{
			// s represents sequential
			this.play_type = 's';
		}
		else if(play_type == 'b')
		{
			// b represents ban
			this.play_type = 'b';
		}
		else if(play_type == 'r')
		{
			// r represents right
			this.play_type = 'r';
		}
		else
		{
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Get the play type of the radio.
	 * @return the character of the radio play type.
	 */
	public char getPlayType()
	{
		return this.play_type;
	}
	
	
	/**
	 * Construct the url of this radio.
	 * The url is used to request music resources.
	 * This method uses a default id of a song.
	 * @return the url after construction.
	 */
	public String constructRadioUrl()
	{
		return this.constructRadioUrl(0);
	}
	
	
	/**
	 * Construct the url of this radio.
	 * The url is used to request music resources.
	 * @param song_id the id of last music listened to.
	 * @return the url after construction.
	 */
	public String constructRadioUrl(int song_id)
	{
		this.url = basic_url + "&type=" + this.play_type + "&channel=" + this.channel + "&sid=" + song_id;
		return this.url;
	}
	
	
	public void generateAlbum(JSONArray json_array)
	{
		int album_size = json_array.length();
		if (album == null)
		{
			album = new ArrayList<Music>();
		}
		else
		{
			album.clear();
		}
		
		for (int k = 0; k < album_size; k++)
		{
			Music music = null;
			try 
			{
				music = new Music(json_array.getJSONObject(k));
			} 
			catch (JSONException e)
			{
				e.printStackTrace();
			}
			if (music != null)
			{
				album.add(music);
			}
			
		}
		
		this.currentPlayIndex = 0;
		
	}
	
	
	/**
	 * Get music from album to play.
	 * @return the music to play.
	 */
	public Music getMusic()
	{
		Music music = null;
		if (this.currentPlayIndex < album.size()) 
		{
			music = album.get(this.currentPlayIndex);
			this.currentPlayIndex++;
		}
		
		return music;
	}
	

}
