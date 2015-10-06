package gvrFlickr;

import java.util.ArrayList;

import org.vr.flickr.FlickrContainer;



import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class ContentThreadHandler extends Handler{
	public static final int TAGGED_DATA_FETCHED = 0;
	public static final int THUMBNAIL_URL_FETCHED = 1;
	public static final int IMAGE_URL_FETCHED = 2;
	
	Activity mActivity;
	ContentLoader mLoader;
	
	public ContentThreadHandler(Activity actv,ContentLoader loader) {
		mActivity = actv;
		mLoader = loader;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case TAGGED_DATA_FETCHED:
			if (msg.obj != null) {
			
				 mLoader.setTaggedDataList((ArrayList<FlickrContainer>) msg.obj);
				 MainVrActivity vr = (MainVrActivity)mActivity;
				 vr.loadContent();
			}
			break;
		case THUMBNAIL_URL_FETCHED:
		
			break;
		case IMAGE_URL_FETCHED:

			break;
		}
		super.handleMessage(msg);
	}
}
