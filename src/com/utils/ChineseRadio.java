/**
 * 
 */
package com.utils;

/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a Chinese radio.
 * All music in this radio are sung by Chinese.
 * @author William
 *
 */
public class ChineseRadio extends Radio {
	
	private int start_song_id = 1454902;

	
	/**
	 * The default constructor.
	 */
	public ChineseRadio() 
	{
		super(1);
	}

	
	/**
	 * The constructor has only one parameter -- play type of radio.
	 * @param play_type indicates the play type of this radio.
	 */
	public ChineseRadio(char play_type)
	{
		super(1, play_type);
	}
	
	
	/**
	 * Construct the url of this radio.
	 * The url is used to request music resources.
	 * This method uses a default id of Chinese song.
	 * @return the url after construction.
	 */
	public String constructRadioUrl()
	{
		return super.constructRadioUrl(this.start_song_id);
	}


}
