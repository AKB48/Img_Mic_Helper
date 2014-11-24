package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.nfc.cardemulation.OffHostApduService;
import android.util.Log;


public class OilPaintingProcessor implements Processor {

	
	private volatile static OilPaintingProcessor uniqueInstance = null;
	private int operator_size = 5;
	private int[] threshold = {32, 64, 96, 128, 160, 192, 224, 256};
	
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
		int half_operator_size = operator_size/2;
		int[] R = new int[width * height];
		int[] G = new int[width * height];
		int[] B = new int[width * height];
		int[] r = new int[width * height];
		int[] g = new int[width * height];
		int[] b = new int[width * height];
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		
		for (int i = half_operator_size; i < height-half_operator_size; i++) 
		{
			for (int j = half_operator_size; j < width-half_operator_size; j++) 
			{
				int index = i*width+j;
				int oldPixel = bitmap.getPixel(j, i); 
			    R[index] = (oldPixel >> 16) & 0xff;
				G[index] = (oldPixel >> 8) & 0xff;
				B[index] = oldPixel & 0xff;

                r[index] = level(R[index]);
                g[index] = level(G[index]);
                b[index] = level(B[index]);
				
			}
		}
		
		int r_ave, g_ave, b_ave;
		for (int i = half_operator_size; i < height-half_operator_size; i++) 
		{
			for (int j = half_operator_size; j < width-half_operator_size; j++) 
			{
				r_ave = 0;
				g_ave = 0;
				b_ave = 0;
				int cur_index = i*width+j;
//				int loop_index = 0;
//				int rc,gc,bc;
//				rc = 0; gc = 0; bc = 0;
//				for (int k2 = i - half_operator_size; k2 <= i +half_operator_size; k2++)
//				{
//					for (int k1 = j - half_operator_size; k1 <= j+half_operator_size; k1++)
//					{
//						loop_index = k2*width+k1;
//						
//						if (r[cur_index] == r[loop_index])
//						{
//							r_ave += R[loop_index];
//							rc ++;
//						}
//						if (g[cur_index] == g[loop_index])
//						{
//							g_ave += G[loop_index];
//							gc ++;
//						}
//						if (b[cur_index] == b[loop_index])
//						{
//							b_ave += B[loop_index];
//							bc++;
//						}
//					}
//				}
//                r_ave = r_ave / rc;
//                g_ave = g_ave / gc;
//                b_ave = b_ave / bc;
//				newPixel = 0xff000000 | (r_ave << 16) | (g_ave << 8) | b_ave;
				newPixel = 0xff000000 | (r[cur_index] << 16) | (g[cur_index] << 8) | b[cur_index];
				tempBitmap.setPixel(j, i, newPixel);
				
			}
		}
		return tempBitmap;
	}
	
	
	private int level(int pixel)
	{
		for (int i = 0; i < 8; i++)
		{
			if (pixel < threshold[i])
				return threshold[i]-1;
		}
		return threshold[7]-1;
	}


}
