package org.shumenxc.meteo2;

import java.io.File;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

public class HTTPRequest {

	public static HttpResponse post(List<NameValuePair> nameValuePairs, HttpPost httpPost) {
	  
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);
	      
		
		HttpClient httpClient = new DefaultHttpClient(httpParams); 
		HttpContext localContext = new BasicHttpContext();
	    HttpResponse response = null; 
	     
	   
	    try {
	        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


	        for(int index=0; index < nameValuePairs.size(); index++) {
	            if(nameValuePairs.get(index).getName().equalsIgnoreCase("file")) {
	                // If the key equals to "image", we use FileBody to transfer the data
	                entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
	            } else {
	                // Normal string data
	                entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
	            }
	        }

	        httpPost.setEntity(entity);

	        response = httpClient.execute(httpPost, localContext); 

	    } catch (Exception e) {
	        Log.v("http", e.getMessage()); 
	    }
	    
		return response;
	}
	 
	
	public static HttpPost getPost(String url) {
		return new HttpPost(url);
	}
	
	
}
