package com.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;


/**
 * This is a singleton class.
 * The processor transforms an image into a disc shape style.
 * @author William
 *
 */
public class DiscShapeProcessor implements Processor {

	
	private volatile static DiscShapeProcessor uniqueInstance = null;
	private int resultWidth = 0;
	private int resultHeight = 0;
	private int background_color = 0xff000000;
	
	
	private DiscShapeProcessor()
	{
		
	}
	
	
	/**
	 * Set the parameter resultWidth, which indicates the width of the image after processing.
	 * @param width the width of the result image. It must be in the range of (0, +infinite)
	 * @return true if the parameter is successfully set.
	 *            false if the parameter is illegal.
	 */
	public Boolean setResultWidth(int width)
	{
		if (width <= 0)
		{
			return false;
		}
		else
		{
			this.resultWidth = width;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter resultWidth.
	 * @return the value of resultWidth.
	 */
	public int getResultWidth()
	{
		return this.resultWidth;
	}
	
	
	/**
	 * Set the parameter resultHeight, which indicates the height of the image after processing.
	 * @param height the height of the result image. It must be in the range of (0, +infinite)
	 * @return true if the parameter is successfully set.
	 *            false if the parameter is illegal.
	 */
	public Boolean setResultHeight(int height)
	{
		if (height <= 0)
		{
			return false;
		}
		else
		{
			this.resultHeight = height;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter resultHeight.
	 * @return the value of resultHeight.
	 */
	public int getResultHeight()
	{
		return this.resultHeight;
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return DiscShapeProcessor the unique instance of this class
	 */
	public static DiscShapeProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(DiscShapeProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new DiscShapeProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }


	@Override
	public Bitmap process(Bitmap bitmap) {

		if (bitmap == null)
		{
			return null;
		}
		
		Bitmap destBitmap = scaleBitmap(bitmap, resultWidth, resultHeight);
		
		int center_x = resultWidth >> 1;
		int center_y = resultHeight >> 1;
		
		int innerRadius = (int) (0.1f * resultWidth);
		int outerRadius = (int) (0.5f * resultWidth);
		if (resultHeight < resultWidth)
		{
			innerRadius = (int) (0.1f * resultHeight);
			outerRadius = (int) (0.5f * resultHeight);
		}
		
		int innerRadius2 = innerRadius * innerRadius;
		int outerRadius2 = outerRadius * outerRadius;
		int distance = 0;
		int dx;
		int dy;
		
		for (int i = 0; i < resultHeight; i++)
		{
			
			for (int j = 0; j < resultWidth; j++)
			{
				
				dx = j - center_x;
				dy = i - center_y;
				distance = dx * dx + dy * dy;
				if (distance <= innerRadius2 || distance >= outerRadius2)
				{
					destBitmap.setPixel(j, i, background_color);
				}
				else
				{
					continue;
				}
			}
		}
		
		return destBitmap;
				
	}
	
	
	/**
	 * Scale a bitmap to a specific size.
	 * @param source the original bitmap to be scaled.
	 * @param new_width the width of new bitmap.
	 * @param new_height the height of new bitmap.
	 * @return the bitmap that after scaling.
	 */
	private Bitmap scaleBitmap(Bitmap source, int new_width, int new_height)
	{	
		int width = source.getWidth();
		int height = source.getHeight();
		
		float width_ratio = ((float)new_width) / width;
		float height_ratio = ((float)new_height) / height;

	    Matrix matrix = new Matrix();
	    matrix.postScale(width_ratio, height_ratio);
	        
	    Bitmap dest = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
	    
	    return dest;
	
	}
	

}
