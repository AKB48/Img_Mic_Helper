package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * The processor transform an image into a mosaic style.
 * The operator looks like this:
 * -----------------
 * |  1/9 | 1/9 | 1/9 |
 * -----------------
 * |  1/9 | 1/9 | 1/9 |
 * -----------------
 * |  1/9 | 1/9 | 1/9 |
 * -----------------
 * @author William
 *
 */
public class MosaicProcessor implements Processor {

	
	private volatile static MosaicProcessor uniqueInstance = null;
	private int mosaic_size = 7;
	
	private MosaicProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return MosaicProcessor the unique instance of this class
	 */
	public static MosaicProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(MosaicProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new MosaicProcessor();
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
		int margin = mosaic_size / 2;
		int operator_area = mosaic_size * mosaic_size;
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int i = margin; i < height-margin; i+=mosaic_size) 
		{
			for (int j = margin; j < width-margin; j+=mosaic_size)
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
				
				// assign the new pixel to all the pixels inside the operator
				for (int k1 = i-margin; k1 <= i+margin; k1++) 
				{
					for (int k2 = j-margin; k2 <= j+margin; k2++) 
					{
						tempBitmap.setPixel(k2, k1, newPixel);		
					}
				}
				
			}
		}
		return tempBitmap;
		
	}

}
