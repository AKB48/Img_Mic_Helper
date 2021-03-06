/**
 * 
 */
package com.utils.radio;


/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a new music radio.
 * All music in this radio are created recently.
 * @author WilliamDeng
 *
 */
public class NewMusicRadio extends Radio {

    private int start_song_id = 1897635;

    
    /**
     * The default constructor.
     */
    public NewMusicRadio() 
    {
        super(61);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public NewMusicRadio(char play_type)
    {
        super(61, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of new song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }

    
}
