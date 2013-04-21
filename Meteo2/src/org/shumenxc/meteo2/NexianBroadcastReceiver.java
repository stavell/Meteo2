package org.shumenxc.meteo2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NexianBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	
		try {
		
			Settings.BluetoothService = new Intent(context, BluetoothService.class);
			context.startService(Settings.BluetoothService); 
			
		} catch (Exception e) {
			Log.e("btService", e.getMessage());
		}
		
		
		try {
			Settings.CameraService = new Intent(context, CameraService.class);
			context.startService(Settings.CameraService);
		} catch (Exception e) {
			Log.e("camService", e.getMessage());
		}
		
		Log.e("broadcast", intent.getAction());
	}


}
