package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 *  This is a singleton class.
 *  It is used to transform an image into a cold style image.
 *  The cold style color is (0, 50, 50).
 * @author William
 *
 */
public class ColdProcessor implements Processor {

	
	private volatile static ColdProcessor uniqueInstance = null;
	private int[] cold = {0, 50, 50};
	
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
		
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				int oldPixel = bitmap.getPixel(j, i); 
				a = oldPixel & 0xff000000;
				r = ((oldPixel >> 16) & 0xff)  + cold[0];
                g = ((oldPixel >> 8) & 0xff)  + cold[1];
                b = (oldPixel & 0xff) + cold[2];
                
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
