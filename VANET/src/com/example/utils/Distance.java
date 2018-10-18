package com.example.utils;


public class Distance {
	
	private double earth_radius = 6378137.0;
	public double dis;
	public double calculate(CarInfo Cars,CarInfo OtherCars){
		double radLat1 = (Cars.latitude * Math.PI / 180.0);
		double radLat2 = (OtherCars.latitude * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (Cars.longtitude- OtherCars.longtitude) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s *  earth_radius ;
		dis = Math.round(s * 10000) / 10000;
		return dis;
	}
}

