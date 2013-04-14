package org.shumenxc.meteo2;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
 


@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {


	private ScheduledExecutorService scheduleTaskExecutor;
	private ScheduledFuture<?> takePhotoThread;

	
	private BluetoothInterface bt;
	public CameraInterface camera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
		
		
		bt = new BluetoothInterface(hStat, h);
		camera = new CameraInterface(hCamStat, getApplicationContext()); 
		
	} 

	
	@Override
	protected void onResume() {
		bt.connect();
		camera.open(); 
		
		
		takePhotoThread = scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			  public void run() {
				    camera.takePicture();
			  }
		}, 0, Long.parseLong((String)Settings.systemParams.get("push_interval")), TimeUnit.SECONDS);
		
	
		
		super.onResume();
	}
	
	
	
	protected void onPause() {
		bt.close();
		camera.close();
		
		takePhotoThread.cancel(true);
		
		super.onPause();
	};
	

	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			final String raw = msg.getData().getString("raw");
			Log.e("BT RAW", raw);
			
			new AsyncTask<String, String, String>() {
				@Override
				protected String doInBackground(String... params) {
					try { 
						ArrayList<NameValuePair> reqParams = new ArrayList<NameValuePair>();
						reqParams.add(new BasicNameValuePair("raw", raw));
						HTTPRequest.post(reqParams, HTTPRequest.getPost("http://shumen-xc.org/meteo/raw.php?raw="+raw));
					} catch (Exception e) {
						Log.e("http", e.getMessage());
					} 
					
					return null;
				}
			}.execute(); 
			
			super.handleMessage(msg);
		};
	};
 

	Handler hStat = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.e("bt connection status",Integer.valueOf(msg.arg1).toString());
			
			if( msg.arg1 != 1 ) bt.connect();
		
			super.handleMessage(msg);
		}
		
	};
 
	
	Handler hCamStat = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
		}
		
	};
	

}
