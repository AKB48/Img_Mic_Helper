package com.utils;

import android.graphics.Bitmap;


public class ContrastProcessor implements Processor {

	
	private volatile static ContrastProcessor uniqueInstance = null;
	private int multiple = 2;
	
	private ContrastProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ContrastProcessor the unique instance of this class
	 */
	public static ContrastProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(ContrastProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new ContrastProcessor();
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
		int amount = 3;
		
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
				new_r = (r - new_r) * amount + new_r;
				new_r = (new_r > 255 ? 255 : (new_r < 0 ? 0 : new_r));
				new_g = (g - new_g) * amount + new_g;
				new_g = (new_g > 255 ? 255 : (new_g < 0 ? 0 : new_g));
				new_b = (b - new_b) * amount + new_b;
				new_b = (new_b > 255 ? 255 : (new_b < 0 ? 0 : new_b));
				newPixel = 0xff000000 | (new_r << 16) | (new_g << 8) | new_b;
				
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		
		return tempBitmap;
	}

}
