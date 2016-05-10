package com.chdp.chdpweb.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.User;

public class Utils {

	public static String formatStartTime(String startTime) {
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			return sdf2.format(sdf1.parse(startTime));
		} catch (Exception e) {
			return startTime;
		}
	}

	public static String formatEndTime(String endTime) {
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			return sdf2.format(sdf1.parse(endTime));
		} catch (Exception e) {
			return endTime;
		}
	}

	public static String getMaxTime() {
		return "2099-12-31";
	}

	public static String getMinTime() {
		return "2001-01-01";
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return sdf1.format(date);
	}

	public static String getCurrentDateAndTime() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf1.format(date);
	}

	public static String getOneMonthAgoTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MONTH, -1);
		return sdf.format(ca.getTime());
	}

	public static boolean validStartEndTime(String start, String end) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = sdf.parse(start);
			long startTime = startDate.getTime();
			Date endDate = sdf.parse(end);
			long endTime = endDate.getTime();
			if (endTime > startTime) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static int getProcessTypebyUserAuth(int userAuth) {
		if ((userAuth & 512) != 0) {
			return Constants.RECEIVE;
		} else if ((userAuth & 256) != 0) {
			return Constants.CHECK;
		} else if ((userAuth & 128) != 0) {
			return Constants.MIX;
		} else if ((userAuth & 64) != 0) {
			return Constants.MIXCHECK;
		} else if ((userAuth & 32) != 0) {
			return Constants.SOAK;
		} else if ((userAuth & 16) != 0) {
			return Constants.DECOCT;
		} else if ((userAuth & 8) != 0) {
			return Constants.POUR;
		} else if ((userAuth & 4) != 0) {
			return Constants.CLEAN;
		} else if ((userAuth & 2) != 0) {
			return Constants.PACKAGE;
		} else if ((userAuth & 1) != 0) {
			return Constants.SHIP;
		}
		return 0;
	}

	public static String getPositionwithAuthority(User user) {
		try {
			if ((user.getAuthority() & 512) != 0) {
				return "接方";
			} else if ((user.getAuthority() & 256) != 0) {
				return "审方";
			} else if ((user.getAuthority() & 128) != 0) {
				return "调配";
			} else if ((user.getAuthority() & 64) != 0) {
				return "调配检查";
			} else if ((user.getAuthority() & 32) != 0) {
				return "浸泡";
			} else if ((user.getAuthority() & 16) != 0) {
				return "煎煮";
			} else if ((user.getAuthority() & 8) != 0) {
				return "灌装";
			} else if ((user.getAuthority() & 4) != 0) {
				return "清场";
			} else if ((user.getAuthority() & 2) != 0) {
				return "包装";
			} else if ((user.getAuthority() & 1) != 0) {
				return "配送";
			} else {
				return "未知！";
			}
		} catch (Exception e) {
			return "未知";
		}
	}

	// Need to change
	public static List<Integer> mergeTwoPrsIdList(List<Integer> prs1, List<Integer> prs2) {
		Iterator<Integer> itr = prs2.iterator();
		Integer temp = 0;
		while (itr.hasNext()) {
			temp = itr.next();
			if (!prs1.contains(temp)) {
				prs1.add(temp);
			}
		}
		return prs1;
	}

	public static String getDecoctTime(String start, String end, int decoct_type) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");

		if (end != null && start != null) {
			try {
				Date startDate = sdf1.parse(start);
				Date endDate = sdf1.parse(end);
				String dayStr = sdf2.format(endDate);
				long time = endDate.getTime();
				time = time - Constants.getHeatTime(decoct_type) * 60 * 1000;
				Date newDate = new Date(time);
				String timeStr = dayStr + " " + sdf3.format(startDate) + " - " + sdf3.format(newDate);
				return timeStr;
			} catch (Exception e) {
				return "";
			}
		}

		return "";
	}

	public static String getHeatTime(String end, int decoct_type) {

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");

		if (end != null) {
			try {
				Date endDate = sdf1.parse(end);
				String dayStr = sdf2.format(endDate);
				long time = endDate.getTime();
				time = time - Constants.getHeatTime(decoct_type) * 60 * 1000;
				Date newDate = new Date(time);
				String timeStr = dayStr + " " + sdf3.format(newDate) + " - " + sdf3.format(endDate);
				return timeStr;
			} catch (Exception e) {
				return "";
			}
		}

		return "";
	}

	public static String generateUuid() {
		int randomNum = (int) (Math.random() * 900) + 100;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = df.format(new Date());
		return currentTime + String.valueOf(randomNum);
	}
}
