package com.utils.processor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * 
 */

/**
 * This is a singleton class.
 * This processor transforms an image into a cast style.
 * @author William
 *
 */
public class CastProcessor implements Processor {

	
	private volatile static CastProcessor uniqueInstance = null;
    
    
    private CastProcessor()
    {
        
    }
    
    
    /**
     * the public method to get the unique instance
     * @return CastProcessor the unique instance of this class
     */
    public static CastProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(CastProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new CastProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }

	
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if (bitmap == null)
		{
			return null;		
		}

		
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();

	    Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    
	    int oldPixel = 0;
	    int newPixel = 0;
	    
	    for (int i = 0; i < height; i++)
	    {
	    	
	      for (int j = 0; j < width; j++)
	      {
	    	  
	        oldPixel = bitmap.getPixel(j, i);
	        
	        int r = (oldPixel & 0x00ff0000)>>16;
	        int g = (oldPixel & 0x0000ff00)>>8;
	        int b = oldPixel & 0xff;

	        r = r * 128 / (g + b + 1);
	        r = clamp(r);

	        g = g * 128 / (r + b + 1);
	        g = clamp(g);

	        b = b * 128 / (g + r + 1);
	        b = clamp(b);
	        
	        newPixel = (oldPixel & 0xff000000) | (r << 16) | (g << 8) | b;
	        destBitmap.setPixel(j, i, newPixel);
	        
	      }
	      
	    }

	    return destBitmap;
	    
	}
	
	
    /**
     * Clamp a value to the range [0, 255]
     * @param value the param to set.
     * @return the value of the param after clamp.
     */
    private int clamp(int value) {
        if (value < 0)
        {
            return 0;
        }
        else if (value > 255)
        {
            return 255;
        }

        return value;
        
    }
    

}
