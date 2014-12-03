package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor transforms an image into a sudoku style.
 * The sudoku style means that divides an image into nine parts.
 * There is a stripe between two parts.
 * @author William
 *
 */
public class SudokuProcessor implements Processor {

	
	private volatile static SudokuProcessor uniqueInstance = null;
	private int divider_color = 0xffffffff;
	private int divider_width = 15;
	
	
	private SudokuProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return SudokuProcessor the unique instance of this class
	 */
	public static SudokuProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(SudokuProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new SudokuProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Set the divider color.
	 * @param color the color to be set.
	 * @return false if the param is less than 0, which is illegal.
	 *            true if the divider color is successfully set.
	 */
	public boolean setDividerColor(int color)
	{
		if (color < 0)
		{
			return false;
		}
		else
		{
			divider_color = color;
		}
		
		return true;
	}
	
	
	/**
	 * Get the divider color.
	 * @return the divider color of the sudoku.
	 */
	public int getDividorColor()
	{
		return divider_color;
	}
	
	
	/**
	 * Set the width of divider.
	 * @param width the width of divider that to be set. This param must be in range [0, 100].
	 * @return false if the param is out of range.
	 *            true if the width of divider is successfully set.
	 */
	public boolean setDividerWidth(int width)
	{
		if (width < 0 || width > 100)
		{
			return false;
		}
		else
		{
			divider_width = width;
		}
		
		return true;
	}
	
	
	/**
	 * Get the width of divider.
	 * @return the width of divider.
	 */
	public int getDividerWidth()
	{
		return divider_width;
	}
	
	
	@Override
	public Bitmap process(Bitmap bitmap) {

		if (bitmap == null)
		{
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int new_width = width + divider_width * 2;
		int new_height = height + divider_width * 2;
		int oneThird_width = width / 3;
		int oneThird_height = height / 3;
		int twoThird_width = 2 * width / 3;
		int twoThird_height = 2 * height / 3;
		
		
		Bitmap tempBitmap = Bitmap.createBitmap(new_width, new_height, Config.ARGB_8888);
		
		
		int new_i;
		int new_j;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (i <= oneThird_height)
				{
					new_i = i;
				}
				else if (i > oneThird_height && i <= twoThird_height)
				{
					new_i = i + divider_width;
				}
				else
				{
					new_i = i + 2 * divider_width;
				}
				
				if (j <= oneThird_width)
				{
					new_j = j;
				}
				else if (j > oneThird_width && j <= twoThird_width)
				{
					new_j = j + divider_width;
				}
				else 
				{
					new_j = j + 2 * divider_width;
				}
					
				tempBitmap.setPixel(new_j, new_i, bitmap.getPixel(j, i));
			}
		}
		
		for (int i = 0; i < new_height; i++)
		{
			for (int j = oneThird_width+1; j <= oneThird_width+divider_width; j++)
			{
				tempBitmap.setPixel(j, i, divider_color);
			}
			
			for (int j = twoThird_width+divider_width+1; j <= twoThird_width+2*divider_width; j++)
			{
				tempBitmap.setPixel(j, i, divider_color);
			}
		}
		
		for (int j = 0; j < new_width; j++)
		{
			for (int i = oneThird_height+1; i <= oneThird_height+divider_width; i++)
			{
				tempBitmap.setPixel(j, i, divider_color);
			}
			
			for (int i = twoThird_height+divider_width+1; i <= twoThird_height+2*divider_width; i++)
			{
				tempBitmap.setPixel(j, i, divider_color);
			}
		}
		
		Bitmap destBitmap = scaleBitmap(tempBitmap, width, height);
		tempBitmap.recycle();
		tempBitmap = null;
		
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
