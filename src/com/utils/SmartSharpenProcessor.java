package com.utils;

import android.graphics.Bitmap;


/**
 * This is a singleton class.
 * The processor sharpens an image in a smart way.
 * The enhance image = the blur image + (original image - gaussian blur image) * degree.
 * @author William
 *
 */
public class SmartSharpenProcessor implements Processor {

	
	private volatile static SmartSharpenProcessor uniqueInstance = null;
	private int degree = 3;
	
	private SmartSharpenProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return SmartSharpenProcessor the unique instance of this class
	 */
	public static SmartSharpenProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(SmartSharpenProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new SmartSharpenProcessor();
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
		int r,g,b;
		int new_r, new_g, new_b;
		int oldPixel = 0, newPixel = 0;
		
		Bitmap tempBitmap = GaussianBlurProcessor.getInstance().process(bitmap);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				oldPixel = bitmap.getPixel(j, i);
				newPixel = tempBitmap.getPixel(j, i);
				
				r = (oldPixel >> 16) & 0xff;
				new_r = (newPixel >> 16) & 0xff;
				g = (oldPixel >> 8) & 0xff;
				new_g = (newPixel >> 8) & 0xff;
				b = oldPixel & 0xff;
				new_b = newPixel & 0xff;
				
				new_r = (r - new_r) * degree + new_r;
				new_r = min(255, max(new_r, 0));
				
				new_g = (g - new_g) * degree + new_g;			
				new_g = min(255, max(new_g, 0));
				
				new_b = (b - new_b) * degree + new_b;		
				new_b = min(255, max(new_b, 0));
				
				newPixel = 0xff000000 | (new_r << 16) | (new_g << 8) | new_b;
				
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
