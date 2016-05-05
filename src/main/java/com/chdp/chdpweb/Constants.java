package com.chdp.chdpweb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    public static final int PAGE_SIZE = 3;
    public static final String DEFAULT_PASSWORD = "123456";

    // These params are for Machine
    public static final int DECOCTION_MACHINE = 1;
    public static final int FILLING_MACHINE = 2;

    // These params are for Herb
    public static final int DECOCT_FIRST = 1;
    public static final int DECOCT_LATER = 2;
    public static final int WRAPPED_DECOCT = 3;

    // These params are for Process
    public static final int RECEIVE = 1;
    public static final int CHECK = 2;
    public static final int MIX = 3;
    public static final int MIXCHECK = 4;
    public static final int SOAK = 5;
    public static final int DECOCT = 6;
    public static final int POUR = 7;
    public static final int CLEAN = 8;
    public static final int PACKAGE = 9;
    public static final int SHIP = 10;
    public static final int FINISH = 11;

    // These params are for Sex
    public static final int MAN = 1;
    public static final int WOMAN = 2;

    // 流转出错类型
    public static final int MANUAL_RETURN = 1;
    public static final int ERROR_RETURN = 2;
    
    // 煎药类型
    public static final int DECOCT_ONE = 1;
    public static final int DECOCT_TWO = 2;
    public static final int DECOCT_THREE = 3;
    
    public static String getProcessName(int process) {
        switch (process) {
        case RECEIVE:
            return "接方";
        case CHECK:
            return "审方";
        case MIX:
            return "调配";
        case MIXCHECK:
            return "调配审核";
        case SOAK:
            return "浸泡";
        case DECOCT:
            return "煎煮";
        case POUR:
            return "灌装";
        case CLEAN:
            return "清场";
        case PACKAGE:
            return "包装";
        case SHIP:
            return "运输";
        case FINISH:
            return "完成";
        default:
            return "未知";
        }
    }
    
    public static String getErrorName(int error_type){
    	switch(error_type) {
	    	case ERROR_RETURN:
	    		return "错误回退";
	    	case MANUAL_RETURN:
	    		return "手动回退";
	    	default:
	    		return "未知";
    	}
    }
    
    public static String getDecoctTime(String start, String end, int decoct_type){
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
    	
    	if (end != null && start != null){
    		try{
    			Date startDate = sdf1.parse(start);
    			Date endDate = sdf1.parse(end);
    			String dayStr = sdf2.format(endDate);
    			long time = endDate.getTime();
    			time = time - getHeatTime(decoct_type)*60*1000;
    			Date newDate = new Date(time);
    			String timeStr = dayStr + " " + sdf3.format(startDate) + " - " + sdf3.format(newDate);
    			return timeStr;
    		}catch (Exception e){
    			return "";
    		}
    	}
    	
    	return "";
    }
    
    public static String getHeatTime(String end, int decoct_type){
    	
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
    	
    	if (end != null){
    		try{
    			Date endDate = sdf1.parse(end);
    			String dayStr = sdf2.format(endDate);
    			long time = endDate.getTime();
    			time = time - getHeatTime(decoct_type)*60*1000;
    			Date newDate = new Date(time);
    			String timeStr = dayStr + " " + sdf3.format(newDate) + " - " + sdf3.format(endDate);
    			return timeStr;
    		}catch (Exception e){
    			return "";
    		}
    	}
    	
    	return "";
    }

    public static int getHeatTime(int decoct_type){
    	switch(decoct_type) {
	    	case DECOCT_ONE:
	    		return 20;
	    	case DECOCT_TWO:
	    		return 30;
	    	case DECOCT_THREE:
	    		return 40;
	    	default:
	    		return 30;
    	}
    }
    // 浸泡时间
    public static final long SOAK_TIME = 30 * 60 * 1000;
    
    public static final int ORDER_BEGIN = 1;
    public static final int ORDER_FINISH = 2;
    
    public static final String TEMPLATEPATH = "/Users/zhao_jian/git/CHDPWeb/src/main/webapp/template";
}
