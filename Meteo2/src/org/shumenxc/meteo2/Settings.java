package org.shumenxc.meteo2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Settings {
 
	static Hashtable<String, Object> cameraParams = new Hashtable<String, Object>();
	static Hashtable<String, Object> systemParams = new Hashtable<String, Object>();
	public static String host = new String("http://stavl.com/meteo2/entry.php"); 
	
	
	static { 
   
		try {
			systemParams.put("push_interval", "60");
			
			cameraParams.put("cameraselect", "camera-back");
			cameraParams.put("picture-size", "800x480");
			cameraParams.put("jpeg-quality", 90);
			 
		} catch (Exception e) {
			Log.e("settings", e.getMessage());
		}  
		
	}
	 
	
	
	public static void parseSettingsFromHTTPResponse( HttpResponse response ) {
	
		try {
			StringBuilder builder = new StringBuilder();
			 
			if (response.getStatusLine().getStatusCode() != 200) throw new Exception("bad response status");
		      
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        } 
	        
        	JSONArray jsonArr = new JSONArray(builder.toString()); 
        	
        	Hashtable<String, Object> params = new Hashtable<String, Object>(); 
        	
	        for (int i = 0; i < jsonArr.length(); i++) {
	            JSONObject jsonObject = jsonArr.getJSONObject(i);
	            params.put(jsonObject.getString("key"), jsonObject.get("value")); 
	        }
	        
	        setSettings(params);
	        
		} catch (Exception e) {
			Log.e("http settings", e.getMessage());
		}
		 
		
	}
	
	
	public static void setSettings( Hashtable<String, Object> params ) {
		 
		Iterator<String> ittr = params.keySet().iterator();
		cameraParams.clear();
		systemParams.clear();
		
		while(ittr.hasNext()) {
			String key = ittr.next(); 
			
			if( key.indexOf("camera_") != -1 ) {
				cameraParams.put(key.substring(7), params.get(key));
			}
			
			if( key.indexOf("system_") != -1 ) {
				systemParams.put(key.substring(7), params.get(key));
			}
			 
		} 
		
	}
	
	
	
	
	
}
