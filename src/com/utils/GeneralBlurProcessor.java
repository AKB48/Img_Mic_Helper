package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * The processor transforms an image into a blur style.
 * The blur operator is a mean filter.
 * The operator looks like this:
 *             -----------------
 *             |   1   |   1   |   1   |
 *             -----------------
 *  1/9 *    |   1   |   1   |   1   |
 *             -----------------
 *             |   1   |   1   |   1   |
 *             -----------------
 * @author William
 *
 */
public class GeneralBlurProcessor implements Processor {

	
	private volatile static GeneralBlurProcessor uniqueInstance = null;
	private int raduis = 3;
	
	private GeneralBlurProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return GeneralBlurProcessor the unique instance of this class
	 */
	public static GeneralBlurProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(GeneralBlurProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new GeneralBlurProcessor();
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
		int newPixel = 0;
		int[][] rgb = new int[width*height][4];
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int oldPixel = bitmap.getPixel(j, i);
				int index = i*width+j;
				rgb[index][0] = oldPixel & 0xff000000;
				rgb[index][1] = (oldPixel >> 16) & 0xff;
				rgb[index][2] = (oldPixel >> 8) & 0xff;
				rgb[index][3] = oldPixel & 0xff;
				
			}
		}
		
		int r_ave = 0, g_ave = 0, b_ave = 0;
		int operator_size = (2*raduis+1)*(2*raduis+1);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int index = i*width+j;
				r_ave = 0;
				g_ave = 0;
				b_ave = 0;
			
				for (int k1 = -raduis; k1 <= raduis; k1++)
				{
					int new_i = i + k1;
					if (new_i < 0)
					{
						new_i = 0;
					}
					if (new_i >= height)
					{
						new_i = height-1;
					}
					for (int k2 = -raduis; k2 <= raduis; k2++)
					{
						int new_j = j + k2;
						if (new_j < 0)
						{
							new_j = 0;
						}
						if (new_j >= width)
						{
							new_j = width-1;
						}
						
						int new_index = new_i*width+new_j;
												
						r_ave += rgb[new_index][1];
						g_ave += rgb[new_index][2];
						b_ave += rgb[new_index][3];
						
					}
				}
				
				
				r_ave /= operator_size;
				g_ave /= operator_size;
				b_ave /= operator_size;
				newPixel = rgb[index][0] | (r_ave << 16) | (g_ave << 8) | b_ave;
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}
		
		return tempBitmap;
	}
	

}
