package com.shadows.sliding;

import java.io.File;
import java.util.ArrayList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Sliding extends Activity {

	private Uri mImageCaptureUri;
	ImageView img;
	Util util;
	Grid grid;
	boolean easy = false;
	Bitmap aux;
	ArrayList<Uri> fileList = new ArrayList<Uri>();
	Integer[] view = new Integer[10]; 
    private static final int CROPPED_IMAGE = 1;
    private static final int PICK_FROM_FILE = 2;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sliding);
		util = new Util(this);
		grid = new Grid(this);
		view[0] = R.id.image00;
		view[1] = R.id.image01;
		view[2] = R.id.image02;
		view[3] = R.id.image03;
		view[4] = R.id.image04;
		view[5] = R.id.image05;
		view[6] = R.id.image06;
		view[7] = R.id.image07;
		view[8] = R.id.image08;
		view[9] = R.id.image09;
	}
	
	public void click(View press){
		for(int i=1;i<10;i++)
			if (press.getId()==view[i]){
				if(easy){grid.change(i,grid.getBlank());break;}
				else if(findNeighbors(i,grid.getBlank())){grid.change(i,grid.getBlank());break;}
			}
	}
	
	public void shuffle(View view){
		grid.shuffle();
	}
	
	public boolean findNeighbors(int a,int b){
		switch(a){
			case 1:switch(b){case 2:return true;case 4:return true;}break;
			case 2:switch(b){case 1:return true;case 3:return true;case 5:return true;}break;
			case 3:switch(b){case 2:return true;case 6:return true;}break;
			case 4:switch(b){case 1:return true;case 5:return true;case 7:return true;}break;
			case 5:switch(b){case 2:return true;case 4:return true;case 6:return true;case 8:return true;}break;
			case 6:switch(b){case 3:return true;case 5:return true;case 9:return true;}break;
			case 7:switch(b){case 4:return true;case 8:return true;}break;
			case 8:switch(b){case 5:return true;case 7:return true;case 9:return true;}break;
			case 9:switch(b){case 6:return true;case 8:return true;}break;
		}
		return false;
	}
	
	public void options(View button){
		TextView t = (TextView) findViewById(R.id.text_dif);
		switch(button.getId()){
		case R.id.button_easy:
			t.setText(R.string.easy);
			easy = true;
			break;
		case R.id.button_normal:
			t.setText(R.string.normal);
			easy=false;
			break;
		case R.id.button_options:
			setContentView(R.layout.options);
			TextView tx = (TextView) findViewById(R.id.text_dif);
			if (easy)
				tx.setText(R.string.easy);
			else
				tx.setText(R.string.normal);
			break;
		case R.id.button_options_ok:
			setContentView(R.layout.activity_sliding);
			grid.print(); 
			break;
		}
	}
	
	public void selecPic(View view){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_FROM_FILE);
	}
	ImageAdapter adapter;
	public void gallery(View view){
		setContentView(R.layout.gallery);		 
        GridView gallery = (GridView) findViewById(R.id.gallery);
        fileList.clear();
        fileList.addAll(util.getFileList());
        adapter = new ImageAdapter(this,fileList);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,int pos, long id) {
            	setContentView(R.layout.activity_sliding);
            	grid.load(fileList.get(pos));
            }
        });
        gallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	public boolean onItemLongClick(AdapterView<?> parent,View v,int pos, long id){        		
                //checkLong = true;
                //adapter.(adapter.getItem(pos));
        		File del = new File(fileList.get(pos).toString());
        		boolean deleted = del.delete();
        		if (deleted) Toast.makeText(getApplicationContext(), getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
        		else  Toast.makeText(getApplicationContext(), getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
        		adapter.notifyDataSetChanged();
        		setContentView(R.layout.activity_sliding);
        		grid.print();        		
        		return !deleted;
        	}
		});
	}
	 
		@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode != RESULT_OK) return;
 
		if (requestCode == PICK_FROM_FILE){ 
		        mImageCaptureUri = data.getData();
		        Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setType("image/*");
			    intent.setData(mImageCaptureUri);
			    intent.putExtra("crop", "true");
			    intent.putExtra("outputX", util.getSize());
			    intent.putExtra("outputY", util.getSize());
		        intent.putExtra("aspectX", 1);
		        intent.putExtra("aspectY", 1);
			    intent.putExtra("return-data", false);
			    intent.putExtra("scale", true);
		        intent.putExtra("noFaceDetection",true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, util.gettempUri(1));
			    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			    startActivityForResult(intent, CROPPED_IMAGE);
		}
		else if (requestCode == CROPPED_IMAGE){
			grid.load(util.gettempUri(2));
		}
	}
}