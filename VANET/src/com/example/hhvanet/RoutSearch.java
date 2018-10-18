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
	// ��λ
	private LocationClient mLocationClient = null;
	private ImageView requestBtn;
	// ·�߹滮����
	private RoutePlanSearch mSearch;
	// ��λʱ���ĵ��ע
	private Marker myLocationMarker;
	private String city = "";
	/**
	 * �Ƿ��״ζ�λ
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
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.route);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		setActionBar();
		// --��ʼ�� ��ȡ��ͼ�ؼ�����
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
				// --��λ����
				option.setOpenGps(true); // ��GPRS
				option.setAddrType("all");// ���صĶ�λ���������ַ��Ϣ
				option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
				option.setOpenGps(true);
				option.setLocationMode(LocationMode.Hight_Accuracy);
				// �������ſؼ�����ʾ
				mMapView.showZoomControls(true);
				// ���õ�ͼ����
				mLocationClient.setLocOption(option);
				// ע�ᶨλ�ɹ�����
				mLocationClient.registerLocationListener(new MyLocationListener());

				mLocationClient.start();// ������λSDK
				
				//ȥ���ٶ�logo
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
							Toast.makeText(RoutSearch.this, "mLocationClientΪnull",
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
			 * ����ͼ��ǰλ���ƶ���ָ��λ��
			 * 
			 * @param eBikeAMap
			 *            �ٶȵ�ͼbaiduMap����
			 * @param cenpt��ͼ���ĵ�����
			 * @param zoom���ż���
			 */
			public void updateLocationPosition(BaiduMap eBikeAMap, LatLng cenpt,
					float zoom, boolean isFirstLocation) {
				if(!isFirstLocation){
					return;
				}
				// �����ͼ״̬
				MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom)
						.build();
				// ����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯

				MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
						.newMapStatus(mMapStatus);
				// �ı��ͼ״̬
				mBaiduMap.setMapStatus(mMapStatusUpdate);
			}

			/**
			 * ����·�߹滮
			 */
			public void routePlan(String start, String end, String city) {
				mSearch = RoutePlanSearch.newInstance();
				mSearch.setOnGetRoutePlanResultListener(listener);
				// ������յ�
				PlanNode stNode = PlanNode.withCityNameAndPlaceName(city, start);
				PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, end);
				// ����·�߹滮
				boolean res = mSearch.walkingSearch(new WalkingRoutePlanOption().from(
						stNode).to(enNode));

//				// �ݳ�·�߹滮
//				boolean res = mSearch.drivingSearch(new
//				 DrivingRoutePlanOption().from(stNode).to(enNode));
			}

			/**
			 * ��λ�ɹ�����
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
					 * ��ע��ǰλ�� ������ǵ�һ��,��ɾ��
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
						// δ���������ȡ��ǰ��λ���ĳ���
						try {

							String addr = location.getAddrStr();
							city = addr.substring(addr.indexOf("ʡ") + 1,
									addr.indexOf("��") + 1);
						} catch (Exception e) {
							// TODO: handle exception
						}
						// ------------------����Maker�����
						LatLng point = new LatLng(cenpt.latitude, cenpt.longitude);
						// ����Markerͼ��
						BitmapDescriptor bitmap = BitmapDescriptorFactory
								.fromResource(R.drawable.real_time_location);
						// ����MarkerOption�������ڵ�ͼ�����Marker
						OverlayOptions option = new MarkerOptions().position(point)
								.icon(bitmap);
						// �ڵ�ͼ�����Marker������ʾ
						myLocationMarker = (Marker) mBaiduMap.addOverlay(option);
//						isFirstLoc = false;
						// ------------------
						Log.d("Log", "2333333333--->long:" + cenpt.longitude + "lau:"
								+ cenpt.latitude);
						updateLocationPosition(mBaiduMap, cenpt, 18, isFirstLoc);
						isFirstLoc = false;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();// ����,ÿ�δ�Ӧ��,back�˳�����,�ٴδ򿪾ͻ����nullPointerException
					}

				}
			}

			/**
			 * ·�߹滮�������
			 */
			OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
				/**
				 * ����
				 */
				@Override
				public void onGetWalkingRouteResult(WalkingRouteResult result) {
					// ��ȡ������·�滮���
					if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
						Toast.makeText(RoutSearch.this, "��Ǹ��δ�ҵ����",
								Toast.LENGTH_SHORT).show();
					}
					if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
						// ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
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
					// ��ȡ��������·���滮���
				}
				
				@Override
			    public void onGetDrivingRouteResult(DrivingRouteResult result) {
//			        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//			            Toast.makeText(RoutSearch.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
//			        }
//			        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//			            // ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
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
//			                Log.d("route result", "�����<0" );
//			                return;
//			            }

			        }

//				public void onGetDrivingRouteResult(DrivingRouteResult result) {
//					// ��ȡ�ݳ���·�滮���
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
			 * �̳в��й滮������,ͨ��������Ӧ����ʵ�ֹ���
			 * 
			 * BitmapDescriptor getStartMarker() ��д�˷����Ըı�Ĭ�����ͼ�� BitmapDescriptor
			 * getTerminalMarker() ��д�˷����Ըı�Ĭ���յ�ͼ��
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

			// -----------��д����---------------------------------------------------------------------------

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
				// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
				mMapView.onDestroy();
				if (mSearch != null) {
					mSearch.destroy();
				}
			}

			@Override
			protected void onResume() {
				// TODO Auto-generated method stub
				super.onResume();
				// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
				mMapView.onResume();
			}

			@Override
			protected void onPause() {
				// TODO Auto-generated method stub
				super.onPause();
				// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
				mMapView.onPause();

			}
			  // ��·��ѡ���Dialog
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
