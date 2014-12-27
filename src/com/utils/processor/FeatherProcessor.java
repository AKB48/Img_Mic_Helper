/**
 * 
 */
package com.utils.processor;


import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * This processor feathers an image.
 * The border of the image will become hazy.
 * The parameter size can control the area of the feather effect.
 * @author William
 *
 */
public class FeatherProcessor implements Processor {


    private volatile static FeatherProcessor uniqueInstance = null;
    private float size = 0.5f;   
    
    
    private FeatherProcessor()
    {
        
    }
    
    
    /**
     * the public method to get the unique instance
     * @return FeatherProcessor the unique instance of this class
     */
    public static FeatherProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(FeatherProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new FeatherProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
    
    
    /**
     * Set the parameter size, which represents the area of feather effect.
     * @param size the parameter to set. It must be in the range of [0, 1].
     * @return true if the parameter is successfully set.
     *            false if the parameter is out of range.
     */
    public Boolean setSize(float size)
    {
    	if (size < 0 || size > 1)
    	{
    		return false;
    	}
    	else
    	{
    		this.size = size;
		}
    	
    	return true;
    }
    
    
    /**
     * Get the parameter size.
     * @return the value of size.
     */
    public float getSize()
    {
    	return this.size;
    }
    
    
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if(bitmap == null)
		{
			return null;
		}

		
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    
	    int ratio = width >height ? height * 32768 /width : width * 32768/height;

	    int cx = width >> 1;
	    int cy = height >> 1;
	    int max = cx * cx + cy * cy;
	    int min = (int)(max * (1 - size));
	    int difference = max - min;

	    Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    
	    int r, g, b;
	    int oldPixel = 0;
	    int newPixel = 0;
	    
	    
	    for (int i = 0; i < height; i++)
	    {
	    	
	      for (int j = 0; j < width; j++)
	      {
	    	  
	        oldPixel = bitmap.getPixel(j, i);
	        
	        r = (oldPixel & 0x00ff0000)>>16;
	        g = (oldPixel & 0x0000ff00)>>8;
	        b = oldPixel & 0xff;

	        int dx = cx - j;
	        int dy = cy - i;

	        if (width > height)
	        {
	          dx = (dx * ratio) >> 15;
	        }
	        else
	        {
	          dy = (dy * ratio) >> 15;
	        }

	        int distance2 = dx * dx + dy * dy;
	        float v = ((float) distance2 / difference) * 255;
	        r = (int)(r + v);
	        g = (int)(g + v);
	        b = (int)(b + v);
	        r = clamp(r);
	        g = clamp(g);
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
