package com.utils.processor;


import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * This processor transform an image into a nostalgia/sepia style.
 * This algorithm is that:
 * r= max(0, min(255, 0.393*r + 0.769*g + 0.189*b));
 * g= max(0, min(255, 0.349*r + 0.686*g + 0.168*b));
 * b= max(0, min(255, 0.272*r + 0.534*g + 0.131*b));
 * @author William
 *
 */
public class NostalgiaProcessor implements Processor {

	private volatile static NostalgiaProcessor uniqueInstance = null;
	
	private NostalgiaProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return NostalgiaProcessor the unique instance of this class
	 */
	public static NostalgiaProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(NostalgiaProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new NostalgiaProcessor();
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
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
                g = (oldPixel >> 8) & 0xff;
                b = oldPixel & 0xff;
                r= max(0, min(255, (int)(0.393*r + 0.769*g + 0.189*b) ) );
                g= max(0, min(255, (int)(0.349*r + 0.686*g + 0.168*b) ) );
                b= max(0, min(255, (int)(0.272*r + 0.534*g + 0.131*b) ) );
				newPixel = (a << 24) | (r << 16) | (g << 8) | b;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		return tempBitmap;
	}
	
	/**
	 * return the smaller one of two integer
	 * @param a the first integer
	 * @param b the second integer
	 * @return the smaller one
	 */
	private int min(int a, int b) 
	{
		return a<b?a:b;
	}
	
	
	/**
	 * return the bigger one of two integer
	 * @param a the first integer
	 * @param b the second integer
	 */
	private int max(int a, int b)
	{
		return a>b?a:b;
	}

}
