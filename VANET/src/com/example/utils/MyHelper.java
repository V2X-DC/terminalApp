package com.example.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyHelper extends SQLiteOpenHelper {
	
	private static String DB_NAME = "mydata.db";  //���ݿ�����
	public static String TABLE_NAME = "event"; //����
	
	/**super(����1������2������3������4)�����в���4�Ǵ������ݿ�İ汾��
	 * ��һ�����ڵ���1�����������Ҫ�޸ģ�����ֶΣ����е��ֶΣ�������
	 * һ���ȵ�ǰ�� ����4������� ���Ѹ��µ����д��onUpgrade(),��һ��
	 * ����
	 */
	public MyHelper(Context context) {
				super(context, DB_NAME, null, 2);
				Log.d("table oncreate", "try to  helper");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create table
		Log.d("table oncreate", "try to create table");
		String sql = "CREATE TABLE "+TABLE_NAME + "("
					  + "_id INTEGER PRIMARY KEY,"
					  + "time TEXT," 
					  + "eventContent);";
					 
		Log.e("table oncreate", "create table");
		db.execSQL(sql); 		//������
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.e("update", "update");
//		db.execSQL("ALTER TABLE "+ MyHelper.TABLE_NAME+" ADD sex TEXT"); //�޸��ֶ�
	}
}
