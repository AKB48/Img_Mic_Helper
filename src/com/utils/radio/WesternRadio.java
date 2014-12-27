/**
 * 
 */
package com.utils.radio;



/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a Western radio.
 * All music in this radio are sung by Western.
 * @author WilliamDeng
 *
 */
public class WesternRadio extends Radio {
    
    private int start_song_id = 1478796;

    
    /**
     * The default constructor.
     */
    public WesternRadio() 
    {
        super(2);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public WesternRadio(char play_type)
    {
        super(2, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of Western song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }


}
