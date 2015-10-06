package org.vr.flickr;

import android.graphics.Bitmap;

public class FlickrContainer {
	String mId;
	int mPosition;
	String mThumbURL;
	Bitmap mThumb;
	Bitmap mPhoto;
	String mLargeURL;
	
	
	public FlickrContainer(String id) {
		mId = id;
	}
	
	public String getId() {
		return mId;
	}
	public void setId(String id) {
		this.mId = id;
	}
	public int getPosition() {
		return mPosition;
	}
	public void setPosition(int position) {
		this.mPosition = position;
	}
	public String getThumbURL() {
		return mThumbURL;
	}
	public void setThumbURL(String thumbURL) {
		this.mThumbURL = thumbURL;
	}
	public Bitmap getThumb() {
		return mThumb;
	}
	public void setThumb(Bitmap thumb) {
		this.mThumb = thumb;
	}
	public Bitmap getPhoto() {
		return mPhoto;
	}
	public void setPhoto(Bitmap photo) {
		this.mPhoto = photo;
	}
	public String getLargeURL() {
		return mLargeURL;
	}
	public void setLargeURL(String largeURL) {
		this.mLargeURL = largeURL;
	}
}
