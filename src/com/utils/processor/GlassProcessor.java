package com.utils.processor;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * This is a singleton class.
 * The processor adds a glass effect on the original image.
 * The glass effect is decided by four factors:
 * 1) aasamples, the number of sample;
 * 2) angle, decides the shape of glass;
 * 3) square size, decides the area that isn't covered by glass.
 * 4) curvature, the curvature of glass.
 * The result of this processor is that some parts are covered by glass,
 * the rest part that isn't covered by glass is clear.
 * @author WilliamDeng
 *
 */
public class GlassProcessor implements Processor {

    
    private volatile static GlassProcessor uniqueInstance = null;
    private int aasamples = 17;
    private int angle = 15;
    private int square_size = 20;
    private int curvature = 8;
    private int[][] m_aapoint = null;
    
    
    private GlassProcessor()
    {
        
    }
    
    
    /**
     * Set the aasamples, which represents the number of samples.
     * @param aasamples the param to set. It must be larger than zero.
     * @return true if the param is successfully set.
     *            false if the param is illegal.
     */
    public boolean setAASamples(int aasamples)
    {
        if (aasamples <= 0)
        {
            return false;
        }
        else
        {
            this.aasamples = aasamples;
        }
        
        return true;
    }
    
    
    /**
     * Get the parameter aasamples.
     * @return the value of aasamples.
     */
    public int getAASamples()
    {
        return this.aasamples;
    }
    
    
    /**
     * Set the angle, which decides the shape of the glass effect.
     * @param angle the param to set. It must be in range of [-45, 45].
     * @return true if the param is successfully set.
     *            false if the param is illegal.
     */
    public boolean setAngle(int angle)
    {
        if (angle < -45 || angle > 45)
        {
            return false;
        }
        else
        {
            this.angle = angle;
        }
        
        return true;
    }
    
    
    /**
     * Get the parameter angle.
     * @return the value of angle.
     */
    public int getAngle()
    {
        return this.angle;
    }
    
    
    /**
     * Set the square size, which decides the size of area that isn't covered by the glass.
     * @param square_size the param to set. It must in the range of [2, 200].
     * @return true if the param is successfully set.
     *            false if the param is illegal.
     */
    public boolean setSquareSize(int square_size)
    {
        if (square_size < 2 || square_size > 200)
        {
            return false;
        }
        else
        {
            this.square_size = square_size;
        }
        
        return true;
    }
    
    
    /**
     * Get the parameter square size.
     * @return the value of square size.
     */
    public int getSquareSize()
    {
        return this.square_size;
    }
    
    
    /**
     * Set the curvature, which decides the curvature of the glass.
     * @param curvature the param to set. It must in the range of [-20, 20].
     *            If it is equal to 0, then it will be set to 1.
     * @return true if the param is successfully set.
     *            false if the param is illegal.
     */
    public boolean setCurvature(int curvature)
    {
        if (curvature < -20 || curvature > 20)
        {
            return false;
        }
        else if (curvature == 0)
        {
            this.curvature = 1;
        }
        else
        {
            this.curvature = curvature;
        }
        
        return true;
    }
    
    
    /**
     * Get the parameter curvature.
     * @return the value of curvature.
     */
    public int getCurvature()
    {
        return this.curvature;
    }
    
    
    /**
     * the public method to get the unique instance
     * @return GlassProcessor the unique instance of this class
     */
    public static GlassProcessor getInstance()
    {
        if(uniqueInstance == null)
        {
            synchronized(GlassProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new GlassProcessor();
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
        double m_sin = Math.sin(angle2radian(this.angle));
        double m_cos = Math.cos(angle2radian(this.angle));
        double m_scale = Math.PI / this.square_size;
        double m_curvature = this.curvature * this.curvature / 10.0d * (Math.abs(this.curvature) / this.curvature);
        m_aapoint = new int[this.aasamples][2];
        
        for (int k = 0 ; k < this.aasamples ; k++)
        {
            double x = (k * 4) / (double)this.aasamples;
            double y = k / (double)this.aasamples ;
            x = x - (int)x ;
            this.m_aapoint[k][0] = (int)(m_cos * x + m_sin * y);
            this.m_aapoint[k][1] = (int)(m_cos * y - m_sin * x);
        }
        
        double half_width = width / 2.0d;
        double half_height = height / 2.0d;
        int ratio = width > height ? height * 32768 / width : width * 32768 / height;

        int center_x = width >> 1;
        int center_y = height >> 1;
        int max = center_x * center_x + center_y * center_y;
        int min = (int)(max * 0.5);
        int difference = max - min;
        
        int oldPixel;
        int newPixel;
        int r, g, b;
        
        Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        
        for(int i = 0 ; i < height ; i++)
        {
            
            for(int j = 0 ; j < width ; j++)
            {
                
                int dx = center_x - j;
                int dy = center_y - i;
                
                if (width > height)
                {
                    dy = (dy * ratio) >> 14;
                }
                else
                {
                    dx = (dx * ratio) >> 14;
                }
                
                int distance2 = dx * dx + dy * dy;
                if (distance2 <= min)
                {
                    newPixel = bitmap.getPixel(j, i);
                    destBitmap.setPixel(j, i, newPixel);
                    continue;
                }
                
                int x = (int)(j - half_width);
                int y = (int)(i - half_height);
                r = 0;
                g = 0;
                b = 0;
                
                for (int k = 0 ; k < this.aasamples ; k++)
                {
                    double u = x + this.m_aapoint[k][0] ;
                    double v = y - this.m_aapoint[k][1] ;
                    
                    double s = m_cos * u + m_sin * v ;
                    double t = -m_sin * u + m_cos * v ;
                    
                    s += m_curvature * Math.tan(s * m_scale) ;
                    t += m_curvature * Math.tan(t * m_scale) ;
                    
                    u = m_cos * s - m_sin * t ;
                    v = m_sin * s + m_cos * t ;
                    
                    int xSample = (int)(half_width + u) ;
                    int ySample = (int)(half_height + v) ;
                    
                    if (xSample < 0)
                    {
                        xSample = 0;
                    }
                    else if (xSample >= width)
                    {
                        xSample = width - 1;
                    }
                    
                    if (ySample < 0)
                    {
                        ySample = 0;
                    }
                    else if (ySample >= height)
                    {
                        ySample = height - 1;
                    }
                    
                    oldPixel = bitmap.getPixel(xSample, ySample);
                    r += (oldPixel >> 16) & 0xff;
                    g += (oldPixel >> 8) & 0xff;
                    b += oldPixel & 0xff;
                }
                
                r /= this.aasamples;
                g /= this.aasamples;
                b /= this.aasamples;
                newPixel = 0xff000000 | (r << 16) | (g << 8) | b;
     
                destBitmap.setPixel(j, i, newPixel);
                
            }
        }
        
        return destBitmap;
        
    }
    
    
    /**
     * Transform angle to radian.
     * @param angle the angle to be transformed.
     * @return the result of transform.
     */
    private double angle2radian(int angle) 
    {
        return Math.PI * angle / 180.0d;
    }
    

}
