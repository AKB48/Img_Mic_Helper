/**
 * 
 */
package com.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

/**
 * This is a basic object class.
 * This class is used to describe a music.
 * A music contains some attributes:
 * 1) name -- name of music
 * 2) player -- name of player who plays the music
 * 3) url -- url to retrieve the music
 * 4) cover -- the front cover url of the music
 * @author William
 *
 */
public class Music {
	
	private int song_id = 0;
	private String name = null;
	private String player = null;
	private String url = null;
	private String cover = null;
	
	
	/**
	 * The default constructor.
	 */
	public Music()
	{
		
	}
	
	
	/**
	 * The constructor with five full parameters.
	 * @param song_id the id of the music.
	 * @param name the name of the music.
	 * @param player the name of the player.
	 * @param url the url of the music.
	 * @param cover the url of the music front cover.
	 */
	public Music(int song_id, String name, String player, String url, String cover)
	{
		this.song_id = song_id;
		this.name = name;
		this.player = player;
		this.url = url;
		this.cover = cover;
	}
	
	
	/**
	 * The constructor with four parameters except music front conver.
	 * @param song_id the id of the music.
	 * @param name the name of the music.
	 * @param player the name of the player.
	 * @param url the url of the music.
	 */
	public Music(int song_id, String name, String player, String url)
	{
		this(song_id, name, player, url, null);
	}
	
	
	/**
	 * The constructor with three parameters except music player and front cover.
	 * @param song_id the id of the music.
	 * @param name the name of the music.
	 * @param url the url of the music.
	 */
	public Music(int song_id, String name, String url)
	{
		this(song_id, name, null, url, null);
	}
	
	
	/**
	 * The constructor with two parameters except music name, music player and front cover.
	 * @param song_id the id of the music.
	 * @param url the url of the music.
	 */
	public Music(int song_id, String url)
	{
		this(song_id, null, null, url, null);
	}
	
	
	/**
	 * The constructor with just one parameter -- url.
	 * @param url the url of the music.
	 */
	public Music(String url)
	{
		this(0, null, null, url, null);
	}
	
	
	/**
	 * The constructor analyzes and retrieves the parameters from a json object.
	 * @param json the json object to be analyzed.
	 */
	public Music(JSONObject json)
	{
		int song_id = 0;
		String name = null;
		String player = null;
		String url = null;
		String cover = null;
		
		try 
		{
			song_id = json.getInt("sid");
			name = json.getString("title");
			player = json.getString("artist");
			url = json.getString("url");
			cover = json.getString("picture");
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		finally
		{
			this.song_id = song_id;
			this.name = name;
			this.player = player;
			this.url = url;
			this.cover = cover;
		}
		
	}
	
	
	/**
	 * Set the id of the music.
	 * @param song_id id of music.
	 * @return true if the parameter is successfully set.
	 *            false if the parameter is less than zero, which is illegal.
	 */
	public Boolean setId(int song_id)
	{
		if (song_id < 0)
		{
			return false;
		}
		else
		{
			this.song_id = song_id;
		}
		
		return true;
	}
	
	
	/**
	 * Get the id of the music.
	 * @return the id of the music.
	 */
	public int getId()
	{
		return this.song_id;
	}
	
	
	/**
	 * Set the name of the music.
	 * @param name the name of music.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	/**
	 * Get the name of the music.
	 * @return the name of music.
	 */
	public String getName()
	{
		return this.name;
	}
	
	
	/**
	 * Set the name of the music player.
	 * @param player the name of the music player.
	 */
	public void setPlayer(String player)
	{
		this.player = player;
	}
	
	
	/**
	 * Get the name of the music player.
	 * @return the name of the music player.
	 */
	public String getPlayer()
	{
		return this.player;
	}
	
	
	/**
	 * Set the url of the music.
	 * @param url the url of the music.
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	
	/**
	 * Get the url of the music.
	 * @return the url of the music.
	 */
	public String getUrl()
	{
		return this.url;
	}
	
	
	/**
	 * Set the url of the music cover.
	 * @param cover the url of the music cover.
	 */
	public void setCover(String cover)
	{
		this.cover = cover;
	}
	
	
	/**
	 * Get the url of the music cover.
	 * @return the url of the music cover.
	 */
	public String getCover()
	{
		return this.cover;
	}

}
