package com.app.img_mic_helper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.config.ActivityFlag;
import com.custom_widget.FlowerProgressDialog;
import com.utils.BlackWhiteProcessor;
import com.utils.CeramicProcessor;
import com.utils.ColdProcessor;
import com.utils.ComicProcessor;
import com.utils.CrossProcessingProcessor;
import com.utils.EmbossProcessor;
import com.utils.ErodeProcessor;
import com.utils.ExposureProcessor;
import com.utils.FilmProcessor;
import com.utils.GaussianBlurProcessor;
import com.utils.GeneralBlurProcessor;
import com.utils.GothicProcessor;
import com.utils.GouacheProcessor;
import com.utils.GrayProcessor;
import com.utils.ImageCompressor;
import com.utils.InlayProcessor;
import com.utils.MistProcessor;
import com.utils.MosaicProcessor;
import com.utils.NegativeProcessor;
import com.utils.NostalgiaProcessor;
import com.utils.OilPaintingProcessor;
import com.utils.PaperCutProcessor;
import com.utils.PinkProcessor;
import com.utils.Processor;
import com.utils.RainbowProcessor;
import com.utils.ReflectionProcessor;
import com.utils.ShadowProcessor;
import com.utils.SketchProcessor;
import com.utils.WindProcessor;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.EmbossMaskFilter;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ImageProcessActivity extends Activity {
	
	private ImageView image = null;
	private Bitmap bitmap = null;
	private Bitmap destBitmap = null;
	private Bitmap tempBitmap = null;
	private String filePath = null;
	private List<HashMap<String, Object>> func_bar_items = new ArrayList<HashMap<String, Object>>();
	private int[] subfunc_bar_items = null;
	private GridView function_bar = null;
	private HorizontalScrollView subfunction_bar_container = null;
	private LinearLayout subfunction_bar = null;
	private LayoutInflater mInflater = null;
	private Processor processor = null;
	
	public FlowerProgressDialog mProgressDialog = null;

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
            processor = ImageCompressor.getInstance();
        	bitmap = processor.process(bitmap);
        	break;
        case ActivityFlag.CAMERA_WITH_DATA:
        	filePath = intent.getStringExtra("ImagePath");
        	cleanBitmap();
        	BitmapFactory.Options options = new BitmapFactory.Options();
        	bitmap = BitmapFactory.decodeFile(filePath, options);
        	processor = ImageCompressor.getInstance();
            bitmap = processor.process(bitmap);
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
    	   map.put("itemName", getString(com.config.Config.function_list[i]));
    	   func_bar_items.add(map);
       }
       
       SimpleAdapter functionBarAdapter = new SimpleAdapter
    		   (this, func_bar_items, R.layout.function_bar_content, new String[] {"itemName"},
    		   new int[] {R.id.item_name}) {};
    	
    	function_bar = (GridView)this.findViewById(R.id.function_bar);
    	function_bar.setAdapter(functionBarAdapter);
    	function_bar.setOnItemClickListener(func_bar_item_clickListener);
    	
    	
     	// initialize subfunction bar
        subfunction_bar_container = (HorizontalScrollView)this.findViewById(R.id.subfunction_bar_container);
     	subfunction_bar = (LinearLayout)this.findViewById(R.id.subfunction_bar);
     	
     	
     	// initialize the progressdialog which is used when process image.
     	mProgressDialog = new FlowerProgressDialog(ImageProcessActivity.this).createDialog();
		mProgressDialog.setCancelable(false);
		
    }

	
	// the onitemclicklistener indicates what would happen when the function bar items are clicked.
	private OnItemClickListener func_bar_item_clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			subfunc_bar_items = null;
			switch(position) {
			case 0:
				subfunc_bar_items = com.config.Config.style_list;
				break;
			case 1:
				subfunc_bar_items = com.config.Config.art_list;
				break;
			case 2:
				subfunc_bar_items = com.config.Config.fashion_list;
				break;
			case 3:
				subfunc_bar_items = com.config.Config.classic_list;
				break;
			case 4:
				break;
			default:
				break;
			}
			if (subfunc_bar_items != null)
			{
				loadSubfuncbarContent();
			}
			
		}
	};
	
	/**
	 * Load the sub_function_bar content when a concrete function bar item is clicked.
	 */
	private void loadSubfuncbarContent() {
		// initialize the title of each subfunction bar item
		if (null != subfunction_bar) {
			subfunction_bar.removeAllViews();
		}
		for (int i = 0; i < subfunc_bar_items.length; i++)  
        {  
     		mInflater = LayoutInflater.from(this); 
            View item_view = mInflater.inflate(R.layout.function_bar_content, subfunction_bar, false); 
            TextView item_name = (TextView)item_view.findViewById(R.id.item_name);  
            item_name.setText((String)getText(subfunc_bar_items[i]));
            item_name.setPadding(10, 0, 10, 0);
            final int methodId = subfunc_bar_items[i];
            item_view.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					processor = null;
					switch (methodId) {
					case R.string.black_white:
						processor = BlackWhiteProcessor.getInstance();
						break;
					case R.string.ceramic:
						processor = CeramicProcessor.getInstance();
						break;
					case R.string.cold:
						processor = ColdProcessor.getInstance();
						break;
					case R.string.comic:
						processor = ComicProcessor.getInstance();
						break;
					case R.string.cross_processing:
						processor = CrossProcessingProcessor.getInstance();
						break;
					case R.string.emboss:
						processor = EmbossProcessor.getInstance();
						break;
					case R.string.erode:
						processor = ErodeProcessor.getInstance();
						break;
					case R.string.exposure:
						processor = ExposureProcessor.getInstance();
						break;
					case R.string.film:
						processor = FilmProcessor.getInstance();
						break;
					case R.string.gaussian_blur:
						processor = GaussianBlurProcessor.getInstance();
						break;
					case R.string.blur:
						processor = GeneralBlurProcessor.getInstance();
						break;
					case R.string.gothic:
						processor = GothicProcessor.getInstance();
						break;
					case R.string.gouache:
						processor = GouacheProcessor.getInstance();
						break;
					case R.string.gray:
						processor = GrayProcessor.getInstance();
						break;
					case R.string.inlay:
						processor = InlayProcessor.getInstance();
						break;
					case R.string.mist:
						processor = MistProcessor.getInstance();
						break;
					case R.string.mosaic:
						processor = MosaicProcessor.getInstance();
						break;
					case R.string.negative:
						processor = NegativeProcessor.getInstance();
						break;
					case R.string.nostalgia:
						processor = NostalgiaProcessor.getInstance();
						break;			
					case R.string.oil_painting:
						processor = OilPaintingProcessor.getInstance();
						break;
					case R.string.paper_cut:
						processor = PaperCutProcessor.getInstance();
						break;
					case R.string.pink:
						processor = PinkProcessor.getInstance();
						break;
					case R.string.rainbow:
						processor = RainbowProcessor.getInstance();
						break;
					case R.string.reflection:
						processor = ReflectionProcessor.getInstance();
						break;
					case R.string.shadow:
						processor = ShadowProcessor.getInstance();
						break;		
					case R.string.sketch:
						processor = SketchProcessor.getInstance();
						break;
					case R.string.wind:
						processor = WindProcessor.getInstance();
						break;				
					default:
						break;
					}
					if (processor != null) {
						new processImageTask(ImageProcessActivity.this, processor).execute();
					}
					
				}
            	
            });
            subfunction_bar.addView(item_view);  
        }
		subfunction_bar_container.setVisibility(View.VISIBLE);
	}
	
	
    /**
     * release the memory that bitmap uses.
     */
    private void cleanBitmap() {
		if (bitmap != null)
			bitmap.recycle();
		
	}
    
    
    public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
		private Processor processor;
        private Activity activity = null;
		public processImageTask(Activity activity, Processor processor) {
			this.processor = processor;
			this.activity = activity;
		}
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}
		
		
		@Override
		protected Bitmap doInBackground(Void... params) {
			tempBitmap = destBitmap;
			destBitmap = processor.process(bitmap);
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Bitmap result) {
		    image.setImageBitmap(destBitmap);
		    if (tempBitmap != null) {
		    	tempBitmap.recycle();
		    }
			mProgressDialog.dismiss();
		}
		
    }

}
