package com.utils;

import android.graphics.Bitmap;

/**
 * This is a basic parent class for all kinds of image processor
 * @author Willam
 *
 */
public abstract class Processor {
	
	/**
	 * This is the concrete process method of this processor
	 * @param bitmap the image to be processed
	 * @return Bitmap the image after being processed
	 */
	public abstract Bitmap process(Bitmap bitmap);

}
