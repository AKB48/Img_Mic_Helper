package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor makes the image looks like in mist.
 * @author William
 *
 */
public class MistProcessor implements Processor {

	
	private volatile static MistProcessor uniqueInstance = null;
	private int max_intensity = 9;
	
	
	private MistProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return MistProcessor the unique instance of this class
	 */
	public static MistProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(MistProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new MistProcessor();
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
		int half_max_intensity = max_intensity / 2;
		int shift_intensity = 0;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		int new_i = 0;
		int new_j = 0;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++) 
			{				
				shift_intensity = (int)(Math.random()*max_intensity) - half_max_intensity;
				
				new_i = i + shift_intensity;
				if (new_i < 0)
				{
					new_i = 0;
				}
				if (new_i >= height)
				{
					new_i = height - 1;
				}
				
				new_j = j + shift_intensity;
				if (new_j < 0)
				{
					new_j = 0;
				}
				if (new_j >= width)
				{
					new_j = width - 1;
				}
				
				tempBitmap.setPixel(j, i, bitmap.getPixel(new_j, new_i)); 
				
			}
		}
		
		return tempBitmap;
	}
	
}
