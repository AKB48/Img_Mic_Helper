package com.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.os.Environment;


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
		

		Bitmap tempBitmap = scaleBitmap(bitmap, resultWidth, resultHeight);
		
		int center_x = resultWidth >> 1;
		int center_y = resultHeight >> 1;
		
		int innerRadius = (int) (0.1f * resultWidth);
		int outerRadius = (int) (0.5f * resultWidth);
		if (resultHeight < resultWidth)
		{
			innerRadius = (int) (0.1f * resultHeight);
			outerRadius = (int) (0.5f * resultHeight);
		}
		
		int innerRadius_2 = innerRadius + innerRadius;
		int outerRadius_2 = outerRadius + outerRadius;

		
		Bitmap tempBitmap2 = Bitmap.createBitmap(resultWidth, resultHeight, Config.ARGB_8888);
		
		Canvas canvas = new Canvas(tempBitmap2);
		Paint paint = new Paint();
		Rect rect= new Rect(0, 0, resultWidth, resultHeight);
		RectF rectf =  new RectF(rect);
		paint.setAntiAlias(true);
		paint.setColor(0xffffffff);
		canvas.drawRoundRect(rectf, resultWidth, resultHeight, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(tempBitmap, rect, rect, paint);
		canvas.save();
		
		Rect rect2 = new Rect(center_x-innerRadius, center_y-innerRadius, center_x+innerRadius, center_y+innerRadius);
		rectf = new RectF(rect2);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
		Bitmap destBitmap = Bitmap.createBitmap(resultWidth, resultHeight, Config.ARGB_8888);
		canvas = new Canvas(destBitmap);
        canvas.drawRoundRect(rectf, innerRadius_2, innerRadius_2, paint);
        canvas.drawBitmap(tempBitmap2, rect, rect, paint);
		canvas.save();
		
		if (tempBitmap != null)
		{
			tempBitmap.recycle();
			tempBitmap = null;
		}
		if (tempBitmap2 != null)
		{
			tempBitmap2.recycle();
			tempBitmap2 = null;
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
