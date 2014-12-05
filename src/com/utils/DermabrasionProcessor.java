package com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor makes the human skin looks more smooth,
 * so that makes people in the image more beautiful.
 * The processor actually is a bilateral low pass filtering.
 * The processor can not only remove the noise, but also keep the edges.
 * The processor here is Gaussian.
 * @author William
 *
 */
public class DermabrasionProcessor implements Processor {

	
	private volatile static DermabrasionProcessor uniqueInstance = null;
	private double factor = -0.5;  
    private double distance_sigma = 3.0;  
    private double range_sigma = 30.0;
    private int radius = 5;
    // space distance weight table.
    private double[][] dWeightTable = null;  
    // pixel similarity weight table.
    private double[] sWeightTable = null;  
    
	
	private DermabrasionProcessor()
	{
		
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return DermabrasionProcessor the unique instance of this class
	 */
	public static DermabrasionProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(DermabrasionProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new DermabrasionProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Set distance sigma, which is used to calculate the space distance gaussian weight table. 
	 * @param distance_sigma the param to set.
	 * @return true if the param is succussfully set.
	 */
	public boolean setDistanceSigma(double distance_sigma)
	{
		this.distance_sigma = distance_sigma;
		return true;
	}
	
	
	/**
	 * Get the parameter distance sigma.
	 * @return the value of distance sigma.
	 */
	public double getDistanceSigma()
	{
		return this.distance_sigma;
	}
	
	
	/**
	 * Set range sigma, which is used to calculate the pixel similarity gaussian weight table.
	 * @param range_sigma the param to set.
	 * @return true if the param is successfully set.
	 */
	public boolean setRangeSigma(double range_sigma)
	{
		this.range_sigma = range_sigma;
		return true;
	}
	
	
	/**
	 * Get the parameter range sigma.
	 * @return the value of range sigma.
	 */
	public double getRangeSigma()
	{
		return this.range_sigma;
	}
	
	
	/**
	 * Set the radius, which represents the radius of the bilateral low pass filter.
	 * @param radius the param to set.
	 * @return true if the param is successfully set.
	 *            false if the param is less than zero.
	 */
	public boolean setRadius(int radius)
	{
		if (radius < 0)
		{
			return false;
		}
		else
		{
			this.radius = radius;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter radius.
	 * @return the value of radius.
	 */
	public int getRadius()
	{
		return this.radius;
	}
	
	
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if (bitmap == null)
		{
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		generateDistanceWeightTable();
		generateSimilarityWeightTable();
		
		// record the pixels of original image
		int[] inPixels = new int[width*height]; 
		// record the pixels of image after processing.
        int[] outPixels = new int[width*height];  
        
        bitmap.getPixels(inPixels, 0, width, 0, 0, width, height);
        
        
        double r_sum = 0, g_sum = 0, b_sum = 0;  
        double rWeight = 0, gWeight = 0, bWeight = 0;  
        double rSumWeight = 0, gSumWeight = 0, bSumWeight = 0;  
        int index = 0; 
        int a, r, g, b;
        
        
        for(int i = 0; i < height; i++) 
        {  
 
            for(int j = 0; j < width; j++) 
            {  
            	
                index = i * width + j;  
                
                a = (inPixels[index] >> 24) & 0xff;  
                r = (inPixels[index] >> 16) & 0xff;  
                g = (inPixels[index] >> 8) & 0xff;  
                b = inPixels[index] & 0xff; 
                
                
                int new_i = 0, new_j = 0;  
                int temp_index = 0;  
                int temp_a = 0, temp_r = 0, temp_g = 0, temp_b = 0;  
                
                for(int k1 = -radius; k1 <= radius; k1++) 
                {  
                	
                    for(int k2 = - radius; k2 <= radius; k2++) 
                    {  
                    	
                        if((i + k1) >= 0 && (i + k1) < height) 
                        {  
                            new_i = i + k1;  
                        } 
                        else 
                        {  
                            new_i = 0;  
                        }  
                          
                        if((k2 + j) >= 0 && (k2 + j) < width)
                        {  
                            new_j = j + k2;  
                        }
                        else
                        {  
                            new_j = 0;  
                        }  
                        
                        temp_index = new_i * width + new_j;  
                        temp_a = (inPixels[temp_index] >> 24) & 0xff;  
                        temp_r = (inPixels[temp_index] >> 16) & 0xff;  
                        temp_g = (inPixels[temp_index] >> 8) & 0xff;  
                        temp_b = inPixels[temp_index] & 0xff;  
                          
                        rWeight = dWeightTable[k1+radius][k2+radius]  * sWeightTable[(Math.abs(temp_r - r))];  
                        gWeight = dWeightTable[k1+radius][k2+radius]  * sWeightTable[(Math.abs(temp_g - g))];  
                        bWeight = dWeightTable[k1+radius][k2+radius]  * sWeightTable[(Math.abs(temp_b - b))];  
                          
                        rSumWeight += rWeight;  
                        gSumWeight += gWeight;  
                        bSumWeight += bWeight;  
                        
                        r_sum += (rWeight * (double)temp_r);  
                        g_sum += (gWeight * (double)temp_g);  
                        b_sum += (bWeight * (double)temp_b);  
                        
                    }  
                }  
                  
                r = (int)Math.floor(r_sum / rSumWeight);  
                g = (int)Math.floor(g_sum / gSumWeight);  
                b = (int)Math.floor(b_sum / bSumWeight);  
                outPixels[index] = (a << 24) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);  
                  
                r_sum = 0;
                g_sum = 0;
                b_sum = 0;  
                rWeight = 0;
                gWeight = 0;
                bWeight = 0;  
                rSumWeight = 0;
                gSumWeight = 0;
                bSumWeight = 0;  
                  
            }  
        }  
        
        Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        destBitmap.setPixels(outPixels, 0, width, 0, 0, width, height);
             
        return destBitmap; 
        
	}
	
	
	/**
	 * Generate the distance weight table.
	 * The table records the impact degree of different space distance between two pixels in an image. 
	 */
	private void generateDistanceWeightTable() {  
		
        int size = 2 * radius + 1;  
        dWeightTable = new double[size][size];  
        
        for(int i = -radius; i <= radius; i++) 
        {  
        	
            for(int j = - radius; j <= radius; j++) 
            {  
                double delta = Math.sqrt(i * i + j * j) / distance_sigma;  
                double delta2 = delta * delta;  
                dWeightTable[i+radius][j+radius] = Math.exp(delta2 * factor);  
                
            }  
        }  
           
    }  
	
	
	/**
	 * Generate the pixel similarity weight table.
	 * The table records the impact degree of the similarity of two pixels.
	 * The similarity of two pixels is the absolute difference of their rgb components respectively.
	 */
	 private void generateSimilarityWeightTable() {  
		 
	        sWeightTable = new double[256]; 
	        
	        for(int i=0; i<256; i++) 
	        {       	
	            double delta = Math.sqrt(i * i ) / range_sigma;  
	            double delta2 = delta * delta;  
	            sWeightTable[i] = Math.exp(delta2 * factor);  
	        }  
	 }
	 
	 
	 /**
	* Clamp a value to the range [0, 255]
	* @param value the param to set.
	* @return the value of the param after clamp.
	*/
	private int clamp(int value) {
		if (value < 0)
		{
			return 0;
		}
		else if (value > 255)
		{
			return 255;
		}
		
		return value;
			
	}
	

}
