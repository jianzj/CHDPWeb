package com.chdp.chdpweb.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	
	public static String getMaxTime(){
		return "2099-12-31";
	}
	
	public static String getMinTime(){
		return "2001-01-01";
	}
	
	public static String getCurrentTime(){
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = new Date();
    	return sdf1.format(date);
	}
	
	public static String getOneMonthAgoTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MONTH, -1);
		return sdf.format(ca.getTime());
	}
	
	public static boolean validStartEndTime(String start, String end){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = sdf.parse(start);
			long startTime = startDate.getTime();
			Date endDate = sdf.parse(end);
			long endTime = endDate.getTime();
			if (endTime > startTime){
				return true;
			}
			return false;
		} catch (Exception e){
			return false;
		}
	}
}
