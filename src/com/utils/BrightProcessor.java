package com.utils;

import android.graphics.Bitmap;


/**
 * This is a singleton class.
 * The processor transforms an image into a brighter style.
 * @author William
 *
 */
public class BrightProcessor implements Processor {

	
	private volatile static BrightProcessor uniqueInstance = null;
	
	
	private BrightProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return BrightProcessor the unique instance of this class
	 */
	public static BrightProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(BrightProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new BrightProcessor();
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
		int oldPixel = 0;
		int newPixel = 0;
		int old_r = 0, old_g = 0, old_b = 0;
		int new_r = 0, new_g = 0, new_b = 0;
		int alpha = 0xff000000;
	
		Bitmap tempBitmap = GaussianBlurProcessor.getInstance().process(bitmap);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				oldPixel = bitmap.getPixel(j, i);
				newPixel = tempBitmap.getPixel(j, i);
				
				alpha = oldPixel & 0xff000000;
				old_r = (oldPixel >> 16) & 0xff;
				new_r = (newPixel >>16) & 0xff;
				old_g = (oldPixel >> 8) & 0xff;
				new_g = (newPixel >> 8) & 0xff;
				old_b = oldPixel & 0xff;
				new_b = newPixel & 0xff;
				
				
				// mixture two images.
				// the processed image will become brighter.
				new_r = 255 - (255 - old_r)*(255 - new_r)/255 ;
				new_g = 255 - (255 - old_g)*(255 - new_g)/255 ;
				new_b = 255 - (255 - old_b)*(255 - new_b)/255 ;
				
				newPixel = alpha | (new_r << 16) | (new_g << 8) | new_b;
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}

		
		return  tempBitmap;
		
	}

}
