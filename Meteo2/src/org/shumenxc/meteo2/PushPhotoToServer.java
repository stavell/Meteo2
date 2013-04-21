package org.shumenxc.meteo2;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

	
	class PushPhotoToServer extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			 
			ArrayList<NameValuePair> reqParams = new ArrayList<NameValuePair>(); 
			
			reqParams.add(new BasicNameValuePair("file", Environment.getExternalStorageDirectory()+"/photo.jpg"));
//			reqParams.add(new BasicNameValuePair("settings_version", (String) Settings.systemParams.get("settings_version")));

			try { 
				HttpResponse response = HTTPRequest.post(reqParams, HTTPRequest.getPost(Settings.host));

				Settings.parseSettingsFromHTTPResponse(response);  
				
			} catch (Exception e) {
				Log.e("http", e.getMessage());
			} 
			
			return null;
		}
		
		
		
		
	}