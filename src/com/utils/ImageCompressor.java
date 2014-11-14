package com.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This is a singleton class.
 * It is used to compress an image.
 * @author Willam
 *
 */
public class ImageCompressor extends Processor{
	
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
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while ( baos.toByteArray().length / 1000>25 && options>10) {   
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

}
