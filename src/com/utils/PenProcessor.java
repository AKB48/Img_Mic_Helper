package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processors transforms an image into the style that looks like being drawn by pen.
 * The main idea is that f(x) = 255 - (max-min)*4.
 * The pen color is 38.
 * @author William
 *
 */
public class PenProcessor implements Processor {

	
	private volatile static PenProcessor uniqueInstance = null;
	private int radius = 1;
	
	private PenProcessor()
	{
		
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return PenProcessor the unique instance of this class
	 */
	public static PenProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(PenProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new PenProcessor();
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
		
		int newPixel = 0;
		int rgb[][] = new int[width*height][5];
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		int index;
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				int oldPixel = bitmap.getPixel(j, i); 
				index = i * width + j;
				rgb[index][0] = oldPixel & 0xff000000;
				rgb[index][1] = (oldPixel >> 16) & 0xff;
                rgb[index][2] = (oldPixel >> 8) & 0xff;
                rgb[index][3] = oldPixel & 0xff;
                rgb[index][4] = (int) (rgb[index][1] * 0.3 + rgb[index][2] * 0.59 + rgb[index][3] * 0.11);
			}
		}
		
		
		int new_index;
		int maxPixel = 0, minPixel = 255;
		for (int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				maxPixel = 0;
				minPixel = 255;
				index = i * width + j;
				for (int k1 = -radius; k1 <= radius; k1++)
				{
					int new_i = i + k1;
					if (new_i < 0)
					{
						new_i = 0;
					}
					if (new_i >= height)
					{
						new_i = height - 1;
					}
					for (int k2 = -radius; k2 <= radius; k2++)
					{
						int new_j = j + k2;
						if (new_j < 0)
						{
							new_j = 0;
						}
						if (new_j >= width)
						{
							new_j = width - 1;
						}
						new_index = new_i * width + new_j;
						if (maxPixel < rgb[new_index][4])
						{
							maxPixel = rgb[new_index][4];
						}
						if (minPixel > rgb[new_index][4])
						{
							minPixel = rgb[new_index][4];
						}
					}
				}
				
				newPixel = 255 - (maxPixel - minPixel) * 4;
				if (newPixel < 75)
				{
					newPixel = 38;
				}
				else 
				{
					newPixel = 255;
				}
                
				newPixel = rgb[index][0] | (newPixel << 16) | (newPixel << 8) | newPixel;
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}
		
		return tempBitmap;
	}

}
