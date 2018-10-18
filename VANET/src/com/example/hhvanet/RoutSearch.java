package com.example.hhvanet;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.RouteLineAdapter;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.slidrlayout.fragment.ContentFragment;
import com.example.slidrlayout.interfaces.Resourceble;
import com.example.slidrlayout.util.ViewAnimator;


public class RoutSearch extends AppCompatActivity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// 定位
	private LocationClient mLocationClient = null;
	private ImageView requestBtn;
	// 路线规划对象
	private RoutePlanSearch mSearch;
	// 定位时中心点标注
	private Marker myLocationMarker;
	private String city = "";
	/**
	 * 是否首次定位
	 */
	private boolean isFirstLoc = true;


	private Button startBtn;
	private EditText startEt;
	private EditText endEt;
	private EditText cityEt;
	
	RouteLine route = null;
	 OverlayManager routeOverlay = null;
	 DrivingRouteResult nowResultdrive  = null;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.route);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		setActionBar();
		// --初始化 获取地图控件引用
				mMapView = (MapView) findViewById(R.id.bmapView);
				requestBtn = (ImageView) findViewById(R.id.request_iv);
				startBtn = (Button) findViewById(R.id.start_btn);
				startEt = (EditText) findViewById(R.id.start_et);
				endEt = (EditText) findViewById(R.id.end_et);
				cityEt = (EditText) findViewById(R.id.city_et);

				mBaiduMap = mMapView.getMap();
				mLocationClient = new LocationClient(getApplicationContext());
				startEt.requestFocus();
				LocationClientOption option = new LocationClientOption();
				// --定位参数
				option.setOpenGps(true); // 打开GPRS
				option.setAddrType("all");// 返回的定位结果包含地址信息
				option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
				option.setOpenGps(true);
				option.setLocationMode(LocationMode.Hight_Accuracy);
				// 设置缩放控件不显示
				mMapView.showZoomControls(true);
				// 设置地图参数
				mLocationClient.setLocOption(option);
				// 注册定位成功监听
				mLocationClient.registerLocationListener(new MyLocationListener());

				mLocationClient.start();// 启动定位SDK
				
				//去掉百度logo
	  		    mMapView.removeViewAt(1);

				requestBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mLocationClient != null && mLocationClient.isStarted()) {
							int res = mLocationClient.requestLocation();

						} else if (mLocationClient != null) {
							mLocationClient.start();
						} else {
							Toast.makeText(RoutSearch.this, "mLocationClient为null",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				startBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mBaiduMap.clear();
						// TODO Auto-generated method stub
						String start = startEt.getText().toString();
						String end = endEt.getText().toString();
						String city = cityEt.getText().toString();
						if (city == null || city.equals("")) {
							city = RoutSearch.this.city;
						}
						routePlan(start, end, city);
					}
				});

			}

			/**
			 * 将地图当前位置移动到指定位置
			 * 
			 * @param eBikeAMap
			 *            百度地图baiduMap对象
			 * @param cenpt地图中心点坐标
			 * @param zoom缩放级别
			 */
			public void updateLocationPosition(BaiduMap eBikeAMap, LatLng cenpt,
					float zoom, boolean isFirstLocation) {
				if(!isFirstLocation){
					return;
				}
				// 定义地图状态
				MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom)
						.build();
				// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

				MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
						.newMapStatus(mMapStatus);
				// 改变地图状态
				mBaiduMap.setMapStatus(mMapStatusUpdate);
			}

			/**
			 * 发起路线规划
			 */
			public void routePlan(String start, String end, String city) {
				mSearch = RoutePlanSearch.newInstance();
				mSearch.setOnGetRoutePlanResultListener(listener);
				// 起点与终点
				PlanNode stNode = PlanNode.withCityNameAndPlaceName(city, start);
				PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, end);
				// 步行路线规划
				boolean res = mSearch.walkingSearch(new WalkingRoutePlanOption().from(
						stNode).to(enNode));

//				// 驾车路线规划
//				boolean res = mSearch.drivingSearch(new
//				 DrivingRoutePlanOption().from(stNode).to(enNode));
			}

			/**
			 * 定位成功监听
			 * 
			 * @author liling
			 * 
			 */
			public class MyLocationListener implements BDLocationListener {
				@Override
				public void onReceiveLocation(BDLocation location) {
					if (location == null)
						return;
					/**
					 * 标注当前位置 如果不是第一次,先删除
					 */
					if (myLocationMarker != null && isFirstLoc == false) {
						myLocationMarker.remove();
					}
					LatLng cenpt = new LatLng(location.getLatitude(),
							location.getLongitude());
					/*
					 * Toast.makeText(MainActivity.this, "onReceive:" +
					 * location.getAddrStr(), Toast.LENGTH_SHORT) .show();
					 */
					/*
					 * Toast.makeText(MainActivity.this, "error code:" +
					 * location.getLocType(), Toast.LENGTH_SHORT) .show();
					 */
					try {
						// 未输入城市则取当前定位到的城市
						try {

							String addr = location.getAddrStr();
							city = addr.substring(addr.indexOf("省") + 1,
									addr.indexOf("市") + 1);
						} catch (Exception e) {
							// TODO: handle exception
						}
						// ------------------定义Maker坐标点
						LatLng point = new LatLng(cenpt.latitude, cenpt.longitude);
						// 构建Marker图标
						BitmapDescriptor bitmap = BitmapDescriptorFactory
								.fromResource(R.drawable.real_time_location);
						// 构建MarkerOption，用于在地图上添加Marker
						OverlayOptions option = new MarkerOptions().position(point)
								.icon(bitmap);
						// 在地图上添加Marker，并显示
						myLocationMarker = (Marker) mBaiduMap.addOverlay(option);
//						isFirstLoc = false;
						// ------------------
						Log.d("Log", "2333333333--->long:" + cenpt.longitude + "lau:"
								+ cenpt.latitude);
						updateLocationPosition(mBaiduMap, cenpt, 18, isFirstLoc);
						isFirstLoc = false;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();// 这里,每次打开应用,back退出程序,再次打开就会出现nullPointerException
					}

				}
			}

			/**
			 * 路线规划结果监听
			 */
			OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
				/**
				 * 步行
				 */
				@Override
				public void onGetWalkingRouteResult(WalkingRouteResult result) {
					// 获取步行线路规划结果
					if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
						Toast.makeText(RoutSearch.this, "抱歉，未找到结果",
								Toast.LENGTH_SHORT).show();
					}
					if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
						// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
						// result.getSuggestAddrInfo()
						return;
					}
					if (result.error == SearchResult.ERRORNO.NO_ERROR) {
						WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(
								mBaiduMap);
						mBaiduMap.setOnMarkerClickListener(overlay);
						overlay.setData(result.getRouteLines().get(0));
						overlay.addToMap();
						overlay.zoomToSpan();
					}
				}
				
				public void onGetTransitRouteResult(TransitRouteResult result) {
					// 获取公交换乘路径规划结果
				}
				
				@Override
			    public void onGetDrivingRouteResult(DrivingRouteResult result) {
//			        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			            Toast.makeText(RoutSearch.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
//			        }
//			        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//			            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//			            // result.getSuggestAddrInfo()
//			            return;
//			        }
//
//
//			            if (result.getRouteLines().size() > 1 ) {
//			                nowResultdrive = result;
//
//			                MyTransitDlg myTransitDlg = new MyTransitDlg(RoutSearch.this,
//			                        result.getRouteLines(),
//			                         RouteLineAdapter.Type.DRIVING_ROUTE);
//			                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
//			                    public void onItemClick(int position) {
//			                        route = nowResultdrive.getRouteLines().get(position);
//			                        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
//			                        mBaiduMap.setOnMarkerClickListener(overlay);
//			                        routeOverlay = overlay;
//			                        overlay.setData(nowResultdrive.getRouteLines().get(position));
//			                        overlay.addToMap();
//			                        overlay.zoomToSpan();
//			                    }
//
//			                });
//			                myTransitDlg.show();
//
//			            } else if ( result.getRouteLines().size() == 1 ) {
//			                route = result.getRouteLines().get(0);
//			                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
//			                routeOverlay = overlay;
//			                mBaiduMap.setOnMarkerClickListener(overlay);
//			                overlay.setData(result.getRouteLines().get(0));
//			                overlay.addToMap();
//			                overlay.zoomToSpan();
////			                mBtnPre.setVisibility(View.VISIBLE);
////			                mBtnNext.setVisibility(View.VISIBLE);
//			            } else {
//			                Log.d("route result", "结果数<0" );
//			                return;
//			            }

			        }

//				public void onGetDrivingRouteResult(DrivingRouteResult result) {
//					// 获取驾车线路规划结果
//				}
				@Override
				public void onGetBikingRouteResult(BikingRouteResult arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onGetIndoorRouteResult(IndoorRouteResult arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onGetMassTransitRouteResult(
						MassTransitRouteResult arg0) {
					// TODO Auto-generated method stub
					
				}
			};

			/**
			 * 继承步行规划的子类,通过覆盖相应方法实现功能
			 * 
			 * BitmapDescriptor getStartMarker() 覆写此方法以改变默认起点图标 BitmapDescriptor
			 * getTerminalMarker() 覆写此方法以改变默认终点图标
			 * 
			 * @author qiyaru
			 * 
			 */
			class MyWalkingRouteOverlay extends WalkingRouteOverlay {

				public MyWalkingRouteOverlay(BaiduMap arg0) {
					super(arg0);
					// TODO Auto-generated constructor stub
				}
			}
			class MyDrivingRouteOverlay extends DrivingRouteOverlay {

				public MyDrivingRouteOverlay(BaiduMap arg0) {
					super(arg0);
					// TODO Auto-generated constructor stub
				}
			}

			// -----------重写方法---------------------------------------------------------------------------

//			@Override
//			public boolean onCreateOptionsMenu(Menu menu) {
//				getMenuInflater().inflate(R.menu.main, menu);
//				return true;
//			}
//
//			@Override
//			public boolean onOptionsItemSelected(MenuItem item) {
//				int id = item.getItemId();
//				if (id == R.id.action_settings) {
//					return true;
//				}
//				return super.onOptionsItemSelected(item);
//			}

			@Override
			protected void onDestroy() {
				// TODO Auto-generated method stub
				super.onDestroy();
				// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
				mMapView.onDestroy();
				if (mSearch != null) {
					mSearch.destroy();
				}
			}

			@Override
			protected void onResume() {
				// TODO Auto-generated method stub
				super.onResume();
				// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
				mMapView.onResume();
			}

			@Override
			protected void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
				// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
				mMapView.onPause();

			}
			  // 供路线选择的Dialog
		    class MyTransitDlg extends Dialog {

		        private List<? extends RouteLine> mtransitRouteLines;
		        private ListView transitRouteList;
		        private  RouteLineAdapter mTransitAdapter;

		        OnItemInDlgClickListener onItemInDlgClickListener;

		        public MyTransitDlg(Context context, int theme) {
		            super(context, theme);
		        }

		        public MyTransitDlg(Context context, List< ? extends RouteLine> transitRouteLines,  RouteLineAdapter.Type
		                type) {
		            this( context, 0);
		            mtransitRouteLines = transitRouteLines;
		            mTransitAdapter = new  RouteLineAdapter( context, mtransitRouteLines , type);
		            requestWindowFeature(Window.FEATURE_NO_TITLE);
		        }

		        @Override
		        protected void onCreate(Bundle savedInstanceState) {
		            super.onCreate(savedInstanceState);
		            setContentView(R.layout.activity_transit_dialog);

		            transitRouteList = (ListView) findViewById(R.id.transitList);
		            transitRouteList.setAdapter(mTransitAdapter);

		            transitRouteList.setOnItemClickListener( new AdapterView.OnItemClickListener() {

		                @Override
		                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		                    onItemInDlgClickListener.onItemClick( position);
//		                    mBtnPre.setVisibility(View.VISIBLE);
//		                    mBtnNext.setVisibility(View.VISIBLE);
		                    dismiss();

		                }
		            });
		        }

		        public void setOnItemInDlgClickLinster( OnItemInDlgClickListener itemListener) {
		            onItemInDlgClickListener = itemListener;
		        }

		    }
		    interface OnItemInDlgClickListener {
		        public void onItemClick(int position);
		    }
		    
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
			    MenuInflater inflater = getMenuInflater();
			    inflater.inflate(R.menu.routesearch, menu);
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
		                    case R.id.routehome:
		                        Toast.makeText(RoutSearch.this, "return home !", Toast.LENGTH_SHORT).show();
		                        finish();
		                        break;
		                    default:
		                        Toast.makeText(RoutSearch.this, "No Correct enter !", Toast.LENGTH_SHORT).show();
		                        break;
		                }
		                return true;
		            }
		        });
		    }
}
