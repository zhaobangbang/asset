package com.lans.infrastructure.util;

import java.util.Calendar;

public class GPSTimeConverter {

	public GPSTimeConverter() {
		// TODO Auto-generated constructor stub
	}
	
	//GPS Time = UNIX Time - 315964800 - 19 + (32 + (current_year - 2000) * 2/9)
	public static int fromUTCTimeS(long uts) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		
		long lGps = uts - 315964800 - 19 + (32 + ((year - 2000)*2/9));
		return (int)lGps;
	}
	
	public static long toUTCTimeS(int gps) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		
		long uts = gps + 315964800 + 19 - (32 + ((year - 2000)*2/9));
		return uts;
	}
	
	public static int fromUTCTimeMS(long utcms) {
		return fromUTCTimeS(utcms/1000);
	}
	
	public static long toUTCTimeMS(int gps) {
		return toUTCTimeS(gps)*1000;
	}

}
