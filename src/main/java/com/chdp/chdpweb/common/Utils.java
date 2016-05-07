package com.chdp.chdpweb.common;

import java.text.SimpleDateFormat;

public class Utils {
		
	public static String formatStartTime(String startTime){
		try{
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			return sdf2.format(sdf1.parse(startTime));
		}catch (Exception e){
			return startTime;
		}
	}
	
	public static String formatEndTime(String endTime){
		try{
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			return sdf2.format(sdf1.parse(endTime));
		}catch (Exception e){
			return endTime;
		}
	}
	
}
