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
				"��̣�"+myApplication.getMileage() +"km",
				"ʱ��: " + myApplication.getSpeed() + "km/h",
				"����ɲ��������" + myApplication.getQuickBrakeTime() + "��",
				"����ɲ��������" + myApplication.getEmgBrakeTime()+ "��",
				"���ټ��ٴ�����" + myApplication.getQuickSpeedUpTime() + "��",
				"�������ٴ�����" + myApplication.getEmgSpeedUpTime()+ "��",
				"����ת�������" + myApplication.getQuickTurn()+ "��",
				"����ת�������" + myApplication.getEmgTurn()+ "��",
				"�෭������" + myApplication.getTurnOver()+ "��"
			};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecognizeResult.this,  android.R.layout.simple_list_item_1, 
				listContent);
		resultList.setAdapter(adapter);
	}
}
