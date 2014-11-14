package com.app.img_mic_helper;

import java.io.FileNotFoundException;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageProcessActivity extends Activity {
	
	ImageView image = null;
	Bitmap bitmap = null;
	String filePath = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
        
        Intent intent = getIntent();
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
       image = (ImageView)this.findViewById(R.id.image_to_process);
       image.setImageBitmap(bitmap);
    }
	
	
    /**
     * release the memory that bitmap uses.
     */
    private void cleanBitmap() {
		if (bitmap != null)
			bitmap.recycle();
		
	}

}
