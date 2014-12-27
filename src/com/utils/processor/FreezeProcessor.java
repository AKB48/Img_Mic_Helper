/**
 * 
 */
package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * This processor transforms an image into a freeze style.
 * @author William
 *
 */
public class FreezeProcessor implements Processor {


    private volatile static FreezeProcessor uniqueInstance = null;
    
    
    private FreezeProcessor()
    {
        
    }
    
    
    /**
     * the public method to get the unique instance
     * @return FreezeProcessor the unique instance of this class
     */
    public static FreezeProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(FreezeProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new FreezeProcessor();
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

	    int r, g, b;
	    int oldPixel = 0;
	    int newPixel = 0;
	    
	    Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    
	    
	    for (int i = 0; i < height; i++)
	    {
	    	
	      for (int j = 0; j < width; j++)
	      {
	    	  
	        oldPixel = bitmap.getPixel(j, i);
	        
	        r = (oldPixel & 0x00ff0000)>>16;
	        g = (oldPixel & 0x0000ff00)>>8;
	        b = oldPixel & 0xff;

	        r = freeze(r-g-b);
	        g = freeze(g-r-b);
	        b = freeze(b-r-g);
	        
	        newPixel = (oldPixel & 0xff000000) | (r << 16) | (g << 8) | b;
	        destBitmap.setPixel(j, i, newPixel);
	        
	      }
	      
	    }

	    return destBitmap;
	    
	}
	
	
	/**
	 * Apply the freeze effect on a pixel.
	 * @param value the value to be processed.
	 * @return the value after processing.
	 */
	private int freeze(int value)
	{
		value = value * 3 / 2;

		if (value < 0)
		{
			value = -value;
		}

		if (value > 255)
		{
			value = 255;
		}
		
		return value;
		
	}
	

}
