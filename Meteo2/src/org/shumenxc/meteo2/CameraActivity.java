package org.shumenxc.meteo2;

import java.util.Iterator;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraActivity extends Activity {

	private Camera mCamera; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		   
		try {
			mCamera = Camera.open();
			
			mCamera.setErrorCallback(new Camera.ErrorCallback() {
				@Override
				public void onError(int error, Camera camera) {
					mCamera.release();
				}
			});
			
			
			try {
				Camera.Parameters cParams = mCamera.getParameters();  
				Iterator<String> it = Settings.cameraParams.keySet().iterator(); 
				
				while(it.hasNext()) {
					String key = it.next();
					Object val = Settings.cameraParams.get(key);
					String type = val.getClass().getSimpleName();
	 			 
					if(type.equalsIgnoreCase("Integer")) {
						cParams.set(key, (Integer)Settings.cameraParams.get(key));	
					} else {
						cParams.set(key, (String)Settings.cameraParams.get(key));
					}
				}  
				mCamera.setParameters(cParams);
				
			} catch (Exception e) {
				Log.e("camera", e.getMessage());
			}

		  
			SurfaceView dummyView = new SurfaceView(getApplicationContext());
			SurfaceHolder dummyHolder = dummyView.getHolder();
			dummyHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
			
			mCamera.setPreviewDisplay(dummyHolder);
			
			mCamera.startPreview();
			
			mCamera.takePicture(null, null, new Camera.PictureCallback() {
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					mCamera.stopPreview();
					mCamera.release();
					
					new SavePhotoAsync().execute(data);   
					
					setResult(RESULT_OK);
					finish();
				}
			});
		 
		} catch (Exception e) {
			Log.e(this.toString(), "Scepi se", e);
			mCamera.release();
			setResult(RESULT_CANCELED);
			finish();
		} 
		
	}
	
	
	@Override
	protected void onPause() {
		mCamera.release();
		super.onPause();
	}
	
 
}



