package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor makes the image looks like in mist.
 * @author William
 *
 */
public class MistProcessor implements Processor {

	
	private volatile static MistProcessor uniqueInstance = null;
	private int radius = 5;
	private int intensity = 7;
	
	
	private MistProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return MistProcessor the unique instance of this class
	 */
	public static MistProcessor getInstance(){
        if(uniqueInstance == null){
            synchronized(MistProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new MistProcessor();
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
		
		int[] intensity_count = new int[intensity+1];
		int[] r_ave = new int[intensity+1];
		int[] g_ave = new int[intensity+1];
		int[] b_ave = new int[intensity+1];
		
		int[][] rgb = new int[width*height][5];
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++) 
			{
				int oldPixel = bitmap.getPixel(j, i); 
				int index = i*width+j;
				rgb[index][0] = (oldPixel >> 24) & 0xff;
				rgb[index][1] = (oldPixel >> 16) & 0xff;
                rgb[index][2] = (oldPixel >> 8) & 0xff;
                rgb[index][3] = oldPixel & 0xff;
                rgb[index][4] = (int) (((rgb[index][1]*0.3 + rgb[index][2]*0.59 + rgb[index][3]*0.11)  * intensity) / 255.0);

			}
		}
		
		int newPixel = 0, r, g, b;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				for (int k = 0; k <= intensity; k++)
				{
					intensity_count[k] = 0;
					r_ave[k] = 0;
					g_ave[k] = 0;
					b_ave[k] = 0;
				}
				int index = i*width+j;
				for (int i_incre = -radius; i_incre <= radius; i_incre++)
				{
					int new_i = i + i_incre;
					if (new_i < 0 || new_i >= height)
					{
						continue;
					}
					for (int j_incre = -radius; j_incre <= radius; j_incre++)
					{
						int new_j = j + j_incre;
						if (new_j < 0 || new_j >= width)
						{
							continue;
						}
						int new_index = new_i*width+new_j;
						intensity_count[rgb[new_index][4]]++;
						r_ave[rgb[new_index][4]] += rgb[new_index][1];
						g_ave[rgb[new_index][4]] += rgb[new_index][2];
						b_ave[rgb[new_index][4]] += rgb[new_index][3];
						
					}
				}
				
				int max_count = 0;
				int max_index = 0;
				for (int k = 0; k < intensity_count.length; k++)
				{
					if (intensity_count[k] > max_count)
					{
						max_count = intensity_count[k];
						max_index = k;
					}
				}
				
				r = r_ave[max_index] / max_count;
				g = g_ave[max_index] / max_count;
				b = b_ave[max_index] / max_count;
				
				newPixel = (rgb[index][0] << 24) | (r << 16) | (g << 8) | b;
				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		
		return tempBitmap;
	}
	
}
