package com.example.hhvanet;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;

import com.example.utils.CarNetActivityManager;
import com.example.utils.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class SignActivity extends Activity{
	private static final String TAG = "SignActivity";
	private EditText carNum;
	private EditText ProductorId;
	private EditText TerminalNum;
	private EditText TerminalCode;
	private EditText Phone;
	private Button sign;
	private Button login;
	//车辆相关信息
	private String CarIdentification = " ";
	private String ManufactureId = " ";
	private String TerminalType = " ";
	private String Terminal_Code = " ";
	private String RegisterPhone = " ";
////	//创建Socket
//	private static MyThread myThread;
	//字节计数
	private byte[] receiveData = new byte[100];
	private int k =0;
	private byte[] registerConfrim = new byte[12];
	private byte[] registerInfo = new byte[100];
	private boolean IsRegister = false;
	//自动登录
	private SharedPreferences sp; 
	private CheckBox rem_pw, auto_login;
	@Override
	 public void onCreate(Bundle savedInstanceState)
	  {
	   super.onCreate(savedInstanceState); 
//       //去除标题  
//       this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	   setContentView(R.layout.sign);
	   carNum = (EditText)findViewById(R.id.carNum);
		ProductorId = (EditText)findViewById(R.id.ProductorId);
		TerminalNum = (EditText)findViewById(R.id.TerminalNum);
		TerminalCode = (EditText)findViewById(R.id.TerminalCode);
		Phone = (EditText)findViewById(R.id.Phone);
		sign = (Button)findViewById(R.id.sign);
		login = (Button)findViewById(R.id.Login);
		rem_pw = (CheckBox)findViewById(R.id.cb_mima);
		auto_login = (CheckBox)findViewById(R.id.cb_auto);
//		//获得实例对象  
//        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE); 
//		//判断记住密码多选框的状态  
//	      if(sp.getBoolean("ISCHECK", false))  
//	        {  
//	          //设置默认是记录密码状态  
//	          rem_pw.setChecked(true);  
//	          carNum.setText(sp.getString("CarNum", ""));  
//	          ProductorId.setText(sp.getString("ProductorId", "")); 
//	          TerminalNum.setText(sp.getString("TerminalNum", "")); 
//	          TerminalCode.setText(sp.getString("TerminalCode", "")); 
//	          Phone.setText(sp.getString("Phone", "")); 
//	          //判断自动登陆多选框状态  
//	          if(sp.getBoolean("AUTO_ISCHECK", false))  
//	          {  
//	                 //设置默认是自动登录状态  
//	                 auto_login.setChecked(true);  
//	                //跳转界面  
//	                Intent intent = new Intent(SignActivity.this,MainActivity.class);  
//	               startActivity(intent);  
//	                  
//	          }  
//	        }  
//	          
//		sign.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				Intent intent = new Intent(SignActivity.this,MainActivity.class);
////				startActivity(intent);
//				byte[] a = SendInfo(2);
//				send(SendInfo(2));
//				System.out.println("安卓想核心板发送注册请求");
//				for (int i = 0; i < a.length; i++) {
//					System.out.println(" " + a[i]);
//				}
//				
//			}
//		});
		login.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				send(SendInfo(1));
//				if (IsRegister) {
//					send(SendInfo(0));
//					if(rem_pw.isChecked())  
//                    {  
//                     //记住用户名、密码、  
//                      Editor editor = sp.edit();  
//                      editor.putString("CarNum", CarIdentification);  
//                      editor.putString("ProductorId",ManufactureId);  
//                      editor.putString("TerminalNum", TerminalType);  
//                      editor.putString("TerminalCode",Terminal_Code);  
//                      editor.putString("Phone", RegisterPhone);  
//                      editor.commit();  
//                    }  
					Intent intent = new Intent(SignActivity.this,MainActivity.class);
//					startActivity(intent);
//				}
			}
		});
		
//		//监听记住密码多选框按钮事件  
//        rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
//            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
//                if (rem_pw.isChecked()) {  
//                      
//                    System.out.println("记住密码已选中");  
//                    sp.edit().putBoolean("ISCHECK", true).commit();  
//                      
//                }else {  
//                      
//                    System.out.println("记住密码没有选中");  
//                    sp.edit().putBoolean("ISCHECK", false).commit();  
//                      
//                }  
//  
//            }  
//        });  
//          
//        //监听自动登录多选框事件  
//        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
//            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
//                if (auto_login.isChecked()) {  
//                    System.out.println("自动登录已选中");  
//                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();  
//  
//                } else {  
//                    System.out.println("自动登录没有选中");  
//                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
//                }  
//            }  
//        });  
		CarNetActivityManager.getInstance().addActivity(SignActivity.this);
          
	  }
    
	private Socket socket;
	
//	private class MyThread extends Thread {
//		
//		
//			private boolean flag = true;
//			private Handler myHandler;
//			public MyThread(Handler handler) {
//				myHandler = handler;
//			}
//			@Override
//			public void run() {
//				try {
//					socket = new Socket("192.168.0.1", 8888);
//					if (socket.isConnected()) {
//						Log.d(TAG, "连接1已建立");
//					}
//					
//					BufferedInputStream br= new BufferedInputStream(socket.getInputStream());
//				    DataInputStream dis=new DataInputStream(br);
//					 while(br !=null && socket.isConnected()&&flag){
//						int i = dis.readInt();
//						Message msg = new Message();
//						msg.arg1 = i;
//						myHandler.sendMessage(msg);
//					}
//					br = null;
//			        dis = null;
//			        socket.close();
//				}catch (Exception e) {
//					e.printStackTrace();			
//				}	
//		}
//			public void close(){
//				flag = false;
//			}
//		}
//	
//	/**  
//     * 发送消息 
//     */  
//    public void send(final byte[] data) {  
//        new Thread() {  
//            @Override  
//            public void run() {  
//  
//                try {  
//                	OutputStream os = socket.getOutputStream();
//                	if (os != null) {
//                        os.write(data); // 写一个UTF-8的信息  
//                        System.out.println("发送消息");  
//					}
//                } catch (IOException e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//        }.start();  
//    }  
//  
// 
//	 Handler handler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				int i =  msg.arg1;
//				byte[] temp = util.intToBytes2(i);
//				System.arraycopy(temp, 0, receiveData, 4*k, 4);
//				System.out.println(temp.toString());
//				k++;
//				if (k == 3 &&temp[2]==0x0d&&temp[3]==0x0d) {
//					System.arraycopy(receiveData,0,registerConfrim,0,12);
//					parse(registerConfrim);
//					Arrays.fill(receiveData, (byte)0);
//					k=0;
//				}
//				if (k == 25) {
//					System.arraycopy(receiveData,0,registerInfo, 0, 100);
//					parse(registerInfo);
//					carNum.setText(CarIdentification);
//					ProductorId.setText(ManufactureId);
//					TerminalNum.setText(TerminalType);
//					TerminalCode.setText(Terminal_Code);
//					Phone.setText(RegisterPhone);
//					Arrays.fill(receiveData, (byte)0);
//					k=0;
//				}
//			}
//	 };
//	 private void parse(byte[] data){
//		//验证校验码
//			byte correct = 0;
//			//内容+校验位+包尾长度
//			int length = 0;
//			//存放包长字节
//			byte[] len = new byte[4];
//			Arrays.fill(len, (byte)0);
//			if(data[0]!=((byte)0x29)||data[1]!=((byte)0x29)){
//				return ;
//			}else {
//				System.arraycopy(data, 5, len, 0, 2);
//				length = util.bytesToInt(len,0);
//				Log.d(TAG,"lenght" + length);
//				
//				//存储数据包内容
//				byte[] content = new byte[length-2];
//				for(int i = 0 ; i < length+4;i++){
//					correct = (byte) (correct ^data[i]);
//				}
//				if (correct != data[ length+4]) {
//					return;
//				}
//				System.arraycopy(data, 7, content, 0,length-3);
//				switch (data[2]) {
//				case ((byte)0x32):
//					if (((byte)data[7]&(byte)0x03) == (byte)0x01) {
//						IsRegister = true;
//					}
//					break;
//				case ((byte)0x33):
//					try {
//						ParseRegisterInfo(content);
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				break;
//				default:
//					break;
//				}
//	 }
//}
//	 private void ParseRegisterInfo(byte[] data) throws UnsupportedEncodingException{
//		 byte[] temp = new byte[20];
//		 byte[] temp1 = new byte[10];
//		 System.arraycopy(data, 0, temp, 0, 20);
//		 CarIdentification = new String(temp,"GBK");
//		 Log.d(TAG, CarIdentification);
//		 System.arraycopy(data, 20, temp, 0, 20);
//		 ManufactureId = new String(temp,"GBK");
//		 Log.d(TAG, ManufactureId);
//		 System.arraycopy(data, 40, temp, 0, 20);
//		 TerminalType = new String(temp,"GBK");
//		 Log.d(TAG, TerminalType);
//		 System.arraycopy(data, 60, temp1, 0, 10);
//		 Terminal_Code = new String(temp1,"GBK");
//		 Log.d(TAG, Terminal_Code);
//		 System.arraycopy(data, 70, temp, 0, 20);
//		 RegisterPhone = new String(temp,"GBK");
//		 Log.d(TAG, RegisterPhone);
//	 }
//	 /**
//	  * 打包要发送的消息
//	  * @param type 发送消息的类型 0 ：安卓下发11p注册信息；
//	  * 1：安卓向11p确认服务器注册信息；2：安卓向11p发送注册信息请求
//	  * @return 消息
//	  */
//	 private byte[] SendInfo(int type){
//		 if (type == 0) {
//			 byte[] sendcarInfo = new byte[100];
//			 Arrays.fill(sendcarInfo, (byte) 0x20);
//			 sendcarInfo[0] = (byte)0x29;
//			 sendcarInfo[1] = (byte)0x29;
//			 sendcarInfo[2] = (byte)0x31;
//			 sendcarInfo[3] = (byte)0x00;
//			 sendcarInfo[4] = (byte)0x00;
//			 byte[] length = util.unsignedShortToByte2(93);
//			 System.arraycopy(length, 0, sendcarInfo, 5, 2);
//			 System.arraycopy(CarIdentification.getBytes(),0,sendcarInfo,7,CarIdentification.getBytes().length);
//			 System.arraycopy(ManufactureId.getBytes(),0,sendcarInfo,27,ManufactureId.getBytes().length);
//			 System.arraycopy(TerminalType.getBytes(),0,sendcarInfo,47,TerminalType.getBytes().length);
//			 System.out.println(TerminalType.getBytes().length);
//			 System.arraycopy(Terminal_Code.getBytes(),0,sendcarInfo,67,Terminal_Code.getBytes().length);
//			 System.out.println(Terminal_Code.getBytes().length);
//			 System.arraycopy(RegisterPhone.getBytes(),0,sendcarInfo,77,RegisterPhone.getBytes().length);
//			 System.out.println(RegisterPhone.getBytes().length);
//			 for(int i = 0 ; i < 97;i++){
//					sendcarInfo[97] = (byte) (sendcarInfo[97] ^sendcarInfo[i]);
//				}
//			 sendcarInfo[98] = (byte)0x0d;
//			 sendcarInfo[99] = (byte)0x0d;
//			 return sendcarInfo;
//		}
//		if (type == 1) {
//			byte[] sendConfirm = new byte[12];
//			 Arrays.fill(sendConfirm, (byte) 0x00);
//			sendConfirm[0] = (byte)0x29;
//			sendConfirm[1] = (byte)0x29;
//			sendConfirm[2] = (byte)0x34;
//			byte[] length = util.unsignedShortToByte2(5);
//		    System.arraycopy(length, 0, sendConfirm, 5, 2);
//		    if (IsRegister) {
//		    	sendConfirm[7] = (byte)0x01;
//			}else {
//				sendConfirm[7] = (byte)0x02;
//			}
//		    sendConfirm[8] = (byte)0x00;
//		    for(int i = 0 ; i < 9;i++){
//		    	sendConfirm[9] = (byte) (sendConfirm[8] ^sendConfirm[i]);
//			}
//		    sendConfirm[10] = (byte)0x0d;
//		    sendConfirm[11] = (byte)0x0d;
//		    return sendConfirm;
//		}
//		else {
//			byte[] sendRegister = new byte[12];
//			 Arrays.fill(sendRegister, (byte) 0x00);
//			sendRegister[0] = (byte)0x29;
//			sendRegister[1] = (byte)0x29;
//			sendRegister[2] = (byte)0x36;
//			byte[] length = util.unsignedShortToByte2(5);
//		    System.arraycopy(length, 0, sendRegister, 5, 2);
//		    sendRegister[7] = (byte)0x00;
//		    sendRegister[8] = (byte)0x00;
//		    for(int i = 0 ; i < 9;i++){
//		    	sendRegister[9] = (byte) (sendRegister[9] ^sendRegister[i]);
//			}
//		    sendRegister[10] = (byte)0x0d;
//		    sendRegister[11] = (byte)0x0d;
//			return sendRegister;
//		}
//		 
//	 }
//	    @Override
//		protected void onStop() {
//			// TODO Auto-generated method stub
//			super.onStop();
//			myThread.close();
//		}
//
//		@Override
//		protected void onStart() {
//			// TODO Auto-generated method stub
//			super.onStart();
//		 myThread = new MyThread(handler);
//		 myThread.start();
//		}
//	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return super.onKeyDown(keyCode, event);
	}
    private void exit() {
		new AlertDialog.Builder(SignActivity.this)
		.setTitle("退出提示")
		.setMessage("退出车联网专项吗？")
		// 取消按键
		.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				})
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						CarNetActivityManager.getInstance().exit();
					}
				}).create().show();
	}
}
