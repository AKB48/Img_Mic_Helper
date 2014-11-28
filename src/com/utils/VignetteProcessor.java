package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into a vignette style.
 * @author William
 *
 */
public class VignetteProcessor implements Processor {

	
	private volatile static VignetteProcessor uniqueInstance = null;
	
	private VignetteProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return VignetteProcessor the unique instance of this class
	 */
	public static VignetteProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(VignetteProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new VignetteProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if (bitmap == null)
			return null;
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		double center_width = 0.5f * width;
		double center_height = 0.5f * height;
		double max_dist = 1.0f / Math.sqrt(center_width*center_width+center_height*center_height);
		double dist = 0.0f;
		double lumen = 0.0f;
		int newPixel = 0;
		int a, r, g, b;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				int oldPixel = bitmap.getPixel(j, i); 
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                
                dist = Math.sqrt((center_width-j)*(center_width-j)+(center_height-i)*(center_height-i));
                lumen = 0.75 / (1.0 + Math.exp((dist * max_dist - 0.73) * 20.0)) + 0.25;
                
                r = (int) (r * lumen);
                g = (int) (g * lumen);
                b = (int) (b * lumen);
                
                
				newPixel = (a << 24) | (r << 16) | (g << 8) | b;
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}
		
		return tempBitmap;
	}

}
