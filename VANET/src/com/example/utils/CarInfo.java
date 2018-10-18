package com.example.utils;

/**
 * ������Ϣ��������Ԫ��
 * @author qyr
 *
 */
public class CarInfo{
	
public CarInfo() {
		this.CarPlateNumber = "yue";
		this.BrakeLevel = brakeLevelStr[0];
		this.TurnLevel = turnLevelStr[0];
		this.OverLevel = overLevelStr[0];
		this.CollisionLevel = collisionlevStr[0];
		this.SpeedUpLevel = speedUpStr[0];
	    this.isLocated = true;
	    this.latitude = 23.155;
	    this.lat_which = true;
	    this.longtitude = 113.264 ;
	    this.long_which = true;
		this.isCollision = false;
		this.isEmergencyAlarm = false;
		this.isFatigueDriving = false;
		this.isLocated = false;
		this.isOverSpeed = false;
		this.isTurnLeft = false;
		this.isTurnRight = false;
//		this.isTurn = false;
		this.isTurnOver = false;
		
		this.direction = 0;
		this.speed = 0;
		this.acceleration_X = 0;
		this.acceleration_Y = 0;
		this.acceleration_Z = 0;
		this.highet = 0;
		this.address = " ";
		//biaozhiwei
	
		this.TurnLflage = false;
		this.TurnRflage = false;
		this.Overflage = false;
		this.Collisionflage = false;
		this.Brakeflage = false;
		this.SpeedUpflage = false;
		this.Time = 0;

	}
///ArrayListҪɾ��������Ļ���Ҫ��дequals����
	public boolean equals(Object obj)//��дequals����,ԭ�ȵ����ж��Ƿ�ͬһ�������ڵ����ж��ַ���
	{
	    if (!(obj instanceof CarInfo))//instanceof �����������������ʱָ�������Ƿ����ض����һ��ʵ��
	        return false;
	    CarInfo p = (CarInfo)obj;
	    return this.CarPlateNumber.equals(p.CarPlateNumber);
	    //this�ǵ����ߣ���ʾ������� ����equals()����������p.name���бȽ�
	    //name��String���󣬵���String��equals()�����ж��ַ��������Ƿ���ȣ�int ��==�ж�����
	}
	
	public String TelNumber;
	public String CarPlateNumber;
	//ɲ�����෭����ײ�ȼ�
	public String[] turnLevelStr = new String[] {"������ת��", "����ת��","����ת��", "����ת��", "����ת��"};
	public String[] brakeLevelStr = new String[] {"������ɲ��", "����ɲ��", "����ɲ��", "����ɲ��", "����ɲ��"};
	public String[] overLevelStr = new String[] {"�޲෭", "��΢�෭����", "�еȲ෭����", "�߲෭����","�Ѿ��෭"};
	public String[] collisionlevStr = new String[] {"����ײ","��΢��ײ����", "�м���ײ����", "����ײ����"};
	public String[] speedUpStr = new String[] { "�޼���","��������","��������","���ټ���","��������"};
	 /**
	  * ���ٵȼ�
	  */
	public String SpeedUpLevel;
	   
	/**
	 * ɲ���ȼ�
	 */
	public String BrakeLevel;
	/**
	 * ת��ȼ�
	 */
	public String TurnLevel;
	/**
	 * �෭�ȼ�
	 */
	public String OverLevel;
	/**
	 * ��ײ�ȼ�
	 */
	public String CollisionLevel;
		
	/**
	 * �Ƿ��Ѿ���λ
	 */
	public boolean isLocated;
	
	/**
	 *  γ��
	 */
	public double latitude;
	
	/**
	 *  ��γor��γ��true��ʾ��γ��false��ʾ��γ
	 */
	public boolean lat_which;
	
	/**
	 *  ����
	 */
	public double longtitude;
	
	/**
	 *  ����or������true��ʾ������false��ʾ����
	 */
	public boolean long_which;
	
	/**
	 *  ����0-359��
	 */
	public float direction;
	
	/**
	 * ����
	 */
	public float speed;
	
	/**
	 * �Ƿ�����ʻ
	 */
	public boolean isOverSpeed;
	
	/**
	 * X����ٶ�ֵ
	 */
	public float acceleration_X;
	/**
	 * Y����ٶ�ֵ
	 */
	public float acceleration_Y;
	/**
	 * Z����ٶ�ֵ
	 */
	public float acceleration_Z;
	
	/**
	 * �Ƿ�ɲ��
	 */
	public boolean isBrake;
	/**
	 * ���θ߶�
	 */
	public double highet;
	
	/**
	 * �Ƿ���ת��
	 */
	public boolean isTurnLeft;
	
	/**
	 * �Ƿ���ת��
	 */
	public boolean isTurnRight;
//	/**
//	 * �Ƿ�ת��
//	 */
//	public boolean isTurn;
	
	/**
	 * �Ƿ�����ײ
	 */
	public boolean isCollision;
	
	/**
	 * �Ƿ����෭
	 */
	public boolean isTurnOver;
	
	/**
	 * �Ƿ񱨾�
	 */
	public boolean isEmergencyAlarm;
	
	/**
	 * �Ƿ�ƣ�ͼ�ʻ
	 */
	public  boolean isFatigueDriving;
	/**
	 * ����������λ��
	 */
	public  String address;
	/**
	 * ������ת��ı�־λ
	 */
	public boolean TurnLflage;
	/**
	 * ɲ����־λ
	 */
	public boolean Brakeflage;
	/**
	 * ������ת��ı�־λ
	 */
	public boolean TurnRflage;
	/**
	 * �����෭�ı�־λ
	 */
	public boolean Overflage;
	/**
	 * ������ײ�ı�־λ
	 */
	public boolean Collisionflage;
	/**
	 * �������ٵı�־λ
	 */
	public boolean SpeedUpflage;
	/**
	 * ��ȡ��ǰʱ��
	 */
	public long Time;
	/**
	 * ���ݶ�λ���������ݸ��±�������Ϣ
	 * @param lat		��λ�õ���ά��
	 * @param lon		��λ�õ��ľ���
	 * @param sped	��λ�õ����ٶ�
	 * @param direction 
	 */
	public void update(double lat, double lon, float sped, float direction) {
		  this.lat_which = ((this.latitude = lat) > 0) ? true : false; //γ��
		  this.long_which = ((this.longtitude = lon) > 0) ? true : false; //����
		  this.speed = sped;
		  this.direction = direction;
		  this.isLocated = true;
		  return;
	}
	
}


