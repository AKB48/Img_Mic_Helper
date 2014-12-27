package com.utils.processor;



import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor whitens the skin in an image.
 * The processor uses a skin detection algorithm to find out which pixel is skin.
 * If a pixel is a skin, whiten it as much as possible.
 * Otherwise, whiten it but blend it with the original pixel to make it looks close to the origin.
 * The result is that the skin will be whitened.
 * The non-skin parts will be whitened not so much.
 * @author William
 *
 */
public class WhiteningProcessor implements Processor {

	
	private volatile static WhiteningProcessor uniqueInstance = null;
	private int beta = 5;
    
	
	private WhiteningProcessor()
	{
		
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return WhiteningProcessor the unique instance of this class
	 */
	public static WhiteningProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(WhiteningProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new WhiteningProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Set the parameter beta, which represents the degree of whitening.
	 * The larger beta, the whiter image.
	 * @param beta the param to set.
	 * @return true if the param is successfully set.
	 *            false if the param is not larger than zero.
	 */
	public boolean setBeta(int beta)
	{
		if (beta <= 0)
		{
			return false;
		}
		else
		{
			this.beta = beta;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter beta.
	 * @return the value of beta.
	 */
	public int getBeta()
	{
		return this.beta;
	}
	
	
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if (bitmap == null)
		{
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int a, r, g, b;
		int new_r, new_g, new_b;
		int oldPixel, newPixel;
			
		Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		
		for (int i = 0; i < height; i++)
		{
			
			for (int j = 0; j < width; j++)
			{
				
				oldPixel = bitmap.getPixel(j, i);
				a = (oldPixel >> 24) & 0xff;
				r = (oldPixel >> 16) & 0xff;
				g = (oldPixel >> 8) & 0xff;
				b = oldPixel & 0xff;
				

				new_r = (int) (Math.log(r / 255.0d * (beta - 1) + 1) / Math.log(beta) * 255);
				clamp(new_r);
				new_g = (int) (Math.log(g / 255.0d * (beta - 1) + 1) / Math.log(beta) * 255);
				clamp(new_g);
				new_b = (int) (Math.log(b / 255.0d * (beta - 1) + 1) / Math.log(beta) * 255);
				clamp(new_b);
				
				if (!isSkin(r, g, b))
				{
					new_r = (int) (new_r * 0.8 + r * 0.2);
					new_g = (int) (new_g * 0.8 + g * 0.2);
					new_b = (int) (new_b * 0.8 + b * 0.2);

				}  
				
				newPixel = (a << 24) | (new_r << 16) | (new_g << 8) | new_b;
				destBitmap.setPixel(j, i, newPixel);
			}
	
		}
		
		return destBitmap;
		
	}
	
	
	/**
	 * Detect whether a pixel is a skin pixel.
	 * @param r the r component of the pixel.
	 * @param g the g component of the pixel.
	 * @param b the b component of the pixel.
	 * @return true if the pixel is skin.
	 *            false if the pixel is not skin.
	 */
	private boolean isSkin(int r, int g, int b) {
		
	    int max = Math.max(r, Math.max(g, b));  
	    int min = Math.min(r, Math.min(g, b));  
	    int rg = Math.abs(r - g);  
	    
	    if(r > 95 && g > 40 && b > 20 && rg > 15 && (max - min) > 15 && r > g && r > b) 
	    {  
	        return true;  
	    } 
	    else
	    {  
	        return false;  
	    }  
	    
	}  
	
	
	 /**
	* Clamp a value to the range [0, 255]
	* @param value the param to set.
	* @return the value of the param after clamp.
	*/
	private int clamp(int value) {
		if (value < 0)
		{
			return 0;
		}
		else if (value > 255)
		{
			return 255;
		}
		
		return value;
			
	}

	
}
