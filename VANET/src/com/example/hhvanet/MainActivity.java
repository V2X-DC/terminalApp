package com.example.hhvanet;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.e.p;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.example.hhvanet.R;
import com.example.slidrlayout.codetail.animation.SupportAnimator;
import com.example.slidrlayout.codetail.animation.ViewAnimationUtils;
import com.example.slidrlayout.fragment.ContentFragment;
import com.example.slidrlayout.interfaces.Resourceble;
import com.example.slidrlayout.interfaces.ScreenShotable;
import com.example.slidrlayout.model.SlideMenuItem;
import com.example.slidrlayout.util.ViewAnimator;
import com.example.utils.CarInfo;
import com.example.utils.CarNetActivityManager;
import com.example.utils.DatabaseUtil;
import com.example.utils.EventInfo;
import com.example.utils.util;;


/**
 * 车联网专项
 * @author qyr
 *@version 1.0
 */
public class MainActivity extends AppCompatActivity  implements ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator<SlideMenuItem> viewAnimator;
    private LinearLayout linearLayout;
	
	MapView mMapView = null;  //这个是地图主控件
	public static BaiduMap mBaiduMap = null;//地图主控件
	private static final String TAG ="MainActivity";
	WindowManager.LayoutParams lp = null;//窗口布局参数问题
	public int mflickerCount = 6;//闪烁摇曳
	private int TIME = 500;
	Handler handler2 = null;
//	 private int myupdateCount = 0;
	// 定位信息
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean isFirstLoc = true; 
	 //车辆信息按钮
	Button btn1;
	//路线规划
	Button navButton;
	//天气预报
	Button weather;
	Button others;
	//传递报警信息
	Bundle bundle1 = new Bundle();//key-value对，字符串到各种Parcelable types，两个activiy之间的通讯可通过bundle类实现 
	String alarmString = " ";
	//接收消息的次数
    private int mCount = 0;
    private int oCount = 0;
	//车辆信息
	public static CarInfo myCarInfo = new CarInfo();
	public static CarInfo emergentCarInfo = new CarInfo();
	public static CarInfo emergentCarInfo2 = new CarInfo();
	public static CarInfo emergentCarInfo3 = new CarInfo();
	public static CarInfo emergentCarInfo4 = new CarInfo();
	public static CarInfo emergentCarInfo5 = new CarInfo();
	private static List<CarInfo> otherCars = new ArrayList<CarInfo>();
	private byte[] buf1 = new byte[8192];
	private byte[] buf = new byte[8192];
	//		private byte[] INfo = new byte[60];
	//		private byte[] OBU = new byte[48];
	private static MyApplication app;//这个是干啥用的呢？
	
	  //本车是否报警的标志位
	private boolean isTurnOver;
	private boolean isbrake;
	private boolean isTurnLeft;
	private boolean isTurnRight;
	private boolean isCollision;
	private String oldBrakeLevel ;
	private String oldTurnLevel ;
	private String oldOverLevel ;
	private String oldCollisionLevel;
	private String oldSpeedUpLevel;
	
	//读取数据的循环次数
	//		public int k = 0;
	public int id = 1;
	private static MyThread myThread;//这个线程用于创建与Linxu的socket通信
	//地理编码
	private static  String address= " ";
//	TextView carAddress;
	
	//数据库
	private DatabaseUtil mDBUtil;	
	//存储下发事件 
	private static  String InfoDown = " ";
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd "+"hh:mm:ss") ;
	private static int Length = 0; 
	
	//语音报警
	MediaPlayer mpMediaPlayer;
	
	TextView parameter;
	
	//经济驾驶
	private double stdv = 21.169;
	private double stda = 1.977;
	private double stdw1 = 13.1802;
	private double stdw2 = 6.5901;
	private double stdw3 = 3.2951;
	private double stdw4 = 1.977;
	private double stdr1 = 6.5901;
	private double stdr2 = 3.2951;
	private double stdr3 = 1.977;
	private double stdTPI = 21.17;
	private double stdTPId = 13.18;
	/**
	 * 同方向只有1条机动车道的道路，城市道路为每小时不超过50公里
	 */
	private int Vertical_max = 50;
		/**
	     * 设置车辆显示位置以及图标
	     * @param car
	     */
    public void initOverLay(CarInfo car , boolean isMycar) {
    	OverlayOptions option = null;
    	BitmapDescriptor  bd1 = BitmapDescriptorFactory.fromResource(R.drawable.car_w_r);//
		BitmapDescriptor  bd2 = BitmapDescriptorFactory.fromResource(R.drawable.car_r_w);//换成白色背景黑色图标
	    TextOptions textOption;//百度地图上显示文字
        Bundle bundle = new Bundle();//Activity 之间通信
		// 定义Maker坐标点
		LatLng ll = new LatLng(car.latitude, car.longtitude);
		ll = GPS2Baidu(ll);
		LatLng ll1 = new LatLng(myCarInfo.latitude, myCarInfo.longtitude);
		ll1 = GPS2Baidu(ll1);
		LatLng ch= new LatLng(car.latitude+0.0001, car.longtitude);//这个干什么用的呢？在这个图标上面添加文字信息
		ch = GPS2Baidu(ch);
		// 构建Marker图标
		if (isMycar) {
			option = new MarkerOptions().icon(bd1).position(ll);
		}else{
			option = new MarkerOptions().icon(bd2).position(ll);
		}
		
		bundle.putSerializable("car", car.CarPlateNumber);//(key, string)
		// 构建MarkerOption，用于在地图上添加Marker
		
		if((car.isFatigueDriving)||(car.isOverSpeed)
				||(car.BrakeLevel=="快速刹车")||(car.TurnLevel=="快速转弯")||(car.BrakeLevel=="紧急刹车")
				||(car.TurnLevel=="紧急转弯")||(car.OverLevel=="中等侧翻风险")||(car.OverLevel=="高等侧翻风险")
				||(car.OverLevel=="已经侧翻")||(car.CollisionLevel == "中级碰撞风险")||(car.CollisionLevel == "高碰撞风险")){
			textOption = new TextOptions().position(ch).text(car.CarPlateNumber).fontSize(25).fontColor(0xffff0000);//有报警信号显示红色闪烁
		}else{
			textOption = new TextOptions().position(ch).text(car.CarPlateNumber).fontSize(25);//没有危险显示默认色	
		}
		Overlay marker1 = mBaiduMap.addOverlay(option);//在标注的图层上添加当前信息图层
		marker1.setExtraInfo(bundle);
		//添加当前图层文字信息
		Overlay textOverlay = mBaiduMap.addOverlay(textOption);
		textOverlay.setExtraInfo(bundle);
	    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll1);
	    mBaiduMap.setMapStatus(u); 
//	    carAddress.setText(myCarInfo.address);
    }
    
    /**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
    public class SDKReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d(TAG, "action: " + s);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
            	Toast.makeText(getApplication(),"key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置",Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
            	Toast.makeText (getApplication(),"key 验证成功! 功能可以正常使用",Toast.LENGTH_SHORT).show();

            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
            	Toast.makeText (getApplication(),"网络出错",Toast.LENGTH_SHORT).show();
            }
        }
    }
	    
	    private SDKReceiver mReceiver;
	    private int state_type = 0;

    Handler handler = new Handler() {
    	@Override
		public void handleMessage(Message msg) {
			int i =  msg.arg1;
			System.arraycopy(buf, 0, buf1, 0, i);//这个i有可能是多个信息一起的长度
			Arrays.fill(buf, (byte)0);
			if((buf1[0] == 0x29) && (buf1[1] == 0x29) && (buf1[2] == 0x37)) {
				parseOtherInfo(buf1);
			}else if ((buf1[0] == 0x29) && (buf1[1] == 0x29) && (buf1[2] == 0x35)) {//这个预留
				parseOtherInfo(buf1);//服务器下发的信息,目前没有做这一块的话，先留着
//						String str = InfoDown.substring(0, Length-1);
				AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT).setTitle("路况提醒")
					.setMessage(InfoDown).setPositiveButton("确定",null).create();
									 
				 Window  dialogWindow = dialog.getWindow();
				 dialogWindow.setGravity(Gravity.BOTTOM);
				 dialogWindow.setWindowAnimations(R.style.diashow);
				 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				 lp.alpha = 0.9f;
				 dialogWindow.setAttributes(lp);
				 dialog.show();

				if (mDBUtil.numSqlite() > 201) {//这个是数据库？
					 int ID=mDBUtil.Delete(id);//应该是删除最旧的信息
					 Log.d("qiqiqiqiqiqyarurururur", "删除了"+ID+"数据库的长度为："+mDBUtil.numSqlite());
					 id ++;
				}
			    EventInfo eventInfo = new EventInfo();//事件信息有三个元素，事件，id,事件内容
				eventInfo.setTime(sdf.format(new Date()));
				eventInfo.setEventContent(InfoDown);
			    mDBUtil.Insert(eventInfo);//这个数据库就是用于保存是个EventInfo类
			}else if((buf1[0] == 0x29) && (buf1[1] == 0x29) &&((buf1[2] == 0x56)||(buf1[2] == 0x11)||
					(buf1[2] == 0x20)||(buf1[2] == 0x22)||(buf1[2] == 0x23)||(buf1[2] == 0x24)||(buf1[2] == 0x25)
		    		||(buf1[2] == 0x26)||(buf1[2] == 0x27)||(buf1[2] == 0x28)||(buf1[2] == 0x29))){
		    	try {
					state_type = parse(buf1);
					Log.e(TAG, "handleMessage "+ buf1[i-1]+ " "+state_type);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	if (state_type == 1) {//本车信息
//						app.setVertical(myCarInfo.speed);
		    		if (isTurnOver == false && myCarInfo.isTurnOver == true) {
						myCarInfo.Overflage = true;
						if (myCarInfo.OverLevel == "轻微侧翻风险") {
							int a = app.getNormTurn() +1;
							app.setNoTurnOver(a);
						}
						if (myCarInfo.OverLevel == "中等侧翻风险") {
							int a = app.getMidTurnOver() +1;
							app.setNoTurnOver(a);
						}
						if((myCarInfo.OverLevel == "高侧翻风险")||(myCarInfo.OverLevel == "已经侧翻")){
							int a = app.getTurnOver() + 1;
		    				app.setTurnOver(a);
		    			}
					}
	    			if ((isbrake == false) && (myCarInfo.isBrake == true)) {
						myCarInfo.Brakeflage = true;
						if (myCarInfo.BrakeLevel == "快速刹车") {
							int a = app.getQuickBrakeTime()+ 1;
							app.setQuickBrakeTime(a);
						}
		    			if (myCarInfo.BrakeLevel == "紧急刹车") {
		    				int a = app.getEmgBrakeTime()+ 1;
							app.setEmgBrakeTime(a);
						}
					}
	    			if (myCarInfo.isTurnLeft) {
						if ((isTurnLeft == false) && (myCarInfo.isTurnLeft == true)){
							myCarInfo.TurnLflage = true;
							if (myCarInfo.TurnLevel == "缓慢转弯") {
								int a = app.getSlowTurn()+ 1;
								app.setSlowTurn(a);
							}
							if (myCarInfo.TurnLevel == "正常转弯") {
								int a = app.getNormTurn()+ 1;
								app.setNormTurn(a);
						
							}
							if (myCarInfo.TurnLevel == "快速转弯") {
								int a = app.getQuickTurn()+ 1;
								app.setQuickTurn(a);

							}
			    			if (myCarInfo.TurnLevel == "紧急转弯") {
			    				int a = app.getEmgTurn()+ 1;
								app.setEmgTurn(a);
							}
				        }
	    	        }	
	    		    if (myCarInfo.isTurnRight) {
						if ((isTurnRight == false )&& (myCarInfo.isTurnRight == true)) {
							myCarInfo.TurnRflage = true;	
							if (myCarInfo.TurnLevel == "缓慢转弯") {
								int a = app.getSlowTurn()+ 1;
								app.setSlowTurn(a);
							}
							if (myCarInfo.TurnLevel == "正常转弯") {
								int a = app.getNormTurn()+ 1;
								app.setNormTurn(a);
							}
							if (myCarInfo.TurnLevel == "快速转弯") {
								int a = app.getQuickTurn()+ 1;
								app.setQuickTurn(a);
							}
			    			if (myCarInfo.TurnLevel == "紧急转弯") {
			    				int a = app.getEmgTurn()+ 1;
								app.setEmgTurn(a);
							}
						}
					}
		    	
					if (oldCollisionLevel =="轻微碰撞风险"&&myCarInfo.CollisionLevel =="中级碰撞风险") {
						myCarInfo.Collisionflage = true;
					}
					if (oldCollisionLevel =="中级碰撞风险"&&myCarInfo.CollisionLevel =="高碰撞风险") {
						myCarInfo.Overflage = true;
					}
					if (oldOverLevel =="轻微侧翻风险"&&myCarInfo.OverLevel =="中等侧翻风险") {
						myCarInfo.Overflage = true;
					}
					if (oldOverLevel =="中等侧翻风险"&&myCarInfo.OverLevel =="高侧翻风险") {
						myCarInfo.Overflage = true;
					}
					if (oldOverLevel =="高侧翻风险"&&myCarInfo.OverLevel =="已经侧翻") {
						myCarInfo.Overflage = true;
					}
					if (((oldSpeedUpLevel == "正常加速") &&( myCarInfo.SpeedUpLevel == "快速加速"))
							||((oldBrakeLevel == "无加速")&&(myCarInfo.SpeedUpLevel == "快速加速"))||
							((oldBrakeLevel == "缓慢加速")&&(myCarInfo.SpeedUpLevel == "快速加速"))) {
						int a = app.getQuickSpeedUpTime()+ 1;
						app.setQuickSpeedUpTime(a);
						myCarInfo.SpeedUpflage = true;
					}
					if (((oldSpeedUpLevel == "快速加速") &&( myCarInfo.SpeedUpLevel == "紧急加速"))||
							((oldSpeedUpLevel == "无加速") &&( myCarInfo.SpeedUpLevel == "紧急加速"))||
							((oldSpeedUpLevel == "缓慢加速") &&( myCarInfo.SpeedUpLevel == "紧急加速"))||
							((oldSpeedUpLevel == "正常加速") &&( myCarInfo.SpeedUpLevel == "紧急加速"))) {
						int a = app.getEmgSpeedUpTime()+1;
						app.setEmgSpeedUpTime(a);
						myCarInfo.SpeedUpflage = true;
					}
					isTurnOver = myCarInfo.isTurnOver;
					isbrake = myCarInfo.isBrake;
					isTurnLeft= myCarInfo.isTurnLeft;
					isTurnRight = myCarInfo.isTurnRight;
					isCollision = myCarInfo.isCollision;
					oldBrakeLevel = myCarInfo.BrakeLevel;
					oldTurnLevel = myCarInfo.TurnLevel;
					oldOverLevel = myCarInfo.OverLevel;
					oldCollisionLevel = myCarInfo.CollisionLevel;
					oldSpeedUpLevel = myCarInfo.SpeedUpLevel;
					app.setMyCarInfo(myCarInfo);
					otherCars = app.getOtherCarInfo();
					if((otherCars != null)&&(!otherCars.isEmpty())) {
						for (CarInfo carinfo2 : app.getOtherCarInfo()) {
		    				if (System.currentTimeMillis() - carinfo2.Time > 5000) {
								app.removeOtherCar(carinfo2);
								break;
							}
						}
					}
					otherCars = app.getOtherCarInfo();
					mBaiduMap.clear();//清除之后重新画图
					initOverLay(myCarInfo, true);
					PopWindow(myCarInfo);
					if((otherCars != null)&&(!otherCars.isEmpty()))
		    			for (CarInfo carinfo2 : otherCars) {
		    				Log.e(TAG, "其他车的车牌号为：" + carinfo2.CarPlateNumber);
							initOverLay(carinfo2, false);
						}
				}
					
				if (state_type ==2 ) {
					otherCars = app.getOtherCarInfo();
					if((otherCars != null)&&(!otherCars.isEmpty())) {
						mBaiduMap.clear();
						for (CarInfo carinfo2 : otherCars) {
							initOverLay(carinfo2, false);
						}
						initOverLay(myCarInfo, true);    				
					}
				}
				if (state_type == 3 ) {
				    PopWindow(emergentCarInfo);
				}
				if (state_type == 4 ) {
					PopWindow(emergentCarInfo);
				}
				if (state_type == 5 ) {
					PopWindow(emergentCarInfo);
				}
				if (state_type == 6 ) {
					PopWindow(emergentCarInfo);
				}
				if (state_type == 7 ) {
					PopWindow(emergentCarInfo);
				}
				if (state_type == 8 ) {
					PopWindow(emergentCarInfo);
//							parameter.append(emergentCarInfo.SpeedUpLevel);
				}
				if (state_type == 9){
					//单播信息，可能切换到蓝牙供其他手机玩家
				}
				if (state_type == 10){
					//广播短信息
				}
				Arrays.fill(buf1, (byte)0);
			}
		}
//
	};
		
	private void getOverflowMenu() {
        ViewConfiguration viewConfig = ViewConfiguration.get(this);
         
        try {
            Field overflowMenuField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(null != overflowMenuField){
                overflowMenuField.setAccessible(true);
                overflowMenuField.setBoolean(viewConfig, false);
            }
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	    
	    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
    	super.onCreate(savedInstanceState);   
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.activity_main);
        
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//这个是用于侧滑菜单的控件
        drawerLayout.setScrimColor(Color.TRANSPARENT);//设置抽屉空余处颜色 ，也就是另外没有侧滑的部分可见
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);//这个算是侧滑的左栏布局器
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        
        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, drawerLayout, this);
        
        if((Build.VERSION.SDK_INT >= 10) & (Build.VERSION.SDK_INT < 21)) {
	            getOverflowMenu();
	    }
        app = (MyApplication)getApplication();
         
        CarNetActivityManager.getInstance().addActivity(MainActivity.this);
        mDBUtil = new DatabaseUtil(MainActivity.this);
            
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.bmapView);  
        mBaiduMap = mMapView.getMap();  
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        lp = getWindow().getAttributes();
        mflickerCount = 11;
		handler2 = new Handler();
        handler2.postDelayed(runnable, TIME);//这是用于闪屏操作
      
   // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);//权限问题
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);//
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);//网络问题
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
		//初始化地图
        init();
        //为每个marker添加点击事件
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				String carString = marker.getExtraInfo().getString("car");
				if (carString.equals(myCarInfo.CarPlateNumber)) {
					openInfowindow(myCarInfo, marker);
				}
				else {
					otherCars = app.getOtherCarInfo();
					if((otherCars != null)&&(!otherCars.isEmpty()))
						for (CarInfo i : otherCars) {
							if (carString.equals(i.CarPlateNumber)) {
								openInfowindow(i, marker);
							}
						}
						
				}
				return false;
			}
		});
        
        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			//点击地图的其他地方就隐藏信息窗口
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
					mBaiduMap.hideInfoWindow();
				}
			});
        
//	        myThread = new MyThread(handler);
//			myThread.start();
	    
	}  
	    
    @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		myThread.close();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 myThread = new MyThread(handler);
		 myThread.start();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		 myThread = new MyThread(handler);
		 myThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//	        //return super.onOptionsItemSelected(item);
//		switch (item.getItemId()){
//			case R.id.action_home:
//	        case R.id.Cars:
//	            Intent intent2 = new Intent(MainActivity.this,CarnetActivity.class);
//	            startActivity(intent2);
//	            break;
//	        case R.id.Route:
//	            Intent intent3 = new Intent(MainActivity.this,RoutSearch.class);
//	            startActivity(intent3);
//	            break;
//	        case R.id.message:
//			case R.id.action_message:
//	        	Intent Toqueryctivity = new Intent(MainActivity.this,System_Down.class);
//				startActivity(Toqueryctivity);
//	            break;
//	        case R.id.Assess:
//	        	Intent intent = new Intent(MainActivity.this,DriveAssess.class);
//				startActivity(intent);
//	            break;
//	        default:
//	            break;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	private void init() {
	    mMapView.removeViewAt(1);//不显示百度地图logo
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
		mBaiduMap.clear();
		initOverLay(myCarInfo, true);
	}
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
     }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
//	        myThread.close();
    }	
    @Override  
    protected void onDestroy() {  
    	super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();
     // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
     // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }  
	 
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(),
//                        location.getLongitude());
        		LatLng ll = new LatLng(myCarInfo.latitude, myCarInfo.longtitude);
        		ll = GPS2Baidu(ll);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    /**
     * 显示车辆信息的窗口
     * @param carInfo
     * @param marker
     */
    public void openInfowindow(CarInfo carInfo,Marker marker){
    	 String  info = "车牌号码为：" + carInfo.CarPlateNumber +"\n行车速度为："+carInfo.speed+",方向为："+carInfo.direction
           		+"\n"+carInfo.BrakeLevel+","+carInfo.TurnLevel+","+carInfo.OverLevel +",\n" + carInfo.CollisionLevel+", "+carInfo.SpeedUpLevel;
    	 InfoWindow mInfoWindow;  
         TextView carTextView = new TextView(getApplicationContext());
         carTextView.setBackgroundResource(R.drawable.popup);
         carTextView.setText(info);
         carTextView.setPadding(30, 20, 30, 50);  
         carTextView.setTextColor(Color.parseColor("#0000FF"));
         final LatLng ll = marker.getPosition();  
//             info = info +"\n"+GeoCoder(ll);
         Point p = mBaiduMap.getProjection().toScreenLocation(ll);  
         p.y -= 100;  
         LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);  
         mInfoWindow = new InfoWindow(carTextView, llInfo, 1);  
         mBaiduMap.showInfoWindow(mInfoWindow);  
    }
	       
    /**
     * 根据经纬度信息反解析地址
     * @param marker1
     * @return  地址
     */
    public String GeoCoder(Overlay marker1){
    	 //实例化一个地理编码查询对象  
        GeoCoder geoCoder = GeoCoder.newInstance();  
        //设置反地理编码位置坐标  
        ReverseGeoCodeOption op = new ReverseGeoCodeOption();  
//            op.location(marker1.getPosition());  
        op.location(new LatLng(myCarInfo.latitude, myCarInfo.longtitude));  
        //发起反地理编码请求(经纬度->地址信息)  
        geoCoder.reverseGeoCode(op);  
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {   
            @Override  
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {  
                //获取点击的坐标地址  
                address = arg0.getAddress();  
                System.out.println("address="+address);  
            }  
              
            @Override  
            public void onGetGeoCodeResult(GeoCodeResult arg0) {  
            }  
        });  
 
    	return address;
    }
    
    public static String GeoCoder(LatLng latLng){
    	 //实例化一个地理编码查询对象  
       GeoCoder geoCoder = GeoCoder.newInstance();  
       //设置反地理编码位置坐标  
       ReverseGeoCodeOption op = new ReverseGeoCodeOption();  
//           op.location(marker1.getPosition());  
       op.location(latLng);  
       //发起反地理编码请求(经纬度->地址信息)  
       geoCoder.reverseGeoCode(op);  
       geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {   
           @Override  
           public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {  
               //获取点击的坐标地址  
               address = arg0.getAddress();  
               System.out.println("address="+address);  
           }  
             
           @Override  
           public void onGetGeoCodeResult(GeoCodeResult arg0) {  
           }  
       });  

    	return address;
    }
	    
//		private Socket socket;
	private LocalSocket socket;
    /**
     * 建立socket连接,用于android与底层Linux交互，现在使用的是LocalSocket通信方式
     * @author qyr
     *
     */
    public class MyThread extends Thread {
		private Handler myHandler;
		private boolean flag = true;
		private byte[] buffer = new byte[8192];
		private int socketlength = 0;
		
		public MyThread(Handler handler) {
			myHandler = handler;
		}
		
		@Override
		public void run() {
			try {
//					socket = new Socket("192.168.0.1", 32351);//Tcp Port,Udp Port 是32352
                socket = new LocalSocket();
                LocalSocketAddress address = new LocalSocketAddress("unixstr", LocalSocketAddress.Namespace.ABSTRACT);

				while (flag) {
	                //local 客户端 比一般的tcp/udp快一倍，
	                socket.connect(address);
			    	  if (socket.isConnected()) {
							socket.setReceiveBufferSize(8192);
							socket.setSendBufferSize(8192);
			                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			                
					    	while(dis != null){
					    		socketlength = dis.read(buffer);//buffer以字节方式存储socket信息，然后socketlength记录接收的字节数
					    		if(socketlength != -1){
					    			Log.e(TAG, "接收socket信息长度是"+socketlength);
						    		System.arraycopy(buffer, 0, buf, 0, socketlength);//buffer[0]-->buf1[0]
									Message msg = new Message();
									msg.arg1 = socketlength;//把字节数传输给handler message处理
									myHandler.sendMessage(msg);
//									Thread.sleep(10);//睡眠10ms
					    		}else {
//									Thread.sleep(10);//睡眠10ms
					    		}
					    	}
					   }else {
				   		  Thread.sleep(500);
						  socket.close();
			              socket = new LocalSocket();
					   }
			    }
				socket.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void close(){
			flag = false;
		}
	}
	    
/**
		 * 解析数据包
		 * @param data 消息
		 * @return 0：表示消息格式错误；1：返回本车信息；2：返回其他车辆信息 3:侧翻紧急消息 
		 * 					4：刹车紧急消息 5：转弯紧急消息 6：碰撞紧急消息 7：疲劳驾驶紧急消息，9，单播信息，应该有对应的车牌号吧？
		 *          10，广播短消息
 * @throws UnsupportedEncodingException 
		 */
	public static final int parse(byte[] data) throws UnsupportedEncodingException{
		//验证校验码
		byte correct = data[0];
		CarInfo temp3 = new CarInfo();
		//内容+校验位+包尾长度
		int length = 0;
		//存放包长字节
		byte[] len = new byte[4]; //长度为什么要四个字节？
		Arrays.fill(len, (byte)0);//对数组清零
		System.arraycopy(data, 8, len, 0, 2);//把data[8] copy len[0],length 2
		length = util.bytesToInt(len, 0);//低位在前，高位在后
		for(int i = 1 ; i < length + 9; i++){ //最后一位0x0d不在校验区
			correct = (byte) (correct ^ data[i]);
		}
//		if (correct != 0) {
//			Log.e(TAG, "校验不正确: " + data[2] + " 长度是：" + length);
//			return 0;
//		}
			//判断是何种消息
		switch (data[2]) {
			case((byte)0x11):
			case((byte)0x22): //周期广播，获取的应该是邻居的信息
				Log.e(TAG, "进入0x22进行信息解析");
				temp3 = parseCarInfo(data, temp3);
        		CarInfo temp5 = new CarInfo();
        		boolean samePlate = false;
        		otherCars = app.getOtherCarInfo();
        		if((otherCars != null)&&(!otherCars.isEmpty())) {
		        	for (CarInfo carInfo2 : otherCars) {
		        		if (carInfo2.CarPlateNumber.equals(temp3.CarPlateNumber)) {//本身有他的车牌号
		        			temp3.TurnLflage = carInfo2.TurnLflage;
		        			temp3.TurnRflage = carInfo2.TurnRflage;
		        			temp3.Brakeflage = carInfo2.Brakeflage;
		        			temp3.Overflage  = carInfo2.Overflage;
		        			temp3.Collisionflage = carInfo2.Collisionflage;
		        			temp3.SpeedUpflage   = carInfo2.SpeedUpflage;
							if ((carInfo2.isTurnOver == false) && (temp3.isTurnOver == true)) {
								temp3.Overflage = true;
							}
							if (((carInfo2.isBrake) == false && (temp3.isBrake == true))||((carInfo2.BrakeLevel == "快速刹车")&& (temp3.BrakeLevel == "紧急刹车"))) {
								temp3.Brakeflage = true;
							}
							if ((carInfo2.isCollision == false) && (temp3.isCollision == true)) {
								temp3.Collisionflage = true;
							}
							if (((carInfo2.OverLevel == "中等侧翻风险")&& (temp3.OverLevel == "高等侧翻风险"))
									||((carInfo2.OverLevel == "高等侧翻风险")&&( temp3.OverLevel == "已经侧翻"))) {
								temp3.Overflage = true;
							}
						
							if ((carInfo2.TurnLevel == "快速转弯")&& (temp3.TurnLevel == "紧急转弯")) {
								if (temp3.isTurnLeft) {
									temp3.TurnLflage = true;
								}
								if (temp3.isTurnRight) {
									temp3.TurnRflage = true;	
								}
							}
							if (temp3.isTurnLeft) {
								if ((carInfo2.isTurnLeft == false) && (temp3.isTurnLeft == true)){
									temp3.TurnLflage = true;
								}
							}
							
							if (temp3.isTurnRight) {
								if ((carInfo2.isTurnRight == false) && (temp3.isTurnRight == true)) {
									temp3.TurnRflage = true;	
								}
							}	
							if (((carInfo2.SpeedUpLevel == "正常加速") &&( temp3.SpeedUpLevel == "快速加速"))
									||((carInfo2.SpeedUpLevel == "无加速")&&(temp3.SpeedUpLevel == "快速加速"))||
									((carInfo2.SpeedUpLevel  == "缓慢加速")&&(temp3.SpeedUpLevel == "快速加速"))
									||((carInfo2.SpeedUpLevel == "快速加速") &&( temp3.SpeedUpLevel == "紧急加速"))||
									((carInfo2.SpeedUpLevel == "无加速") &&( temp3.SpeedUpLevel == "紧急加速"))||
									((carInfo2.SpeedUpLevel == "缓慢加速") &&( temp3.SpeedUpLevel == "紧急加速"))||
									((carInfo2.SpeedUpLevel == "正常加速") &&( temp3.SpeedUpLevel == "紧急加速"))) {
								temp3.SpeedUpflage = true;
							}
							temp5 = carInfo2;
							samePlate = true;
							break;
		        		}	
		        	}
		        	if(samePlate) {
		        		otherCars.remove(temp5);
		        		samePlate = false;
		        	}
        		}
        		temp3.Time = System.currentTimeMillis();
        		otherCars.add(temp3);
        		Log.e(TAG, "其他车辆车牌数目：" + otherCars.size());
        		app.setOtherCar(otherCars);
        		return 2;
			case((byte)0x20):return 9;	//单播信息
			case((byte)0x23):return 10;	//广播短消息
			case ((byte)0x56)://自身姿态信息
				Log.e(TAG, "进入0x56进行信息解析");
				temp3 = parseCarInfo(data, temp3);
				myCarInfo = temp3;
				myCarInfo.Time = System.currentTimeMillis();
				return 1;
			case ((byte)0x24)://侧翻紧急消息
				Log.e(TAG, "------->收到24<---------");
			 	emergentCarInfo = parseCarInfo(data, temp3);
			 	return 3;
			case ((byte)0x25)://紧急刹车
				Log.e(TAG, "------->收到25<---------");
			 	emergentCarInfo = parseCarInfo(data, temp3);;
				return 4;
			case ((byte)0x26)://紧急转弯
				Log.e(TAG, "------->收到246<---------");
			 	emergentCarInfo = parseCarInfo(data, temp3);;
			 	return 5;
			case ((byte)0x27)://碰撞
				Log.e(TAG, "------->收到27<---------");
			 	emergentCarInfo = parseCarInfo(data, temp3);;
			 	Log.e(TAG,"碰撞车牌：" + emergentCarInfo.CarPlateNumber);
			 	return 6;
			case ((byte)0x28)://疲劳驾驶
				Log.e(TAG, "------->收到28<---------");
			 	emergentCarInfo = parseCarInfo(data, temp3);;
			 	return 7;
			case ((byte)0x29)://急加速
				Log.e(TAG, "------->收到29<---------");
			 	emergentCarInfo = parseCarInfo(data, temp3);
			 	return 8;
			default:
				Log.e(TAG,"解析主信令错误");
				return 0;
	
		}
	}
	
	public static CarInfo changeState(CarInfo carInfo){
	
		return carInfo;
	}		
		
	/**
	 * 解析其他车辆信息
	 * @param data 消息内容
	 * @param length 消息长度
	 * @throws UnsupportedEncodingException 
	 * 这个只用于处理0x56
	 */
	private static CarInfo parseCarInfo(byte[] data , CarInfo temp) throws UnsupportedEncodingException {
//			CarInfo temp = new CarInfo();
		// TODO Auto-generated method stub
		byte[] carNumberCopy = new byte[9];
		if(data[2] == 0x11){//邻居车辆回复
			System.arraycopy(data, 19, carNumberCopy, 0, 9);//邻居车牌号
			temp.CarPlateNumber = new String(carNumberCopy,"GBK");
			//获得车辆的经纬度
			temp.latitude = util.byteArrayToDouble(data, 28)/1000;
			System.out.println("纬度：" + temp.latitude);
			temp.longtitude = util.byteArrayToDouble(data, 36)/1000;
			System.out.println("经度：" + temp.longtitude);
			//车辆的速度
			temp.speed = util.byte2float(data, 44);
			//行车方向
			temp.direction = util.byte2float(data, 48);
			//x轴加速度
			temp.acceleration_X = util.byte2float(data, 52);
			//Y轴加速度
			temp.acceleration_Y = util.byte2float(data, 56);
			//Z轴加速度
			temp.acceleration_Z = util.byte2float(data, 60);
			//车辆的海拔高度
			temp.highet = 0;//64~71
			//车辆的状态
			if ( (byte)(data[72]&((byte)0x01)) == (byte)0x01) {
				temp.isLocated = true;
			}else {
				temp.isLocated = false;
			}
			if ( (byte)(data[72]&((byte)0x02)) ==  (byte)0x02) {
				//北纬
				temp.lat_which= true;
			}else {
				//南纬
				temp.lat_which = false;
			}
			if ( (byte)(data[72]&((byte)0x04)) ==  (byte)0x04) {
				//东经
				temp.long_which= true;
			}else {
				//西经
				temp.long_which = false;
			}
			if ( (byte)(data[72]&((byte)0x08)) ==  (byte)0x08) {
				temp.isBrake= true;
			}else {
				temp.isBrake = false;
			}
			if ( (byte)(data[72]&((byte)0x10)) ==  (byte)0x10) {
				temp.isOverSpeed= true;
			}else {
				temp.isOverSpeed = false;
			}
			if ( (byte)(data[72]&((byte)0x20)) ==  (byte)0x20) {
				temp.isFatigueDriving= true;
			}else {
				temp.isFatigueDriving = false;
			}
			//原来判断是否左转
			if ( (byte)(data[72]&((byte)0x40) )==  (byte)0x40) {
				temp.isTurnLeft= true;
			}else {
				temp.isTurnLeft = false;
			}
			//原来判断是都
			if ( (byte)(data[72]&((byte)0x80)) ==  (byte)0x80) {
				temp.isTurnRight= true;
			}else {
				temp.isTurnRight = false;
			}
			if ( (byte)(data[73]&((byte)0x01)) ==  (byte)0x01) {
				temp.isCollision = true;
			}else {
				temp.isCollision = false;
			}
			if ( (byte)(data[73]&((byte)0x02)) == (byte) 0x02) {
				temp.isTurnOver = true;
			}else {
				temp.isTurnOver = false;
			}
			 byte level1 = (byte) (data[73]&((byte)0x1C));
			 int brake_level = level1 >> 2;
			//				 Log.d(TAG, "刹车等级"+ temp.BrakeLevel);
			 temp.BrakeLevel = temp.brakeLevelStr[brake_level];
			 byte level2 = (byte) (data[73]&((byte)0xE0));
			 int turn_level = (level2 >>5)&0x07;
			//				 Log.d(TAG, "turn_level:" + turn_level);
			 temp.TurnLevel = temp.turnLevelStr[turn_level];
			//				 Log.d(TAG, "转弯等级"+ temp.TurnLevel);
			 int over_level = data[74]&((byte)0x07);
			 temp.OverLevel = temp.overLevelStr[over_level];
			//				 Log.d(TAG, "侧翻等级"+ temp.OverLevel);
			 byte level3 = (byte)(data[74]&(byte)0x18);
			 int collision_level = (level3>>3)&(byte)0x03;
			 temp.CollisionLevel = temp.collisionlevStr[collision_level];
			 
			 byte level4 = (byte) (data[74]&((byte)0xE0));
			 int speedUP = (level4 >>5)&0x07;
			//				 Log.d(TAG, "turn_level:" + turn_level);
			 temp.SpeedUpLevel = temp.speedUpStr[speedUP];
			//				 parameter.setText(speedUP+temp.SpeedUpLevel);
			 LatLng ll = GPS2Baidu(new LatLng(temp.latitude, temp.longtitude));
			 temp.address = GeoCoder(ll); 	
		}else if((data[2] == 0x22) || (data[2] == 0x24) || (data[2] == 0x25) 
				|| (data[2] == 0x26) || (data[2] == 0x27) || (data[2] == 0x28) 
				|| (data[2] == 0x29) || (data[2] == 0x56) ){
			System.arraycopy(data, 10, carNumberCopy, 0, 9);
			temp.CarPlateNumber = new String(carNumberCopy,"GBK");
			//获得车辆的经纬度
			temp.latitude = util.byteArrayToDouble(data, 19)/1000;
			System.out.println("纬度：" + temp.latitude);
			temp.longtitude = util.byteArrayToDouble(data, 27)/1000;
			System.out.println("经度：" + temp.longtitude);
			//车辆的速度
			temp.speed = util.byte2float(data, 35);
			//行车方向
			temp.direction = util.byte2float(data, 39);
			//x轴加速度
			temp.acceleration_X = util.byte2float(data, 43);
			//Y轴加速度
			temp.acceleration_Y = util.byte2float(data, 47);
			//Z轴加速度
			temp.acceleration_Z = util.byte2float(data, 51);
			//车辆的海拔高度
			temp.highet = 0;//55~62
			//车辆的状态
			if ( (byte)(data[63]&((byte)0x01)) == (byte)0x01) {
				temp.isLocated = true;
			}else {
				temp.isLocated = false;
			}
			if ( (byte)(data[63]&((byte)0x02)) ==  (byte)0x02) {
				//北纬
				temp.lat_which= true;
			}else {
				//南纬
				temp.lat_which = false;
			}
			if ( (byte)(data[63]&((byte)0x04)) ==  (byte)0x04) {
				//东经
				temp.long_which= true;
			}else {
				//西经
				temp.long_which = false;
			}
			if ( (byte)(data[63]&((byte)0x08)) ==  (byte)0x08) {
				temp.isBrake= true;
			}else {
				temp.isBrake = false;
			}
			if ( (byte)(data[63]&((byte)0x10)) ==  (byte)0x10) {
				temp.isOverSpeed= true;
			}else {
				temp.isOverSpeed = false;
			}
			if ( (byte)(data[63]&((byte)0x20)) ==  (byte)0x20) {
				temp.isFatigueDriving= true;
			}else {
				temp.isFatigueDriving = false;
			}
			//原来判断是否左转
			if ( (byte)(data[63]&((byte)0x40) )==  (byte)0x40) {
				temp.isTurnLeft= true;
			}else {
				temp.isTurnLeft = false;
			}
			//原来判断是都
			if ( (byte)(data[63]&((byte)0x80)) ==  (byte)0x80) {
				temp.isTurnRight= true;
			}else {
				temp.isTurnRight = false;
			}
			if ( (byte)(data[64]&((byte)0x01)) ==  (byte)0x01) {
				temp.isCollision = true;
			}else {
				temp.isCollision = false;
			}
			if ( (byte)(data[64]&((byte)0x02)) == (byte) 0x02) {
				temp.isTurnOver = true;
			}else {
				temp.isTurnOver = false;
			}
			 byte level1 = (byte) (data[64]&((byte)0x1C));
			 int brake_level = level1 >> 2;
			//				 Log.d(TAG, "刹车等级"+ temp.BrakeLevel);
			 temp.BrakeLevel = temp.brakeLevelStr[brake_level];
			 byte level2 = (byte) (data[64]&((byte)0xE0));
			 int turn_level = (level2 >>5)&0x07;
			//				 Log.d(TAG, "turn_level:" + turn_level);
			 temp.TurnLevel = temp.turnLevelStr[turn_level];
			//				 Log.d(TAG, "转弯等级"+ temp.TurnLevel);
			 int over_level = data[65]&((byte)0x07);
			 temp.OverLevel = temp.overLevelStr[over_level];
			//				 Log.d(TAG, "侧翻等级"+ temp.OverLevel);
			 byte level3 = (byte)(data[65]&(byte)0x18);
			 int collision_level = (level3>>3)&(byte)0x03;
			 temp.CollisionLevel = temp.collisionlevStr[collision_level];
			 
			 byte level4 = (byte) (data[65]&((byte)0xE0));
			 int speedUP = (level4 >>5)&0x07;
			//				 Log.d(TAG, "turn_level:" + turn_level);
			 temp.SpeedUpLevel = temp.speedUpStr[speedUP];
			//				 parameter.setText(speedUP+temp.SpeedUpLevel);
			 LatLng ll = GPS2Baidu(new LatLng(temp.latitude, temp.longtitude));
			 temp.address = GeoCoder(ll); 	
		}
		return temp;
	}
	/**
	 * 检验从服务转发的消息——包括油量，油耗等,这是服务器的信息
	 * @param data
	 * @throws UnsupportedEncodingException 
	 */
	private  void parseOtherInfo(byte[] data){
		//验证校验码
		byte correct = data[0];
		//内容+校验位+包尾长度
		int length = 0;
		//存放包长字节
		byte[] len = new byte[4];
		Arrays.fill(len, (byte)0);
		System.arraycopy(data, 8, len, 0, 2);//拷贝两个字节，后面的第三第四依然是0
		length = util.bytesToInt(len, 0);//内容+校验码+包尾
		//0x29,0x29,prototype,seq32,hop,length,data,校验	
		for(int i = 1 ; i < length + 9;i++){
			correct =(byte) (correct ^ data[i]);
		}
		if (correct != 0) {
			Log.d(TAG, "x消息校验错误");
			return;
		}				
		switch (data[2]) {
			case((byte)0x35)://服务器传来的信息
				System.arraycopy(data, 7, len, 0, 2);
			    Length = util.bytesToInt( len,0);
				byte[] content1 = new byte[Length];
				System.arraycopy(data, 9, content1, 0,Length);
				try {
					InfoDown = new String(content1,"GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					Log.d(TAG, "解析历史下发信息消息失败");
					e.printStackTrace();
				}
			
				break;
			case ((byte)0x37)://OBD信息
				byte[] content = new byte[length-3];
				System.arraycopy(data, 7, content, 0,length-3);
				Log.d(TAG, "解析OBD消息");
				Calculate(content);
			break;
			default:
				break;
		}
	}
	/**
	 * 计算油耗，里程，节气门开度等
	 * @param data
	 */
	private void Calculate(byte[] data){
		app.setTpi(app.getTpi());
		byte[] temp1 = new byte[4];
		byte[] temp2 = new byte[8];
		System.arraycopy(data, 0, temp1, 0, 4);
		app.setSpeed(util.bytesToInt(temp1, 0));
		app.setVertical(util.bytesToInt(temp1, 0));
		Log.d(TAG, "speed" + util.bytesToInt(temp1, 0));
		app.setVertical1(app.getSpeed());
		System.arraycopy(data, 4, temp2, 0, 8);
		app.setTpi(util.byteArrayToDouble(temp2, 0));
		Log.d(TAG, "tpi" + util.byteArrayToDouble(temp2, 0));
		System.arraycopy(data, 12, temp2, 0, 8);
		app.setMileage(util.byteArrayToDouble(temp2, 0));
		Log.d(TAG, "Mileage" + util.byteArrayToDouble(temp2, 0));
		System.arraycopy(data, 20, temp2, 0, 8);
		app.setOil(util.byteArrayToDouble(temp2, 0));
		Log.d(TAG, "Oil" + util.byteArrayToDouble(temp2, 0));
		parameter.setText("速度："+app.getSpeed() + "km/h,节气门开度：" + app.getTpi() +"\n里程：" 
		+app.getMileage() + "km, 油耗：" + app.getOil());
		if (Score()[0]<85||Score()[1]<85) {
			startActivity(new  Intent(MainActivity.this,AssessAll.class));
		}
	}
	/**
	 * 弹性窗中的信息
	 * @param carInfo
	 * @return
	 */
	public static String popMessage(CarInfo carInfo){
//			String s =  carInfo.CarPlateNumber;
		String s = "警报：";
//			s = s + carInfo.CarPlateNumber;
		if (carInfo.isBrake) {

			if (carInfo.BrakeLevel == "快速刹车") {
				s += "快速刹车。";
			}
			if (carInfo.BrakeLevel == "紧急刹车") {
				s += "紧急刹车。";
			}
		}
		if(carInfo.isTurnLeft){

		if (carInfo.TurnLevel == "快速转弯") {
			s += "快速左转弯。";
		}
		if (carInfo.TurnLevel == "紧急转弯") {
			s += "紧急左转弯。";
		}
		}
		if(carInfo.isTurnRight){

			if (carInfo.TurnLevel == "快速转弯") {
				s += "快速右转弯。";
			}
			if (carInfo.TurnLevel == "紧急转弯") {
				s += "紧急右转弯。";
			}
			}
		if (carInfo.isTurnOver) {
//				if (carInfo.OverLevel == "轻微侧翻风险"	) {
//					s += "轻微侧翻风险.";
//				}
		if (carInfo.OverLevel == "中等侧翻风险"	) {
			s += "中等侧翻风险.";
		}
		if (carInfo.OverLevel == "高侧翻风险") {
			s += "高等侧翻风险.";
		}
		if (carInfo.OverLevel == "已经侧翻") {
			s += "发生侧翻.";
		}
		}
		if (carInfo.isCollision) {
			if (carInfo.CollisionLevel == "中级碰撞风险") {
				 s += "中级碰撞风险";
			}
			if (carInfo.CollisionLevel == "高碰撞风险") {
				s+= "高碰撞风险";
			}
		}
		if (carInfo.SpeedUpLevel == "快速加速") {
			s += "快速加速";
		}
		if (carInfo.SpeedUpLevel == "紧急加速") {
			s += "紧急加速";
		}
		return s;
	}
	
	/**
	 * 闪屏
	 * @param carInfo
	 * @return
	 */
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			try{
				handler2.postDelayed(this, TIME);
				if(mflickerCount <= 6){
					if((mflickerCount%2) == 0){
						lp.screenBrightness = 1.0f;
					}else{
						lp.screenBrightness = 0.0f;
					}
					getWindow().setAttributes(lp);
//						if(mflickerCount == 0){
//							 NotificationManager manger = (NotificationManager) 
//							 getSystemService(Context.NOTIFICATION_SERVICE); 
//							 Notification notification = new Notification();
//							 notification.defaults=Notification.DEFAULT_SOUND;
//							 manger.notify(1, notification);
//						}
					mflickerCount ++;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}			
			// TODO Auto-generated method stub
			
		}
	};
	
	public void PopWindow(CarInfo car){
		if (car.equals(myCarInfo)) {
			if (myCarInfo.TurnLflage) {
				 myCarInfo.TurnLflage = false;
				 /*报警，不用弹框式的，用下边文本上滑*/
				 final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
				 .setMessage(popMessage(car)).setPositiveButton("确定",null).create();
				alg.show();
				 
				 /*这个是后面加的的动画演示*/
				 Window  dialogWindow = alg.getWindow();
				 dialogWindow.setGravity(Gravity.BOTTOM);
				 dialogWindow.setWindowAnimations(R.style.diashow1);
//				 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				 lp.alpha = 0.9f;
//				 dialogWindow.setAttributes(lp);
//				 alg.show();
				 mflickerCount = 0;
				 new Handler().postDelayed(new Runnable() {
					 public void run() {
						 alg.dismiss();
					 }
				 }, 3000);
				 
				 NotificationManager manger = (NotificationManager) 
				 getSystemService(Context.NOTIFICATION_SERVICE); 
				 Notification notification = new Notification();
				 notification.defaults=Notification.DEFAULT_SOUND;
				 manger.notify(1, notification);
			}
			if (myCarInfo.TurnRflage) {
				 myCarInfo.TurnRflage = false;
				 
				 final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
				 .setMessage(popMessage(car)).setPositiveButton("确定",null).create();		 
				 alg.show();
				 
				 Window  dialogWindow = alg.getWindow();
				 dialogWindow.setGravity(Gravity.BOTTOM);
				 dialogWindow.setWindowAnimations(R.style.diashow1);
//				 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				 lp.alpha = 0.9f;
//				 dialogWindow.setAttributes(lp);
//				 alg.show();
				 mflickerCount = 0;
				 new Handler().postDelayed(new Runnable() {
					 public void run() {
						 alg.dismiss();
					 }
				 }, 3000);
				 NotificationManager manger = (NotificationManager) 
				 getSystemService(Context.NOTIFICATION_SERVICE); 
				 Notification notification = new Notification();
				 notification.defaults=Notification.DEFAULT_SOUND;
				 manger.notify(1, notification);
			}
			if (myCarInfo.Brakeflage) {
				 myCarInfo.Brakeflage = false;
				 final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
				 .setMessage(popMessage(car)).setPositiveButton("确定",null).create();
				 alg.show();
				 Window  dialogWindow = alg.getWindow();
				 dialogWindow.setGravity(Gravity.BOTTOM);
				 dialogWindow.setWindowAnimations(R.style.diashow1);
//				 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				 lp.alpha = 0.9f;
//				 dialogWindow.setAttributes(lp);
//				 alg.show();
				 
				 mflickerCount = 0;
				 new Handler().postDelayed(new Runnable() {
					 public void run() {
						 alg.dismiss();
					 }
				 }, 3000);
				 NotificationManager manger = (NotificationManager) 
				 getSystemService(Context.NOTIFICATION_SERVICE); 
				 Notification notification = new Notification();
				 notification.defaults=Notification.DEFAULT_SOUND;
				 manger.notify(1, notification);
			}
			if (myCarInfo.Overflage) {
				 myCarInfo.Overflage = false;
				 final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
				 .setMessage(popMessage(car)).setPositiveButton("确定", null).create();
				 alg.show();
				 Window  dialogWindow = alg.getWindow();
				 dialogWindow.setGravity(Gravity.BOTTOM);
				 dialogWindow.setWindowAnimations(R.style.diashow1);
//				 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				 lp.alpha = 0.9f;
//				 dialogWindow.setAttributes(lp);
//				 alg.show();
				 
				 mflickerCount = 0;
				 new Handler().postDelayed(new Runnable() {
					 public void run() {
						 alg.dismiss();
					 }
				 }, 3000);
				 NotificationManager manger = (NotificationManager) 
				 getSystemService(Context.NOTIFICATION_SERVICE); 
				 Notification notification = new Notification();
				 notification.defaults=Notification.DEFAULT_SOUND;
				 manger.notify(1, notification);
			}
			if (myCarInfo.Collisionflage) {
				 myCarInfo.Collisionflage = false;
				 final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
				 .setMessage(popMessage(car)).setPositiveButton("确定",null).create();
				 alg.show();
				 Window  dialogWindow = alg.getWindow();
				 dialogWindow.setGravity(Gravity.BOTTOM);
				 dialogWindow.setWindowAnimations(R.style.diashow1);
//				 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				 lp.alpha = 0.9f;
//				 dialogWindow.setAttributes(lp);
//				 alg.show();

				 mflickerCount = 0;
				 new Handler().postDelayed(new Runnable() {
					 public void run() {
						 alg.dismiss();
					 }
				 }, 3000);
				 NotificationManager manger = (NotificationManager) 
				 getSystemService(Context.NOTIFICATION_SERVICE); 
				 Notification notification = new Notification();
				 notification.defaults=Notification.DEFAULT_SOUND;
				 manger.notify(1, notification);
//					 startAlarm(this);
			}
			if (myCarInfo.SpeedUpflage) {
				 myCarInfo.SpeedUpflage = false;
				 final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
					.setMessage(popMessage(car)).setPositiveButton("确定",null).create();
				 alg.show();
				 Window  dialogWindow = alg.getWindow();
				 dialogWindow.setGravity(Gravity.BOTTOM);
				 dialogWindow.setWindowAnimations(R.style.diashow1);
//				 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//				 lp.alpha = 0.9f;
//				 dialogWindow.setAttributes(lp);
//				 alg.show();

				 mflickerCount = 0;
				 new Handler().postDelayed(new Runnable() {
					 public void run() {
						 alg.dismiss();
					 }
				 }, 3000);
				 NotificationManager manger = (NotificationManager) 
				 getSystemService(Context.NOTIFICATION_SERVICE); 
				 Notification notification = new Notification();
				 notification.defaults=Notification.DEFAULT_SOUND;
				 manger.notify(1, notification);
			}
		}
		else {
			otherCars = app.getOtherCarInfo();
			if((otherCars != null)&&(!otherCars.isEmpty()))
			for (CarInfo i : otherCars) {
				if (i.CarPlateNumber.equals(car.CarPlateNumber) ){
					if (i.TurnLflage) {
						i.TurnLflage = false;
						final  AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
							.setMessage(popMessage(car)).setPositiveButton("确定",null).create();
						 alg.show();
						 Window  dialogWindow = alg.getWindow();
						 dialogWindow.setGravity(Gravity.BOTTOM);
						 dialogWindow.setWindowAnimations(R.style.diashow1);
//						 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//						 lp.alpha = 0.9f;
//						 dialogWindow.setAttributes(lp);
//						 alg.show();

						 mflickerCount = 0;
						 new Handler().postDelayed(new Runnable() {
							 public void run() {
								 alg.dismiss();
							 }
						 }, 3000);
						 NotificationManager manger = (NotificationManager) 
						 getSystemService(Context.NOTIFICATION_SERVICE); 
						 Notification notification = new Notification();
						 notification.defaults=Notification.DEFAULT_SOUND;
						 manger.notify(1, notification);
					}
					if (i.TurnRflage) {
						i.TurnRflage = false;
						 final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
							.setMessage(popMessage(car)).setPositiveButton("确定",null).create();
						 alg.show();
						 Window  dialogWindow = alg.getWindow();
						 dialogWindow.setGravity(Gravity.BOTTOM);
						 dialogWindow.setWindowAnimations(R.style.diashow1);
//						 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//						 lp.alpha = 0.9f;
//						 dialogWindow.setAttributes(lp);
//						 alg.show();

						 mflickerCount = 0;
						 new Handler().postDelayed(new Runnable() {
							 public void run() {
								 alg.dismiss();
							 }
						 }, 3000);
						 NotificationManager manger = (NotificationManager) 
						 getSystemService(Context.NOTIFICATION_SERVICE); 
						 Notification notification = new Notification();
						 notification.defaults=Notification.DEFAULT_SOUND;
						 manger.notify(1, notification);
					}
					if (i.Brakeflage) {
						i.Brakeflage = false;
						final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
							.setMessage(popMessage(car)).setPositiveButton("确定",null).create();
						 alg.show();
						 Window  dialogWindow = alg.getWindow();
						 dialogWindow.setGravity(Gravity.BOTTOM);
						 dialogWindow.setWindowAnimations(R.style.diashow1);
//						 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//						 lp.alpha = 0.9f;
//						 dialogWindow.setAttributes(lp);
//						 alg.show();

						 mflickerCount = 0;
						 new Handler().postDelayed(new Runnable() {
							 public void run() {
								 alg.dismiss();
							 }
						 }, 3000);
						 NotificationManager manger = (NotificationManager) 
						 getSystemService(Context.NOTIFICATION_SERVICE); 
						 Notification notification = new Notification();
						 notification.defaults=Notification.DEFAULT_SOUND;
						 manger.notify(1, notification);
					}
					if (i.Collisionflage) {
						i.Collisionflage = false;
						final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
						.setMessage(popMessage(car)).setPositiveButton("确定",null).create();
						 alg.show();
						 Window  dialogWindow = alg.getWindow();
						 dialogWindow.setGravity(Gravity.BOTTOM);
						 dialogWindow.setWindowAnimations(R.style.diashow1);
//						 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//						 lp.alpha = 0.9f;
//						 dialogWindow.setAttributes(lp);
//						 alg.show();

						 mflickerCount = 0;	
						 new Handler().postDelayed(new Runnable() {
							 public void run() {
								 alg.dismiss();
							 }
						 }, 3000);
						 NotificationManager manger = (NotificationManager) 
						 getSystemService(Context.NOTIFICATION_SERVICE); 
						 Notification notification = new Notification();
						 notification.defaults=Notification.DEFAULT_SOUND;
						 manger.notify(1, notification);

					}
					if (i.Overflage) {
						i.Overflage = false;
						final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
						.setMessage(popMessage(car)).setPositiveButton("确定", null).create();
						 alg.show();
						 Window  dialogWindow = alg.getWindow();
						 dialogWindow.setGravity(Gravity.BOTTOM);
						 dialogWindow.setWindowAnimations(R.style.diashow1);
//						 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//						 lp.alpha = 0.9f;
//						 dialogWindow.setAttributes(lp);
//						 alg.show();

						 mflickerCount = 0;
						 new Handler().postDelayed(new Runnable() {
							 public void run() {
								 alg.dismiss();
							 }
						 }, 3000);
						 NotificationManager manger = (NotificationManager) 
						 getSystemService(Context.NOTIFICATION_SERVICE); 
						 Notification notification = new Notification();
						 notification.defaults=Notification.DEFAULT_SOUND;
						 manger.notify(1, notification);
					}
					if (i.SpeedUpflage) {
						i.SpeedUpflage = false;
						final AlertDialog alg = new AlertDialog.Builder(MainActivity.this).setTitle(car.CarPlateNumber)
						.setMessage(popMessage(car)).setPositiveButton("确定",null).create();
						 alg.show();
						 Window  dialogWindow = alg.getWindow();
						 dialogWindow.setGravity(Gravity.BOTTOM);
						 dialogWindow.setWindowAnimations(R.style.diashow1);
//						 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//						 lp.alpha = 0.9f;
//						 dialogWindow.setAttributes(lp);
//						 alg.show();

						 mflickerCount = 0;
						 new Handler().postDelayed(new Runnable() {
							 public void run() {
								 alg.dismiss();
							 }
						 }, 3000);
						NotificationManager manger = (NotificationManager) 
						getSystemService(Context.NOTIFICATION_SERVICE); 
					    Notification notification = new Notification();
					    notification.defaults=Notification.DEFAULT_SOUND;
					    manger.notify(1, notification);
					}
				}
			}
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return super.onKeyDown(keyCode, event);
	}
    private void exit() {
    	 new AlertDialog.Builder(MainActivity.this)
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
    private static LatLng GPS2Baidu(LatLng sourceLatLng){// 将GPS设备采集的原始GPS坐标转换成百度坐标  
    	CoordinateConverter converter  = new CoordinateConverter();  
    	converter.from(CoordType.GPS);  
    	// sourceLatLng待转换坐标  
    	converter.coord(sourceLatLng);  
    	LatLng desLatLng = converter.convert();
    	return desLatLng;
    }
    
    /**
	 * 求取10分钟内速度的均值
	 * @param vertical
	 * @return
	 */
	private float Average(List<Float> vertical){
		float averge  = 0;
		for(int i = 0; i < vertical.size();i++)
			averge = averge + vertical.get(i).floatValue();
		averge = averge/vertical.size();
		return averge;
	}
	/**
	 * 速度合理性隶属度函数
	 * @param speed 车辆的瞬时速度
	 * @param vertical 10分钟内车辆的速度集合
	 * @return
	 */
	private double VerticalReasonable(float speed,List<Float> vertical){
		double result = 0;
		if (speed < 3) {
			result = 1;
		}else {
			if (speed <= 0.8*Average(vertical)) {
				result = Math.pow(Math.E, -Math.pow((speed-0.8*Average(vertical))/stdv, 2));
			}
			else {
				if (speed <= 1.2*Average(vertical)) {
					result = 1;
				}else {
					if (speed <= Vertical_max) {
						result = Math.pow(Math.E, -Math.pow((speed-1.2*Average(vertical))/stdv, 2));
					}
					else {
						result = 0;
					}
				}
			}
		}
		return result;
		
	}
	/**
	 * 速度稳定性隶属度
	 * @param Accelerate 车辆的瞬时加速度
	 * @return
	 */
	private  double Vertical_Stable(float Accelerate){
		double result = 0;
		if(Math.abs(Accelerate) <=1){
			result =1;
		}else {
			result = Math.pow(Math.E,-Math.pow((Math.abs(Accelerate)-1)/stda,2));
		}
		return result;
	}
	/**
	 * 角速度隶属度
	 * @return
	 */
	private double Angular_Speed(int kw1,int kw2,int kw3,int kw4 ){
		double result = 0;
		result = 0.1*Math.pow(Math.E, -Math.pow(kw1/stdw1, 2))+0.2*Math.pow(Math.E, -Math.pow(kw2/stdw2, 2))
				+0.3*Math.pow(Math.E, -Math.pow(kw3/stdw3, 2))+0.4*Math.pow(Math.E, -Math.pow(kw4/stdw4, 2));
		return result;
		
	}
	/**
	 * 滚动角隶属度
	 * @return
	 */
	private double Roll(int kr1,int kr2,int kr3){
		double result = 0;
		result = 0.2*Math.pow(Math.E, -Math.pow(kr1/stdr1, 2))+0.3*Math.pow(Math.E, -Math.pow(kr2/stdr2, 2))
				+0.5*Math.pow(Math.E, -Math.pow(kr3/stdr3, 2));
		return result;
	}
	/**
	 * 安全驾驶评价
	 * @return
	 */
	private double SafetyAssess(float speed,List<Float> vertical,float Accelerate,int kw1,int kw2,int kw3,int kw4,int kr1,int kr2,int kr3){
		double result = 0;
		result = 0.5396*(0.5*VerticalReasonable(speed,vertical)+0.5*Vertical_Stable(Accelerate))+0.2969*Angular_Speed(kw1,kw2,kw3,kw4)+0.1634*Roll(kr1,kr2,kr3);
		return result;
	}
	/**
	 * 节气门开度合理性函数
	 * @param tpi 实时的节气门数据
	 * @return
	 */
	private double OpenReasonable(float speed,double tpi){
		double result = 0;
		//当前速度下的理想节气门函数
		double Tpi = 0.507*speed + 28.66;
		if (tpi < 0.8*Tpi) {
			result = Math.pow(Math.E, -Math.pow((tpi-0.8*Tpi)/stdTPI, 2));
//				result = Math.pow(Math.E, -Math.pow((30.98-0.8*Tpi)/stdTPI, 2));
		}else {
			if ((0.8*Tpi<=tpi)&&(tpi<=1.2*Tpi)) {
				result = 1;
			}else {
				result = Math.pow(Math.E, -Math.pow((tpi-1.2*Tpi)/stdTPI, 2));
//					result = Math.pow(Math.E, -Math.pow((30.98-1.2*Tpi)/stdTPI, 2));
			}
		}
		return result;
	}
	/**
	 * 节气门开度稳定性隶属度函数可以用油耗变化量来衡量
	 * @param tpi
	 * @return
	 */
	private double OpenStable(double oil,double preTpi){
		double result = 0;
		result = Math.pow(Math.E, -Math.pow(Math.abs(oil-preTpi)/stdTPId, 2));
//			result = Math.pow(Math.E, -Math.pow(Math.abs(1.35)/stdTPId, 2));
		return result;
	}
	private double  EconomicalAssess(float speed,double tpi,double preTpi,double oil){
		double result = 0;
		result = 0.6*OpenReasonable(speed, tpi) + 0.4*OpenStable(oil,preTpi);
		return result;
	}
	private double assessAll(float speed,List<Float> vertical,float Accelerate,int kw1,int kw2,int kw3,int kw4,int kr1,int kr2,int kr3,double tpi,double preTpi,double oil){
		double result = 0;
		result = 0.6*SafetyAssess(speed,vertical,Accelerate,kw1,kw2,kw3,kw4,kr1,kr2,kr3)+0.4*EconomicalAssess(speed,tpi,preTpi,oil);
		return result;
	}
	private int[] Score(){
		List<Float> vertical = app.getVertical();
		/**
		 * 加速度隶属度
		 *kw1:慢速转弯的次数
		 */
		int kw1 = app.getSlowTurn();
		/**
		 * 角速度隶属度
		 * kw2:正常转弯的次数
		 */
		int kw2 = app.getNormTurn();
		/**
		 * 角速度隶属度
		 * kw3:快速转弯的次数
		 */
		int kw3 = app.getQuickTurn();
		/**
		 * 角速度隶属度
		 * kw4:紧急转弯的次数
		 */
		int kw4 = app.getEmgTurn();
		/**
		 * 滚动角隶属度
		 * kr1:无侧翻或轻微侧翻风险次数
		 */
		int kr1 = app.getNoTurnOver();
		/**
		 * 滚动角隶属度
		 * kr2:中等侧翻风险次数
		 */
		int kr2 = app.getMidTurnOver();
		/**
		 * 滚动角隶属度
		 * kr3:高等侧翻风险次数
		 */
		int kr3 = app.getTurnOver();	
		
		float accelerate = app.getMyCarInfo().acceleration_X;
		/**
		 * 车辆的瞬时速度
		 */
		float Vertical = app.getSpeed();
		/**
		 * 节气门开度
		 */
		double tpi = app.getTpi();
		/**
		 * 油耗
		 */
		double oil = app.getOil();
		/**
		 * 前一时刻的节气门开度
		 */
		double PreTpi = app.getPreOil();
		/**
		 *本车速度
		 */
		float speed = app.getMyCarInfo().speed;
		int[] score = new int[3];
		score[0] =	(int)(SafetyAssess(Vertical,vertical,accelerate,kw1, kw2, kw3,kw4,kr1, kr2, kr3)*100);
		System.out.println("value1 = " + score[0]);
		score[1] = (int)(EconomicalAssess(Vertical,tpi,PreTpi,oil)*100);
		System.out.println("value2 = " + score[1]);
		score[2] = (int)( assessAll(speed,vertical,accelerate,kw1, kw2, kw3,kw4,kr1, kr2, kr3,tpi,PreTpi,oil)*100);
		app.setScore(score);
		return score;
	}
	
    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(ContentFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(ContentFragment.CARMESSAGE, R.drawable.carmessage);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(ContentFragment.DRIACTION, R.drawable.driveraction);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(ContentFragment.ROUTE, R.drawable.route);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(ContentFragment.SYSMESSAGE, R.drawable.systemmessage);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(ContentFragment.SHOP, R.drawable.cheekscreature);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(ContentFragment.PARTY, R.drawable.chinacreature);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(ContentFragment.MOVIE, R.drawable.earscreature);
        list.add(menuItem7);
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("VANET");
        toolbar.setLogo(R.drawable.gemini);
        
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_information:
                        Toast.makeText(MainActivity.this, "个人信息 !", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_message:
                        Toast.makeText(MainActivity.this, "个人聊天记录 !", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "相关设置信息 !", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "No Correct enter !", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    
    @Override
    public void onSwitch(Resourceble slideMenuItem, int position) {
        switch (slideMenuItem.getName()) {
            case ContentFragment.CLOSE:
                return ;
            case ContentFragment.CARMESSAGE:
	            Intent intent2 = new Intent(MainActivity.this, CarnetActivity.class);
	            startActivity(intent2);
                return ;
            case ContentFragment.DRIACTION:
	        	Intent intent = new Intent(MainActivity.this, DriveAssess.class);
				startActivity(intent);
                return ;
            case ContentFragment.ROUTE:
	            Intent intent3 = new Intent(MainActivity.this, RoutSearch.class);
	            startActivity(intent3);
                return ;
            case ContentFragment.SYSMESSAGE:
	        	Intent Toqueryctivity = new Intent(MainActivity.this, System_Down.class);
				startActivity(Toqueryctivity);
                return ;
            case ContentFragment.PARTY:
            case ContentFragment.SHOP:
            case ContentFragment.MOVIE:
            default:
                return ;
        }
    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }
	
//		//提示音  
//		   private static void startAlarm(Context context) {  
//		       Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);  
//		       if (notification == null) return;  
//		       Ringtone r = RingtoneManager.getRingtone(context, notification);  
//		       r.play();  
//		   } 
}