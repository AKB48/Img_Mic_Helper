package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor magnify the source image in its center circle area.
 * @author William
 *
 */
public class MagnifierProcessor implements Processor {

	
	private volatile static MagnifierProcessor uniqueInstance = null;
	private int multiple = 2;
	
	private MagnifierProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return MagnifierProcessor the unique instance of this class
	 */
	public static MagnifierProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(MagnifierProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new MagnifierProcessor();
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
		int center_width = width / 2;
		int center_height = height / 2;
		int radius = 0;
		
		if (width > height)
		{
			radius = (int) (height * 0.3);
		}
		else
		{
			radius = (int) (width * 0.3);
		}
		
		int radius2 = radius * radius;
		int distance = 0;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				
				distance = (center_height-i) * (center_height-i) + (center_width-j) * (center_width-j);
				
				if (distance < radius2)
				{
					int new_i = (int)((double)(i - center_height) / multiple + center_height);
					int new_j = (int)((double)(j - center_width) / multiple + center_width);
					
					tempBitmap.setPixel(j, i, bitmap.getPixel(new_j, new_i));
					
				}
				else 
				{
					tempBitmap.setPixel(j, i, bitmap.getPixel(j, i));
					
				}
				
			}
		}
		
		return tempBitmap;
	}

}
