package com.utils;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

/**
 * This is a singleton class.
 * The processor transforms an image into a style that looks like having been blown by wind.
 * @author William
 *
 */
public class WindProcessor implements Processor {

	
	private volatile static WindProcessor uniqueInstance = null;
	private int max_wind_intensity = 15;
	private int wind_intensity = 0;
	
	private WindProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return WindProcessor the unique instance of this class
	 */
	public static WindProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(WindProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new WindProcessor();
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
        int current_pixel = 0, new_pixel = 0;
        int[] pastR = new int[max_wind_intensity];
        int[] pastG = new int[max_wind_intensity];
        int[] pastB = new int[max_wind_intensity];
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		for (int i = 0; i < height; i++) 
		{
			wind_intensity = (int)(Math.random()*max_wind_intensity) + 1;

			for (int k = 0; k < wind_intensity; k++)
			{
				pastR[k] = 0;
				pastG[k] = 0;
				pastB[k] = 0;
			}
			for (int j = 0; j < width; j++) 
			{
				int index = j % wind_intensity;
				current_pixel = bitmap.getPixel(j, i);
				pastR[index] = (current_pixel >> 16) & 0xff;
				pastG[index] = (current_pixel >> 8) & 0xff;
				pastB[index] = current_pixel & 0xff;
				new_pixel = current_pixel & 0xff000000;
				
				int temp_pixel = 0;
				for (int k = 0; k <wind_intensity; k++) {
					temp_pixel += pastR[k];
				}
				new_pixel = new_pixel | ( (temp_pixel / wind_intensity) << 16);
				
				temp_pixel = 0;
				for (int k = 0; k <wind_intensity; k++) {
					temp_pixel += pastG[k];
				}
				new_pixel = new_pixel | ( (temp_pixel / wind_intensity) << 8);
				
				temp_pixel = 0;
				for (int k = 0; k <wind_intensity; k++) {
					temp_pixel += pastB[k];
				}
				new_pixel = new_pixel | (temp_pixel / wind_intensity);
				
				tempBitmap.setPixel(j, i, new_pixel);
				
			}
		}
		
		return tempBitmap;
	}

}
