package com.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;

/**
 *  This is a singleton class.
 *  It is used to transform a colorful image into a gray image.
 * @author William
 *
 */
public class GrayProcessor implements Processor {

	private volatile static GrayProcessor uniqueInstance = null;
	
	private GrayProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return GrayProcessor the unique instance of this class
	 */
	public static GrayProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(GrayProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new GrayProcessor();
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
                tempPixel = (int)((r*0.3)+(b*0.59)+(g*0.11));
				newPixel = (a << 24) | (tempPixel << 16) | (tempPixel << 8) | tempPixel;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}

}
