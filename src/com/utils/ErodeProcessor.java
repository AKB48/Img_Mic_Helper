package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * The processor transform an image into a style that looks like having been eroded.
 * @author William
 *
 */
public class ErodeProcessor implements Processor {

	
	private volatile static ErodeProcessor uniqueInstance = null;
	private int erode__width_size = 8;
	private int erode__height_size = 4;
	
	private ErodeProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ErodeProcessor the unique instance of this class
	 */
	public static ErodeProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(ErodeProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new ErodeProcessor();
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
        int current_pixel = 0;
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (i % erode__height_size == 0)
				{
					if (j % erode__width_size == 0)
					{
						current_pixel = bitmap.getPixel(j, i); 
					}
					
					tempBitmap.setPixel(j, i, current_pixel);
					
				}
				else {
					tempBitmap.setPixel(j, i, bitmap.getPixel(j, i-1));
				}
				
			}
		}
		
		return tempBitmap;
	}

}
