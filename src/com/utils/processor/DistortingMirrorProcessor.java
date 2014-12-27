package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into the distorting mirror style.
 * The processed image looks like watching through a distorting mirror around its center.
 * @author William
 *
 */
public class DistortingMirrorProcessor implements Processor {

	
	private volatile static DistortingMirrorProcessor uniqueInstance = null;
	private int multiple = 2;
	
	private DistortingMirrorProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return DistortingMirrorProcessor the unique instance of this class
	 */
	public static DistortingMirrorProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(DistortingMirrorProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new DistortingMirrorProcessor();
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
			radius = (int) (height * 0.4);
		}
		else
		{
			radius = (int) (width * 0.4);
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
					
					int new_i = (int) ((double)(i - center_height) / multiple);
					int new_j = (int) ((double)(j - center_width) / multiple);
					new_i = (int)(new_i * (Math.sqrt(distance) / (radius / multiple) ) );
					new_j = (int)(new_j * (Math.sqrt(distance) / (radius / multiple) ) );
					new_i = new_i + center_height;
					new_j = new_j + center_width;
					
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
