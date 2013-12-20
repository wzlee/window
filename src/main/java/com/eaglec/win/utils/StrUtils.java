package com.eaglec.win.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class StrUtils {

	public StrUtils() {
	}

	// 检测字符串是否存在中文字符
	public static boolean isChineseChar(String s) {
		boolean flag = false;
		if (s == null) {
			return true;
		}
		byte abyte0[] = s.getBytes();
		for (int i = 0; i < abyte0.length; i++) {
			if (abyte0[i] >= 0) {
				continue;
			}
			flag = true;
			break;
		}
		return flag;
	}

	public static long toLong(Object obj) {
		long retLong = 0L;
		try {
			retLong = Long.valueOf(toStr(obj));
		} catch (Exception e) {
			System.out.println(e);
		}
		return retLong;
	}

	public static final String toStr(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	public static String arrStrToStr(String[] s) {
		String str = "";
		if (s == null) {
			return "";
		}
		for (int i = 0; i < s.length; i++) {
			if (i == 0) {
				str += s[i];
			} else {
				str += "," + s[i];
			}
		}
		return str;
	}

	// 把字符串转换为数字
	public static final int toInt(String s, int i) {
		s = toStr(s);
		if (s == null) {
			return i;
		}
		if (!strIsDigital(s) || s.length() < 1) {
			return i;
		}
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}

	// 字符串转数字。默认值为0
	public static final int toInt(String s) {
		return toInt(s, 0);
	}

	public static final int toInt(Object obj) {
		if (obj != null) {
			return toInt(obj.toString(), 0);
		} else {
			return 0;
		}
	}

	public static final Float toFloat(Object obj) {
		if (obj != null) {
			return new Float(obj.toString());
		} else {
			return 0.0f;
		}
	}

	// 检测字符串是否为数字串
	public static final boolean strIsDigital(String s) {
		boolean flag = true;
		if (s == null) {
			return false;
		}
		char ac[] = s.toCharArray();
		for (int i = 0; i < ac.length;) {
			if (!Character.isDigit(ac[i])) {
				flag = false;
			}
			break;
		}
		return flag;
	}

	/**
	 * 格式化string为Date
	 * 
	 * @param datestr
	 * @return date
	 */
	public static Date parseDate(String datestr) {
		if (null == datestr || "".equals(datestr)) {
			return null;
		}
		try {
			String fmtstr = null;
			if (datestr.indexOf(':') > 0) {
				fmtstr = "yyyy-MM-dd HH:mm:ss";
			} else {

				fmtstr = "yyyy-MM-dd";
			}
			SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);
			return sdf.parse(datestr);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断此字符串是否为空、空字符串，或"null"
	 * 
	 * @author haley
	 * @param radioValue
	 *            -- 传入的值
	 * @param val
	 *            -- 待比较的值
	 * @return 是否选中
	 */
	public static boolean isNull(String s) {
		return (s == null || s.equals("null") || s.equals("")) ? true : false;
	}

	/**
	 * 按格式返回当前日期时间
	 * 
	 * @author
	 * @param yyyy-MM-dd HH:ss:mm
	 * @return
	 */
	public static String getNow(String dateStyle) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(dateStyle);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return bartDateFormat.format(cal.getTime());
	}

	/**
	 * ISO-8859-1转 utf-8
	 * 
	 * @author
	 * @param str
	 * @return
	 */
	public static String FormatIsoToUTF(String s) {
		s = toStr(s);
		try {
			s = new String(s.getBytes("ISO-8859-1"), "utf-8");
		} catch (Exception e) {

		}
		return s;
	}

	/**
	 * @date：2013-5-24
	 * @author：lwch
	 * @description：将list数据转换为String
	 */
	public static String listToStr(List<Object> list) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		return sb.toString();
	}

	/**
	 * @date: 2013-6-14
	 * @author：lwch
	 * @description：将html标签中的双引号替换为单引号
	 */
	public static String htmlShift(String html) {
		html = html.replaceAll("\"", "&quot;");
		return html;
	}

	/**
	 * @author XiaDi
	 * @since 2013-7-18
	 * @param content
	 * @return String 去除所有HTML"<>"的标签
	 */
	public static String replaceHTML(String content) {
		return content.replaceAll("<[^>]+>", "");
	}
	
	/**
	 * @date: 2013-9-7
	 * @author：lwch
	 * @description：去除所有HTML"<>"的标签，并且只截取"end"个字符
	 */
	public static String replaceHTML(String content, int start, int end) {
		content = content.replaceAll("<[^>]+>", "").replaceAll("&nbsp;", "");
		if (content.length() < end) {
			return content;
		}
		return content = content.substring(start, end);
	}

	/**
	 * 格式化日期工具
	 * 
	 * @author liuliping
	 * @since 2013-07-18
	 *
	 * @param pattern 日期格式，例如"yyyy-MM-dd HH:mm:ss"
	 * @param date 日期对象
	 * @return 格式化后日期字符串对象"2013-07-18 12:00:00"
	 */
	public final static String formateDate(String pattern, Date date) {
		DateFormat df = new SimpleDateFormat(pattern);
		String result = df.format(date);
		df = null;
		return result;
	}
	
    /** 
     * 将url参数转换成map 
     * @param param aa=11&bb=22&cc=33 
     * @return 
     */  
    public static Map<String, Object> getUrlParams(String param) {  
        Map<String, Object> map = new HashMap<String, Object>(0);  
        if (StringUtils.isBlank(param)) {  
            return map;  
        }  
        String[] params = param.split("&");  
        for (int i = 0; i < params.length; i++) {  
            String[] p = params[i].split("=");  
            if (p.length == 2) {  
                map.put(p[0], p[1]);  
            }  
        }  
        return map;  
    }  
  
    /** 
     * 将map转换成url 
     * @param map 
     * @return 
     */  
    public static String getUrlParamsByMap(Map<String, Object> map) {  
        if (map == null) {  
            return "";  
        }  
        StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, Object> entry : map.entrySet()) {  
            sb.append(entry.getKey() + "=" + entry.getValue());  
            sb.append("&");  
        }  
        String s = sb.toString();  
        if (s.endsWith("&")) {  
            s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");  
        }  
        return s;  
    }  
}
