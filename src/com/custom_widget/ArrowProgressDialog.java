package com.custom_widget;


import com.app.img_mic_helper.R;

import android.R.bool;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The DIY progress dialog displays when processing image.
 * @author William
 *
 */
public class ArrowProgressDialog extends Dialog{
	private Context context = null;
	private ArrowProgressDialog mDialog = null;
	private Animation animation = null;
	private ImageView progressImage = null;
	private TextView progressMsg = null;
	
	public ArrowProgressDialog(Context context){
		super(context);
		this.context = context;
	}
	
	public ArrowProgressDialog(Context context, int theme) {
        super(context, theme);
    }
	
	/**
	 * Decide what will display when the progress dialog appear.
	 * @return ArrowProgressDialog an instance of ArrowProgressDialog.
	 */
	public ArrowProgressDialog createDialog() {

		LayoutInflater inflater = LayoutInflater.from(this.context);
		View v = inflater.inflate(R.layout.flower_progress_bar, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.flower_progress_bar_container);

		mDialog = new ArrowProgressDialog(this.context, R.style.CustomProgressDialog);
		mDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		mDialog.progressImage = (ImageView) v.findViewById(R.id.loadingImageView);
		mDialog.progressMsg = (TextView) v.findViewById(R.id.loadingmsg);
		mDialog.animation = AnimationUtils.loadAnimation(this.context, R.anim.progress_rotating);

		return mDialog;
	}
    
    
    /**
     * 
     * set the message that display on the dialog that inform users.
     * @param strMessage the string to be set as a message.
     * @return true when the method successfully return.
     *
     */
    public boolean setMessage(String strMessage){
    	
    	if (progressMsg != null){
    		progressMsg.setText(strMessage);
    	}
    	
    	return true;
    }
    
    /**
     * an animation will display when the dialog appears.
     */
    public void show() {
    	progressImage.startAnimation(animation);
    	super.show();
    }
}
