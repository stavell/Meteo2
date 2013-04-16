package org.shumenxc.meteo2;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothInterface {

	private static final UUID SPPUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static String address = "00:12:05:09:94:96";

	BluetoothAdapter btAdapter;
	BluetoothSocket btSocket;
	
	InputStream inStream;
	OutputStream outStream;
	BluetoothDevice device;
	Handler handler;
	Handler handlerAct;
	
	ReceiverThread receiverThread;
	
	public BluetoothInterface( Handler hstatus, Handler h ) {
		
		try {

			btAdapter = BluetoothAdapter.getDefaultAdapter();
					
		} catch (Exception e) {
			Log.e("btInterface", e.getMessage());
		}
		

		handlerAct = h;
		handler = hstatus;
		
		
	}
	
	
	public void sendData(String data) {
		
	}
	
	public void connect() {
		
		new Thread() {
			public void run() {
				try {

					device = btAdapter.getRemoteDevice(address);

					if( device.getBondState() != BluetoothDevice.BOND_BONDED) throw new Exception("Not bonded,please bond and then come back");

					btSocket = device.createRfcommSocketToServiceRecord(SPPUUID);

					inStream = btSocket.getInputStream();
					outStream = btSocket.getOutputStream();
					
					btSocket.connect();
					
					Message msg = handler.obtainMessage();
					msg.arg1 = 1;
					handler.sendMessage(msg);
					
					receiverThread = new ReceiverThread(handlerAct);
					receiverThread.start(); 
					
				} catch (Exception e) {
					Log.e("ConnectionFailed", e.getMessage());
				}
				
			};
		}.start();
		
		
	}
	
	
	public void close() {
		try {
			btSocket.close();
			receiverThread.stop = true;
			
		} catch (Exception e) {
			Log.e("Close failed", e.getMessage());
		}
	}
	
	
	private class ReceiverThread extends Thread {
		
		Handler handler;
		volatile Boolean stop = false;
		byte buffer[] = new byte[255];
		Integer bytes = Integer.valueOf(0);
		StringBuilder sb;
		
		public ReceiverThread(Handler h) {
			handler = h;
			sb = new StringBuilder();
		}

		synchronized protected void kill(Thread t) {
			t = null;
		}
		
		@Override
		public void run() {
			
		    while (true) {
		    	if(stop) this.kill(this);
	        	try {
	        		bytes = inStream.read(buffer);
	        		
                	for (int i = 0; i < bytes; i++) {
                		sb.append(String.format("-%02x", buffer[i]));
					}
                	
                	if(sb.length() > 20 && sb.indexOf("-0a-00") > 0) {
                		Message msg = handler.obtainMessage();
                		Bundle b = new Bundle();
                		b.putString("raw", sb.toString());
                		msg.setData(b);
                		handler.sendMessage(msg);
                		sb.setLength(0);
                	}
                	
	            } catch (Exception e) {
	    	    	Log.e("btinputThread", e.getMessage());
	    	    	break;
	            }
	        }
	        
			super.run();
		}

		
	}
	
	
}
