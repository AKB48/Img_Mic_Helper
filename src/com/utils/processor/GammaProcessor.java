package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor applies gamma correction on the original image.
 * The factor gamma controls the result of the processor.
 * The default result is to make the original image looks darker.
 * @author William
 *
 */
public class GammaProcessor implements Processor {

	
	private volatile static GammaProcessor uniqueInstance = null;
	private double gamma = 0.5d;
	private int[] palette = null;
    
	
	private GammaProcessor()
	{
		
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return GammaProcessor the unique instance of this class
	 */
	public static GammaProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(GammaProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new GammaProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Set the parameter gamma, which will change the brightness of an image.
	 * If gamma is less than 1, it makes the image darker.
	 * If gamma is greater than 1, it makes the image brighter.
	 * @param gamma the param to set.
	 * @return true if the param is successfully set.
	 *            false if the param is not larger than zero.
	 */
	public boolean setgamma(double gamma)
	{
		if (gamma <= 0)
		{
			return false;
		}
		else
		{
			this.gamma = gamma;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter gamma.
	 * @return the value of gamma.
	 */
	public double getGamma()
	{
		return this.gamma;
	}
	
	
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if (bitmap == null)
		{
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		int a, r, g, b;
		int oldPixel;
		int newPixel;
		
		generateGammaPalette();
		
		for (int i = 0; i < height; i++)
		{
			
			for (int j = 0; j < width; j++)
			{
				
				oldPixel = bitmap.getPixel(j, i);
				
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
				g = (oldPixel >> 8) & 0xff;
				b = oldPixel & 0xff;
				
				r = palette[r];
				g = palette[g];
				b = palette[b];
				
				newPixel = (a << 24) | (r << 16) | (g << 8) | b;
				destBitmap.setPixel(j, i, newPixel);
				
			}
		}
		
		return destBitmap;
		
	}
	
	
	/**
	 * Generate the palette of gamma correction according to the factor gamma.
	 */
	private void generateGammaPalette()
	{
		palette = new int[256];
		int pixel;
		
		for (int k = 0; k < 256; k++) 
		{
			pixel = (int) ((255.0 * Math.pow(k / 255.0, 1.0 / this.gamma)) + 0.5);
			if (pixel > 255)
			{
				pixel = 255;
			}

			palette[k] = pixel;
			
		}
		
	}

	
}
