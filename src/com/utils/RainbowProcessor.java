package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * The processor transform an image into a rainbow style.
 * @author William
 *
 */
public class RainbowProcessor implements Processor {

	private volatile static RainbowProcessor uniqueInstance = null;
	
	private RainbowProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return RainbowProcessor the unique instance of this class
	 */
	public static RainbowProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(RainbowProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new RainbowProcessor();
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
		int oldPixel = 0;
		int newPixel = 0, a, r, g, b;
		int horizontal_gradient = 0, vertical_gradient = 0;
		int right_next_pixel = 0, down_next_pixel = 0;
		int temp_component = 0;
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (right_next_pixel != 0)
				{
					oldPixel = bitmap.getPixel(j, i);
				}
				if (j+1 < width)
				{
					right_next_pixel = bitmap.getPixel(j+1, i);
				}
				else 
				{
					right_next_pixel = 0;
				}
				if (i+1 < height)
				{
					down_next_pixel = bitmap.getPixel(j, i+1);
				}
				else
				{
					down_next_pixel = 0;
				}
				a = oldPixel & 0xff000000;
				r = (oldPixel >> 16) & 0xff;
				horizontal_gradient = r - ( (right_next_pixel >> 16) & 0xff );
				vertical_gradient = r - ( (down_next_pixel >> 16) & 0xff );
				temp_component = (int)(2 * Math.sqrt(horizontal_gradient*horizontal_gradient*1.0+
						vertical_gradient*vertical_gradient*1.0));
				r = ( (temp_component > 255 ? 255 : temp_component) << 16);

                g = (oldPixel >>8) & 0xff;
                horizontal_gradient = g - ( (right_next_pixel >> 8) & 0xff );
                vertical_gradient = g - ( (down_next_pixel >> 8) & 0xff );
                temp_component = (int)(2 * Math.sqrt(horizontal_gradient*horizontal_gradient*1.0+
						vertical_gradient*vertical_gradient*1.0));
                g = ( (temp_component > 255 ? 255 : temp_component) << 8 );
                
                b = oldPixel & 0xff;
                horizontal_gradient = b- (right_next_pixel & 0xff);
                vertical_gradient = b - (down_next_pixel & 0xff);
				temp_component = (int)(2 * Math.sqrt(horizontal_gradient*horizontal_gradient*1.0+
						vertical_gradient*vertical_gradient*1.0));
                b = (temp_component > 255 ? 255 : temp_component);
                
                // blend each channel
				newPixel = a | r | g | b;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}
	

}
