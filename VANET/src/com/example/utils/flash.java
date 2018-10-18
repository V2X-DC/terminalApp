package com.example.utils;

import java.util.Timer;
import java.util.TimerTask;

import com.baidu.mapapi.map.TextOptions;

import android.content.Context;
import android.util.Log;

public class flash {
	private static final String TAG = "flash";
	private FashThread flashThread =null;
	Context context;
	public flash(Context context) {
		this.context = context;
	}
	
	public void  spark(TextOptions textOptions){
		Log.d(TAG,"creat!");
		flashThread = new FashThread(textOptions);
        
		flashThread.start();
//		flashThread.setDaemon(true);
		Log.d(TAG,"start success!");
	}
	
	public  class FashThread extends Thread {
		// TODO Auto-generated constructor stub
		
		
		public FashThread(TextOptions textOptions){
			this.textOptions = textOptions;
		}
		public TextOptions textOptions;
		
		public void  spark() {
			 Timer timer = new Timer();
		     TimerTask task = new TimerTask() {
			public void run() {
				Log.d(TAG,"run success!");
				int color = 0;
					if(color == 0){
						color =1;
						textOptions.fontColor(0xffff0000);//ºìÉ«
						Log.d(TAG,"red");
					}
					if(color == 1){
						color =0;
						textOptions.fontColor(0xffffffff);//°×É«
						Log.d(TAG,"white");
					}
			
			}
				};
				timer.schedule(task, 0,1000);
			 
					}
		@Override 
		public void run(){
			try{
				spark();
			}catch(Exception e){
				
			}
		
		}
	}
	
}


