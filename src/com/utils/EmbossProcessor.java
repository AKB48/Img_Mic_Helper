package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into an emboss style.
 * @author William
 *
 */
public class EmbossProcessor implements Processor {

	
	private volatile static EmbossProcessor uniqueInstance = null;
	private int increment = 128;
	
	private EmbossProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return EmbossProcessor the unique instance of this class
	 */
	public static EmbossProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(EmbossProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new EmbossProcessor();
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
		int a, r, g, b;
		int[] prePixel = {0, 0, 0}, preprePixel = {0, 0, 0}, currentPixel = {0, 0, 0};
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int j = 0; j < width; j++) {
			for (int i = 0; i < height; i++) {
				int oldPixel = bitmap.getPixel(j, i); 
				a = oldPixel & 0xff000000;
				currentPixel[0] = (oldPixel >> 16) & 0xff;
                currentPixel[1] = (oldPixel >> 8) & 0xff;
                currentPixel[2] = oldPixel & 0xff;
                
                r = currentPixel[0] - preprePixel[0] + increment;
                r = (r > 255 ? 255 : r);
                g = currentPixel[1] - preprePixel[0] + increment;
                g = (g > 255 ? 255 : g);
                b = currentPixel[1] - preprePixel[2] + increment;
                b = (b > 255 ? 255 : b);
                
                newPixel = (int)((r*0.3)+(b*0.59)+(g*0.11));
				newPixel = a | (newPixel << 16) | (newPixel << 8) | newPixel;
				tempBitmap.setPixel(j, i, newPixel);
				
				for (int k = 0; k < 3; k++)
				{
					preprePixel[k] = prePixel[k];
					prePixel[k] = currentPixel[k];
				}
			}
		}
		return tempBitmap;
	}

}
