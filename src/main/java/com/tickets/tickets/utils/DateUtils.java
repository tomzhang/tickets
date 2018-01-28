package com.tickets.tickets.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	
	/**
	 * 将字符串日期转换为日期
	 * @param date  yyyy-mm-dd HH24:MI:SS
	 * @return
	 * @throws Exception
	 */
	public static Date getDate(String date) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd 00:00:00");
		return sdf.parse(date);
	}

}
