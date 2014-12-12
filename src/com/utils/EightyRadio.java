/**
 * 
 */
package com.utils;

/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a Eighty radio.
 * All music in this radio are created in 1980s.
 * @author WilliamDeng
 *
 */
public class EightyRadio extends Radio {

    private int start_song_id = 137243;

    
    /**
     * The default constructor.
     */
    public EightyRadio() 
    {
        super(4);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public EightyRadio(char play_type)
    {
        super(4, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of 1980s' song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }

    
}
