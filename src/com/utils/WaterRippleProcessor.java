package com.utils;
import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor produces a water ripple effect on the original image.
 * @author William
 *
 */
public class WaterRippleProcessor implements Processor {

	
	private volatile static WaterRippleProcessor uniqueInstance = null;
	
	// the wave length of water ripple
	private float wavelength = 30;  
	// the amplitude of water ripple
    private float amplitude = 10;  
    // the phase of water ripple
    private float phase = (float) (0.5f * Math.PI);  
    // the position of center of the water ripple in an image
    private float centerX = 0.5f;  
    private float centerY = 0.5f;  
    // the radius of the water ripple
    private float radius = 50;  
    // the square of radius
    private float radius2 = 0;  
    // the concrete position of center of the water ripple in the coordinate of an image
    private float icenterX;  
    private float icenterY;
  
	
	
	private WaterRippleProcessor()
	{
		
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return WaterRippleProcessor the unique instance of this class
	 */
	public static WaterRippleProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(WaterRippleProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new WaterRippleProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Set wave length of water ripple.
	 * @param wavelength the value of wave length to set.
	 * @return true if the param is successfully set.
	 *            false if the param is less than zero, which is illegal.
	 */ 
	public boolean setWavelength(float wavelength)
	{
		if (wavelength < 0)
		{
			return false;
		}
		else
		{
			this.wavelength = wavelength;
		}
		
		return true;
	}
	
	
	/**
	 * Get wave length of water ripple.
	 * @return the value of wave length.
	 */
	public float getWavelength()
	{
		return this.wavelength;
	}
	
	
	/**
	 * Set amplitude of water ripple.
	 * @param amplitude the value of amplitude to set.
	 * @return true if the param is successfully set.
	 *            false if the param is less than zero, which is illegal.
	 */
	public boolean setAmplitude(float amplitude)
	{
		if (amplitude < 0)
		{
			return false;
		}
		else
		{
			this.amplitude = amplitude;
		}
		
		return true;
	}
	
	
	/**
	 * Get amplitude of water ripple.
	 * @return the value of amplitude of water ripple.
	 */
	public float getAmplitude()
	{
		return this.amplitude;
	}
	
	
	/**
	 * Set phase of water ripple.
	 * @param phase the value of phase to set.
	 * @return true if the param is successfully set.
	 *            false if the param is less than zero, which is illegal.
	 */
	public boolean setPhase(float phase)
	{
		if (phase < 0)
		{
			return false;
		}
		else
		{
			this.phase = phase;
		}
		
		return true;
	}
	
	
	/**
	 * Get phase of water ripple.
	 * @return the value of phase of water ripple.
	 */
	public float getPhase()
	{
		return this.phase;
	}
	
	
	/**
	 * Set the position of center of water ripple in horizontal direction.
	 * @param centerX the value to set.
	 * @return true if the param is in range of [0, 1].
	 *            false if the param is out of range.
	 */
	public boolean setCenterX(float centerX)
	{
		if (centerX < 0 || centerX > 1)
		{
			return false;
		}
		else
		{
			this.centerX = centerX;
		}
		
		return true;
	}
	
	
	/**
	 * Get the position of center of water ripple in horizontal direction.
	 * @return the value of centerX.
	 */
	public float getCenterX()
	{
		return this.centerX;
	}
	
	
	/**
	 * Set the position of center of water ripple in vertical direction.
	 * @param centerY the value to set.
	 * @return true if the param is in range of [0, 1].
	 *            false if the param is out of range.
	 */
	public boolean setCenterY(float centerY)
	{
		if (centerY < 0 || centerY > 1)
		{
			return false;
		}
		else
		{
			this.centerY = centerY;
		}
		
		return true;
	}
	
	
	/**
	 * Get the position of center of water ripple in vertical direction.
	 * @return the value of centerY.
	 */
	public float getCenterY()
	{
		return this.centerY;
	}
	
	
	/**
	 * Set radius of water ripple.
	 * @param radius the value of radius of water ripple to set.
	 * @return false if the param is less than zero, which is illegal.
	 *            true if the param is successfully set.
	 */
	public boolean setRadius(float radius)
	{
		if (radius < 0)
		{
			return false;
		}
		else
		{
			this.radius = radius;
			this.radius2 = radius * radius;
		}
		
		return true;
	}
	
	
	/**
	 * Get radius of water ripple.
	 * @return the value of radius.
	 */
	public float getRadius()
	{
		return this.radius;
	}
	
	
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if (bitmap == null)
		{
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
	    
		// record the pixels of original image
	    int[] inPixels = new int[width*height];
	    // record the pixels of image after processing.
        int[] outPixels = new int[width*height];  
        
        
        bitmap.getPixels(inPixels, 0, width, 0, 0, width, height);
        icenterX = width * centerX;  
        icenterY = height * centerY;  
        
        if (width > height)
        {
        	setRadius(height * 0.3f);
        }
        else
        {
        	setRadius(width * 0.3f);
		}
        if ( radius == 0 )  
        {
            setRadius(Math.min(icenterX, icenterY)); 
        } 
        
        int index = 0;  
        // record the position of a pixel after ripple.
        float[] out = new float[2];  
        
        for(int i=0; i<height; i++) 
        {  
            for(int j=0; j<width; j++) 
            {  
                index = i * width + j;  
                    
                generateWaterRipples(j, i, out);  
                int srcX = (int)Math.floor( out[0] );  
                int srcY = (int)Math.floor( out[1] );  
                float xWeight = out[0] - srcX;  
                float yWeight = out[1] - srcY;  
                int nw, ne, sw, se;  
                  
                
                if ( srcX >= 0 && srcX < width-1 && srcY >= 0 && srcY < height-1) 
                {  
                    int new_index = width*srcY + srcX;  
                    nw = inPixels[new_index];  
                    ne = inPixels[new_index+1];  
                    sw = inPixels[new_index+width];  
                    se = inPixels[new_index+width+1];  
                } 
                else
                {  
                    nw = pixelChoose( inPixels, srcX, srcY, width, height );  
                    ne = pixelChoose( inPixels, srcX+1, srcY, width, height );  
                    sw = pixelChoose( inPixels, srcX, srcY+1, width, height );  
                    se = pixelChoose( inPixels, srcX+1, srcY+1, width, height );  
                }  
                  
                
                outPixels[index] = bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);  
            }  
        }  
  
        Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        destBitmap.setPixels(outPixels, 0, width, 0, 0, width, height);
        
        
        return destBitmap;  
	}
	  
    
	/**
	 * Choose the correct pixel in correct position in a specific strategy.
	 * @param pixels the original pixels array that records all the pixels of the original image. 
	 * @param x the position of horizontal direction to check.
	 * @param y the position of vertical direction to check.
	 * @param width the width of the original image.
	 * @param height the height of the original image.
	 * @return the chosen pixel.
	 */
    private int pixelChoose(int[] pixels, int x, int y, int width, int height) {  
        int new_x = x, new_y = y;
        if (x < 0)
        {
        	new_x = 0;
        }
        if (x > width-1)
        {
        	new_x = width-1;
        }
        if (y < 0)
        {
        	new_y = 0;
        }
        if (y > height-1)
        {
        	new_y = height-1;
        }
        return pixels[new_y*width+new_x ];  
    }  
  
    
    /**
     * Generate the water ripple effect.
     * @param x the original position of horizontal direction.
     * @param y the original position of vertical direction.
     * @param out the array that stores the new position of both horizontal and vertical direction
     *                 that after ripple.
     */
    private void generateWaterRipples(int x, int y, float[] out) {  
        float dx = x-icenterX;  
        float dy = y-icenterY;  
        float distance2 = dx * dx + dy * dy;  

        if (distance2 > radius2)
        {   
            out[0] = x;  
            out[1] = y;  
        } 
        else 
        {    
        	
            float distance = (float)Math.sqrt(distance2);  
    
            // record the energy lose.
            float amount = amplitude * (float)Math.sin(distance / wavelength * Math.PI * 2 - phase);      
            amount *= (radius - distance) / radius;            
            if ( distance != 0 )  
            {
                amount *= wavelength / distance;  
            }
 
            out[0] = x + dx * amount;  
            out[1] = y + dy * amount;  
            
        }  
        
    }  
    
    
    /**
	 * Bilinear interpolation of ARGB values.
	 * @param x the X interpolation parameter in range of [0, 1]
	 * @param y the y interpolation parameter in range of [0, 1]
	 * @param rgb array of four ARGB values in the order NW, NE, SW, SE
	 * @return the interpolated value
	 */
	public int bilinearInterpolate(float x, float y, int nw, int ne, int sw, int se) {
		float m0, m1;
		int a0 = (nw >> 24) & 0xff;
		int r0 = (nw >> 16) & 0xff;
		int g0 = (nw >> 8) & 0xff;
		int b0 = nw & 0xff;
		int a1 = (ne >> 24) & 0xff;
		int r1 = (ne >> 16) & 0xff;
		int g1 = (ne >> 8) & 0xff;
		int b1 = ne & 0xff;
		int a2 = (sw >> 24) & 0xff;
		int r2 = (sw >> 16) & 0xff;
		int g2 = (sw >> 8) & 0xff;
		int b2 = sw & 0xff;
		int a3 = (se >> 24) & 0xff;
		int r3 = (se >> 16) & 0xff;
		int g3 = (se >> 8) & 0xff;
		int b3 = se & 0xff;

		float cx = 1.0f-x;
		float cy = 1.0f-y;

		m0 = cx * a0 + x * a1;
		m1 = cx * a2 + x * a3;
		int a = (int)(cy * m0 + y * m1);

		m0 = cx * r0 + x * r1;
		m1 = cx * r2 + x * r3;
		int r = (int)(cy * m0 + y * m1);

		m0 = cx * g0 + x * g1;
		m1 = cx * g2 + x * g3;
		int g = (int)(cy * m0 + y * m1);

		m0 = cx * b0 + x * b1;
		m1 = cx * b2 + x * b3;
		int b = (int)(cy * m0 + y * m1);

		return (a << 24) | (r << 16) | (g << 8) | b;
		
	}


}
