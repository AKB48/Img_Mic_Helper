package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processors sharpens an image.
 * Enhance the details in an image.
 * The sharpen operator is laplacian operator.
 * The operator look like this:
 * ----------------------------
 * |    0.0    |    -0.2    |    0.0    |
 * ----------------------------
 * |   -0.2    |    1.8     |   -0.2    |
 * ----------------------------
 * |    0.0    |    -0.2    |    0.0     |
 * ----------------------------
 * @author William
 *
 */
public class SharpenProcessor implements Processor {


	private volatile static SharpenProcessor uniqueInstance = null;
	private int radius = 1;
	private double laplacian[] = {-1.0, -1.0, -1.0, -1.0, 9.0, -1.0, -1.0, -1.0, -1.0};
	
	private SharpenProcessor()
	{
		
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return SharpenProcessor the unique instance of this class
	 */
	public static SharpenProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(SharpenProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new SharpenProcessor();
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
		double r, g, b;
		int rgb[][] = new int[width*height][4];
		
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
			}
		}
		
		
		int new_index;
		int laplacian_index;
		for (int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				r = 0.0;
				g = 0.0;
				b = 0.0;
				index = i * width + j;
				laplacian_index = 0;
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
						r += laplacian[laplacian_index] * rgb[new_index][1];
						g += laplacian[laplacian_index] * rgb[new_index][2];
						b += laplacian[laplacian_index] * rgb[new_index][3];
						laplacian_index++;
					}
				}
				
//				r += rgb[index][1];
				r = (r > 255 ? 255 : (r < 0 ? 0 : r));
//				g += rgb[index][2];
				g = (g > 255 ? 255 : (g < 0 ? 0 : g));
//				b += rgb[index][3];
				b = (b > 255 ? 255 : (b < 0 ? 0 : b));
                
				newPixel = rgb[index][0] | ((int)r << 16) | ((int)g << 8) | (int)b;
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}
		
		return tempBitmap;
	}

}
