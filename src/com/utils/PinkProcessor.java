package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 *  This is a singleton class.
 *  It is used to transform an image into a pink style image.
 *  The pink style color is (215, 126, 172).
 * @author Willam
 *
 */
public class PinkProcessor implements Processor {

	private volatile static PinkProcessor uniqueInstance = null;
	private int[] pink = {215, 126, 172};
	private double alpha = 0.25;
	
	private PinkProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return PinkProcessor the unique instance of this class
	 */
	public static PinkProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(PinkProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new PinkProcessor();
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
				a = (oldPixel & 0xff000000) >> 24;
				r = (oldPixel & 0x00ff0000) >>16;
                g = (oldPixel & 0x0000ff00) >> 8;
                b = oldPixel & 0x000000ff;
                r = min(255, max(0, (int)((alpha+0.1) * pink[0] + (1-alpha-0.1) * r) ) );
                g = min(255, max(0, (int)(alpha * pink[1] + (1-alpha) * g) ) );
                b = min(255, max(0, (int)(alpha * pink[2] + (1-alpha) * b) ) );
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
