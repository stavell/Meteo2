package org.shumenxc.meteo2;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

@SuppressLint("HandlerLeak")
public class BluetoothService extends Service {

	private BluetoothInterface bt;
	
	
	@Override
	public void onCreate() {

		bt = new BluetoothInterface(hStat, h);
		
		bt.connect();
		
		super.onCreate();
	}
	
	
	@Override
	public void onDestroy() {

		bt.close();

		super.onDestroy();
		
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
	
	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			final String raw = "android:"+msg.getData().getString("raw");
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
	

}
