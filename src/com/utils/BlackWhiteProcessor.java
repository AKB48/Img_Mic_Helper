package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * This processor transform an image into a black white two colors style.
 * If the pixel is bigger than 128, it would be set to 255, 
 * otherwise 0.
 * @author Willam
 *
 */
public class BlackWhiteProcessor implements Processor {

	
	private volatile static BlackWhiteProcessor uniqueInstance = null;
	private int threshold = 128;
	
	private BlackWhiteProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return BlackWhiteProcessor the unique instance of this class
	 */
	public static BlackWhiteProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(BlackWhiteProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new BlackWhiteProcessor();
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
		int newPixel = 0, tempPixel = 0, a, r, g, b;
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int oldPixel = bitmap.getPixel(j, i); 
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                tempPixel = pixelChoose( (int)( (r*0.3)+(b*0.59)+(g*0.11) ) );
                newPixel = (a << 24) | (tempPixel << 16) | (tempPixel << 8) | tempPixel;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}
	
	
	private int pixelChoose(int oldPixel)
	{
		return oldPixel<threshold?0:255;
	}

}
