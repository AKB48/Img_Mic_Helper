package com.utils;


import com.app.img_mic_helper.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


/**
 * This is a singleton class.
 * The processor adds a bubble effect onto the original image.
 * @author William
 *
 */
public class BubbleProcessor implements Processor {

	
	private volatile static BubbleProcessor uniqueInstance = null;
	private double opacity = 0.5;
	private Context context = null;
	
	
	private BubbleProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return BubbleProcessor the unique instance of this class
	 */
	public static BubbleProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(BubbleProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new BubbleProcessor();
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
		
		if (this.context == null)
		{
			return bitmap.copy(Config.ARGB_8888, false);
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		
		Bitmap tempBitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bubble);
		Bitmap bubbleBitmap;
		if (height > width)
		{
			bubbleBitmap = rotateBitmap(tempBitmap);
			tempBitmap.recycle();
			tempBitmap = bubbleBitmap;
		}
		bubbleBitmap = scaleBitmap(tempBitmap, width, height);
		tempBitmap.recycle();
		tempBitmap = null;
		
		tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	
		
		int r, g, b;
		int r1, g1, b1;
		int r2, g2, b2;
		int oldPixel1, oldPixel2;
		int newPixel = 0;
		
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				oldPixel1 = bubbleBitmap.getPixel(j, i);
				r1 = (oldPixel1 >> 16) & 0xff;
				g1 = (oldPixel1 >> 8) & 0xff;
				b1 = oldPixel1 & 0xff;
				
				oldPixel2 = bitmap.getPixel(j, i);
				r2 = (oldPixel2 >> 16) & 0xff;
				g2 = (oldPixel2 >> 8) & 0xff;
				b2 = oldPixel2 & 0xff;
				
               
				r = pixelChoose(r2, r1);
				r = (int) (opacity * r + (1 - opacity) * r2);
				r = r > 255 ? 255 : r;
				r = r < 0 ? 0 : r;
				g = pixelChoose(g2, g1);
				g = (int) (opacity * g + (1 - opacity) * g2);
				g = g > 255 ? 255 : g;
				g = g < 0 ? 0 : g;
				b = pixelChoose(b2, b1);
				b = (int) (opacity * b + (1 - opacity) * b2);
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
	
	
	/**
	 * Rotate bitmap 90 degree.
	 * @param source the bitmap to rotate.
	 * @return the bitmap after rotating.
	 */
	private Bitmap rotateBitmap(Bitmap source)
	{
		int width = source.getWidth();
		int height = source.getHeight();
		
	    Matrix matrix = new Matrix();
	    matrix.postRotate(90.0f);
	        
	    Bitmap dest = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
	    
	    return dest;
	
	} 
	
	
	private int pixelChoose(int v1, int v2) 
	{
		  if ( v1 > 127.5 )
		  {
		      return (int)(v2 + (255.0 - v2) * ((v1 - 127.5) / 127.5) * (0.5 - Math.abs(v2-127.5)/255.0));
		   }
		  else
		  {
		      return (int)(v2 - v2 * ((127.5 -  v1) / 127.5) * (0.5 - Math.abs(v2-127.5)/255.0));
		   }
		  
	}


}
