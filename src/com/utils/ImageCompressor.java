package com.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * This is a singleton class.
 * It is used to compress an image.
 * @author William
 *
 */
public class ImageCompressor implements Processor{
	
	private volatile static ImageCompressor uniqueInstance = null;
	
	private ImageCompressor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ImageCompressor the unique instance of this class
	 */
	public static ImageCompressor getInstance(){
        if(uniqueInstance == null){
            synchronized(ImageCompressor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new ImageCompressor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	

	@Override
	public Bitmap process(Bitmap bitmap) {
		if (bitmap == null)
			return null;
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int new_width = width;
		int new_height = height;
		
		if (width > height && width > 800) 
		{
		    double ratio = width / 800.0d;
		    new_width = 800;
		    new_height = (int) (height / ratio);
		}
		else if (height >= width && height > 800)
		{
		    double ratio = height / 800.0d;
		    new_height = 800;
		    new_width = (int) (width / ratio);
		}
		
		if (new_width != width || new_height != height)
		{
		    Bitmap tempBitmap = scaleBitmap(bitmap, new_width, new_height);
		    bitmap.recycle();
		    bitmap = tempBitmap;
		    tempBitmap = null;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while ( baos.toByteArray().length / 1000 > 100 && options > 10) {   
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }  
        
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        if (bitmap != null)
        	bitmap.recycle();
        bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
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
