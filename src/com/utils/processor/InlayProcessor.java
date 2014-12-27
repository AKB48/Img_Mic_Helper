package com.utils.processor;

import java.util.Set;


import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * The processor transform an image into a inlay style
 * @author William
 *
 */
public class InlayProcessor implements Processor {

	
	private volatile static InlayProcessor uniqueInstance = null;
	private int operator_size = 6;
	
	private InlayProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return InlayProcessor the unique instance of this class
	 */
	public static InlayProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(InlayProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new InlayProcessor();
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
				if (j % operator_size == 0)
				{
					current_pixel = bitmap.getPixel(j, i); 
				}
				
				tempBitmap.setPixel(j, i, current_pixel);

				}
			}
		
		return tempBitmap;
	}

}
