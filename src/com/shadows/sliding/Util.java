package com.shadows.sliding;

import java.io.File;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

public class Util{
	static int size,h,w;
	static int filecount=0;
	static File tempFile;
	static File dir;
	static Uri tempUri;
	ArrayList<Uri> fileList = new ArrayList<Uri>();
	
	//Constructor
	public Util(Context ctx){
		setSize(ctx);
		setTempUri();
	}
	
	public float getFactor(){return Math.abs(h-w);}
	public int getSize(){return size;}
	public Uri gettempUri(int i){if(i==1)setTempUri();return tempUri;}
	public ArrayList<Uri> getFileList(){ListDir(dir);return fileList;}
	
	@SuppressWarnings("deprecation")
	public void setSize(Context ctx){
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		w=0;
		h=0;
		if(android.os.Build.VERSION.SDK_INT >= 13){
			sizeaux(display);
		}
		else{
			w = display.getWidth(); h = display.getHeight();
			if (w<h)size = w;
			else size = h;
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void sizeaux(Display display) {
		w=0;
		h=0;
		Point screen = new Point();
		display.getSize(screen);w=screen.x;h=screen.y;
		if (w<h)size = w;
		else size = h;
	}
	
	public void setTempUri(){
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sliding";
		dir = new File(file_path);
		if(!dir.exists())
			dir.mkdirs();
		do{
			filecount+=1;
			tempFile = new File(dir, "slidingtmp" +filecount+ ".jpg");
		}while(tempFile.exists());
		filecount = 0;
		tempUri = Uri.fromFile(tempFile);
	}
	
	public void cleanTempFile(){
		if(tempFile.exists())tempFile.delete();
	}
	
	 void ListDir(File f){
	     File[] files = f.listFiles();
	     fileList.clear();
	     for (File file : files){
	      fileList.add(Uri.fromFile(file));  
	     }
	 }
	
}
