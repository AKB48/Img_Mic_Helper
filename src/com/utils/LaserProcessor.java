package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor adds a laser effect on the original image.
 * First, generate a motion blur image of the original image.
 * The parameter threshold decides which pixels need to process.
 * Then, blend the motion blur image and the original image
 * according to the parameter strength.
 * The parameter strength decides the strength of the laser.
 * @author WilliamDeng
 *
 */
public class LaserProcessor implements Processor {

    
    private volatile static LaserProcessor uniqueInstance = null;
    private float threshold = 0.5f;  
    private float strength = 0.8f;   
    
    
    private LaserProcessor()
    {
        
    }
    
    
    /**
     * the public method to get the unique instance
     * @return LaserProcessor the unique instance of this class
     */
    public static LaserProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(LaserProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new LaserProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
    
    
    /**
     * Set the threshold, which represents the threshold that the pixel needs to process.
     * @param threshold the param to set. It must be in the range of [0, 1].
     * @return true if the param is successfully set.
     *            false if the param is out of range.
     */
    public boolean setThreshold(float threshold)
    {
        if (threshold < 0 || threshold > 1)
        {
            return false;
        }
        else
        {
            this.threshold = threshold;
        }
        
        return true;
    }
    
    
    /**
     * Get the parameter threshold.
     * @return the value of threshold.
     */
    public float getThreshold()
    {
        return this.threshold;
    }
    
    
    /**
     * Set the strength, which represents the strength of the laser.
     * @param strength the param to set. It must be in the range of [0, 1].
     * @return true if the param is successfully set.
     *            false if the param is out of range.
     */
    public boolean setStrength(float strength)
    {
        if (strength < 0 || strength > 1)
        {
            return false;
        }
        else
        {
            this.strength = strength;
        }
        
        return true;
    }
    
    
    /**
     * Get the parameter strength.
     * @return the value of strength.
     */
    public float getStrength()
    {
        return this.strength;
    }
    
    
    @Override
    public Bitmap process(Bitmap bitmap) {
        
        if (bitmap == null)
        {
            return null;
        }
        
        int width = bitmap.getWidth();
        int height = bitmap.getHeight(); 
  
        Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
  
        int _3threshold = (int)(threshold * 3 * 255);  
        int a, r, g, b;
        int _a, _r, _g, _b;
        int oldPixel;
        int _oldPixel;
        int newPixel = 0xff000000;
        
        
        for ( int i = 0; i < height; i++ ) 
        {  

            for ( int j = 0; j < width; j++ ) 
            {  
                
                oldPixel = bitmap.getPixel(j, i); 
                
                a = (oldPixel  >> 24) & 0xff;  
                r = (oldPixel >> 16) & 0xff;  
                g = (oldPixel >> 8) & 0xff;  
                b = oldPixel & 0xff;  
                
                int sum = r + g + b;  
                if (sum < _3threshold)  
                {
                    newPixel = 0xff000000;  
                }
                else 
                {  
                    sum /= 3;  
                    newPixel = a | (sum << 16) | (sum << 8) | sum;  
                }  
                
                tempBitmap.setPixel(j, i, newPixel);
            }  
        }  
  
        Bitmap destBitmap = MotionProcessor.getInstance().process(tempBitmap);
        tempBitmap.recycle();
        tempBitmap = null;
        tempBitmap = Bitmap.createBitmap(destBitmap);
        
          
        for ( int i = 0; i < height; i++ ) 
        {  
   
            for ( int j = 0; j < width; j++ ) 
            {  
                
                oldPixel = tempBitmap.getPixel(j, i);  
                a = (oldPixel  >> 24) & 0xff;  
                r = (oldPixel >> 16) & 0xff;  
                g = (oldPixel >> 8) & 0xff;  
                b = oldPixel & 0xff;  
                  
                _oldPixel = bitmap.getPixel(j, i);  
                _a = (_oldPixel >> 24) & 0xff;  
                _r = (_oldPixel >> 16) & 0xff;  
                _g = (_oldPixel >> 8) & 0xff;  
                _b = _oldPixel & 0xff;  
                  
                if ( r > 0 ) 
                {  
                    r = clamp((int)(r * strength) + _r);  
                    g = clamp((int)(g * strength) + _g);  
                    b = clamp((int)(b * strength) + _b);  
                } 
                else
                {  
                    r = _r;  
                    g = _g;  
                    b = _b;  
                }  
  
                newPixel = a | (r << 16) | (g << 8) | b;  
                destBitmap.setPixel(j, i, newPixel);
                
            }  
        }  
  
        tempBitmap.recycle();
        tempBitmap = null;
        
        return destBitmap;  
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
