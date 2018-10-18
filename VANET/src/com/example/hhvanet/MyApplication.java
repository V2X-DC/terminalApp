package com.example.hhvanet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.utils.CarInfo;

import android.R.integer;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.Preference;

public class MyApplication extends Application{
//	SharedPreferences preferences;
//	SharedPreferences.Editor editor;
	private int quickBrakeTime;
	private int emgBrakeTime;
	private int slowTurn;
	private int normTurn;
	private int quickTurn;
	private int emgTurn;
	private int NoTurnOver;
	private int MidTurnOver;
	private int TurnOver;
	private double oil;
	private int quickSpeedUpTime;
	private int emgSpeedUpTime;
	/**
	 * 节气门开度
	 */
	private double tpi;
	/**
	 * 前一状态的节气门数据
	 */
	private double preOil;
	/**
	 * 里程数
	 */
	private double Mileage;
	/**
	 * 车辆运行的时速
	 */
	private int speed;
	private List<Float> vertical = new ArrayList<Float>();
	private List<Float> vertical1 = new ArrayList<Float>();
	private CarInfo myCarInfo = new CarInfo();
	private List<CarInfo> otherCarInfos = new ArrayList<CarInfo>();
//	private Map<String, String> map = new HashMap<String, String>();
	/**
	 * 经济驾驶得分
	 */
	private int[] score = new int[3];
     public int getQuickBrakeTime() {
		return quickBrakeTime;
	}
    public void setQuickBrakeTime(int quickBrakeTime) {
		this.quickBrakeTime = quickBrakeTime;
	}
     public int getEmgBrakeTime() {
		return emgBrakeTime;
	}
     public void setEmgBrakeTime(int emgBrakeTime) {
		this.emgBrakeTime = emgBrakeTime;
	}
     public int getSlowTurn(){
    	 return slowTurn;
     }
     public void setSlowTurn(int slowTurn){
    	 this.slowTurn = slowTurn;
     }
     public int getNormTurn(){
    	 return normTurn;
     }
     public void setNormTurn(int normTurn){
    	 this.normTurn = normTurn;
     }
     public int getQuickTurn() {
		return quickTurn;
	}
      public void setQuickTurn(int quickTurn) {
		this.quickTurn = quickTurn;
	}
      public int getEmgTurn() {
		return emgTurn;
	}
      public void setEmgTurn(int emgTurn) {
		this.emgTurn = emgTurn;
	}
      public int getNoTurnOver() {
  		return NoTurnOver;
  	}
        public void setNoTurnOver(int noTurnOver) {
  		this.NoTurnOver= noTurnOver;
  	}
        public int getMidTurnOver() {
      		return MidTurnOver;
      	}
            public void setMidTurnOver(int MidTurnOver) {
      		this.MidTurnOver= MidTurnOver;
      	}
      public int getTurnOver() {
		return TurnOver;
	}
      public void setTurnOver(int turnOver) {
		this.TurnOver = turnOver;
	}
      public List<Float> getVertical() {
		return vertical;
	}
     public void setVertical(float vertical) {
    	 if (this.vertical.size()>600) {
			this.vertical.remove(0);
		}
		this.vertical.add(vertical);
	}
     public List<Float> getVertical1() {
 		return vertical1;
 	}
      public void setVertical1(float vertical) {
     	 if (this.vertical.size()>160) {
 			this.vertical.remove(0);
 		}
 		this.vertical.add(vertical);
 	}
     public CarInfo getMyCarInfo(){
    	 return myCarInfo;
     }
     public void setOtherCar(List<CarInfo> carInfos) {
		if(!carInfos.isEmpty())
			this.otherCarInfos = carInfos;
	}
     public void removeOtherCar(CarInfo car){
    	 if(!otherCarInfos.isEmpty()) 
    		 for(CarInfo carInfo : this.otherCarInfos) {
    			 if(car.CarPlateNumber.equals(carInfo.CarPlateNumber)) {
    		    	 this.otherCarInfos.remove(carInfo);//remove一个类，必须重写equals
    		    	 break;
    			 }
    		 }

     }

     public List<CarInfo> getOtherCarInfo(){
    		 return otherCarInfos;
     }
     public void setMyCarInfo(CarInfo myCarInfo) {
		this.myCarInfo = myCarInfo;
	}
     public double getTpi() {
		return tpi;
	}
    public void setTpi(double tpi) {
		this.tpi = tpi;
	}
    public double getMileage() {
		return Mileage;
	}
    public void setMileage(double mileage) {
		Mileage = mileage;
	}
    public void setSpeed(int speed) {
		this.speed = speed;
	}
   public int getSpeed() {
		return speed;
	}
   public double getPreOil() {
	return preOil;
   }
   public void setPreOil(double preOil) {
	this.preOil = preOil;
}
 public void setOil(double oil) {
	this.oil = oil;
}
public double getOil() {
	return oil;
}
 public void setScore(int[] score) {
	this.score = score;
}
public int[] getScore() {
	return score;
}
public void setQuickSpeedUpTime(int quickSpeedUpTime) {
	this.quickSpeedUpTime = quickSpeedUpTime;
}
public int getQuickSpeedUpTime() {
	return this.quickSpeedUpTime;
}
 public void setEmgSpeedUpTime(int emgSpeedUpTime) {
	this.emgSpeedUpTime = emgSpeedUpTime;
}
public int getEmgSpeedUpTime() {
	return this.emgSpeedUpTime;
}
    @Override  
    public void onCreate(){  
        super.onCreate();  
//        preferences = this.getSharedPreferences("userBehavior", Context.MODE_WORLD_READABLE); 
//        Editor editor = preferences.edit();  
//        editor.putInt("EmgBrakeTime", emgBrakeTime);  
//        editor.putInt("EmgTurn", emgTurn);  
//        editor.putInt("MidTurnOver", MidTurnOver);
//        editor.putInt("NormTurn", normTurn);  
//        editor.putInt("NoTurnOver", NoTurnOver);
//        editor.putInt("QuickBrakeTime", quickBrakeTime);  
//        editor.putInt("QuickTurn", quickTurn);  
//        editor.putInt("SlowTurn", slowTurn);  
//        editor.putInt("Speed", speed);  
////        editor.putString("Vertical1", vertical1.toString()) ;
//        editor.commit(); 
        setEmgBrakeTime(0);
        setEmgTurn(0);
        setMidTurnOver(0);
        setMileage(0);
        setNormTurn(0);
        setNoTurnOver(0);
        setQuickBrakeTime(0);
        setQuickTurn(0);
        setSlowTurn(0);
        setTpi(0);
        setTurnOver(0);
        setSpeed(0);
        setPreOil(0);
        setOil(0);
        setQuickSpeedUpTime(0);
        setEmgSpeedUpTime(0);
    }
	
}
