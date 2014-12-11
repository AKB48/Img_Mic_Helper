/**
 * 
 */
package com.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.security.auth.PrivateCredentialPermission;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.R.integer;
import android.os.Handler;
import android.os.Message;

/**
 * This is a baisc object class.
 * It is a singleton class.
 * This class is used to process all network requests.
 * @author William
 *
 */
public class HttpSender {

	
	private volatile static HttpSender uniqueInstance = null;
	
	
	/**
	 * The default constructor
	 */
	private HttpSender()
	{
		
	}
	
	/**
	 * the public method to get the unique instance
	 * @return HttpSender the unique instance of this class
	 */
	public static HttpSender getInstance()
	{
        if(uniqueInstance == null)
        {
            synchronized(HttpSender.class) 
            {
                if(uniqueInstance == null) 
                {
                    uniqueInstance = new HttpSender();
                }
            }
        }
        
        return uniqueInstance;       
    }
	
	
	/**
	 * Send a http get request.
	 * @param url the url to request using get method.
	 * @param requestCode an integer to indicate this request.
	 * @param mHandler the handler to get http response.
	 */
	public void Httpget(String url, int requestCode, Handler mHandler)
    {
		RequestThread mThread = new RequestThread();
		mThread.url = url;
		mThread.mHandler = mHandler;
		mThread.requestCode = requestCode;
        mThread.start();
        
    }
	
	
	private void get(String httpUrl, int requestCode, Handler mHandler)
	{
		 HttpGet httpget = new HttpGet(httpUrl);
		 HttpClient client = new DefaultHttpClient();
		 StringBuilder str = new StringBuilder();
		 BufferedReader buffer = null;

		 try 
		 {  
			 // set time out limit
			 client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			 HttpResponse httpRes = client.execute(httpget);
	            
			 if(httpRes.getStatusLine().getStatusCode() == 200)
			 {
				 buffer = new BufferedReader(new InputStreamReader(httpRes.getEntity().getContent()));
				 for(String s = buffer.readLine(); s != null ; s = buffer.readLine())
				 {
					 str.append(s);
				 }
				 buffer.close();
	                
				 
				 String result = str.toString();
				 Message message = Message.obtain();
				 message.what = requestCode;
				 message.obj = result;
				 mHandler.sendMessage(message);
	                
			 }
			 
	     } 
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 
	}
	
	
	class RequestThread extends Thread
	{
		private String url = null;
		private Handler mHandler = null;
		private int requestCode = 0;
		
    	public void run() {
    		get(url, requestCode, mHandler);
    	}
    }

}
