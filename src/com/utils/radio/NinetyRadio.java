/**
 * 
 */
package com.utils.radio;


/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a Eighty radio.
 * All music in this radio are created in 1990s.
 * @author WilliamDeng
 *
 */
public class NinetyRadio extends Radio {

    private int start_song_id = 992174;

    
    /**
     * The default constructor.
     */
    public NinetyRadio() 
    {
        super(5);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public NinetyRadio(char play_type)
    {
        super(5, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of 1990s' song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }

    
}
