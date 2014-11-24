package com.utils;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into a paper cut style.
 * @author William
 *
 */
public class PaperCutProcessor implements Processor {

	
	private volatile static PaperCutProcessor uniqueInstance = null;
	private int threshold = 140;
	private int front_color = 0xffff0000;
	private int background_color = 0xffffffff;
	
	private PaperCutProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return PaperCutProcessor the unique instance of this class
	 */
	public static PaperCutProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(PaperCutProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new PaperCutProcessor();
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
		int tempPixel = 0, r, g, b;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int oldPixel = bitmap.getPixel(j, i); 
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                
                tempPixel = max(r, g, b);
                
                if (tempPixel < threshold)
                {
                	tempBitmap.setPixel(j, i, front_color);
                }
                else 
                {
                	tempBitmap.setPixel(j, i, background_color);
                }
                
			}
		}
		return tempBitmap;
	}
	
	
	/**
	 * This method finds the largest one of three integers.
	 * @param r the first integer, represents r channel
	 * @param g the second integer, represents g channel
	 * @param b the third integer, represents b channel
	 * @return the largest one integer
	 */
	private int max(int r, int g, int b)
	{
		int max_int = r;
		
		if (max_int < g)
		{
			max_int = g;
		}
		
		if (max_int < b)
		{
			max_int = b;
		}
		
		return max_int;
	}

}
