package cn.com.zbev.charger.netzbplus.utils;

import java.util.Calendar;

public final class Util {

	public static Calendar getCalendar(byte[] buff) {
		return getCalendar(buff,0);
	}

	/*
	 * 从6个字节提取y,m,d,h,m,s
	 */
	public static Calendar getCalendar(byte[] buff,int offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, buff[offset] + 2000);
		calendar.set(Calendar.MONTH, buff[offset+1] - 1);
		calendar.set(Calendar.DAY_OF_MONTH, buff[offset+2]);
		calendar.set(Calendar.HOUR_OF_DAY, buff[offset+3]);
		calendar.set(Calendar.MINUTE, buff[offset+4]);
		calendar.set(Calendar.SECOND, buff[offset+5]);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
}
