package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into the style that having four dark corners.
 * @author William
 *
 */
public class DarkCornerProcessor implements Processor {

	
	private volatile static DarkCornerProcessor uniqueInstance = null;
	private int ratio = 0;
	
	private DarkCornerProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return DarkCornerProcessor the unique instance of this class
	 */
	public static DarkCornerProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(DarkCornerProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new DarkCornerProcessor();
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
		
		double center_width = 0.5f * width;
		double center_height = 0.5f * height;
		int newPixel = 0;
		int a, r, g, b;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		ratio = width >  height ?  height * 32768 / width : width * 32768 /  height;
        int max_dist = (int) (center_width * center_width + center_height * center_height);
        int min_dist = (int)(max_dist * 0.3);
        int difference = max_dist - min_dist;
		
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				int oldPixel = bitmap.getPixel(j, i); 
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                
                
                int dx = (int) (center_width - j);
                int dy = (int) (center_height - i);
                
                if (width > height)
                {
                   dx = (int)(dx * ratio) >> 15;
                }
                else
                {
                   dy = (int)(dy * ratio) >> 15;
                }
                
                int dist_square = dx * dx + dy * dy;

                if (dist_square > min_dist)
                {
                   // calculate dark level
                   int dark_level = ((max_dist - dist_square) << 8) / difference;
                   dark_level *= dark_level;

                   // apply dark level to each channels.
                   r = (r * dark_level) >> 16;
                   g = (g * dark_level) >> 16;
                   b = (b * dark_level) >> 16;

                   // normalize to [0, 255]
                   r = (int)(r > 255 ? 255 : (r < 0 ? 0 : r));
                   g = (int)(g > 255 ? 255 : (g < 0 ? 0 : g));
                   b = (int)(b > 255 ? 255 : (b < 0 ? 0 : b));
                }
                
				newPixel = (a << 24) | (r << 16) | (g << 8) | b;
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}
		
		return tempBitmap;
	}

}
