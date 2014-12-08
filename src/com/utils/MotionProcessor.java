/**
 * 
 */
package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

/**
 * This is a singleton class.
 * The processor applies a motion effect on the original image.
 * The motion processor is also a kind of blur.
 * However, the motion blur bases on different angles.
 * The following three factors will play import roles on the result:
 * 1) distance -- the size of operator
 * 2) angle  -- the direction of motion
 * 3) zoom / scale -- the degree of scale
 * @author WilliamDeng
 *
 */
public class MotionProcessor implements Processor {

    
    private volatile static MotionProcessor uniqueInstance = null;
    private float distance = 0.0f;
    private float angle = 0.0f;  
    private float zoom = 0.4f; 
    
    private MotionProcessor()
    {
        
    }
    
    
    /**
     * the public method to get the unique instance
     * @return MotionProcessor the unique instance of this class
     */
    public static MotionProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(MotionProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new MotionProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
    
    
    /**
     * Set the distance, which represents the size of operator.
     * @param distance the param to set.
     * @return true if the param is successfully set.
     *            false if the param is less than zero.
     */
    public boolean setDistance(float distance)
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
    public float getDistance()
    {
        return this.distance;
    }
    
    
    /**
     * Set the angle, which represents the direction of motion.
     * @param angle the param to set.
     * @return true if the param is successfully set.
     */
    public boolean setAngle(float angle)
    {
        this.angle = angle;
        return true;
    }
    
    
    /**
     * Get the parameter angle.
     * @return the value of angle.
     */
    public float getAngle()
    {
        return this.angle;
    }
    
    
    /**
     * Set the zoom, which represents the degree of scale.
     * @param zoom the param to set.
     * @return true if the param is successfully set.
     */
    public boolean setZoom(float zoom)
    {
        this.zoom = zoom;
        return true;
    }
    
    
    /**
     * Get the parameter zoom.
     * @return the value of zoom.
     */
    public float getZoom()
    {
        return this.zoom;
    }
    
    
    /* (non-Javadoc)
     * @see com.utils.Processor#process(android.graphics.Bitmap)
     */
    @Override
    public Bitmap process(Bitmap bitmap) {
        
        if (bitmap == null)
        {
            return null;
        }
        
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        int[] inPixels = new int[width*height];  
        int[] outPixels = new int[width*height];  
        
        int center_x = width/2;  
        int center_y = height/2;  
          
        float sinAngle = (float)Math.sin(angle/180.0f * Math.PI);  
        float cosAngle = (float)Math.cos(angle/180.0f * Math.PI);  
          
        float radius = (float)Math.sqrt(center_x * center_x + center_y * center_y);  
        float maxDistance = distance + radius * zoom;  
          
        int iteration = (int)maxDistance;  
        
        bitmap.getPixels(inPixels, 0, width, 0, 0, width, height);
        Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        
        int a = 0xff000000;
        int r = 0;
        int g = 0;
        int b = 0;
        int index = 0;
        
        
        for(int i = 0; i < height; i++) 
        {  
            
            for(int j = 0; j < width; j++) 
            {  
                
                int newY = i;
                int newX= j;    
                int count = 0;
                float shift_x = 0.0f;
                float shift_y = 0.0f;  
                
                for(int k = 0; k < iteration; k++) 
                {  
                    
                    newX = j;  
                    newY = i;  
                      
                    if(distance > 0) 
                    {  
                        newY = (int)Math.floor((newY + k * sinAngle));  
                        newX = (int)Math.floor((newX + k * cosAngle));  
                    }  
                    
                    float ratio = (float)k / iteration;  
                    
                    if (newX < 0 || newX >= width)
                    {  
                        break;  
                    }  
                    if (newY < 0 || newY >= height) 
                    {  
                        break;  
                    }  
                      
                    float scale = 1 - zoom * ratio;  
                    shift_x = center_x - center_x * scale;  
                    shift_y = center_y - center_y * scale;  
                    newY = (int)(newY * scale + shift_y);  
                    newX = (int)(newX * scale + shift_x);  
                      
     
                    int pixel = inPixels[newY * width + newX];  
                    a += (pixel >> 24) & 0xff;  
                    r += (pixel >> 16) & 0xff;  
                    g += (pixel >> 8) & 0xff;  
                    b += pixel & 0xff;  
                    count++;  
                }  
                  
 
                if (count == 0) 
                {  
                    outPixels[index] = inPixels[index];  
                } 
                else 
                {  
                    a = clamp((int)(a / count));  
                    r = clamp((int)(r / count));  
                    g = clamp((int)(g / count));  
                    b = clamp((int)(b / count));  
                    
                    outPixels[index] = (a << 24) | (r << 16) | (g << 8) | b;  
                    
                }  
                
                index++;  
                
            }  
        }  
  
        destBitmap.setPixels(outPixels, 0, width, 0, 0, width, height);  
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
