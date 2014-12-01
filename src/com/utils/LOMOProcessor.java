package com.utils;

import android.graphics.Bitmap;


/**
 * This is a singleton class.
 * The processor transforms an image into a lomo style.
 * @author William
 *
 */
public class LOMOProcessor implements Processor {

	
	private volatile static LOMOProcessor uniqueInstance = null;
	
	
	private LOMOProcessor()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return LOMOProcessor the unique instance of this class
	 */
	public static LOMOProcessor getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(ContrastProcessor.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new LOMOProcessor();
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
		
		// using two temp bitmap benefits to recycle.
		Bitmap tempBitmap = null;
		Bitmap tempBitmap2 = null;

		
		// step1 : contrast
		tempBitmap = Contrast2Processor.getInstance().process(bitmap);
		
		
		// step2 : softlight
		tempBitmap2 = SoftlightProcessor.getInstance().process(tempBitmap);
		tempBitmap.recycle();
		tempBitmap = null;

		
		// step3 : cross processing
		tempBitmap = CrossProcessingProcessor.getInstance().process(tempBitmap2);
		tempBitmap2.recycle();
		tempBitmap2 = null;
		
		
		// step4 : vignette
		tempBitmap2 = VignetteProcessor.getInstance().process(tempBitmap);
		tempBitmap.recycle();
		tempBitmap = null;
		
		return tempBitmap2;
	}

	
}
