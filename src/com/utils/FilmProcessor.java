package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class
 * The processor transform an image into film post style
 * If pixel >= 128, new pixel = 192, 64 otherwise.
 * @author Willam
 *
 */
public class FilmProcessor implements Processor {

	
	private volatile static FilmProcessor uniqueInstance = null;
	
	private FilmProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return FilmProcessor the unique instance of this class
	 */
	public static FilmProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(FilmProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new FilmProcessor();
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
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int oldPixel = bitmap.getPixel(j, i); 
				a = oldPixel & 0xff000000;
				r = (oldPixel >> 16) & 0xff;
				r = ( (r >= 128 ? 192 : 64) << 16 );
                g = (oldPixel >>8) & 0xff;
                g = ( (g >= 128 ? 192 : 64) << 8 );
                b = oldPixel & 0xff;
                b = (b >= 128 ? 192 : 64);
                // blend each channel
				newPixel = a | r | g | b;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}

}
