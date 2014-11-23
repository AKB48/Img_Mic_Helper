package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 *  This is a singleton class.
 *  This processor transform an image into its negative colors.
 * @author William
 *
 */
public class NegativeProcessor implements Processor {

	
	private volatile static NegativeProcessor uniqueInstance = null;
	
	private NegativeProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return NegativeProcessor the unique instance of this class
	 */
	public static NegativeProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(NegativeProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new NegativeProcessor();
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
		int newPixel = 0, a, r, g, b;
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int oldPixel = bitmap.getPixel(j, i); 
				a = oldPixel & 0xff000000;
				// 255 - red channel
				r = 0x00ff0000 - (oldPixel & 0x00ff0000);
				// 255 - green channel
                g = 0x0000ff00 - (oldPixel & 0x0000ff00);
                // 255 - blue channel
                b = 0xff - (oldPixel & 0xff);
                // blend each channel
				newPixel = a | r | g | b;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}

}
