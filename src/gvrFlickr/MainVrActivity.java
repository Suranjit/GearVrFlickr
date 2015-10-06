package gvrFlickr;

import org.gearvrf.GVRActivity;
import android.app.AlertDialog;
import android.os.Bundle;



public class MainVrActivity extends GVRActivity{


	ContentLoader mContentLoader;
	ContentThreadHandler mThreadMessageHandler;
	FeedManager mFeedVrManager;

	 @Override
	    protected void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        mContentLoader = new ContentLoader(this);
	        mThreadMessageHandler = new ContentThreadHandler(this,mContentLoader);
	        mContentLoader.setMessageHandler(mThreadMessageHandler);
	        mContentLoader.searchForTags();
	        mFeedVrManager = 	new FeedManager(mContentLoader);
	       // setScript(new SampleViewManager(), "gvr_note4.xml");
	        setScript(mFeedVrManager, "gvr_note4.xml");
	    }
	 
	public void loadContent() {
		mFeedVrManager.postUpdateContent();
	}
}

