/**
 * 
 */
package com.utils.radio;


/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a rock radio.
 * All music in this radio are in a rock style.
 * @author WilliamDeng
 *
 */
public class RockRadio extends Radio {

    private int start_song_id = 1642510;

    
    /**
     * The default constructor.
     */
    public RockRadio() 
    {
        super(7);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public RockRadio(char play_type)
    {
        super(7, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of rock song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }

    
}
