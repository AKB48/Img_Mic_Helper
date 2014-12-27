package com.utils.processor;


import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into a cross processing style.
 * @author William
 *
 */
public class CrossProcessingProcessor implements Processor {

	
	private volatile static CrossProcessingProcessor uniqueInstance = null;
	
	private CrossProcessingProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return CrossProcessingProcessor the unique instance of this class
	 */
	public static CrossProcessingProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(CrossProcessingProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new CrossProcessingProcessor();
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
		
		int rgb[][] = new int[256][3];
		double color_level = 0;
		double _r, _g;
		for (int k = 0; k < 256; k++)
		{
			color_level = (k < 128 ? k : 256 - k);
			_r = Math.pow(color_level, 3) / 64.0 / 256.0;
			rgb[k][0] = (int) (k < 128 ? _r : 256 - _r);
			_g = Math.pow(color_level, 2) / 128.0;
			rgb[k][1] = (int) (k < 128 ? _g : 256 - _g);
			rgb[k][2] = k / 2 + 0x25;
			
		}
		
		int newPixel = 0;
		int a, r, g, b;
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				int oldPixel = bitmap.getPixel(j, i); 
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;

				newPixel = (a << 24) | (rgb[r][0] << 16) | (rgb[g][1] << 8) | rgb[b][2];
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		
		return tempBitmap;
	}

}
