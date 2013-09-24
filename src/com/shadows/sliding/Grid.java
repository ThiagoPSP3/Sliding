package com.shadows.sliding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Grid {
	Integer[] view = new Integer[10];
	Bitmap[] bit = new Bitmap[10];
	int blank = 9;
	Bitmap aux;
	Util util;
	Activity ctx;
	ImageView img;
	
	public int getBlank(){return blank;}
	public Bitmap getBit(){return bit[0];}
	public Grid(Activity context){
		ctx = context;
		util = new Util(ctx);
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
		bit[0] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm);
		bit[1] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm1);
		bit[2] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm2);
		bit[3] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm3);
		bit[4] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm4);
		bit[5] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm5);
		bit[6] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm6);
		bit[7] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm7);
		bit[8] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm8);
		bit[9] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm9);
		print();
	}
	
	private void adjustScreen() {
		LinearLayout l = (LinearLayout)ctx.findViewById(R.id.linLayout0);
		l.setWeightSum(util.getFactor()+util.getSize());
		l = (LinearLayout)ctx.findViewById(R.id.linLayout1);
		
		LinearLayout.LayoutParams lPar = (LinearLayout.LayoutParams) l.getLayoutParams();
		lPar.weight = util.getFactor()/2;
		l.setLayoutParams(lPar);
		
		l = (LinearLayout)ctx.findViewById(R.id.linLayout5);
		lPar = (LinearLayout.LayoutParams) l.getLayoutParams();
		lPar.weight = util.getFactor()/2;
		l.setLayoutParams(lPar);
		
		l = (LinearLayout)ctx.findViewById(R.id.linLayout2);
		lPar = (LinearLayout.LayoutParams) l.getLayoutParams();
		lPar.weight = util.getSize()/3;
		l.setLayoutParams(lPar);
		
		l = (LinearLayout)ctx.findViewById(R.id.linLayout3);
		lPar = (LinearLayout.LayoutParams) l.getLayoutParams();
		lPar.weight = util.getSize()/3;
		l.setLayoutParams(lPar);
		
		l = (LinearLayout)ctx.findViewById(R.id.linLayout4);
		lPar = (LinearLayout.LayoutParams) l.getLayoutParams();
		lPar.weight = util.getSize()/3;
		l.setLayoutParams(lPar);		
	}
	
	public void print(){
		for(int i=0;i<10;i++){ 
			img = (ImageView) ctx.findViewById(view[i]);
			img.setImageBitmap(bit[i]);
		}
		adjustScreen();
		shuffle();
	}
	
	public void load(Uri image){
		try {
			int auxStep;
			Bitmap auxBitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(),image);
			bit[0] = Bitmap.createScaledBitmap(auxBitmap, util.getSize(), util.getSize(), false);
			if (bit[0].getWidth()>bit[0].getHeight())auxStep = bit[0].getHeight()/3;
			else auxStep = bit[0].getWidth()/3;
			for (int i=0;i<3;i++)
				for(int j=0;j<3;j++)
					bit[i*3+j+1] = Bitmap.createBitmap(bit[0],j*auxStep,i*auxStep,auxStep,auxStep);
			bit[9] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nysm9);
			blank = 9;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		print();
	}
	
	public void shuffle(){
		//shuffle 1000 times, then stops when the blank is in the position number 9
		for(int i=0;i<1000||blank!=9;i++){
			if 		(blank == 1) shuffleAux(2,4,0,0,2);				
			else if (blank == 2) shuffleAux(1,3,5,0,3);
			else if (blank == 3) shuffleAux(2,6,0,0,2);
			else if (blank == 4) shuffleAux(1,5,7,0,3);			
			else if (blank == 5) shuffleAux(2,4,6,8,4);
			else if (blank == 6) shuffleAux(3,5,9,0,3);
			else if (blank == 7) shuffleAux(4,8,0,0,2);			
			else if (blank == 8) shuffleAux(5,7,9,0,3);
			else if (blank == 9) shuffleAux(6,8,0,0,2);
		}
	}
	
	public void shuffleAux(int a, int b, int c,int d,int num){
		Random r = new Random();
		int rand = r.nextInt(num) + 1;
		if 		(rand == 1) change(a,blank);
		else if (rand == 2) change(b,blank);
		else if (rand == 3) change(c,blank);
		else if (rand == 4) change(d,blank);
	}
	
	public void change(int numa,int numb){
		ImageView img = (ImageView) ctx.findViewById(view[numa]);
		img.setImageBitmap(bit[numb]);
		img = (ImageView) ctx.findViewById(view[numb]);		
		img.setImageBitmap(bit[numa]);
		aux = bit[numa];
		bit[numa]=bit[numb];
		bit[numb] = aux;
		blank = numa;
	}
}
