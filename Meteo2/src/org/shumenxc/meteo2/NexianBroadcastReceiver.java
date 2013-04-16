package org.shumenxc.meteo2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NexianBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {


		Intent BluetoothService = new Intent(context, BluetoothService.class);
		context.startService(BluetoothService); 
		
		Intent CameraService = new Intent(context, CameraService.class);
		context.startService(CameraService);
		
		Log.e("broadcast", intent.getAction());

	}

}
