package com.utils.processor;


import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into the style that looks like having many ceramic tiles. 	
 * @author William
 *
 */
public class CeramicProcessor implements Processor {

	
	private volatile static CeramicProcessor uniqueInstance = null;
	private int operator_size = 31;
	private int[] boundColor = {0xff494949, 0xff656565, 0xff7d7d7d, 0xffa1a1a1};
	
	private CeramicProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return CeramicProcessor the unique instance of this class
	 */
	public static CeramicProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(CeramicProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new CeramicProcessor();
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
		int newPixel = 0, a, r, g, b;
		int margin = operator_size / 2;
		int operator_area = operator_size * operator_size;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = margin; i < height-margin; i+=operator_size) 
		{
			for (int j = margin; j < width-margin; j+=operator_size)
			{
				// initialize four channels
				a = 0xff000000;
				r = 0;
				g = 0;
				b = 0;
				
				for (int k1 = i-margin; k1 <= i+margin; k1++) 
				{
					for (int k2 = j-margin; k2 <= j+margin; k2++) 
					{
						// calculate the sum of all pixels inside the operator
						int oldPixel = bitmap.getPixel(k2, k1); 
						a = (oldPixel >> 24) & 0xff;
						r += (oldPixel >> 16) & 0xff;
		                g += (oldPixel >> 8) & 0xff;
		                b += oldPixel & 0xff;
						
					}
				}
				
				// calculate the average of r, g, b channels.
				r /= operator_area;
				g /= operator_area;
				b /= operator_area;
				
				// the average becomes the value of new pixel
				newPixel = (a << 24) | (r << 16) | (g << 8) | b;
				
				// decide the current grid should display dark or bright
				// -1 represents dark and 1 represents bright
				int random_num = Math.random() < 0.5 ? -1 : 1;
				int[] incre_pixel = new int[3];
				
				// the gradual change of pixel near the bound
				for (int k = 0; k < 3; k++)
				{
					r += random_num;
					if (r < 0)
					{
						r = 0;
					}
					if ( r > 255)
					{
						r = 255;
					}
					
					g += random_num;
					if (g < 0)
					{
						g = 0;
					}
					if ( g > 255)
					{
						g = 255;
					}
					
					b += random_num;
					if (b < 0)
					{
						b = 0;
					}
					if ( b > 255)
					{
						b = 255;
					}
					incre_pixel[k] = (a << 24) | (r << 16) | (g << 8) | b;
				}
				
				// assign the new pixel to all the pixels inside the operator
				for (int k1 = i-margin; k1 <= i+margin; k1++) 
				{
					for (int k2 = j-margin; k2 <= j+margin; k2++) 
					{
						// the outer bound pixel should be very dark or very bright
						if (k1 >= i+margin-1 || k2 >= j+margin-1) 
						{
							if (random_num == -1)
							{
								tempBitmap.setPixel(k2, k1, boundColor[0]);
							}
							else {
								tempBitmap.setPixel(k2, k1, boundColor[3]);
							}
						}
						// the gradual change pixels
						else if (k1 == i+margin-2 || k2 == j+margin-2) 
						{
							tempBitmap.setPixel(k2, k1, incre_pixel[2]);						
						}
						else if (k1 == i+margin-3 || k2 == j+margin-3) 
						{
							tempBitmap.setPixel(k2, k1, incre_pixel[1]);						
						}		
						else if (k1 == i+margin-4 || k2 == j+margin-4) 
						{
							tempBitmap.setPixel(k2, k1, incre_pixel[0]);						
						}									
						else 
						{
							tempBitmap.setPixel(k2, k1, newPixel);
						}
								
					}
				}
				
			}
		}
		
		return tempBitmap;
	}

}
