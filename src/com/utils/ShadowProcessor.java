package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * The processor transforms an image into a shadow style.
 * @author William
 *
 */
public class ShadowProcessor implements Processor {

	
	private volatile static ShadowProcessor uniqueInstance = null;
	private int max_shift_intensity = 15;
	private int shift_intensity = 0;
	
	private ShadowProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ShadowProcessor the unique instance of this class
	 */
	public static ShadowProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(ShadowProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new ShadowProcessor();
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
        int current_pixel = 0, new_pixel = 0;
        
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int i = 0; i < height; i++) 
		{
			shift_intensity = (int)(Math.random()*max_shift_intensity) - 7;
			
			for (int j = 0; j < width; j++) 
			{			
				if (shift_intensity > 0 && j < shift_intensity)
					current_pixel = 0xff000000;
				else if (shift_intensity < 0 && j - shift_intensity >= width)
					current_pixel = 0xff000000;
				else {
					current_pixel = bitmap.getPixel(j-shift_intensity, i);
				}

				new_pixel = (current_pixel >> 16) & 0xff;

				if (new_pixel > 128)
				{
					new_pixel = 255;
				}
				else
				{
					new_pixel = 92;
				}
					
				new_pixel = 0xff000000 | (new_pixel << 16) | (new_pixel << 8) | new_pixel;
				
				tempBitmap.setPixel(j, i, new_pixel);
				
			}
		}
		
		return tempBitmap;
	}

}
