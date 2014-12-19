/**
 * 
 */
package com.utils;

import java.util.Random;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This is a singleton class.
 * The processor transforms an image into the smear style.
 * @author William
 *
 */
public class SmearProcessor implements Processor {

	
	private volatile static SmearProcessor uniqueInstance = null;
	private Random randomGenerator = null;
	private long seed = 567;
	private float density = 0.5f;
	private int distance = 8;
	private float mix = 0.5f;
    
    
    private SmearProcessor()
    {

    }
    
    
    /**
     * the public method to get the unique instance
     * @return SmearProcessor the unique instance of this class
     */
    public static SmearProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(SmearProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new SmearProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
    
    
    /**
     * Set the parameter density, which represents the degree of the smear effect.
     * @param density the parameter to set. It must be in the range of [0, 1]
     * @return true if the param is successfully set.
     *            false if the param is out of range.
     */
    public Boolean setDensity(float density)
    {
    	if (density < 0 || density > 1)
    	{
    		return false;
    	}
    	else
    	{
    		this.density = density;
		}
    	
    	return true;
    }
    
    
    /**
     * Get the parameter density
     * @return the value of density.
     */
    public float getDensity()
    {
    	return this.density;
    }
    
    
    /**
     * Set the parameter distance, which represents the size of smear area. 
     * @param distance the parameter to set. It must not be less than zero.
     * @return true if the parameter is successfully set.
     *            false if the parameter is illegal.
     */
    public Boolean setDistance(int distance)
    {
    	if (distance < 0)
    	{
    		return false;
    	}
    	else
    	{
    		this.distance = distance;
		}
    	
    	return true;
    }
    
    
    /**
     * Get the parameter distance.
     * @return the value of distance.
     */
    public int getDistance()
    {
    	return this.distance;
    }
    
    
    /**
     * Set the parameter mix, which represents the mix portion of two pixels.
     * @param mix the parameter to set. It must be in the range of [0, 1]
     * @return true if the param is successfully set.
     *            false if the param is out of range.
     */
    public Boolean setMix(float mix)
    {
    	if (mix < 0 || mix > 1)
    	{
    		return false;
    	}
    	else
    	{
    		this.mix = mix;
		}
    	
    	return true;
    }
    
    
    /**
     * Get the parameter mix.
     * @return the value of mix.
     */
    public float getMix()
    {
    	return this.mix;
    }

    
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if (bitmap == null)
		{
			return null;
		}
		
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		bitmap.getPixels(inPixels, 0, width, 0, 0, width, height);
		bitmap.getPixels(outPixels, 0, width, 0, 0, width, height);
		
		randomGenerator = new Random();
		randomGenerator.setSeed(seed);


		int numShapes = 0;
		int radius = distance + 1;
		int radius2 = radius * radius;
		numShapes = (int)(2 * density * width * height / radius);
		int difference = 0;
		int oldPixel = 0;
		int tempPixel = 0;
		
		Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		
		for (int k = 0; k < numShapes; k++) 
		{
			int sx = (randomGenerator.nextInt() & 0x7fffffff) % width;
			int sy = (randomGenerator.nextInt() & 0x7fffffff) % height;
			oldPixel = inPixels[sy * width + sx];
			
			for (int i = sy - radius; i < sy + radius + 1; i++) 
			{
				
				for (int j = sx - radius; j < sx + radius + 1; j++) 
				{
					
					difference = (j - sx) * (j - sx) + (i - sy) * (i - sy);
					if (j >= 0 && j < width && i >= 0 && i < height && difference <= radius2) 
					{
						tempPixel = outPixels[i * width + j];
						outPixels[i * width + j] = mixColors(mix, tempPixel, oldPixel);
					}
				}
			}
		}
		
		destBitmap.setPixels(outPixels, 0, width, 0, 0, width, height);
		
		return destBitmap;
		
	}
	
	
	/**
	 * Linear interpolation of ARGB values.
	 * @param portion the interpolation parameter, represents the mix portion of the pixel1.
	 * @param pixel1 the lower interpolation range.
	 * @param pixel2 the upper interpolation range.
	 * @return the interpolated value.
	 */
	public static int mixColors(float portion, int pixel1, int pixel2) {
		int a1 = (pixel1 >> 24) & 0xff;
		int r1 = (pixel1 >> 16) & 0xff;
		int g1 = (pixel1 >> 8) & 0xff;
		int b1 = pixel1 & 0xff;
		int a2 = (pixel2 >> 24) & 0xff;
		int r2 = (pixel2 >> 16) & 0xff;
		int g2 = (pixel2 >> 8) & 0xff;
		int b2 = pixel2 & 0xff;
		a1 = (int)(a1 + portion * (a2 - a1));
		r1 = (int)(r1 + portion * (r2 - r1));
		g1 = (int)(g1 + portion * (g2 - g1));
		b1 = (int)(b1 + portion * (b2 - b1));
		return (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
	}
	

}
