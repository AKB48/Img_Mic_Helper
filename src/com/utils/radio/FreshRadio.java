/**
 * 
 */
package com.utils.radio;


/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a fresh radio.
 * All music in this radio will give listeners a clean and fresh feeling.
 * @author WilliamDeng
 *
 */
public class FreshRadio extends Radio {

    private int start_song_id = 1742970;

    
    /**
     * The default constructor.
     */
    public FreshRadio() 
    {
        super(76);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public FreshRadio(char play_type)
    {
        super(76, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of fresh song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }

    
}
