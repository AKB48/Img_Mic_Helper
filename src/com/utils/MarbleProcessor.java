package com.utils;

import java.util.Random;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor adds a marbling effect on the original image. 
 * @author William
 *
 */
public class MarbleProcessor implements Processor {

	
	private volatile static MarbleProcessor uniqueInstance = null;
	private float[] sinTable = null;
	private float[] cosTable = null;
	private float xScale = 10.0f;
	private float yScale = 10.0f;
	private float amount = 1.0f;
	private float turbulence = 1.0f;
	private Random randomGenerator = null;
	
	
	private int B = 0x100;
	private int BM = 0xff;
	private int N = 0x1000;
	private int[] p = null;
	private float[][] g2 = null;
	
	
	/**
	 * The constructor.
	 * Initialize the random number generator.
	 * Generate the sin table and cos table.
	 * Generate the noise arrays.
	 */
	private MarbleProcessor()
	{
		randomGenerator = new Random();
		generateParamTable();
		generateNoiseArray();
	}
	
	
	/**
	 * the public method to get the unique instance
	 * @return MarbleProcessor the unique instance of this class
	 */
	public static MarbleProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(MarbleProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new MarbleProcessor();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Set xScale, which represents the degree of marbling on the horizontal direction.
	 * @param xScale the value of xScale.
	 * @return true if xScale is successfully set.
	 *            false if xScale is less than zero.
	 */
	public boolean setxScale(float xScale)
	{
		if (xScale < 0)
		{
			return false;
		}
		else
		{
			this.xScale = xScale;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter xScale.
	 * @return the value of xScale.
	 */
	public float getxScale()
	{
		return this.xScale;
	}
	
	
	/**
	 * Set yScale, which represents the degree of marbling on the vertical direction.
	 * @param yScale the value of yScale.
	 * @return true if yScale is successfully set.
	 *            false if yScale is less than zero.
	 */
	public boolean setyScale(float yScale)
	{
		if (yScale < 0)
		{
			return false;
		}
		else
		{
			this.yScale = yScale;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter yScale.
	 * @return the value of yScale.
	 */
	public float getyScale()
	{
		return this.yScale;
	}
	
	
	/**
	 * Set amount, which represents the amount of marbling.
	 * @param amount the param to set.
	 * @return true if the param is successfully set.
	 */
	public boolean setAmount(float amount)
	{
		this.amount = amount;
		return true;
	}
	
	
	/**
	 * Get the parameter amount.
	 * @return the value of amount.
	 */
	public float getAmount()
	{
		return this.amount;
	}
	
	
	/**
	 * Set turbulence, which represents how turbulent the effect is.
	 * The larger turbulence is, the more small grains are in the image.
	 * @param turbulence the param to set.
	 * @return true if the param is successfully set.
	 * 			  false if turbulence is less than zero.
	 */
	public boolean setTurbulence(float turbulence)
	{
		if (turbulence < 0)
		{
			return false;
		}
		else
		{
			this.turbulence = turbulence;
		}
		
		return true;
	}
	
	
	/**
	 * Get the parameter turbulence.
	 * @return the value of turbulence.
	 */
	public float getTurbulence()
	{
		return this.turbulence;
	}
	
	
	@Override
	public Bitmap process(Bitmap bitmap) {
		
		if (bitmap == null)
		{
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
	    
		// record the pixels of original image
	    int[] inPixels = new int[width*height];
	    // record the pixels of image after processing.
        int[] outPixels = new int[width*height];  
        
        
        bitmap.getPixels(inPixels, 0, width, 0, 0, width, height);
        
        int index = 0;  
        // record the position of a pixel after ripple.
        float[] out = new float[2];  
        
        for(int i=0; i<height; i++) 
        {  
            for(int j=0; j<width; j++) 
            {  
                index = i * width + j;  
                    
                generateMarbleTexture(j, i, out);
                int srcX = (int)Math.floor( out[0] );  
                int srcY = (int)Math.floor( out[1] );  
                float xWeight = out[0] - srcX;  
                float yWeight = out[1] - srcY;  
                int nw, ne, sw, se;  
                  
                
                if ( srcX >= 0 && srcX < width-1 && srcY >= 0 && srcY < height-1) 
                {  
                    int new_index = width*srcY + srcX;  
                    nw = inPixels[new_index];  
                    ne = inPixels[new_index+1];  
                    sw = inPixels[new_index+width];  
                    se = inPixels[new_index+width+1];  
                } 
                else
                {  
                    nw = pixelChoose( inPixels, srcX, srcY, width, height );  
                    ne = pixelChoose( inPixels, srcX+1, srcY, width, height );  
                    sw = pixelChoose( inPixels, srcX, srcY+1, width, height );  
                    se = pixelChoose( inPixels, srcX+1, srcY+1, width, height );  
                }  
                  
                
                outPixels[index] = bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);  
            }  
        }  
  
        Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        destBitmap.setPixels(outPixels, 0, width, 0, 0, width, height);
        
        
        return destBitmap;
	}
	
	
	/**
	 * Generate the arrays about the generation of Perlin noise.
	 */
	private void generateNoiseArray()
	{
		float temp_float;
		int temp_int;
		int temp_index;
		
		p = new int[B + B + 2];
		g2 = new float[B + B + 2][2];
		
		for (int i = 0; i < B; i++) 
		{
			p[i] = i;

			for (int j = 0; j < 2; j++)
			{
				g2[i][j] = (float)((random() % (B + B)) - B) / B;
			}
			
			temp_float = (float)Math.sqrt(g2[i][0] * g2[i][0] + g2[i][1] * g2[i][1]);
			g2[i][0] = g2[i][0] / temp_float;
			g2[i][1] = g2[i][1] / temp_float;
			
		}

		for (int i = B-1; i >= 0; i--)
		{
			temp_int = p[i];
			p[i] = p[temp_index = random() % B];
			p[temp_index] = temp_int;
		}

		for (int i = 0; i < B + 2; i++) 
		{
			p[B + i] = p[i];
			
			for (int j = 0; j < 2; j++)
			{
				g2[B + i][j] = g2[i][j];
			}
			
		}
		
	}
	
	
	/**
	 * Generate the parameter tables.
	 * The parameter tables including:
	 * 1) sin table
	 * 2) cos table
	 * which is used to generate marble effect.
	 */
	private void generateParamTable() 
	{
		sinTable = new float[256];
		cosTable = new float[256];
		
		for (int i = 0; i < 256; i++) 
		{
			float angle = (float) (2 * Math.PI * i / 256.0f * turbulence);
			sinTable[i] = (float)(-yScale * Math.sin(angle));
			cosTable[i] = (float)(yScale * Math.cos(angle));
		}
		
	}

	
	/**
	 * Choose the correct pixel in correct position in a specific strategy.
	 * @param pixels the original pixels array that records all the pixels of the original image. 
	 * @param x the position of horizontal direction to check.
	 * @param y the position of vertical direction to check.
	 * @param width the width of the original image.
	 * @param height the height of the original image.
	 * @return the chosen pixel.
	 */
    private int pixelChoose(int[] pixels, int x, int y, int width, int height) {  
        int new_x = x, new_y = y;
        if (x < 0)
        {
        	new_x = 0;
        }
        if (x > width-1)
        {
        	new_x = width-1;
        }
        if (y < 0)
        {
        	new_y = 0;
        }
        if (y > height-1)
        {
        	new_y = height-1;
        }
        return pixels[new_y*width+new_x ];  
    }  
  
    
    /**
     * Generate the marble effect.
     * @param x the original position of horizontal direction.
     * @param y the original position of vertical direction.
     * @param out the array that stores the new position of both horizontal and vertical direction
     *                 that after marbling and turbulence.
     */
    private void generateMarbleTexture(int x, int y, float[] out) {  
    	
    	int displacement = clamp((int)(127 * (1 + noise2(x / xScale, y / xScale))));
		out[0] = x + sinTable[displacement] * amount;
		out[1] = y + cosTable[displacement] * amount;  
        
    }  
    
    
    /**
	 * Bilinear interpolation of ARGB values.
	 * @param x the X interpolation parameter in range of [0, 1]
	 * @param y the y interpolation parameter in range of [0, 1]
	 * @param rgb array of four ARGB values in the order NW, NE, SW, SE
	 * @return the interpolated value
	 */
	private int bilinearInterpolate(float x, float y, int nw, int ne, int sw, int se) {
		float m0, m1;
		int a0 = (nw >> 24) & 0xff;
		int r0 = (nw >> 16) & 0xff;
		int g0 = (nw >> 8) & 0xff;
		int b0 = nw & 0xff;
		int a1 = (ne >> 24) & 0xff;
		int r1 = (ne >> 16) & 0xff;
		int g1 = (ne >> 8) & 0xff;
		int b1 = ne & 0xff;
		int a2 = (sw >> 24) & 0xff;
		int r2 = (sw >> 16) & 0xff;
		int g2 = (sw >> 8) & 0xff;
		int b2 = sw & 0xff;
		int a3 = (se >> 24) & 0xff;
		int r3 = (se >> 16) & 0xff;
		int g3 = (se >> 8) & 0xff;
		int b3 = se & 0xff;

		float cx = 1.0f-x;
		float cy = 1.0f-y;

		m0 = cx * a0 + x * a1;
		m1 = cx * a2 + x * a3;
		int a = (int)(cy * m0 + y * m1);

		m0 = cx * r0 + x * r1;
		m1 = cx * r2 + x * r3;
		int r = (int)(cy * m0 + y * m1);

		m0 = cx * g0 + x * g1;
		m1 = cx * g2 + x * g3;
		int g = (int)(cy * m0 + y * m1);

		m0 = cx * b0 + x * b1;
		m1 = cx * b2 + x * b3;
		int b = (int)(cy * m0 + y * m1);

		return (a << 24) | (r << 16) | (g << 8) | b;
		
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
	
	
	/**
	 * Compute 2D Perlin noise.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return noise value at (x, y)
	 */
	private float noise2(float x, float y) {
		
		int i, j, k;
		int bx0, bx1, by0, by1;
		int b00, b10, b01, b11;
		float rx0, rx1, ry0, ry1;
		float[] q;
		float sx, sy, a, b, t, u, v;
		
		
		t = x + N;
		bx0 = ((int)t) & BM;
		bx1 = (bx0+1) & BM;
		rx0 = t - (int)t;
		rx1 = rx0 - 1.0f;
	
		t = y + N;
		by0 = ((int)t) & BM;
		by1 = (by0+1) & BM;
		ry0 = t - (int)t;
		ry1 = ry0 - 1.0f;
	
		i = p[bx0];
		j = p[bx1];

		b00 = p[i + by0];
		b10 = p[j + by0];
		b01 = p[i + by1];
		b11 = p[j + by1];

		sx = rx0 * rx0 * (3.0f - 2.0f * rx0);
		sy = ry0 * ry0 * (3.0f - 2.0f * ry0);

		q = g2[b00]; u = rx0 * q[0] + ry0 * q[1];
		q = g2[b10]; v = rx1 * q[0] + ry0 * q[1];
		a = u + sx * (v - u);

		q = g2[b01]; u = rx0 * q[0] + ry1 * q[1];
		q = g2[b11]; v = rx1 * q[0] + ry1 * q[1];
		b = u + sx * (v - u);

		return 1.5f * (a + sy * (b - a));
	}
	
	
	/**
	 * Generate a random number.
	 * @return the generated random number.
	 */
	private int random()
	{
		return randomGenerator.nextInt() & 0x7fffffff;
	}
	
	
	
}
