package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into the comic style.
 * @author William
 *
 */
public class ComicProcessor implements Processor {


	private volatile static ComicProcessor uniqueInstance = null;
	
	private ComicProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ComicProcessor the unique instance of this class
	 */
	public static ComicProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(ComicProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new ComicProcessor();
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
		int newPixel = 0, tempPixel = 0;
		int a, r, g, b;
		int new_r, new_g, new_b;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int oldPixel = bitmap.getPixel(j, i); 
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                
                new_r = Math.abs(g - b + g + r) * r / 256;
                if (new_r > 255)
                {
                	new_r = 255;
                }
                
                new_g = Math.abs(b - g + b + r) * r / 256;
                if (new_g > 255)
                {
                	new_g = 255;
                }
                
                new_b = Math.abs(b - g + b + r) * g / 256;
                if (new_b > 255)
                {
                	new_b = 255;
                }
                
                tempPixel = (int)((new_r*0.3)+(new_b*0.59)+(new_g*0.11));
				newPixel = (a << 24) | (tempPixel << 16) | (tempPixel << 8) | tempPixel;
				
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}

}
