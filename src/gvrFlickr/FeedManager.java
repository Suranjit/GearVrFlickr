package gvrFlickr;

import java.util.ArrayList;
import java.util.List;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRCameraRig;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMesh;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;
import org.gearvrf.GVRTexture;
import org.gearvrf.GVRRenderData.GVRRenderMaskBit;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVRAnimationEngine;
import org.gearvrf.scene_objects.GVRVideoSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject.GVRVideoType;
import org.vr.gvrsocial.R;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;

public class FeedManager extends GVRScript{

	ContentLoader mContentLoader;
	private static final float ANIMATION_DURATION = 0.3f;
    private static final float SELECTED_SCALE = 2.0f;

    private GVRContext mGVRContext = null;
    private List<GVRSceneObject> mBoards = new ArrayList<GVRSceneObject>();
    private GVRSceneObject mBoardParent;
    private int mSelected = 0;
    private GVRAnimationEngine mAnimationEngine;

    private static final int LOOK_UP = 1;
    private static final int LOOK_FRONT = 0;
    private static final int LOOK_DOWN = -1;
    private static final float LOOK_AT_THRESHOLD = 0.2f;
    private int mLookAtMode = LOOK_FRONT;
    private GVRAnimation mRotationAnimation;
    
    
    private int RENDER_STATE_INITIAL = 256;
    private int RENDER_STATE_LOAD_CONTENT = 128;
    private int RENDER_STATE_INIT_CONTENT_LOADED= 64;
    private int RENDER_STATE = RENDER_STATE_INITIAL;
    
    GVRScene mainScene;
	
	
	public FeedManager(ContentLoader cLoader) {
		
		mContentLoader = cLoader;
		
	}
	@Override
	public void onInit(GVRContext gvrContext) throws Throwable {
		    mGVRContext = gvrContext;

	        mAnimationEngine = gvrContext.getAnimationEngine();

	         mainScene = mGVRContext.getNextMainScene();

	        mainScene.getMainCameraRig().getLeftCamera()
	                .setBackgroundColor(Color.WHITE);
	        mainScene.getMainCameraRig().getRightCamera()
	                .setBackgroundColor(Color.WHITE);

	        mainScene.getMainCameraRig().getTransform()
	                .setPosition(0.0f, 0.0f, 0.0f);

	   
		
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub
		if(RENDER_STATE==RENDER_STATE_LOAD_CONTENT ) {
			addContentBoards();
			RENDER_STATE = RENDER_STATE_INIT_CONTENT_LOADED;
		}
		
		
	}
	
	public void postUpdateContent() {
		
		if(RENDER_STATE==RENDER_STATE_INITIAL) {
			RENDER_STATE = RENDER_STATE_LOAD_CONTENT;
		}
		
	}
	
	public void addContentBoards() {
	     List<GVRTexture> numberTextures = new ArrayList<GVRTexture>();

        for (int i=0;i<5;i++) {
            numberTextures.add(new GVRBitmapTexture(mGVRContext,mContentLoader.getBitmapImageSync(i)));
        }

        for (int i = 0; i < numberTextures.size(); ++i) {
            GVRSceneObject number = new GVRSceneObject(mGVRContext, 2.0f, 1.0f,
                    numberTextures.get(i));
            number.getTransform().setPosition(0.0f, 0.0f, -5.0f);
            float degree = 360.0f * i / (numberTextures.size() + 1);
            number.getTransform().rotateByAxisWithPivot(degree, 0.0f, 1.0f,
                    0.0f, 0.0f, 0.0f, 0.0f);
            mBoards.add(number);
        }


        mBoardParent = new GVRSceneObject(mGVRContext);

        for (GVRSceneObject board : mBoards) {
            mBoardParent.addChildObject(board);
        }

        mainScene.addSceneObject(mBoardParent);

        mBoardParent.getTransform().rotateByAxisWithPivot(90.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 0.0f);

        mBoards.get(mSelected).getTransform()
                .setScale(SELECTED_SCALE, SELECTED_SCALE, 0.0f);
	}

}
