package com.example.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseUtil {
	private MyHelper helper;

	public DatabaseUtil(Context context) {
		super();
		helper = new MyHelper(context);
	}
	
	/**插入数据
	 * @param String
	 * */
	public boolean Insert(EventInfo eventInfo){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "+MyHelper.TABLE_NAME
					+"(time,eventContent) values (" 
					+ "'"+eventInfo.getTime()
					+ "' ," + "'"+ eventInfo.getEventContent() + "'" + ")"; 
		try { 
			  db.beginTransaction();
			db.execSQL(sql); 
			 db.setTransactionSuccessful();
			return true;
			} catch (SQLException e){  
				Log.e("err", "insert failed"); 
				return false;
				}finally{
					db.endTransaction();
					db.close();
				}
		
	}
	public long numSqlite (){
	 	SQLiteDatabase db = helper.getReadableDatabase();
	 	Cursor cursor = db.query(MyHelper.TABLE_NAME,null,null,null, null, null, null);
	 	int count = 0;
	 	cursor.moveToFirst(); 
	 	while(cursor.moveToNext()){
	 		count++;
	 	}
	    cursor.close();  
	    return count;  
 }
	
	
	/**更新数据
	 * @param Person person , int id
	 * */
	
	public void Update(EventInfo eventInfo ,int id){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("time", eventInfo.getTime());
		values.put("eventContent", eventInfo.getEventContent());
		int rows = db.update(MyHelper.TABLE_NAME, values, "_id=?", new String[] { id + "" });	
		db.close();
	}
	
	/**删除数据
	 * @param int id
	 * @return 
	 * */
	
	public int Delete(int id){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		int raw = db.delete(MyHelper.TABLE_NAME, "_id=?", new String[]{id+""});
		db.close();
		return id;
	}
	
	/**所有数据
	 * 
	 * */
	public List<EventInfo> queryAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<EventInfo> list = new ArrayList<EventInfo>();
		Cursor cursor = db.query(MyHelper.TABLE_NAME,null,null,null, null, null, null);
		while(cursor.moveToNext()){
			EventInfo eventInfo = new EventInfo();
			eventInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));   
			eventInfo.setTime(cursor.getString(cursor.getColumnIndex("time"))); 
			eventInfo.setEventContent(cursor.getString(cursor.getColumnIndex("eventContent")));
			 list.add(eventInfo);
		}
		db.close();
		return list;
	}
	
//	
	/**按姓名进行查找并排序
	 * 
	 * */
	public List<EventInfo> queryBytime(String time){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<EventInfo> list = new ArrayList<EventInfo>();
		Cursor cursor = db.query(MyHelper.TABLE_NAME, new String[]{"_id","time","eventContent"}, "time like ? " ,new String[]{"%" +time + "%" }, null, null, "time asc");
//		Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
		while(cursor.moveToNext()){
			EventInfo eventInfo= new EventInfo();
			eventInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));   
			eventInfo.setTime(cursor.getString(cursor.getColumnIndex("time"))); 
			eventInfo.setEventContent(cursor.getString(cursor.getColumnIndex("eventContent")));
			 list.add(eventInfo);
		}
		db.close();
		return list;
	} 
	
	
//	/**按id查询
//	 * 
//	 * */
//	public EventInfo queryByid(int id){
//		
//		SQLiteDatabase db = helper.getReadableDatabase();
//		EventInfo eventInfo = new EventInfo();
//		Cursor cursor = db.query(MyHelper.TABLE_NAME, new String[]{"time","eventContent"}, "_id=?",new String[]{ id + ""}, null, null, null);
////		db.delete(table, whereClause, whereArgs)
//		while(cursor.moveToNext()){
//			 eventInfo.setId(id);   
//			 eventInfo.setTime(cursor.getString(cursor.getColumnIndex("time"))); 
//			 eventInfo.setEventContent(cursor.getString(cursor.getColumnIndex("eventContent")));
//		}
//		db.close();
//		return eventInfo;
//	}
//	

}
