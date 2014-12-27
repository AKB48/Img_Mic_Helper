package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into a sketch style.
 * @author William
 *
 */
public class SketchProcessor implements Processor {

	
	private volatile static SketchProcessor uniqueInstance = null;
	private int operator_size = 5;
	private int threshold = 7;
	private int front_color = 0xff000000;
	private int background_color = 0xffffffff;
	
	private SketchProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return SketchProcessor the unique instance of this class
	 */
	public static SketchProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(SketchProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new SketchProcessor();
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
		int[] tempPixels = new int[width*height];
		int r, g, b;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++) 
			{
				int oldPixel = bitmap.getPixel(j, i); 
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                
                tempPixels[i * width + j] = (int)((r*0.3)+(b*0.59)+(g*0.11));

			}
		}
		
		int half_operator_size = operator_size / 2;
		int average_pixel = 0;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++) 
			{
				average_pixel = 0;

				for (int k1 = i - half_operator_size; k1 <= i + half_operator_size; k1++)
				{
					if ( k1 < 0 || k1 >= height)
					{
						continue;
					}
					for (int k2 = j - half_operator_size; k2 <= j + half_operator_size; k2++)
					{
						if (k2 < 0 || k2 >= width)
						{
							continue;
						}
						if (k1 == i && k2 == j)
						{
							continue;
						}
						average_pixel += tempPixels[k1 * width + k2];
					}
				}
				
				average_pixel /= (operator_size*operator_size - 1);
				
				if (Math.abs(tempPixels[i * width + j] - average_pixel) > threshold)
				{
					tempBitmap.setPixel(j, i, front_color);
				}
				else {
					tempBitmap.setPixel(j, i, background_color);
				}
				
			}
		}
		
		return tempBitmap;
	}

}
