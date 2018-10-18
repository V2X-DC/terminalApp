package com.example.hhvanet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecognizeResult extends Activity{
	private MyApplication myApplication;
	private ListView resultList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recognize);
		resultList = (ListView)findViewById(R.id.result_list);
		myApplication = (MyApplication)getApplication();
		String[] listContent = {
				"里程："+myApplication.getMileage() +"km",
				"时速: " + myApplication.getSpeed() + "km/h",
				"快速刹车次数：" + myApplication.getQuickBrakeTime() + "次",
				"紧急刹车次数：" + myApplication.getEmgBrakeTime()+ "次",
				"快速加速次数：" + myApplication.getQuickSpeedUpTime() + "次",
				"紧急加速次数：" + myApplication.getEmgSpeedUpTime()+ "次",
				"快速转弯次数：" + myApplication.getQuickTurn()+ "次",
				"紧急转弯次数：" + myApplication.getEmgTurn()+ "次",
				"侧翻次数：" + myApplication.getTurnOver()+ "次"
			};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecognizeResult.this,  android.R.layout.simple_list_item_1, 
				listContent);
		resultList.setAdapter(adapter);
	}
}
