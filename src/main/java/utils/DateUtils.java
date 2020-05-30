package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static String getTimeStamp() {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		return timeStamp;
	}

	public static String getTimeStamp(String sFormat) {
		String timeStamp = new SimpleDateFormat(sFormat).format(new Date());
		return timeStamp;
	}
	
	public static String getDateBeforeXDays(String sFormat, int iDays) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -iDays);
		SimpleDateFormat s = new SimpleDateFormat(sFormat);
		String sDateBeforeNDays = s.format(new Date(cal.getTimeInMillis()));
		System.out.println("Date before " + iDays + " days :" + sDateBeforeNDays);
		return sDateBeforeNDays;
	}

	public static boolean isSecondDateBeforeFirstDate(String sFormat, String sFirstDate, String sSecondDate) {
		boolean bResult;
		SimpleDateFormat dt = new SimpleDateFormat(sFormat);
		Date firstDate = null;
		Date secondDate = null;
		try {
			firstDate = dt.parse(sFirstDate);
			secondDate = dt.parse(sSecondDate);

		} catch (ParseException e) {
			System.out.println("Exception occured while parsing date");
			return false;
		}

		if (firstDate.after(secondDate) || firstDate.equals(secondDate)) {
			System.out.print(true + " : " + sSecondDate);
			bResult = true;
		} else {
			System.out.println(false + " : " + sSecondDate);
			bResult = false;
		}
		return bResult;
	}
}
