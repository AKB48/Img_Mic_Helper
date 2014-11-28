package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;


/**
 * This is a singleton class.
 * The processors transforms an image into a soft light style.
 * @author William
 *
 */
public class SoftlightProcessor implements Processor {

	
	private volatile static SoftlightProcessor uniqueInstance = null;
	private float opacity = 0.6f;
	
	
	private SoftlightProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return SoftlightProcessor the unique instance of this class
	 */
	public static SoftlightProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(SoftlightProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new SoftlightProcessor();
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
		int oldPixel = 0;
		int newPixel = 0;
		int old_r = 0, old_g = 0, old_b = 0;
		int new_r = 0, new_g = 0, new_b = 0;
		int alpha = 0xff000000;
	
		Bitmap tempBitmap = GaussianBlurProcessor.getInstance().process(bitmap);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				oldPixel = bitmap.getPixel(j, i);
				newPixel = tempBitmap.getPixel(j, i);
				
				alpha = oldPixel & 0xff000000;
				old_r = (oldPixel >> 16) & 0xff;
				new_r = (newPixel >>16) & 0xff;
				old_g = (oldPixel >> 8) & 0xff;
				new_g = (newPixel >> 8) & 0xff;
				old_b = oldPixel & 0xff;
				new_b = newPixel & 0xff;
				
				// mixture two images into the final image.
				// the mixture portion is opacity.
				// The bigger opacity, the more palpable result.
				new_r = (int) ((float)new_r * opacity + (float)old_r * (1 - opacity));
				new_g = (int) ((float)new_g * opacity + (float)old_g * (1 - opacity));
				new_b = (int) ((float)new_b * opacity + (float)old_b * (1 - opacity));

				
				newPixel = alpha | (new_r << 16) | (new_g << 8) | new_b;
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}

		
		return  tempBitmap;
		
	}	

}
