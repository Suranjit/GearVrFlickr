package gvrFlickr;

import java.util.ArrayList;

import org.vr.flickr.FlickrApiManager;
import org.vr.flickr.FlickrContainer;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.graphics.Bitmap;



public class ContentLoader {

	ArrayList<FlickrContainer> mContentList;
	ContentThreadHandler mMessageHandler;
	Context mContext;
	
	
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
	
	
	public ContentLoader(Context ctx) {
		mContentList = new ArrayList<FlickrContainer> ();
		mContext = ctx;
		initImageLoader(mContext);

	}
	
	public ArrayList<FlickrContainer> getContentList() {
		return mContentList;
	}
	
	public void searchForTags() {	
		new Thread(getTaggedData).start();
	}
	
	public void setMessageHandler(ContentThreadHandler handler) {
		mMessageHandler= handler;
	}
	
	Runnable getTaggedData = new Runnable() {
		@Override
		public void run() {
				FlickrApiManager.searchImagesByTag(mContext, "stars",mMessageHandler);
		}
	};

	public void setTaggedDataList(ArrayList<FlickrContainer> contentList) {
		// TODO Auto-generated method stub
		mContentList = contentList;
		
	}
	
	public void loadUrlImages() {
		for(int i=0;i<mContentList.size();i++) {

			ImageLoader.getInstance().loadImage(mContentList.get(i).getLargeURL(), new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			        // Do whatever you want with Bitmap
			    }
			});
			
		}
	}
	
	public void getBitmapImageAsync(int idx) {
		ImageLoader.getInstance().loadImage(mContentList.get(idx).getLargeURL(), new SimpleImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		        // Do whatever you want with Bitmap
		    }
		});
	}
	
	public Bitmap getBitmapImageSync(int idx) {
	
		return ImageLoader.getInstance().loadImageSync(mContentList.get(idx).getLargeURL());
		
	}
	
	
}
