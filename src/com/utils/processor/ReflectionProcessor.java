package com.utils.processor;


import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;


/**
 * This is a singleton class.
 * The processor transforms an image into a reflection style.
 * @author William
 *
 */
public class ReflectionProcessor implements Processor {

	
	private volatile static ReflectionProcessor uniqueInstance = null;
	private int max_intensity = 9;
	
	private ReflectionProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ReflectionProcessor the unique instance of this class
	 */
	public static ReflectionProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(ReflectionProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new ReflectionProcessor();
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
		int half_height = (int) (height * 0.5);
		int newPixel = 0;
		
		Bitmap tempBitmap = scaleBitmap(bitmap, width, half_height);
		height = half_height * 2;
		Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		int half_max_intensity = max_intensity / 2;
		int shift_intensity = 0;
		
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				if (i < half_height)
				{
					destBitmap.setPixel(j, i, tempBitmap.getPixel(j, i));
				}
				else 
				{
					shift_intensity = (int)(Math.random()*max_intensity) - half_max_intensity;
					
					int new_i = height - 1 - i + shift_intensity;
					if (new_i < 0)
					{
						new_i = 0;
					}
					if (new_i >= half_height)
					{
						new_i = half_height - 1;
					}
					
					int new_j = j + shift_intensity;
					if (new_j < 0)
					{
						new_j = 0;
					}
					if (new_j >= width)
					{
						new_j = width - 1;
					}
					
					destBitmap.setPixel(j, i, tempBitmap.getPixel(new_j, new_i));
				}
				
			}
		}
		
		tempBitmap.recycle();
		return destBitmap;
	}
	
	
	/**
	 * Scale a bitmap to a specific size.
	 * @param source the original bitmap to be scaled.
	 * @param new_width the width of new bitmap.
	 * @param new_height the height of new bitmap.
	 * @return the bitmap that after scaling.
	 */
	private Bitmap scaleBitmap(Bitmap source, int new_width, int new_height)
	{	
		int width = source.getWidth();
		int height = source.getHeight();
		
		float width_ratio = ((float)new_width) / width;
		float height_ratio = ((float)new_height) / height;

	    Matrix matrix = new Matrix();
	    matrix.postScale(width_ratio, height_ratio);
	        
	    Bitmap dest = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
	    
	    return dest;
	
	}

}
