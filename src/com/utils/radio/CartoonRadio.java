/**
 * 
 */
package com.utils.radio;


/**
 * This is a concrete radio class the extends from parent class radio.
 * This is a Cartoon radio.
 * All music in this radio are from cartoons and comics.
 * @author WilliamDeng
 *
 */
public class CartoonRadio extends Radio {

    private int start_song_id = 169922;

    
    /**
     * The default constructor.
     */
    public CartoonRadio() 
    {
        super(28);
    }

    
    /**
     * The constructor has only one parameter -- play type of radio.
     * @param play_type indicates the play type of this radio.
     */
    public CartoonRadio(char play_type)
    {
        super(28, play_type);
    }
    
    
    /**
     * Construct the url of this radio.
     * The url is used to request music resources.
     * This method uses a default id of cartoon song.
     * @return the url after construction.
     */
    public String constructRadioUrl()
    {
        return super.constructRadioUrl(this.start_song_id);
    }

    
}
