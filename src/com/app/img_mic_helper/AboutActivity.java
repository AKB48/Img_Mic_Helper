/**
 * 
 */
package com.app.img_mic_helper;

import com.app.img_mic_helper.R;
import com.app.img_mic_helper.share.WXShare;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * The activity class provides the function to show information about this application.
 * @author William
 *
 */
public class AboutActivity extends Activity {

	
	private ActionBar actionBar;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
        	onBackPressed();
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
        
    }
	

}
