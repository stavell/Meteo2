package org.shumenxc.meteo2;

import java.io.File;
import java.io.FileOutputStream;

import android.os.AsyncTask;
import android.os.Environment;

class SavePhotoAsync extends AsyncTask<byte[], String, String> {
    
	@Override
    protected String doInBackground(byte[]... jpeg) {
      File targetFile = new File(Environment.getExternalStorageDirectory(), "photo.jpg");

      if (targetFile.exists()) {
    	  targetFile.delete();
      }

      try {
        FileOutputStream fos=new FileOutputStream(targetFile.getPath());
        fos.write(jpeg[0]);
        fos.close();	        
      }
      catch (java.io.IOException e) {
        
    	  
      } 
      
      return(null);
    }
 
    
  }