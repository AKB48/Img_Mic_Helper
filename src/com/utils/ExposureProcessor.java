package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * The processor transform an image into an exposure style.
 * If the pixel is less than the threshold, invert it.
 * @author William
 *
 */
public class ExposureProcessor implements Processor {
	
	
	private volatile static ExposureProcessor uniqueInstance = null;
	private int threshold = 128;
	
	private ExposureProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ExposureProcessor the unique instance of this class
	 */
	public static ExposureProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(ExposureProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new ExposureProcessor();
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
		int currentPixel = 0, newPixel = 0;
		int a,r,g,b;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++) 
			{
				currentPixel = bitmap.getPixel(j, i);
				a = currentPixel & 0xff000000;
				r = (currentPixel >> 16) & 0xff;
				g = (currentPixel >> 8) & 0xff;
				b = currentPixel & 0xff;
				
				r = (r < this.threshold ? 255 - r : r);
				g = (g < this.threshold ? 255 - g : g);
				b = (b < this.threshold ? 255 - b : b);
				
				newPixel = a | (r << 16) | (g << 8) | b;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		
		return tempBitmap;
	}

}
