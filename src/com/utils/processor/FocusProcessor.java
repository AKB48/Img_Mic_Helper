package com.utils.processor;


import android.graphics.Bitmap;


/**
 * This is a singleton class.
 * The processor applies a focus effect on the original image.
 * The focus part likes a lens, which is very clear.
 * The other parts is blurry, which is processed by gaussian blur processor.
 * Hence, this processor has the effect that a lens focuses on one part of the image,
 * and neglects the other parts.
 * @author WilliamDeng
 *
 */
public class FocusProcessor implements Processor {

    
    private volatile static FocusProcessor uniqueInstance = null;
    private float Size = 0.7f;    
    
    
    private FocusProcessor()
    {
        
    }
    
    
    /**
     * Set the Size, which represents the focus lens size in the image.
     * @param Size the param to set. It must be in the range of [0, 1].
     * @return true if the param is successfully set.
     *            false if the param is out of range.
     */
    public boolean setSize(float Size)
    {
        if (Size < 0 || Size > 1)
        {
            return false;
        }
        else
        {
            this.Size = Size;
        }
        
        return true;
    }
    
    
    /**
     * Get the parameter Size.
     * @return the value of Size.
     */
    public float getSize()
    {
        return this.Size;
    }
    
    
    /**
     * the public method to get the unique instance
     * @return FocusProcessor the unique instance of this class
     */
    public static FocusProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(FocusProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new FocusProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
    
    
    @Override
    public Bitmap process(Bitmap bitmap) 
    {
        
        if (bitmap == null)
        {
            return null;
        }
        
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        int ratio = width > height ? height * 32768 / width : width * 32768 / height;
    
        int center_x = width >> 1;
        int center_y = height >> 1;
        int max = center_x * center_x + center_y * center_y;
        int min = (int)(max * (1 - Size));
        int difference = max - min;

        Bitmap destBitmap = GaussianBlurProcessor.getInstance().process(bitmap);

        
        for (int i = 0; i < height; i++)
        {
      
            for (int j = 0; j < width; j++)
            {

                int dx = center_x - j;
                int dy = center_y - i;
                
                if (width > height)
                {
                    dy = (dy * ratio) >> 14;
                }
                else
                {
                    dx = (dx * ratio) >> 14;
                }
                
                int distance2 = dx * dx + dy * dy;
                if (distance2 <= min)
                {
                    destBitmap.setPixel(j, i, bitmap.getPixel(j, i));         
                }
                
            }
        }
        
        return destBitmap;
    }

    
}
