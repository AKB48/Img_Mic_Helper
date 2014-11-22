package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 *  This is a singleton class.
 *  It is used to transform an image into a gothic style image.
 *  The gothic style color is (143,122,16).
 * @author Willam
 *
 */
public class GothicProcessor implements Processor {

	
	private volatile static GothicProcessor uniqueInstance = null;
	private int[] gothic = {143, 122, 16};
	private double alpha = 0.25;
	
	private GothicProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return GothicProcessor the unique instance of this class
	 */
	public static GothicProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(GothicProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new GothicProcessor();
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
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                r = min(255, max(0, (int)( (alpha+0.1) * gothic[0] + (1-alpha-0.1) * r) ) );
                g = min(255, max(0, (int)( (alpha+0.1) * gothic[1] + (1-alpha-0.1) * g) ) );
                b = min(255, max(0, (int)(alpha * gothic[2] + (1-alpha) * b) ) );
				newPixel = (a << 24) | (r << 16) | (g << 8) | b;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}
	
	
	/**
	 * return the smaller one of two integer
	 * @param a the first integer
	 * @param b the second integer
	 * @return the smaller one
	 */
	private int min(int a, int b) 
	{
		return a<b?a:b;
	}
	
	/**
	 * return the bigger one of two integer
	 * @param a the first integer
	 * @param b the second integer
	 */
	private int max(int a, int b)
	{
		return a>b?a:b;
	}
	

}
