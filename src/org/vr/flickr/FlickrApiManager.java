package org.vr.flickr;

import gvrFlickr.ContentThreadHandler;



import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Message;

public class FlickrApiManager {

	private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/?method=";
	private static final String FLICKR_PHOTOS_SEARCH_STRING = "flickr.photos.search";
	private static final String FLICKR_GET_SIZES_STRING = "flickr.photos.getSizes";
	private static final int FLICKR_PHOTOS_SEARCH_ID = 1;
	private static final int FLICKR_GET_SIZES_ID = 2;
	private static final int NUMBER_OF_PHOTOS = 5;
	
	//You can set here your API_KEY
	private static final String APIKEY_SEARCH_STRING = "YOUR_FLICKR_API_KEY";
	
	private static final String TAGS_STRING = "&tags=";
	private static final String PHOTO_ID_STRING = "&photo_id=";
	private static final String FORMAT_STRING = "&format=json";
	public static final int PHOTO_THUMB = 111;
	public static final int PHOTO_LARGE = 222;
	
	private static String createUrl(int methodId,String param) {
		String method_type = "";
		String url = null;
		switch (methodId) {
		case FLICKR_PHOTOS_SEARCH_ID:
			method_type = FLICKR_PHOTOS_SEARCH_STRING;
			url = FLICKR_BASE_URL + method_type + APIKEY_SEARCH_STRING + TAGS_STRING + param + FORMAT_STRING + "&per_page="+NUMBER_OF_PHOTOS+"&media=photos";
			break;
		case FLICKR_GET_SIZES_ID:
			method_type = FLICKR_GET_SIZES_STRING;
			url = FLICKR_BASE_URL + method_type + PHOTO_ID_STRING + param + APIKEY_SEARCH_STRING + FORMAT_STRING;
			break;
		}
		return url;
	}
	
	public static void getImageURLS(FlickrContainer fCon) {
		String url = createUrl(FLICKR_GET_SIZES_ID, fCon.mId);
		ByteArrayOutputStream baos = URLConnector.readBytes(url);
		String json = baos.toString();
		try {
			JSONObject root = new JSONObject(json.replace("jsonFlickrApi(", "").replace(")", ""));
			JSONObject sizes = root.getJSONObject("sizes");
			JSONArray size = sizes.getJSONArray("size");
			for (int i = 0; i < size.length(); i++) {
				JSONObject image = size.getJSONObject(i);
				if (image.getString("label").equals("Small")) {
					fCon.setThumbURL(image.getString("source"));
				} else if (image.getString("label").equals("Medium")) {
					fCon.setLargeURL(image.getString("source"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static class GetThumbnailsThread extends Thread {
		ContentThreadHandler mMessageHandler;
		FlickrContainer mFlickrContainer;

		public GetThumbnailsThread(ContentThreadHandler chndlr, FlickrContainer flickrContainer) {
			mMessageHandler = chndlr;
			mFlickrContainer = flickrContainer;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			/*mFlickrContainer.mThumb = getThumbnail(mFlickrContainer);
			if (mFlickrContainer.mThumb != null) {
				Message msg = Message.obtain(uih, UIHandler.ID_UPDATE_ADAPTER);
				uih.sendMessage(msg);

			}*/
		}

	}
	
	public static ArrayList<FlickrContainer> searchImagesByTag(Context ctx, String tag,ContentThreadHandler chndlr) {
		String url = createUrl(FLICKR_PHOTOS_SEARCH_ID, tag);
		ArrayList<FlickrContainer> tmp = new ArrayList<FlickrContainer>();
		String jsonString = null;
		try {
			if (URLConnector.isOnline(ctx)) {
				ByteArrayOutputStream baos = URLConnector.readBytes(url);
				jsonString = baos.toString();
			}
			try {
				JSONObject root = new JSONObject(jsonString.replace("jsonFlickrApi(", "").replace(")", ""));
				JSONObject photos = root.getJSONObject("photos");
				JSONArray imageJSONArray = photos.getJSONArray("photo");
				for (int i = 0; i < imageJSONArray.length(); i++) {
					JSONObject item = imageJSONArray.getJSONObject(i);
					FlickrContainer flickrCon = new FlickrContainer(item.getString("id"));
					flickrCon.mPosition = i;
					tmp.add(flickrCon);
					getImageURLS(flickrCon);
				}
				Message msg = Message.obtain(chndlr, ContentThreadHandler.TAGGED_DATA_FETCHED);
				msg.obj = tmp;
				chndlr.sendMessage(msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NullPointerException nue) {
			nue.printStackTrace();
		}

		return tmp;
	}

}
