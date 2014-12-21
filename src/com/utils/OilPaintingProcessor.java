package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.nfc.cardemulation.OffHostApduService;
import android.util.Log;


/**
 * This is a singleton class.
 * The proceesor transforms an image into an oilpainting style.
 * @author William
 *
 */
public class OilPaintingProcessor implements Processor {

	
	private volatile static OilPaintingProcessor uniqueInstance = null;
	private int radius = 7;
	private int distance = 35;
	
	private OilPaintingProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return OilPaintingProcessor the unique instance of this class
	 */
	public static OilPaintingProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(OilPaintingProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new OilPaintingProcessor();
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
		int newPixel = 0;
		// store the pixels using yiq channels
		double[][] yiq = new double[width*height][3];
		// store the pixels using rgb channels
		int[][] rgb = new int[width*height][4];
		
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int index = i*width+j;
				int oldPixel = bitmap.getPixel(j, i);
				// achieve the rgb channels
				rgb[index][0] = (oldPixel >> 24) & 0xff;
				rgb[index][1] = (oldPixel >> 16) & 0xff;
				rgb[index][2] = (oldPixel >> 8) & 0xff;
				rgb[index][3] = oldPixel & 0xff;
				
				// RGB to YIQ
				// Y channel
				yiq[index][0] = 0.299f  * rgb[index][1] + 0.587f * rgb[index][2] + 0.114f  * rgb[index][3]; 
				// I channel
				yiq[index][1] = 0.5957f * rgb[index][1] - 0.2744f * rgb[index][2] - 0.3212f * rgb[index][3]; 
				//Q channel
				yiq[index][2] = 0.2114f * rgb[index][1] - 0.5226f * rgb[index][2] + 0.3111f * rgb[index][3]; 
			}
		}

		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		
		int operator_size = radius * radius;     // the range to process
		int dist_square = distance * distance;  
		int shift = 0;
		int loop_times = 0;
		
		for (int i = 0; i < height; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				int index = i*width+j;
				int cur_i, cur_j, old_i, old_j;
				double cur_Y, cur_I, cur_Q;
				double old_Y, old_I, old_Q;
				cur_i = i;
				cur_j = j;
				cur_Y = yiq[index][0];
				cur_I = yiq[index][1];
				cur_Q = yiq[index][2];
				loop_times = 0;
				
				do {
					old_i = cur_i;
					old_j = cur_j;
					old_Y = cur_Y;
					old_I = cur_I;
					old_Q = cur_Q;
					
					double sum_i = 0.0d;
					double sum_j = 0.0d;
					double sum_Y = 0.0d;
					double sum_I = 0.0d;
					double sum_Q = 0.0d;
					int counter = 0;
					
					for (int i_incre = -radius; i_incre <= radius; i_incre++)
					{
						int new_i = cur_i + i_incre;
						if (new_i < 0 || new_i >= height)
						{
							continue;
						}
						for (int j_incre = -radius; j_incre <= radius; j_incre++)
						{
							int new_j = cur_j + j_incre;
							if (new_j < 0 || new_j >= width)
							{
								continue;
							}
							if (i_incre*i_incre + j_incre*j_incre < operator_size)
							{
								int new_index = new_i*width+new_j;
								double new_Y = yiq[new_index][0];
								double new_I = yiq[new_index][1];
								double new_Q = yiq[new_index][2];
								
								double dY = cur_Y - new_Y;
								double dI = cur_I - new_I;
								double dQ = cur_Q - new_Q;
								
								if (dY*dY+dI*dI+dQ*dQ <= dist_square)
								{
									sum_i += new_i;
									sum_j += new_j;
									sum_Y += new_Y;
									sum_I += new_I;
									sum_Q += new_Q;
									counter++;
								}
							}
							
						}
						
					}
					
					cur_Y = sum_Y / counter;
					cur_I = sum_I / counter;
					cur_Q = sum_Q / counter;
					Log.i("", "");			
					cur_i = (int) (sum_i / counter + 0.5);
					cur_j = (int) (sum_j / counter + 0.5);
					
					int di = cur_i - old_i;
					int dj = cur_j - old_j;
					double dY = cur_Y - old_Y;
					double dI = cur_I - old_I;
					double dQ = cur_Q - old_Q;
					
					shift = (int) (di*di + dj*dj + dY*dY + dI*dI + dQ*dQ);
					loop_times++;
					
				} while (shift > 1 && loop_times < 150);
				
				// YIQ to RGB
				rgb[index][1] = (int)(cur_Y + 0.9563f * cur_I + 0.6210f * cur_Q);  
				rgb[index][2] = (int)(cur_Y - 0.2721f * cur_I - 0.6473f * cur_Q);  
				rgb[index][3] = (int)(cur_Y - 1.1070f * cur_I + 1.7046f * cur_Q);
				
				
			    newPixel = (rgb[index][0] << 24) | (rgb[index][1] << 16) | (rgb[index][2] << 8) | rgb[index][3];
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}
		
		return tempBitmap;
	}
	


}
