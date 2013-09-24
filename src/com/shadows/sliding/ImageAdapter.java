package com.shadows.sliding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
	private Context mContext;
	public ArrayList<Uri> uri = new ArrayList<Uri>();
	Util util;
	
	public ImageAdapter(Context c,ArrayList<Uri> array){
        mContext = c;
        uri.clear();
        uri.addAll(array);
        util = new Util(mContext);
    }

	@Override
	public int getCount() {
		return uri.size();
	}

	@Override
	public Object getItem(int pos) {
		return uri.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		Bitmap aux;
		ImageView imageView = null;
		try {
			aux = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri.get(pos));
			aux = Bitmap.createScaledBitmap(aux, util.getSize()/4, util.getSize()/4, false);		
			int size = util.getSize()/2 - 40;
			imageView = new ImageView(mContext);
		        imageView.setImageBitmap(aux);
		        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		        imageView.setLayoutParams(new GridView.LayoutParams(size,size));
		        imageView.setPadding(20, 20, 20, 20);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
        return imageView;
	}
}
