package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor makes an image have a high contrast.
 * This is another version of contrast processor.
 * It processes images in yiq space.
 * @author William
 *
 */
public class Contrast2Processor implements Processor {

	
private volatile static Contrast2Processor uniqueInstance = null;
	
	
	private Contrast2Processor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return Contrast2Processor the unique instance of this class
	 */
	public static Contrast2Processor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(Contrast2Processor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new Contrast2Processor();
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
		int newPixel = 0;
		double[][] YIQ = new double[width*height][3];
		
		// the histogram of an image
		// store the number of each gray level pixel that appears in an image
		int[] hist =  new int[256];
		// initialize the hist array
		for (int k = 0; k < 256; k++)
		{
			hist[k] = 0;
		}
		// store the mapping relation of gray level pixels after histogram equalization
		double[] palette = new double[256];
		
		Bitmap tempBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		
		int index = 0;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int oldPixel = bitmap.getPixel(j, i);
				index = i * width + j;
				YIQ[index] = rgb2yiq(oldPixel);
				hist[(int) YIQ[index][0]]++;
				
			}
		}
		
		int image_size = width * height;
		// accumulation
		palette[0] = (double)hist[0] / image_size * 255;
		for (int k = 1; k < 256; k++)
		{
			hist[k] = hist[k - 1] + hist[k];
			// generate the palette according to the hist of the image.
			palette[k] = (double)hist[k] / image_size * 255;
		}
		
		
		// modify the pixels in the image according to the generated palette.
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				index = i * width + j;
				YIQ[index][0] = palette[(int) YIQ[index][0]];
				// convert pixel from yiq space to rgb space
				newPixel = yiq2rgb(YIQ[index]);

				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		
		
		return tempBitmap;
	}
	
	
	
	/**
	 * Change a pixel from rgb space to yiq space.
	 * yiq space is also a color space just like rgb space.
	 * yiq space generally is used in televison system in North America.
	 * yiq space contains also three components.
	 * They are Y, I, and Q.
	 * Y represents the intensity of an image.
	 * I represents the change from orange to cyan.
	 * Q represents the change from purple to green.
	 * @param pixel the original pixel for conversion, the pixel must be in rgb space.
	 * @return the converted pixel in yiq space, a double type array with size of 3, 
	 *            the y, i, q component respectively. 
	 */
	private double[] rgb2yiq(int pixel)
	{
		double[] yiq = new double[3];
		
		int r = (pixel >> 16) & 0xff;
		int g = (pixel >> 8) & 0xff;
		int b = pixel & 0xff;
		
		// Y channel
		yiq[0] = 0.299f  * r + 0.587f * g + 0.114f  * b; 
		// I channel
		yiq[1] = 0.5957f * r - 0.2744f * g - 0.3212f * b; 
		//Q channel
		yiq[2] = 0.2114f * r - 0.5226f * g + 0.3111f * b;
		
		return yiq;
	}
	
	
	/**
	 * Change a pixel from yiq space to rgb space.
	 * The value of each component of rgb space after conversion is in range [0, 255]
	 * @param yiq a double type array with size of 3. It represents the pixel to be converted.
	 * @return an int type number, the result pixel in rgb space that after conversion.
	 */
	private int yiq2rgb(double[] yiq)
	{
		int r = (int)(yiq[0] + 0.9563f * yiq[1] + 0.6210f * yiq[2]);  
		int g = (int)(yiq[0] - 0.2721f * yiq[1] - 0.6473f * yiq[2]);  
		int b = (int)(yiq[0] - 1.1070f * yiq[1] + 1.7046f * yiq[2]);
		
		// check bounds
		r = r > 255 ? 255 : r;
		r = r < 0 ? 0 : r;
		g = g > 255 ? 255 : g;
		g = g < 0 ? 0 : g;
		b = b > 255 ? 255 : b;
		b = b < 0 ? 0 : b;
		
		int pixel = 0xff000000 | (r << 16) | (g << 8) | b;
		
		return pixel;
	}
	

}
