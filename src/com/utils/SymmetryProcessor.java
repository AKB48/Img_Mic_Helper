package com.utils;

import android.R.layout;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into the style that the image is symmetry.
 * The symmetry part is the left half part of an image.
 * @author William
 *
 */
public class SymmetryProcessor implements Processor {

	
	private volatile static SymmetryProcessor uniqueInstance = null;
	
	private SymmetryProcessor()
	{
		
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return SymmetryProcessor the unique instance of this class
	 */
	public static SymmetryProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(SymmetryProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new SymmetryProcessor();
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
		int half_width = width / 2;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (j < half_width)
				{
					tempBitmap.setPixel(j, i, bitmap.getPixel(j, i));
				}
				else 
				{
					tempBitmap.setPixel(j, i, bitmap.getPixel(width-1-j, i));
				}
			}
		}
		
		return tempBitmap;
		
	}

}
