package com.tickets.tickets.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	
	/**
	 * 将字符串日期转换为日期
	 * @param date  yyyy-mm-dd HH24:MI:SS
	 * @return
	 * @throws Exception
	 */
	public static Date getDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

}
