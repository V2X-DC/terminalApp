package com.example.hhvanet;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.slidrlayout.fragment.ContentFragment;
import com.example.slidrlayout.interfaces.Resourceble;
import com.example.slidrlayout.model.SlideMenuItem;
import com.example.slidrlayout.util.ViewAnimator;
import com.example.utils.CarInfo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 车辆信息列表，其他车辆信息采取滚动模式显示
 * @author qyr
 *
 */
public class CarnetActivity extends AppCompatActivity{
			public static Activity cActivity  ;
			Button button;
			private TextView myNUM= null;
			private TextView mySped= null;
			private TextView myTurn= null;
			private ListView otherCarsList = null;
			private TextView mAddress = null;
			protected final static String TAG = CarnetActivity.class.getSimpleName();
			private MyApplication myApplication;
			
			//车辆信息
			public static  CarInfo myCarInfo = new CarInfo();
			public static CarInfo temp1 = new CarInfo();
			public static List<CarInfo> otherCars = new ArrayList<CarInfo>();
			//存储地址
			String address = " ";
			
			
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.carlist);
				

		        setActionBar();
		        
				myApplication = (MyApplication)getApplication();
				myCarInfo = myApplication.getMyCarInfo();
				otherCars = myApplication.getOtherCarInfo();
				//show = (EditText)findViewById(R.id.show);
				myNUM = (TextView)findViewById(R.id.Mnum);
				mySped=(TextView)findViewById(R.id.Msped);
				myTurn = (TextView)findViewById(R.id.mTurn);
				otherCarsList = (ListView)findViewById(R.id.othercarlist);
				mAddress = (TextView)findViewById(R.id.address);
				myNUM.setText("车牌号码为："+myCarInfo.CarPlateNumber);
				mySped.setText("行车速度为："+myCarInfo.speed+"m/s，行车方向为："+myCarInfo.direction  + "°");
				myTurn.setText(myCarInfo.TurnLevel +"  "+myCarInfo.BrakeLevel+"  "+myCarInfo.OverLevel + " " + myCarInfo.CollisionLevel+" " + myCarInfo.SpeedUpLevel);
				mAddress.setText(myCarInfo.address);
				if(getOtherCarsInfo() != null) {
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(CarnetActivity.this, android.R.layout.simple_list_item_1, getOtherCarsInfo());
					otherCarsList.setAdapter(adapter);	
				}
//				ActionBar actionBar = getActionBar();
//				actionBar.setDisplayHomeAsUpEnabled(true);
			}
//			@Override
//			public boolean onCreateOptionsMenu(Menu menu) {
//				// Inflate the menu; this adds items to the action bar if it is present.
//				getMenuInflater().inflate(R.menu.main, menu);
//				return true;
//			}

			  
			
			private String[] getOtherCarsInfo() {
				// TODO Auto-generated method stub
				if(otherCars == null)
					return null;
				String[] s = new String[otherCars.size()];
				int i = 0;
				for (CarInfo car:otherCars) {	
					s[i++] = "车牌号码为："+car.CarPlateNumber + "\n行驶的速度为："+ car.speed +"m/s,"+"方向为："+car.direction+"°\n"
							+car.BrakeLevel + "," + car.TurnLevel+"," + car.OverLevel + "," + car.CollisionLevel+","+car.SpeedUpLevel +"\n该车当前位于："+ car.address;
				}
				return s;
			}
			 /**
		     * 根据经纬度信息反解析地址
		     * @param marker
		     * @return  地址
		     */
		    public String GeoCoder(LatLng latLng ){
		    	
		    	 //实例化一个地理编码查询对象  
	            GeoCoder geoCoder = GeoCoder.newInstance();  
	            //设置反地理编码位置坐标  
	            ReverseGeoCodeOption op = new ReverseGeoCodeOption();  
	            op.location(latLng);  
	            //发起反地理编码请求(经纬度->地址信息)  
	            geoCoder.reverseGeoCode(op);  
	            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {  
	                  
	                @Override  
	                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {  
	                    //获取点击的坐标地址  
	                    address = arg0.getAddress(); 
	                }  
	                  
	                @Override  
	                public void onGetGeoCodeResult(GeoCodeResult arg0) {  
	                }  
	            });  
	     
		    	return address;
		    }
		    
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
			    MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.main, menu);
			    return true;
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
		                    case R.id.CarActive:
		                        Toast.makeText(CarnetActivity.this, "return home !", Toast.LENGTH_SHORT).show();
		                        finish();
		                        break;
		                    default:
		                        Toast.makeText(CarnetActivity.this, "No Correct enter !", Toast.LENGTH_SHORT).show();
		                        break;
		                }
		                return true;
		            }
		        });
		    }
		    
	}