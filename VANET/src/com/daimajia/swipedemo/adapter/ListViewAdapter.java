package com.daimajia.swipedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.hhvanet.R;
import com.example.hhvanet.System_Down;
import com.example.utils.DatabaseUtil;
import com.example.utils.EventInfo;

public class ListViewAdapter extends BaseSwipeAdapter {
	
	private DatabaseUtil mUtil;
	
	private List<EventInfo> list = null;
    private Context mContext;

    public ListViewAdapter(Context mContext) {
        this.mContext = mContext;
    }
    
    public ListViewAdapter(Context mContext, List<EventInfo> l) {
        this.mContext = mContext;
        this.list = l;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        
		mUtil = new DatabaseUtil(this.mContext);
//		//获取选择对象
//		list = mUtil.queryAll();
		
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            //打开时动画
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
        	//双击
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
    //这是显示内容
    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
        t.setText(list.get(position).getId() + list.get(position).getTime() + list.get(position).getEventContent());
   
        final SwipeLayout sl = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
        final TextView delete = (TextView)convertView.findViewById(R.id.delete);
        delete.setTag(position);
        delete.setOnClickListener(new View.OnClickListener() {
        	//单击
            @Override
            public void onClick(View view) {
            	int pos = (Integer) delete.getTag();
            	list.remove(pos);
            	mUtil.Delete(list.get(pos).getId());
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();  
                sl.close();
            }
        });
    }

    @Override
    public int getCount() {
        return (int)(list.size());
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
