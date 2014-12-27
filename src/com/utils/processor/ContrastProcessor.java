package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor makes an image have a high contrast.
 * A very bright image will become not so bright,
 * and a very dark image will become not so dark.
 * The processor aims at making these images look more comfortable.
 * @author William
 *
 */
public class ContrastProcessor implements Processor {

	
	private volatile static ContrastProcessor uniqueInstance = null;
	
	
	private ContrastProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return ContrastProcessor the unique instance of this class
	 */
	public static ContrastProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(ContrastProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new ContrastProcessor();
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
		double[][] HSI = new double[width*height][3];
		
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
				HSI[index] = rgb2hsi(oldPixel);
				hist[(int) (HSI[index][2] * 255)]++;
				
			}
		}
		
		int image_size = width * height;
		// accumulation
		palette[0] = (double)hist[0] / image_size;
		
		for (int k = 1; k < 256; k++)
		{
			hist[k] = hist[k - 1] + hist[k];
			// generate the palette according to the hist of the image.
			palette[k] = (double)hist[k] / image_size;
		}
		
		
		// modify the pixels in the image according to the generated palette.
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				index = i * width + j;
				HSI[index][2] = palette[(int) (HSI[index][2] * 255)];
				// convert pixel from hsi space to rgb space
				newPixel = hsi2rgb(HSI[index]);

				tempBitmap.setPixel(j, i, newPixel);
			}
		}
		
		
		return tempBitmap;
	}
	
	
	
	/**
	 * Change a pixel from rgb space to hsi space
	 * hsi space is also a color space just like rgb space
	 * hsi space contains also three components.
	 * They are H, S, and I.
	 * H represents to hue, decides the basis of a color, such as red color, blue color, green color...
	 * S represents to saturation, decides the saturation of a color. The larger value of saturation,
	 * the purer the color displays.
	 * I represents to intensity, decides how bright a pixel will display.
	 * @param pixel the original pixel for conversion, the pixel must be in rgb space.
	 * @return the converted pixel in hsi space, a double type array with size of 3, 
	 *            the h, s, i component respectively. 
	 *            Each of them is in range [0, 1].
	 */
	private double[] rgb2hsi(int pixel) 
	{
		
		double[] hsi = new double[3];
		
		int original_r = (pixel >> 16) & 0xff;
		int original_g = (pixel >> 8) & 0xff;
		int original_b = pixel & 0xff;
	
		// normalize the three channels 
		// [0, 255] -> [0, 1]
		double r = (double) original_r / 255.0;
		double g = (double) original_g / 255.0;
		double b = (double) original_b / 255.0;
		
		double temp1 = 0.5 * ((r - g) + (r - b));
		double temp2 = Math.sqrt((r - g) * (r - g) + (r - b) * (g - b));
		
		
		// calculate the H component
		if( temp2 == 0 ) 
		{
			hsi[0] = 0;
		}
		else
		{
			double theta = Math.acos(temp1 / temp2);
			
			if(b <= g) 
			{
				hsi[0] = theta / (2 * Math.PI);
			}
			else 
			{
				hsi[0] = ((2 * Math.PI) - theta) / (2 * Math.PI);
			}
		}
		
		// calculate the S component
		if( r + g + b == 0 ) 
		{
			hsi[1] = 0;
		}
		else 
		{
			hsi[1] = 1 - (3 / (r + g + b)) * Math.min(Math.min(r, g), b);
		}
		
		// calculate the I component
		hsi[2] = (r + g + b) / 3;
		
		return hsi;
	}
	
	
	/**
	 * Change a pixel from hsi space to rgb space.
	 * The value of each component of rgb space after conversion is in range [0, 255]
	 * @param hsi a double type array with size of 3. It represents the pixel to be converted.
	 * @return an int type number, the result pixel in rgb space that after conversion.
	 */
	private int hsi2rgb(double[] hsi) 
	{
		double H = hsi[0];
		double S = hsi[1];
		double I = hsi[2];
		int r, g, b;
		double temp_r, temp_g, temp_b;
		
		
		// convert HSI to RGB
		H *= (2 * Math.PI);
		if( H >= 0 && H < (2 * Math.PI/3) ) 
		{
			temp_b = I * (1 - S);
			temp_r = I * (1 + S * Math.cos(H) / Math.cos((Math.PI / 3) - H));
			temp_g = 3 * I - (temp_r + temp_b);
		}
		else if( H >= (2 * Math.PI / 3) && H < (4 * Math.PI / 3)) 
		{
			H -= (2 * Math.PI / 3);
			temp_r = I * (1 - S);
			temp_g = I * (1 + S * Math.cos(H) / Math.cos((Math.PI / 3) - H));
			temp_b = 3 * I - (temp_r + temp_g);
		}
		else 
		{
			H -= (4 * Math.PI / 3);
			temp_g = I * (1 - S);
			temp_b = I * (1 + S * Math.cos(H) / Math.cos((Math.PI / 3) - H));
			temp_r = 3 * I - (temp_g + temp_b);
		}
		
		
		// reflect values of rgb from [0, 1] to [0, 255]
		r = (int) (temp_r * 255);
		g = (int) (temp_g * 255);
		b = (int) (temp_b * 255);
	
		
		// check bounds
		r = r > 255 ? 255 : r;
		r = r < 0 ? 0 : r;
		g = g > 255 ? 255 : g;
		g = g < 0 ? 0 : g;
		b = b > 255 ? 255 : b;
		b = b < 0 ? 0 : b;
		
		
		// generate result pixel
		int pixel = 0xff000000 | (r << 16) | (g << 8) | b;
		
		return pixel;
	}
	

}
