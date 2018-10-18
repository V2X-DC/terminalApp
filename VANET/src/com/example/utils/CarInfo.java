package com.example.utils;

/**
 * 车辆信息所包含的元素
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
///ArrayList要删除对象类的话，要重写equals方法
	public boolean equals(Object obj)//重写equals方法,原先的是判断是否同一对象，现在的是判断字符串
	{
	    if (!(obj instanceof CarInfo))//instanceof 运算符是用来在运行时指出对象是否是特定类的一个实例
	        return false;
	    CarInfo p = (CarInfo)obj;
	    return this.CarPlateNumber.equals(p.CarPlateNumber);
	    //this是调用者，表示后传入对象 调用equals()方法跟参数p.name进行比较
	    //name是String对象，调用String的equals()方法判断字符串内容是否相等，int 的==判断数字
	}
	
	public String TelNumber;
	public String CarPlateNumber;
	//刹车、侧翻、碰撞等级
	public String[] turnLevelStr = new String[] {"无明显转弯", "缓慢转弯","正常转弯", "快速转弯", "紧急转弯"};
	public String[] brakeLevelStr = new String[] {"无明显刹车", "缓慢刹车", "正常刹车", "快速刹车", "紧急刹车"};
	public String[] overLevelStr = new String[] {"无侧翻", "轻微侧翻风险", "中等侧翻风险", "高侧翻风险","已经侧翻"};
	public String[] collisionlevStr = new String[] {"无碰撞","轻微碰撞风险", "中级碰撞风险", "高碰撞风险"};
	public String[] speedUpStr = new String[] { "无加速","缓慢加速","正常加速","快速加速","紧急加速"};
	 /**
	  * 加速等级
	  */
	public String SpeedUpLevel;
	   
	/**
	 * 刹车等级
	 */
	public String BrakeLevel;
	/**
	 * 转弯等级
	 */
	public String TurnLevel;
	/**
	 * 侧翻等级
	 */
	public String OverLevel;
	/**
	 * 碰撞等级
	 */
	public String CollisionLevel;
		
	/**
	 * 是否已经定位
	 */
	public boolean isLocated;
	
	/**
	 *  纬度
	 */
	public double latitude;
	
	/**
	 *  南纬or北纬：true表示北纬，false表示南纬
	 */
	public boolean lat_which;
	
	/**
	 *  经度
	 */
	public double longtitude;
	
	/**
	 *  东经or西经：true表示东经，false表示西经
	 */
	public boolean long_which;
	
	/**
	 *  方向：0-359度
	 */
	public float direction;
	
	/**
	 * 车速
	 */
	public float speed;
	
	/**
	 * 是否超速行驶
	 */
	public boolean isOverSpeed;
	
	/**
	 * X轴加速度值
	 */
	public float acceleration_X;
	/**
	 * Y轴加速度值
	 */
	public float acceleration_Y;
	/**
	 * Z轴加速度值
	 */
	public float acceleration_Z;
	
	/**
	 * 是否刹车
	 */
	public boolean isBrake;
	/**
	 * 海拔高度
	 */
	public double highet;
	
	/**
	 * 是否左转弯
	 */
	public boolean isTurnLeft;
	
	/**
	 * 是否右转弯
	 */
	public boolean isTurnRight;
//	/**
//	 * 是否转弯
//	 */
//	public boolean isTurn;
	
	/**
	 * 是否发生碰撞
	 */
	public boolean isCollision;
	
	/**
	 * 是否发生侧翻
	 */
	public boolean isTurnOver;
	
	/**
	 * 是否报警
	 */
	public boolean isEmergencyAlarm;
	
	/**
	 * 是否疲劳驾驶
	 */
	public  boolean isFatigueDriving;
	/**
	 * 车辆所处的位置
	 */
	public  String address;
	/**
	 * 车辆左转弯的标志位
	 */
	public boolean TurnLflage;
	/**
	 * 刹车标志位
	 */
	public boolean Brakeflage;
	/**
	 * 车辆右转弯的标志位
	 */
	public boolean TurnRflage;
	/**
	 * 车辆侧翻的标志位
	 */
	public boolean Overflage;
	/**
	 * 车辆碰撞的标志位
	 */
	public boolean Collisionflage;
	/**
	 * 车辆加速的标志位
	 */
	public boolean SpeedUpflage;
	/**
	 * 获取当前时间
	 */
	public long Time;
	/**
	 * 根据定位产生的数据更新本车的信息
	 * @param lat		定位得到的维度
	 * @param lon		定位得到的经度
	 * @param sped	定位得到的速度
	 * @param direction 
	 */
	public void update(double lat, double lon, float sped, float direction) {
		  this.lat_which = ((this.latitude = lat) > 0) ? true : false; //纬度
		  this.long_which = ((this.longtitude = lon) > 0) ? true : false; //经度
		  this.speed = sped;
		  this.direction = direction;
		  this.isLocated = true;
		  return;
	}
	
}


