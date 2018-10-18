package com.example.hhvanet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.utils.DatabaseUtil;
import com.example.utils.EventInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.daimajia.swipedemo.adapter.ListViewAdapter;

public class System_Down extends AppCompatActivity  {
	private ListView mList; //显示查询结果
    private ListViewAdapter mAdapter;
    private Context mContext = this;
	
	private DatabaseUtil mUtil;
	
	private List<EventInfo> list = null;
	
	private int id = 1 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.query);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);
		setActionBar();
		//初始化
		mList = (ListView)findViewById(R.id.mlist);
		//获取数据库
		mUtil = new DatabaseUtil(System_Down.this);
//		//获取选择对象
		list = mUtil.queryAll();
		if (list.size() >=200) {
			mUtil.Delete(id);
			list.remove(0);
		}
		List<Map<String, Object>> templist = new ArrayList<Map<String,Object>>();
		for(EventInfo eventInfo:list){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("_id", eventInfo.getId());
			map.put("time", eventInfo.getTime());
			map.put("eventContent", eventInfo.getEventContent());
			templist.add(map);
			if (templist.size()>= 200) {
				map.remove(0);
				templist.remove(0);
			}
		}
		
		 mAdapter = new ListViewAdapter(this, list);
		 mList.setAdapter(mAdapter);
	     mAdapter.setMode(Attributes.Mode.Single);
	     mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	 //这个应该是点击后出现条目滑动
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                ((SwipeLayout)(mList.getChildAt(position - mList.getFirstVisiblePosition()))).open(true);
	            }
	        });
	     mList.setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                return false;
	            }
	        });
	     //长按
	     mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	            @Override
	            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
	                Toast.makeText(mContext, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
	                return true;
	            }
	        });
	     mList.setOnScrollListener(new AbsListView.OnScrollListener() {
	            @Override
	            public void onScrollStateChanged(AbsListView view, int scrollState) {
	            }

	            @Override
	            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	
	            }
	        });

	     mList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	            }
	
	            @Override
	            public void onNothingSelected(AdapterView<?> parent) {
	            }
	        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.system_down, menu);
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
                    case R.id.syshome:
                        Toast.makeText(System_Down.this, "return home !", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:
                        Toast.makeText(System_Down.this, "No Correct enter !", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}