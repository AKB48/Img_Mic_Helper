package com.utils;
import android.app.ActionBar.Tab;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Camera.Size;
import android.util.Log;


/**
 * This is a singleton class.
 * The processor transforms an image into a blur style.
 * The blur operator is gaussian.
 * @author William
 *
 */
public class GaussianBlurProcessor implements Processor {

	
	private volatile static GaussianBlurProcessor uniqueInstance = null;
	private int raduis = 5;
	private double sigma = 10.0;
	private double[] gaussianWeightMatrix;
	
	private GaussianBlurProcessor(){
	}
	
	/**
	 * the public method to get the unique instance
	 * @return GaussianBlurProcessor the unique instance of this class
	 */
	public static GaussianBlurProcessor getInstance() {
        if(uniqueInstance == null){
            synchronized(GaussianBlurProcessor.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new GaussianBlurProcessor();
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
		generateGaussianWeightMatrix();
		int[] source = new int[width*height];
		int[] dest = new int[width*height];
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		bitmap.getPixels(source, 0, width, 0, 0, width, height);
		blur(source, dest, width, height);
		blur(dest, source, height, width);
		
		tempBitmap.setPixels(source, 0, width, 0, 0, width, height);
		
		return  tempBitmap;
			
	}
	
	
	private void blur(int[] source, int[] dest, int width, int height)
	{
		int index = 0;
		int new_index = 0, new_width = 0;
		double r_sum = 0, g_sum = 0, b_sum = 0;
		int alpha = 0xff000000;
		
		for (int i = 0; i < height; i++)
		{
			index = i;
			for (int j = 0; j < width; j++)
			{
				r_sum = 0;
				g_sum = 0;
				b_sum = 0;
				for (int k = -raduis; k <= raduis; k++)
				{
					new_width = j + k;
					if (new_width < 0)
					{
						new_width = 0;
					}
					if (new_width >= width)
					{
						new_width = width - 1;
					}
					new_index = i * width + new_width;
					alpha = (source[new_index] >> 24) & 0xff;
					r_sum += (double)((source[new_index] >> 16) & 0xff) * gaussianWeightMatrix[k+raduis];
					g_sum += (double)((source[new_index] >> 8) & 0xff) * gaussianWeightMatrix[k+raduis];
					b_sum += (double)(source[new_index] & 0xff) * gaussianWeightMatrix[k+raduis];
				}
				
				r_sum = (r_sum < 0 ? 0 : (r_sum > 255 ? 255 : r_sum));
				g_sum = (g_sum < 0 ? 0 : (g_sum > 255 ? 255 : g_sum));
				b_sum = (b_sum < 0 ? 0 : (b_sum > 255 ? 255 : b_sum));
				
				dest[index] = (alpha << 24) | ((int)r_sum << 16) | ((int)g_sum << 8) | (int)b_sum;
				index += height;
				
			}
			
		}
		
		
	}
	
	
	private void generateGaussianWeightMatrix()
	{
		double _2sigma2 = 2 * sigma * sigma;
		double _2pi = 2 * Math.PI;
		double sqrt2piSigma = Math.sqrt(_2pi) * sigma;
		int operator_size = 2 * raduis + 1;
		gaussianWeightMatrix = new double[operator_size];
		int index = 0;
		double sum = 0.0;
		
		for (int i = -raduis; i <= raduis; i++)
		{
			double distance = i * i;
			gaussianWeightMatrix[index] = Math.exp( (-distance) / _2sigma2) / sqrt2piSigma;
			sum += gaussianWeightMatrix[index];
			index++;
		}
		
		for (int i = 0; i <gaussianWeightMatrix.length; i++)
		{
			gaussianWeightMatrix[i] /= sum;
			
		}
		
	}
	

}
