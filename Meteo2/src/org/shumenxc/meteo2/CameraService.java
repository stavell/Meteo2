package org.shumenxc.meteo2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

@SuppressLint("HandlerLeak")
public class CameraService extends Service {

	private ScheduledExecutorService scheduleTaskExecutor;

	private CameraInterface camera;
	
	
	@Override
	public void onCreate() {
		
		scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
		
		camera = new CameraInterface(hCamStat, getApplicationContext()); 
		
		camera.open();

		Long takePictureInterval = Long.parseLong((String) Settings.systemParams.get("photo_push_interval"));
		
		Log.e("pictureInterval", takePictureInterval.toString());
		
		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			  public void run() {
				  camera.takePicture();
			  }
		}, 0, takePictureInterval, TimeUnit.SECONDS);

		
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {

		camera.close();

		scheduleTaskExecutor.shutdownNow();
		
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {

		return super.onUnbind(intent);
	}
	
	Handler hCamStat = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	
}
