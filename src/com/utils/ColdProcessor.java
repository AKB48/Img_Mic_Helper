package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 *  This is a singleton class.
 *  It is used to transform an image into a cold style image.
 *  The cold style color is (6, 177, 248).
 * @author Willam
 *
 */
public class ColdProcessor implements Processor {

	
	private volatile static ColdProcessor uniqueInstance = null;
	private int[] cold = {6, 177, 248};
	private double alpha = 0.15;
	
	private ColdProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ColdProcessor the unique instance of this class
	 */
	public static ColdProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(ColdProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new ColdProcessor();
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
				r = (int)( alpha * cold[0] + (1-alpha) * ( (oldPixel >> 16) & 0xff) );
                g = (int)( alpha * cold[1] + (1-alpha) * ( (oldPixel >> 8) & 0xff) );
                b = (int)( (alpha+0.05) * cold[2] + (1-alpha-0.05) * (oldPixel & 0xff) );
                r = r > 255 ? 255 : (r < 0 ? 0 : r);
                g = g > 255 ? 255 : (g < 0 ? 0 : g);
                b = b > 255 ? 255 : (b < 0 ? 0 : b);
				newPixel = a | (r << 16) | (g << 8) | b;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}

}
