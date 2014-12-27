package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * This processor transform an image into a black white two colors style.
 * If the pixel is bigger than 128, it would be set to 255, 
 * otherwise 0.
 * @author William
 *
 */
public class BlackWhiteProcessor implements Processor {

	
	private volatile static BlackWhiteProcessor uniqueInstance = null;
	private double threshold = 128.0f;
	
	private BlackWhiteProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return BlackWhiteProcessor the unique instance of this class
	 */
	public static BlackWhiteProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(BlackWhiteProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new BlackWhiteProcessor();
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
		int image_size = width*height;
		int r, g, b;
		int[][] rgb = new int[image_size][2];
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		threshold = 0.0f;
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				int index = i*width+j;
				int oldPixel = bitmap.getPixel(j, i); 
				rgb[index][0] = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                rgb[index][1] = (int)( (r*0.3)+(b*0.59)+(g*0.11) );
                threshold += rgb[index][1] * 1.0 / image_size;

			}
		}
		
		
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				int index = i*width+j;				
                newPixel = pixelChoose(rgb[index][1]);
                               
                newPixel = (rgb[index][0] << 24) | (newPixel << 16) | (newPixel << 8) | newPixel;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		
		return tempBitmap;
	}
	
	
	private int pixelChoose(int oldPixel)
	{
		return oldPixel < threshold ? 0 : 255;
	}

}
