/**
 * 
 */
package com.app.img_mic_helper.wxapi;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.app.img_mic_helper.R;
import com.config.Config;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.utils.processor.BlackWhiteProcessor;

/**
 * This is a singleton class.
 * This class is used to share message to WeChat.
 * @author William
 * 
 */
public class WXShare {

	
	private volatile static WXShare uniqueInstance = null;
	private IWXAPI mWXApi;
	
	
	/**
	 * The only constructor.
	 */
	private WXShare() 
	{

	}

	
	/**
	 * the public method to get the unique instance
	 * @return WXShare the unique instance of this class
	 */
	public static WXShare getInstance(){
        if(uniqueInstance == null){
            synchronized(WXShare.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new WXShare();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Share a image to wechat.
	 * @param context the context.
	 * @param file the image file to share.
	 * @throws Exception wechat is uninstalled or wechat version is too low will throw exception.
	 */
	public void shareImagetoWX(Context context, File file) throws Exception
	{
		mWXApi = WXAPIFactory.createWXAPI(context, Config.APP_ID);
		mWXApi.registerApp(Config.APP_ID);
		
		if (!mWXApi.isWXAppInstalled())
		{
			Toast.makeText(context, R.string.wxapp_uninstall, Toast.LENGTH_LONG).show();
			throw new ActivityNotFoundException();
		}
		if (!mWXApi.isWXAppSupportAPI())
		{
			Toast.makeText(context, R.string.version_too_low, Toast.LENGTH_LONG).show();
			throw new Exception(context.getString(R.string.version_too_low));
		}
		
		WXMediaMessage msg = new WXMediaMessage();
		if (file != null)
		{
			WXImageObject imgObj = new WXImageObject();
			imgObj.setImagePath(file.getAbsolutePath());
			msg.mediaObject = imgObj;
		
			Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, Config.THUMB_SIZE, Config.THUMB_SIZE, true);
			bmp.recycle();
			msg.thumbData = bmpToByteArray(thumbBmp, true);
		}
		
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		mWXApi.sendReq(req);
	}
	
    
	/**
	 * Share a music to wechat.
	 * @param context the context.
	 * @param micUrl the url of the share music.
	 * @param text the text to share about the music.
	 * @throws Exception wechat is uninstalled or wechat version is too low will throw exception.
	 */
	public void shareMusictoWX(Context context, String micUrl, String text) throws Exception
	{
		mWXApi = WXAPIFactory.createWXAPI(context, Config.APP_ID);
		mWXApi.registerApp(Config.APP_ID);
		
		if (!mWXApi.isWXAppInstalled())
		{
			Toast.makeText(context, R.string.wxapp_uninstall, Toast.LENGTH_LONG).show();
			throw new ActivityNotFoundException();
		}
		if (!mWXApi.isWXAppSupportAPI())
		{
			Toast.makeText(context, R.string.version_too_low, Toast.LENGTH_LONG).show();
			throw new Exception(context.getString(R.string.version_too_low));
		}
		
		WXMediaMessage msg = new WXMediaMessage();
		if (micUrl != null && !micUrl.equals(""))
		{
			WXWebpageObject micObj = new WXWebpageObject();
			micObj.webpageUrl = micUrl;
			msg.mediaObject = micObj;
		}
		
		if (!text.equals(""))
		{
			msg.title = text;
		}
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		mWXApi.sendReq(req);
	}
	
	
	/**
	 * Convert a bitmap to its byte array form.
	 * @param bmp the bitmap to be converted.
	 * @param recycle true to recycle the bitmap after convert.
	 * @return the byte array of the bitmap.
	 */
	private byte[] bmpToByteArray(Bitmap bmp, boolean recycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (recycle) 
		{
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try 
		{
			output.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	
}
