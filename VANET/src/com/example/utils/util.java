package com.example.utils;

/**
 * ����ʵ����
 * @author qyr
 *
 */
public class util {
	
	/**  
	    * byte������ȡint��ֵ��������������(��λ��ǰ����λ�ں�)��˳�򣬺ͺ�intToBytes��������ʹ�� 
	    *   
	    * @param src  
	    *            byte����  
	    * @param offset  
	    *            ������ĵ�offsetλ��ʼ  
	    * @return int��ֵ  
	    */    
	public static int bytesToInt(byte[] src, int offset) {  
	    int value;    
	    value = (int) ((src[offset] & 0xFF)   
	            | ((src[offset+1] & 0xFF)<<8)   
	            | ((src[offset+2] & 0xFF)<<16)   
	            | ((src[offset+3] & 0xFF)<<24));  
	    return value;  
	}  
	//�ֽ�תdouble
	/** 
	 * byte����תdouble 
	 *  ������Ҫһ����Ϊ8���ֽ�����
	 * @param Array 
	 * @param Pos Ҫ���㲿�ֵ���ʼλ��
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
	 * �ֽ�����תΪfloat
	 * ��������ĳ���Ϊ4
	 * @param data
	 * @param offset���������е�ƫ����
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
	    * ��int��ֵת��Ϊռ�ĸ��ֽڵ�byte���飬������������(��λ��ǰ����λ�ں�)��˳�� ��bytesToInt��������ʹ�� 
	    * @param value  
	    *            Ҫת����intֵ 
	    * @return byte���� 
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
	    * ��int��ֵת��Ϊռ�ĸ��ֽڵ�byte���飬������������(��λ��ǰ����λ�ں�)��˳��  ��bytesToInt2��������ʹ�� 
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
	 * ������Ϣ��ʾ�ı�������
	 * @param carInfo ������Ϣ
	 * @param n = 1ʱ��ʾ������n = 2 ʱ��ʾ��������
	 * @return
	 */
	public static String textString(CarInfo carInfo,int n){
		String s = null;
		if (n == 1) {
			s = "�������ƺ�Ϊ��" + carInfo.CarPlateNumber+", �г��ٶ�Ϊ��" + carInfo.speed 
					+ "km/h, \nX��ļ��ٶ�Ϊ��" + carInfo.acceleration_X
					+ "m/s^2,Y��ļ��ٶ�Ϊ��" + carInfo.acceleration_Y
					+ "m/s^2,Z��ļ��ٶ�Ϊ��" + carInfo.acceleration_Z
					+ "m/s^2, �г�����Ϊ��"+carInfo.direction+"��,���θ߶�Ϊ��" + carInfo.highet 
					+ carInfo.highet + ","+ carInfo.TurnLevel +"," 
					+  carInfo.BrakeLevel + "," + carInfo.OverLevel;
			
			if(carInfo.isCollision)
				s += "������ײ, ";
			else
				s += "δ������ײ, ";
			
			if(carInfo.isEmergencyAlarm)
				s += "��������, ";
			else
				s += "δ��������, ";
			
			if(carInfo.isFatigueDriving)
				s += "ƣ�ͼ�ʻ��";
			else
				s += "δƣ�ͼ�ʻ��";
			
			if(carInfo.isLocated) {
				s += "γ�ȣ�" +carInfo. latitude + "��, ���ȣ�" +carInfo. longtitude + "��";
			} else {
				s += "����δ��λ";
		}
		}
		if (n == 2) {
			s = "���ƺ�Ϊ��" + carInfo.CarPlateNumber+" ���г��ٶ�Ϊ��" + carInfo.speed + "km/h, X��ļ��ٶ�Ϊ��" + carInfo.acceleration_X
					+ "m/s^2,Y��ļ��ٶ�Ϊ��" + carInfo.acceleration_Y
					+ "m/s^2,Z��ļ��ٶ�Ϊ��" + carInfo.acceleration_Z
					+ "m/s^2, �г�����Ϊ��"+carInfo.direction+"��\n,���θ߶�Ϊ��" + carInfo.highet + ","
					+ carInfo.TurnLevel +"," +  carInfo.BrakeLevel + "," + carInfo.OverLevel;
			
			if(carInfo.isCollision)
				s += "������ײ, ";
			else
				s += "δ������ײ, ";
			
			if(carInfo.isEmergencyAlarm)
				s += "��������, ";
			else
				s += "δ��������, ";
			
			if(carInfo.isFatigueDriving)
				s += "ƣ�ͼ�ʻ\n";
			else
				s += "δƣ�ͼ�ʻ\n";
			
			if(carInfo.isLocated) {
				s += "γ�ȣ�" +carInfo. latitude + "��, ���ȣ�" +carInfo. longtitude + "��\n";
			} else {
				s += "����δ��λ\n";
		}
		}
		return s;
	}

	/**
	 * short����ת��Ϊ2�ֽڵ�byte����
	 * 
	 * @param s
	 *            short����
	 * @return byte����
	 */
	public static byte[] unsignedShortToByte2(int s) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (s >> 8 & 0xFF);
		targets[1] = (byte) (s & 0xFF);
		return targets;
	}

}
