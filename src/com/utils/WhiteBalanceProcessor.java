package com.utils;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor performs an auto white balance onto the original image.
 * The method of auto white balance is gray world.
 * @author William
 *
 */
public class WhiteBalanceProcessor implements Processor {

	
	private volatile static WhiteBalanceProcessor uniqueInstance = null;
	
	
	private WhiteBalanceProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return WhiteBalanceProcessor the unique instance of this class
	 */
	public static WhiteBalanceProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(WhiteBalanceProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new WhiteBalanceProcessor();
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
		
		double r_ave = 0;
		double g_ave = 0;
		double b_ave = 0;
		double image_size = width * height * 1.0;
		
		int[][] rgb = new int[(int) image_size][4];
		int index = 0;
		
		
		for (int i = 0; i < height; i++)
		{
			
			for (int j = 0; j < width; j++)
			{
				
				int oldPixel = bitmap.getPixel(j, i);
				index = i * width + j;
				
				rgb[index][0] = (oldPixel >> 24) & 0xff;
				rgb[index][1] = (oldPixel >> 16) & 0xff;
				rgb[index][2] = (oldPixel >> 8) & 0xff;
				rgb[index][3] = oldPixel & 0xff;
				
				r_ave += ( rgb[index][1] / image_size);
				g_ave += ( rgb[index][2] / image_size);
				b_ave += ( rgb[index][3] / image_size);
				
			}
		}
		
		
		double K = (r_ave + g_ave + b_ave) / 3;
		double K_r = K / r_ave;
		double K_g = K / g_ave;
		double K_b = K / b_ave;
		
		int r;
		int g;
		int b;
		int newPixel;
		
		Bitmap tempbBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		
		for (int i = 0; i < height; i++)
		{
			
			for (int j = 0; j < width; j++)
			{
				
				index = i * width + j;
				
				r = (int) (rgb[index][1] * K_r);
				if (r > 255)
				{
					r = 255;
				}
				else if (r < 0)
				{
					r = 0;
				}
				
				g = (int) (rgb[index][2] * K_g);
				if (g > 255)
				{
					g = 255;
				}
				else if (g < 0)
				{
					g = 0;
				}
				
				b = (int) (rgb[index][3] * K_b);
				if (b > 255)
				{
					b = 255;
				}
				else if (b < 0)
				{
					b = 0;
				}
				
				newPixel = (rgb[index][0] << 24) | (r << 16) | (g << 8) | b;
				tempbBitmap.setPixel(j, i, newPixel);
				
			}
		}
		
		
		return tempbBitmap;
		
	}
	

}
