package org.shumenxc.meteo2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
 


public class MainActivity extends Activity {


	private Intent cameraIntent = null;
	private ScheduledExecutorService scheduleTaskExecutor;
	private ScheduledFuture<?> takePhotoThread;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
		
		takePhotoThread = scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			  public void run() {
				    takePhoto();

			  }
		}, 0, Long.parseLong((String)Settings.systemParams.get("push_interval")), TimeUnit.SECONDS);

		
		
	} 
	
	public void takePhoto() {
		if(cameraIntent != null) return;
		cameraIntent = new Intent(this, CameraActivity.class);
		startActivityForResult(cameraIntent, 999);	
	}


	protected void onActivityResult( int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 999) {
			cameraIntent = null;
			if (resultCode == RESULT_OK) {
				new PushToServer().execute("");
		    	 
            } 
			
		}

	}


}
