/**
 * 
 */
package com.utils;

import org.json.JSONException;
import org.json.JSONObject;

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
	 * The constructor with four full parameters.
	 * @param name the name of the music.
	 * @param player the name of the player.
	 * @param url the url of the music.
	 * @param cover the url of the music front cover.
	 */
	public Music(String name, String player, String url, String cover)
	{
		this.name = name;
		this.player = player;
		this.url = url;
		this.cover = cover;
	}
	
	
	/**
	 * The constructor with three parameters except music front conver.
	 * @param name the name of the music.
	 * @param player the name of the player.
	 * @param url the url of the music.
	 */
	public Music(String name, String player, String url)
	{
		this(name, player, url, null);
	}
	
	
	/**
	 * The constructor with two parameters except music player and front cover.
	 * @param name the name of the music.
	 * @param url the url of the music.
	 */
	public Music(String name, String url)
	{
		this(name, null, url, null);
	}
	
	
	/**
	 * The constructor with just one parameter -- url.
	 * @param url the url of the music.
	 */
	public Music(String url)
	{
		this(null, null, url, null);
	}
	
	
	/**
	 * The constructor analyzes and retrieves the parameters from a json object.
	 * @param json the json object to be analyzed.
	 */
	public Music(JSONObject json)
	{
		String name = null;
		String player = null;
		String url = null;
		String cover = null;
		
		try 
		{
			name = json.getString("Name");
			player = json.getString("player");
			url = json.getString("url");
			cover = json.getString("cover");
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
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
