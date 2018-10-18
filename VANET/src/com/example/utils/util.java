package com.example.utils;

/**
 * 基本实用类
 * @author qyr
 *
 */
public class util {
	
	/**  
	    * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用 
	    *   
	    * @param src  
	    *            byte数组  
	    * @param offset  
	    *            从数组的第offset位开始  
	    * @return int数值  
	    */    
	public static int bytesToInt(byte[] src, int offset) {  
	    int value;    
	    value = (int) ((src[offset] & 0xFF)   
	            | ((src[offset+1] & 0xFF)<<8)   
	            | ((src[offset+2] & 0xFF)<<16)   
	            | ((src[offset+3] & 0xFF)<<24));  
	    return value;  
	}  
	//字节转double
	/** 
	 * byte数组转double 
	 *  整个需要一个长为8的字节数组
	 * @param Array 
	 * @param Pos 要计算部分的起始位置
	 * @return 
	 */  
	public static double byteArrayToDouble(byte[] Array, int Pos) {  
	    long accum = 0;  
	    accum = Array[Pos + 0] & 0xFF;  
	    accum |= (long) (Array[Pos + 1] & 0xFF) << 8;  
	    accum |= (long) (Array[Pos + 2] & 0xFF) << 16;  
	    accum |= (long) (Array[Pos + 3] & 0xFF) << 24;  
	    accum |= (long) (Array[Pos + 4] & 0xFF) << 32;  
	    accum |= (long) (Array[Pos + 5] & 0xFF) << 40;  
	    accum |= (long) (Array[Pos + 6] & 0xFF) << 48;  
	    accum |= (long) (Array[Pos + 7] & 0xFF) << 56;  
	    return Double.longBitsToDouble(accum);  
	}  
	/**
	 * 字节数组转为float
	 * 所需数组的长度为4
	 * @param data
	 * @param offset，在数组中的偏置量
	 * @return
	 */
	public static float byte2float(byte[] data,int offset) { 
		int accum = 0; 
		accum = accum|(data[offset] & 0xff) << 0;
		accum = accum|(data[offset+1] & 0xff) << 8; 
		accum = accum|(data[offset+2] & 0xff) << 16; 
		accum = accum|(data[offset+3] & 0xff) << 24; 
		System.out.println(accum);
		return Float.intBitsToFloat(accum); 
	    }
	/**  
	    * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用 
	    * @param value  
	    *            要转换的int值 
	    * @return byte数组 
	    */    
	public static byte[] intToBytes( int value )   
	{   
	    byte[] src = new byte[4];  
	    src[3] =  (byte) ((value>>24) & 0xFF);  
	    src[2] =  (byte) ((value>>16) & 0xFF);  
	    src[1] =  (byte) ((value>>8) & 0xFF);    
	    src[0] =  (byte) (value & 0xFF);                  
	    return src;   
	}  
	 /**  
	    * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用 
	    */    
	public static byte[] intToBytes2(int value)   
	{   
	    byte[] src = new byte[4];  
	    src[0] = (byte) ((value>>24) & 0xFF);  
	    src[1] = (byte) ((value>>16)& 0xFF);  
	    src[2] = (byte) ((value>>8)&0xFF);   
	    src[3] = (byte) (value & 0xFF);       
	    return src;  
	}  
	/**
	 * 车辆信息显示文本的设置
	 * @param carInfo 车辆信息
	 * @param n = 1时表示本车，n = 2 时表示其他车辆
	 * @return
	 */
	public static String textString(CarInfo carInfo,int n){
		String s = null;
		if (n == 1) {
			s = "本车车牌号为：" + carInfo.CarPlateNumber+", 行车速度为：" + carInfo.speed 
					+ "km/h, \nX轴的加速度为：" + carInfo.acceleration_X
					+ "m/s^2,Y轴的加速度为：" + carInfo.acceleration_Y
					+ "m/s^2,Z轴的加速度为：" + carInfo.acceleration_Z
					+ "m/s^2, 行车方向为："+carInfo.direction+"度,海拔高度为：" + carInfo.highet 
					+ carInfo.highet + ","+ carInfo.TurnLevel +"," 
					+  carInfo.BrakeLevel + "," + carInfo.OverLevel;
			
			if(carInfo.isCollision)
				s += "发生碰撞, ";
			else
				s += "未发生碰撞, ";
			
			if(carInfo.isEmergencyAlarm)
				s += "发生报警, ";
			else
				s += "未发生报警, ";
			
			if(carInfo.isFatigueDriving)
				s += "疲劳驾驶。";
			else
				s += "未疲劳驾驶。";
			
			if(carInfo.isLocated) {
				s += "纬度：" +carInfo. latitude + "°, 经度：" +carInfo. longtitude + "°";
			} else {
				s += "车辆未定位";
		}
		}
		if (n == 2) {
			s = "车牌号为：" + carInfo.CarPlateNumber+" 的行车速度为：" + carInfo.speed + "km/h, X轴的加速度为：" + carInfo.acceleration_X
					+ "m/s^2,Y轴的加速度为：" + carInfo.acceleration_Y
					+ "m/s^2,Z轴的加速度为：" + carInfo.acceleration_Z
					+ "m/s^2, 行车方向为："+carInfo.direction+"度\n,海拔高度为：" + carInfo.highet + ","
					+ carInfo.TurnLevel +"," +  carInfo.BrakeLevel + "," + carInfo.OverLevel;
			
			if(carInfo.isCollision)
				s += "发生碰撞, ";
			else
				s += "未发生碰撞, ";
			
			if(carInfo.isEmergencyAlarm)
				s += "发生报警, ";
			else
				s += "未发生报警, ";
			
			if(carInfo.isFatigueDriving)
				s += "疲劳驾驶\n";
			else
				s += "未疲劳驾驶\n";
			
			if(carInfo.isLocated) {
				s += "纬度：" +carInfo. latitude + "°, 经度：" +carInfo. longtitude + "°\n";
			} else {
				s += "车辆未定位\n";
		}
		}
		return s;
	}

	/**
	 * short整数转换为2字节的byte数组
	 * 
	 * @param s
	 *            short整数
	 * @return byte数组
	 */
	public static byte[] unsignedShortToByte2(int s) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (s >> 8 & 0xFF);
		targets[1] = (byte) (s & 0xFF);
		return targets;
	}

}
