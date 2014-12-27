package com.app.img_mic_helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.app.img_mic_helper.wxapi.WXShare;
import com.config.ActivityFlag;
import com.custom_widget.ArrowProgressDialog;
import com.utils.processor.BlackWhiteProcessor;
import com.utils.processor.BrightProcessor;
import com.utils.processor.BubbleProcessor;
import com.utils.processor.CastProcessor;
import com.utils.processor.CeramicProcessor;
import com.utils.processor.ColdProcessor;
import com.utils.processor.ComicProcessor;
import com.utils.processor.Contrast2Processor;
import com.utils.processor.ContrastProcessor;
import com.utils.processor.CrossProcessingProcessor;
import com.utils.processor.DarkCornerProcessor;
import com.utils.processor.DermabrasionProcessor;
import com.utils.processor.DistortingMirrorProcessor;
import com.utils.processor.EmbossProcessor;
import com.utils.processor.ErodeProcessor;
import com.utils.processor.ExposureProcessor;
import com.utils.processor.FeatherProcessor;
import com.utils.processor.FilmProcessor;
import com.utils.processor.FocusProcessor;
import com.utils.processor.FrameProcessor;
import com.utils.processor.FreezeProcessor;
import com.utils.processor.FrostedGlassProcessor;
import com.utils.processor.GammaProcessor;
import com.utils.processor.GaussianBlurProcessor;
import com.utils.processor.GeneralBlurProcessor;
import com.utils.processor.GlassProcessor;
import com.utils.processor.GothicProcessor;
import com.utils.processor.GouacheProcessor;
import com.utils.processor.GrayProcessor;
import com.utils.processor.ImageCompressor;
import com.utils.processor.InlayProcessor;
import com.utils.processor.LOMOProcessor;
import com.utils.processor.LaserProcessor;
import com.utils.processor.MagnifierProcessor;
import com.utils.processor.MarbleProcessor;
import com.utils.processor.MistProcessor;
import com.utils.processor.MosaicProcessor;
import com.utils.processor.MotionProcessor;
import com.utils.processor.NegativeProcessor;
import com.utils.processor.NostalgiaProcessor;
import com.utils.processor.OilPaintingProcessor;
import com.utils.processor.PaperCutProcessor;
import com.utils.processor.PenProcessor;
import com.utils.processor.PinkProcessor;
import com.utils.processor.Processor;
import com.utils.processor.RainbowProcessor;
import com.utils.processor.ReflectionProcessor;
import com.utils.processor.ShadowProcessor;
import com.utils.processor.SharpenProcessor;
import com.utils.processor.SketchProcessor;
import com.utils.processor.SmartSharpenProcessor;
import com.utils.processor.SmearProcessor;
import com.utils.processor.SoftlightProcessor;
import com.utils.processor.SudokuProcessor;
import com.utils.processor.SymmetryProcessor;
import com.utils.processor.VignetteProcessor;
import com.utils.processor.WaterProcessor;
import com.utils.processor.WaterRippleProcessor;
import com.utils.processor.WhiteBalanceProcessor;
import com.utils.processor.WhiteningProcessor;
import com.utils.processor.WindProcessor;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.EmbossMaskFilter;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.FocusFinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


/**
 * The activity class provides the function to process images.
 * @author William
 *
 */
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
	private  ActionBar actionBar = null;
	private Menu menu = null;
	private Boolean isProcess = false; 
	
	public ArrowProgressDialog mProgressDialog = null;
	

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
            if (bitmap != null)
            {
                Matrix matrix = new Matrix();  
                matrix.postRotate(getCameraPhotoOrientation(filePath));  
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            break;
        default:
        	break;
        }
        // display the image that is going to be processed
       image = (ImageView)this.findViewById(R.id.image_to_process);
       image.setImageBitmap(bitmap);
       
       // initialize the title of each function bar item
       for (int i = 0; i < 5; i++) 
       {
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
     	mProgressDialog = new ArrowProgressDialog(ImageProcessActivity.this).createDialog();
		mProgressDialog.setCancelable(false);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
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
				subfunc_bar_items = com.config.Config.effects_list;
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
            item_view.setPadding(10, 0, 10, 0);
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
					case R.string.bright:
						processor = BrightProcessor.getInstance();
						break;
					case R.string.bubble:
						BubbleProcessor.getInstance().setContext(getApplicationContext());
						processor = BubbleProcessor.getInstance();
						break;
					case R.string.cast:
						processor = CastProcessor.getInstance();
						break;
					case R.string.ceramic:
						processor = CeramicProcessor.getInstance();
						break;
					case R.string.cold:
						processor = ColdProcessor.getInstance();
						break;
					case R.string.contrast_enhancement:
						processor = ContrastProcessor.getInstance();
						break;
					case R.string.contrast_enhancement2:
						processor = Contrast2Processor.getInstance();
						break;
					case R.string.comic:
						processor = ComicProcessor.getInstance();
						break;
					case R.string.cross_processing:
						processor = CrossProcessingProcessor.getInstance();
						break;
					case R.string.dark_corner:
						processor = DarkCornerProcessor.getInstance();
						break;
					case R.string.dermabrasion:
						processor = DermabrasionProcessor.getInstance();
						break;
					case R.string.distorting_mirror:
						processor = DistortingMirrorProcessor.getInstance();
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
					case R.string.feather:
						processor = FeatherProcessor.getInstance();
						break;
					case R.string.film:
						processor = FilmProcessor.getInstance();
						break;
					case R.string.focus:
						processor = FocusProcessor.getInstance();
						break;
					case R.string.frame:
						FrameProcessor.getInstance().setContext(getApplicationContext());
						processor = FrameProcessor.getInstance();
						break;
					case R.string.freeze:
						processor = FreezeProcessor.getInstance();
						break;
					case R.string.frosted_glass:
						processor = FrostedGlassProcessor.getInstance();
						break;
					case R.string.gamma_correction:
						processor = GammaProcessor.getInstance();
						break;
					case R.string.gaussian_blur:
						processor = GaussianBlurProcessor.getInstance();
						break;
					case R.string.blur:
						processor = GeneralBlurProcessor.getInstance();
						break;
					case R.string.glass:
						processor = GlassProcessor.getInstance();
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
					case R.string.laser:
						processor = LaserProcessor.getInstance();
						break;
					case R.string.inlay:
						processor = InlayProcessor.getInstance();
						break;
					case R.string.lomo:
						processor = LOMOProcessor.getInstance();
						break;
					case R.string.magnifier:
						processor = MagnifierProcessor.getInstance();
						break;
					case R.string.marble:
						processor = MarbleProcessor.getInstance();
						break;
					case R.string.mist:
						processor = MistProcessor.getInstance();
						break;
					case R.string.mosaic:
						processor = MosaicProcessor.getInstance();
						break;
					case R.string.motion_blur:
						MotionProcessor.getInstance().setDistance(0.0f);
						MotionProcessor.getInstance().setAngle(0.0f);
						MotionProcessor.getInstance().setZoom(0.4f);
						processor = MotionProcessor.getInstance();
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
					case R.string.pen:
						processor = PenProcessor.getInstance();
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
					case R.string.sharpen:
						processor = SharpenProcessor.getInstance();
						break;
					case R.string.sketch:
						processor = SketchProcessor.getInstance();
						break;
					case R.string.smart_sharpen:
						processor = SmartSharpenProcessor.getInstance();
						break;
					case R.string.smear:
						processor = SmearProcessor.getInstance();
						break;
					case R.string.soft_light:
						processor = SoftlightProcessor.getInstance();
						break;
					case R.string.sudoku:
						processor = SudokuProcessor.getInstance();
						break;
					case R.string.symmetry:
						processor = SymmetryProcessor.getInstance();
						break;
					case R.string.vignette:
						processor = VignetteProcessor.getInstance();
						break;
					case R.string.water:
						processor = WaterProcessor.getInstance();
						break;
					case R.string.water_ripple:
						processor = WaterRippleProcessor.getInstance();
						break;
					case R.string.white_balance:
						processor = WhiteBalanceProcessor.getInstance();
						break;
					case R.string.whitening:
						processor = WhiteningProcessor.getInstance();
						break;
					case R.string.wind:
						processor = WindProcessor.getInstance();
						break;				
					default:
						break;
					}
					if (processor != null) 
					{
						new processImageTask(ImageProcessActivity.this, processor).execute();
						isProcess = true;
						freshMenu();
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
    
    
    public int getCameraPhotoOrientation(String imagePath){  
        int rotate = 0;  
        try {   
            File imageFile = new File(imagePath);  
      
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());  
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
      
            switch (orientation) {  
            case ExifInterface.ORIENTATION_ROTATE_270:  
                rotate = 270;  
                break;  
            case ExifInterface.ORIENTATION_ROTATE_180:  
                rotate = 180;  
                break;  
            case ExifInterface.ORIENTATION_ROTATE_90:  
                rotate = 90;  
                break;  
            }  
      
        } 
        catch (Exception e)
        {  
            e.printStackTrace();  
        }  
        
        return rotate;  
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	setIconEnable(menu, true);
        getMenuInflater().inflate(R.menu.image_process, menu);
        this.menu = menu;
        freshMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.image_return) 
        {
        	image.setImageBitmap(bitmap);
        	isProcess = false;
        	freshMenu();
            return true;
        }
        else if (id == R.id.image_save)
        {
        	final String SDCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File fileDir = new File(SDCardDir + "/Img_Mic_Helper" );
            if(!fileDir.exists())
                fileDir.mkdirs();
            
            Random random = new Random(System.currentTimeMillis());
            java.util.Date date = new java.util.Date(System.currentTimeMillis()); 
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss"); 
            final String fileName = fmt.format(date)+ Math.abs(random.nextInt()) % 100000;
            
            LayoutInflater inflater = LayoutInflater.from(this);  
            View fileNameView = inflater.inflate(R.layout.save_filename, null);  
            final EditText fileNameEditText = (EditText)fileNameView.findViewById(R.id.filename);  
            fileNameEditText.setText(fileName);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);  
            builder.setCancelable(false);  
            builder.setTitle(R.string.filename_inform);  
            builder.setView(fileNameView);  
            builder.setPositiveButton(R.string.ok,  
                    new DialogInterface.OnClickListener() {  
                        public void onClick(DialogInterface dialog, int whichButton) 
                        {  
                            String newFileName = fileNameEditText.getText().toString();
                            if (newFileName.equals(""))
                            {
                            	newFileName = fileName;
                            }
                            File saveFile = new File(SDCardDir + "/Img_Mic_Helper/" + newFileName + ".png");
                        	if (saveFile.exists())
                        	{
                        		saveFile.delete();
                        	}
                        	try 
                        	{
                        		FileOutputStream out = new FileOutputStream(saveFile); 
                        		if (isProcess && destBitmap != null) 
                        		{
                        			destBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); 
                        		}
                        		else
                        		{
                        			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); 
								}
                        		out.flush(); 
                        		out.close(); 
                        		dialog.dismiss();
                        	}
                        	catch (FileNotFoundException e)
                        	{ 
                        		e.printStackTrace(); 
                        	}
                        	catch (IOException e) 
                        	{ 
                        		e.printStackTrace(); 
                        	}
                        }  
                    });  
            builder.setNegativeButton(R.string.cancel,  
                    new DialogInterface.OnClickListener() {  
                        public void onClick(DialogInterface dialog, int whichButton) {  
                            dialog.dismiss();  
                        }  
                    });  
            builder.show();  
        }
        else if (id == R.id.share)
        {
        	final String SDCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File fileDir = new File(SDCardDir + "/Img_Mic_Helper" );
            if(!fileDir.exists())
                fileDir.mkdirs();
            
            File shareFile = new File(SDCardDir + "/Img_Mic_Helper/share.png");
        	if (shareFile.exists())
        	{
        		shareFile.delete();
        	}
        	try 
        	{
        		FileOutputStream out = new FileOutputStream(shareFile); 
        		if (isProcess && destBitmap != null) 
        		{
        			destBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); 
        		}
        		else
        		{
        			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); 
				}
        		out.flush(); 
        		out.close(); 
        		
        	}
        	catch (FileNotFoundException e)
        	{ 
        		e.printStackTrace(); 
        	}
        	catch (IOException e) 
        	{ 
        		e.printStackTrace(); 
        	}
       
        	try 
        	{
				WXShare.getInstance().shareImagetoWX(this.getApplicationContext(), shareFile);
			} 
        	catch (Exception e) 
        	{
				e.printStackTrace();
			}
        	
        }
        else if (id == android.R.id.home)
        {
        	onBackPressed();
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
        
    }
    
    
    private void freshMenu()
    {
    	MenuItem menuItem = null;
    	menuItem = menu.getItem(0);
    	menuItem.setEnabled(isProcess);
    	menuItem = menu.getItem(1);
    	menuItem.setEnabled(isProcess);  	
    }
    
    
    private void setIconEnable(Menu menu, boolean enable)  
    {  
        try   
        {  
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");  
            Method method = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);  
            method.setAccessible(true);  
               
            method.invoke(menu, enable);  
              
        }
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        
    }  
    

}
