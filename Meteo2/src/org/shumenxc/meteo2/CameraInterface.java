package org.shumenxc.meteo2;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import android.content.Context;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraInterface {

	
	private Camera mCamera; 
	private Handler handler;
	private Context context;
	
	
	public CameraInterface(Handler h, Context c) {
		handler = h;
		context = c;
	}
	
	
	public void open() {
		
		try {
			
			mCamera = Camera.open();
		
			mCamera.setErrorCallback(new Camera.ErrorCallback() {
				@Override
				public void onError(int error, Camera camera) {
					mCamera.release();
				}
			});
			
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
			
			SurfaceView dummyView = new SurfaceView(context);
			SurfaceHolder dummyHolder = dummyView.getHolder();
			dummyHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
			
			mCamera.setPreviewDisplay(dummyHolder);
			
			
		} catch (Exception e) {
			Log.e("Camera err", e.getMessage());
		}
		
		
	}
	
	public void close() {
		try {
			mCamera.release();
		} catch (Exception e) {
			Log.e("Camera err", e.getMessage());
		}
	}
	
	
	public void takePicture() {
		
		try { 
			
			mCamera.startPreview();
			mCamera.takePicture(null, null, new Camera.PictureCallback() {
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					mCamera.stopPreview(); 
					 
					new AsyncTask<byte[], String, Boolean>() {
						@Override
						protected Boolean doInBackground(byte[]... params) {
							  try {
								  File targetFile = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
								
								  if (targetFile.exists()) {
									  targetFile.delete();
								  }
							  	FileOutputStream fos=new FileOutputStream(targetFile.getPath());
							    fos.write(params[0]);
							    fos.close();
							    
							  } catch (Exception e) {
							    Log.e("pic save err", e.getMessage());
							    return false;
							  } 									  
							return true;
						};
						protected void onPostExecute(Boolean result) {
							if(!result) return;
								
							new PushPhotoToServer().execute("");
 
						};
					  }.execute(data);
					   
				}
			});
			
			Message msg = handler.obtainMessage();
			msg.arg1 = 1;
			handler.sendMessage(msg);
			
		
		
		} catch (Exception e) {
			mCamera.stopPreview();
			Log.e("take picture failed",e.getMessage());
		}
		
		
	
	}
	
}
