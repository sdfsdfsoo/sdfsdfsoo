package patentsearch.bean.util.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

 

public class DateUtil {
	/**
	 * 将日期字符串1998年06月04日，输出为Date类型
	 */
	public static Date stringToDate(String dateString) {
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
		"yyyy-MM-dd");
		if (dateString != null && !"".equals(dateString.trim())) {
			String year = dateString.substring(0, dateString.lastIndexOf("年"));
			String month = dateString.substring(
					dateString.lastIndexOf("年") + 1, dateString
							.lastIndexOf("月"));
			String day = dateString.substring(dateString.lastIndexOf("月") + 1,
					dateString.lastIndexOf("日"));
			String tempString = (year + "-" + month + "-" + day).trim();
			try {
				return simpleDateFormat.parse(tempString);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;

	}
	/**
	 * 将Date类型输出为：格式2011.07.01
	 */
	public static String dateToTextString(Date date) {
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy.MM.dd");
		if (date != null ) {
	 
				return simpleDateFormat. format(date);	 
		}
		return null;

	}
	/**
	 * 将Date类型输出为：格式2006年12月21日 14时40分59秒 星期四
	 */
	public static String dateToCnTextString(Date date) {
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日 HH时mm分ss秒 E");
		if (date != null ) {
	 
				return simpleDateFormat. format(date);	 
		}
		return null;

	}
	/**
	 * 将Date类型输出为：格式20100704
	 */
	public static String dateToValueString(Date date) {
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyyMMdd");
		if (date != null ) {
				return simpleDateFormat. format(date);	 
		}
		return null;

	}
	/**
	 * 格式20100704-->2011.07.01
	 */
	public static String formatStrToStr(String rawStr) {
		 if(rawStr!=null&&!"".equals(rawStr)&&rawStr.length()==8){
			return rawStr.substring(0, 4)+"."+ rawStr.substring(4, 6)+"."+rawStr.substring(6, 8);
		 }
		return rawStr;
	}
	/**
	 * 格式20100704
	 */
	public static Date formatStrToDate(String rawStr) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			return simpleDateFormat.parse(rawStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
