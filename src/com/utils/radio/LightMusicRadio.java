/**
 * 
 */
package com.utils.radio;


/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a light music radio.
 * All music in this radio are in a light style.
 * @author WilliamDeng
 *
 */
public class LightMusicRadio extends Radio {

    private int start_song_id = 253009;

    
    /**
     * The default constructor.
     */
    public LightMusicRadio() 
    {
        super(9);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public LightMusicRadio(char play_type)
    {
        super(9, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of light song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }

    
}
