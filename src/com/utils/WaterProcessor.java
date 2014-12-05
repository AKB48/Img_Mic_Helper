package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor applies a water wave effect on the image.
 * The horizontal direction will produce a sine wave.
 * And the vertical direction will produce a cosine wave.
 * The wave will be decided by two factors:
 * wave : the amplitude of the wave.
 * period :  the period of the wave.
 * @author William
 *
 */
public class WaterProcessor implements Processor {

	
	private volatile static WaterProcessor uniqueInstance = null;
	private double wave = 25.0;  
    private double period = 128.0; 
    
	
	private WaterProcessor()
	{
		
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return WaterProcessor the unique instance of this class
	 */
	public static WaterProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(WaterProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new WaterProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Set the wave, which represents the amplitude of the wave effect.
	 * @param wave the param to set.
	 * @return true if the param is successfully set.
	 *            false if the param is less than zero.
	 */
	public boolean setWave(double wave)
	{
		if (wave < 0)
		{
			return false;
		}
		else 
		{
			this.wave = wave;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter wave.
	 * @return the value of wave.
	 */
	public double getWave()
	{
		return this.wave;
	}
	
	
	/**
	 * Set the period, which represents the period of the sine / cosine wave.
	 * @param period the param to set. It must in range of (0, +infinite).
	 * @return true if the param is successfully set.
	 *            false if the param is illegal. 
	 */
	public boolean setPeriod(double period)
	{
		if (period <= 0)
		{
			return false;
		}
		else
		{
			this.period = period;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter period.
	 * @return the value of period.
	 */
	public double getPeriod()
	{
		return this.period;
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
		
		
		int iOffset = 0;
		int jOffset = 0;
		int newPixel = 0;
		
		
		for (int i = 0; i < height; i++)
		{
			
			for (int j = 0; j < width; j++)
			{
				
				jOffset = (int)((double)wave * Math.sin(2.0 * Math.PI * (float)i / period));  
				iOffset = (int)((double)wave * Math.cos(2.0 * Math.PI * (float)j / period)); 
                
                int new_i = iOffset + i;
                int new_j = jOffset + j;
                
                if (new_i < 0)
                {
                	new_i = 0;
                }
                else if (new_i >= height)
                {
                	new_i = height - 1;
                }
                
                if (new_j < 0)
                {
                	new_j = 0;
                }
                else if (new_j >= width)
                {
                	new_j = width - 1;
				}
                
                newPixel = bitmap.getPixel(new_j, new_i);
                destBitmap.setPixel(j, i, newPixel);
                
			}
		}
		
		return destBitmap;
		
	}

	
}
