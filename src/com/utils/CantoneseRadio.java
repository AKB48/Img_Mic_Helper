/**
 * 
 */
package com.utils;

/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a Cantonese radio.
 * All music in this radio are sung by Cantonese.
 * @author WilliamDeng
 *
 */
public class CantoneseRadio extends Radio {

    private int start_song_id = 1468457;

    
    /**
     * The default constructor.
     */
    public CantoneseRadio() 
    {
        super(6);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public CantoneseRadio(char play_type)
    {
        super(6, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of Cantonese song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }

    
}
