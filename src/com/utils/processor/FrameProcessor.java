package com.utils.processor;

import com.app.img_mic_helper.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.util.Log;


/**
 * This is a singleton class.
 * The processor adds a beautiful photo frame onto the image.
 * @author William
 *
 */
public class FrameProcessor implements Processor {

	
	private volatile static FrameProcessor uniqueInstance = null;
	private double opacity  = 0.5;
	private Context context = null;
	
	
	private FrameProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return FrameProcessor the unique instance of this class
	 */
	public static FrameProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(FrameProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new FrameProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Set the opacity.
	 * @param value the new value of opacity. It must be in range of [0, 1].
	 * @return false if the param is out of range.
	 *            true if the param is in range and opacity is successfully set.
	 */
	public boolean setOpacity(double value)
	{
		if (value < 0 || value > 1)
		{
			return false;
		}
		else
		{
			opacity = value;
		}
		
		return true;
	}
	
	
	/**
	 * Get the opacity.
	 * @return the opacity.
	 */
	public double getOpacity()
	{
		return opacity;
	}
	
	
	/**
	 * Set context which is used to achieve the image resources.
	 * @param context the context.
	 * @return false if the param is null.
	 *            true if context is successfully set.
	 */     
	public boolean setContext(Context context)
	{
		if (context == null)
		{
			return false;
		}
		else
		{
			this.context = context;
		}
		
		return true;
	}
	
	
	/**
	 * Get the context.
	 * @return the context.
	 */
	public Context getContext()
	{
		return context;
	}
	
	
	@Override
	public Bitmap process(Bitmap bitmap) {

		if (bitmap == null)
		{
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		Bitmap tempBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.frame);
		Bitmap frameBitmap = scaleBitmap(tempBitmap, width, height);
		tempBitmap.recycle();
		tempBitmap = null;
		
		tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		int oldPixel1;
		int oldPixel2;
		int r, g, b;
		int r1, g1, b1;
		int r2, g2, b2;
		int newPixel;
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				oldPixel1 = frameBitmap.getPixel(j, i);
				
				r1 = (oldPixel1 >> 16) & 0xff;
				g1 = (oldPixel1 >> 8) & 0xff;
				b1 = oldPixel1 & 0xff;
				if (r1 > 200 && g1 > 200 && b1 > 200)
				{
					opacity = 0;
				}
				else 
				{
					opacity = 0.7;
				}
					
				oldPixel2 = bitmap.getPixel(j, i);
				r2 = (oldPixel2 >> 16) & 0xff;
				g2 = (oldPixel2 >> 8) & 0xff;
				b2 = oldPixel2 & 0xff;
					
				r = (int) (opacity * r1 + (1 - opacity) * r2);
				r = r > 255 ? 255 : r;
				r = r < 0 ? 0 : r;
				g = (int) (opacity * g1 + (1 - opacity) * g2);
				g = g > 255 ? 255 : g;
				g = g < 0 ? 0 : g;
				b = (int) (opacity * b1 + (1 - opacity) * b2);
				b = b > 255 ? 255 : b;
				b = b < 0 ? 0 : b;
					
				newPixel = 0xff000000 | (r << 16) | (g << 8) | b;
				tempBitmap.setPixel(j, i, newPixel);
			
				
			}
		}
		
		return tempBitmap;
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
