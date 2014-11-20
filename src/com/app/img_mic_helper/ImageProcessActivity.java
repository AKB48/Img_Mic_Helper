package com.app.img_mic_helper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.config.ActivityFlag;
import com.utils.ImageCompressor;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ImageProcessActivity extends Activity {
	
	private ImageView image = null;
	private Bitmap bitmap = null;
	private String filePath = null;
	private List<HashMap<String, Object>> func_bar_items = new ArrayList<HashMap<String, Object>>();
	private List<HashMap<String, Object>> subfunc_bar_items = new ArrayList<HashMap<String, Object>>();
	private GridView function_bar = null;
	private LinearLayout subfunction_bar = null;
	private LayoutInflater mInflater = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
        
        Intent intent = getIntent();
        // get image source
        switch(intent.getIntExtra("ImageSource", 0)) {
        case ActivityFlag.ALBUM_WITH_DATA:
        	cleanBitmap();
        	Uri uri = intent.getData();   
            ContentResolver cr = this.getContentResolver();
            try {
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        	bitmap = ImageCompressor.getInstance().process(bitmap);
        	break;
        case ActivityFlag.CAMERA_WITH_DATA:
        	filePath = intent.getStringExtra("ImagePath");
        	cleanBitmap();
        	BitmapFactory.Options options = new BitmapFactory.Options();
        	bitmap = BitmapFactory.decodeFile(filePath, options);
            bitmap = ImageCompressor.getInstance().process(bitmap);
        	break;
        default:
        	break;
        }
        // display the image that is going to be processed
       image = (ImageView)this.findViewById(R.id.image_to_process);
       image.setImageBitmap(bitmap);
       
       // initialize the title of each function bar item
       for (int i = 0; i < 5; i++) {
    	   HashMap<String, Object> map = new HashMap<String, Object>();
    	   map.put("itemName", getString(R.string.style));
    	   func_bar_items.add(map);
       }
       
       SimpleAdapter functionBarAdapter = new SimpleAdapter
    		   (this, func_bar_items, R.layout.function_bar_content, new String[] {"itemName"},
    		   new int[] {R.id.item_name}) {};
    	
    	function_bar = (GridView)this.findViewById(R.id.function_bar);
    	function_bar.setAdapter(functionBarAdapter);
    	
    	// initialize the title of each subfunction bar item
    	subfunc_bar_items.clear();
        for (int i = 0; i < 20; i++) {
     	   HashMap<String, Object> map = new HashMap<String, Object>();
     	   map.put("itemName", getString(R.string.black_white));
     	  subfunc_bar_items.add(map);
        }
     	
     	subfunction_bar = (LinearLayout)this.findViewById(R.id.subfunction_bar);
     	for (int i = 0; i < 20; i++)  
        {  
     		mInflater = LayoutInflater.from(this); 
            View item_view = mInflater.inflate(R.layout.function_bar_content, subfunction_bar, false); 
            TextView item_name = (TextView)item_view.findViewById(R.id.item_name);  
            item_name.setText((String)subfunc_bar_items.get(i).get("itemName"));
            final int j = i;
            item_view.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if (j%2==0) {
						function_bar.setVisibility(View.GONE);
					}
					else {
						function_bar.setVisibility(View.VISIBLE);
					}
					
				}
            	
            });
            subfunction_bar.addView(item_view);  
        }  
    
    }
	
	
    /**
     * release the memory that bitmap uses.
     */
    private void cleanBitmap() {
		if (bitmap != null)
			bitmap.recycle();
		
	}

}
