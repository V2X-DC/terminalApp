package com.example.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyHelper extends SQLiteOpenHelper {
	
	private static String DB_NAME = "mydata.db";  //数据库名称
	public static String TABLE_NAME = "event"; //表名
	
	/**super(参数1，参数2，参数3，参数4)，其中参数4是代表数据库的版本，
	 * 是一个大于等于1的整数，如果要修改（添加字段）表中的字段，则设置
	 * 一个比当前的 参数4大的整数 ，把更新的语句写在onUpgrade(),下一次
	 * 调用
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
		db.execSQL(sql); 		//创建表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.e("update", "update");
//		db.execSQL("ALTER TABLE "+ MyHelper.TABLE_NAME+" ADD sex TEXT"); //修改字段
	}
}
