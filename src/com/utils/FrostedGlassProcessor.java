/**
 * 
 */
package com.utils;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * @author William
 *
 */
public class FrostedGlassProcessor implements Processor {

	
	private volatile static FrostedGlassProcessor uniqueInstance = null;  
	private int radius = 20;
	private float range = 0.2f;
    
    
    private FrostedGlassProcessor()
    {
        
    }
    
    
    /**
     * the public method to get the unique instance
     * @return FrostedGlassProcessor the unique instance of this class
     */
    public static FrostedGlassProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(FrostedGlassProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new FrostedGlassProcessor();
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
		

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] inPixels = new int[width * height];
        bitmap.getPixels(inPixels, 0, width, 0, 0, width, height);

        int wm = width - 1;
        int hm = height - 1;
        int area = width * height;
        int div = radius + radius + 1;

        int r[] = new int[area];
        int g[] = new int[area];
        int b[] = new int[area];
        int r_sum, g_sum, b_sum;
        int x, y, i, pixel, yp, yi, yw;
        int vmin[] = new int[Math.max(width, height)];

        int div_sum = (div + 1) >> 1;
        div_sum *= div_sum;
        int temp = 256 * div_sum;
        int dv[] = new int[temp];
        
        for (i = 0; i < temp; i++) 
        {
            dv[i] = (i / div_sum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int radius_1 = radius + 1;
        int rout_sum, gout_sum, bout_sum;
        int rin_sum, gin_sum, bin_sum;
        int lowerY = (int) (height * range);
        int upperY = (int) (height * (1 - range));

        for (y = 0; y < height; y++) 
        {
        	
            rin_sum = 0;
            gin_sum = 0;
            bin_sum = 0;
            
            rout_sum = 0;
            gout_sum = 0;
            bout_sum = 0;
            
            r_sum = 0;
            g_sum = 0;
            b_sum = 0;
            
            for (i = -radius; i <= radius; i++) 
            {
            	pixel = inPixels[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                
                sir[0] = (pixel >> 16) & 0xff;
                sir[1] = (pixel >> 8) & 0xff;
                sir[2] = pixel & 0xff;
                
                rbs = radius_1 - Math.abs(i);
                r_sum += sir[0] * rbs;
                g_sum += sir[1] * rbs;
                b_sum += sir[2] * rbs;
                
                if (i > 0) 
                {
                    rin_sum += sir[0];
                    gin_sum += sir[1];
                    bin_sum += sir[2];
                } 
                else 
                {
                    rout_sum += sir[0];
                    gout_sum += sir[1];
                    bout_sum += sir[2];
                }
            }
            
            stackpointer = radius;

            for (x = 0; x < width; x++) 
            {

                r[yi] = dv[r_sum];
                g[yi] = dv[g_sum];
                b[yi] = dv[b_sum];

                r_sum -= rout_sum;
                g_sum -= gout_sum;
                b_sum -= bout_sum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                rout_sum -= sir[0];
                gout_sum -= sir[1];
                bout_sum -= sir[2];

                if (y == 0) 
                {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                pixel = inPixels[yw + vmin[x]];

                sir[0] = (pixel >> 16) & 0xff;
                sir[1] = (pixel >> 8) & 0xff;
                sir[2] = pixel & 0xff;

                rin_sum += sir[0];
                gin_sum += sir[1];
                bin_sum += sir[2];

                r_sum += rin_sum;
                g_sum += gin_sum;
                b_sum += bin_sum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                rout_sum += sir[0];
                gout_sum += sir[1];
                bout_sum += sir[2];

                rin_sum -= sir[0];
                gin_sum -= sir[1];
                bin_sum -= sir[2];

                yi++;
                
            }
            
            yw += width;
            
        }
        
        
        for (x = 0; x < width; x++) 
        {
            rin_sum = 0;
            gin_sum = 0;
            bin_sum = 0;
            
            rout_sum = 0;
            gout_sum = 0;
            bout_sum = 0;
            
            r_sum = 0;
            g_sum = 0;
            b_sum = 0;
            
            yp = -radius * width;
            
            for (i = -radius; i <= radius; i++) 
            {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = radius_1 - Math.abs(i);

                r_sum += r[yi] * rbs;
                g_sum += g[yi] * rbs;
                b_sum += b[yi] * rbs;

                if (i > 0) 
                {
                    rin_sum += sir[0];
                    gin_sum += sir[1];
                    bin_sum += sir[2];
                } 
                else 
                {
                    rout_sum += sir[0];
                    gout_sum += sir[1];
                    bout_sum += sir[2];
                }

                if (i < hm) 
                {
                    yp += width;
                }
            }
            
            yi = x;
            stackpointer = radius;
            
            for (y = 0; y < height; y++) 
            {
                inPixels[yi] = (inPixels[yi] & 0xff000000) | (dv[r_sum] << 16) | (dv[g_sum] << 8) | dv[b_sum];

                r_sum -= rout_sum;
                g_sum -= gout_sum;
                b_sum -= bout_sum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                rout_sum -= sir[0];
                gout_sum -= sir[1];
                bout_sum -= sir[2];

                if (x == 0) 
                {
                    vmin[y] = Math.min(y + radius_1, hm) * width;
                }
                
                pixel = x + vmin[y];

                sir[0] = r[pixel];
                sir[1] = g[pixel];
                sir[2] = b[pixel];

                rin_sum += sir[0];
                gin_sum += sir[1];
                bin_sum += sir[2];

                r_sum += rin_sum;
                g_sum += gin_sum;
                b_sum += bin_sum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                rout_sum += sir[0];
                gout_sum += sir[1];
                bout_sum += sir[2];

                rin_sum -= sir[0];
                gin_sum -= sir[1];
                bin_sum -= sir[2];

                yi += width;
            }
        }

        Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        int[] outPixels = new int[area];
        bitmap.getPixels(outPixels, 0, width, 0, 0, width, height);
        
        int index = 0;
        for (y = lowerY; y <= upperY; y++)
        {
        	for (x = 0; x < width; x++)
        	{
        		index = y * width + x;
        		outPixels[index] = inPixels[index];
        	}
        }
        
        destBitmap.setPixels(outPixels, 0, width, 0, 0, width, height);
        
        return destBitmap;
        
	}
	

}
