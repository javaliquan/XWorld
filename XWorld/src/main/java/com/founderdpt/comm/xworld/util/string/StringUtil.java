/**
 * String 共通方法定义.
 *
 * @project pod
 *
 * @author 
 *
 * @date Sep 01, 2010
 *
 * @version 1.0
 *
 * copyright (c) Founder
 */
package com.founderdpt.comm.xworld.util.string;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * the StringUtil class
 * 
 * @author Administrator
 * 
 */
public class StringUtil {
	/**
	 * 空字符串
	 */
	private static final String EMPTY_STR = "";
	/**
	 * 分隔符
	 */
	private static final String SEP = ",";
	
	/**
	 * 将为NULL的String设置为空串。
	 * 
	 * @param str
	 *            字符串
	 * @return String
	 */	
	public static String trimNull(String str) {
		return str == null ? EMPTY_STR : str;
	}
	
	/**
	 * 将Object(MAP)中所有字符串类型字段为NULL的设置为空串。
	 * 
	 * @param o
	 *            Object（Map）
	 */
	public static void removeNullString(Object o) {
		Class oclass = o.getClass();
		Method[] methods = oclass.getMethods();
		Map<String, Method> map = new HashMap<String, Method>();

		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodName = method.getName();
			if (methodName.substring(0, 3).compareToIgnoreCase("set") == 0) {
				Class p[] = method.getParameterTypes();
				if (p.length == 1 && p[0].equals(String.class)) {
					map.put(methodName.substring(3), method);
				}
			}
		}
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			String methodName = method.getName();
			if (methodName.substring(0, 3).compareToIgnoreCase("get") == 0) {
				Class p[] = method.getParameterTypes();
				if (p.length == 0) {
					try {
						if (method.invoke(o) == null) {
							Method setMethod = map.get(methodName.substring(3));
							if (setMethod != null) {
								setMethod.invoke(o, "");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 判断字符串是否为NULL和空串。
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return !notEmpty(str);
	}
	
	/**
	 * 判断字符串是否不为NULL和空串。
	 * 
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	public static boolean notEmpty(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 把一个list里面的所有str元素用提供的分隔符参数分开。
	 * <p>
	 * list为空，则返回一个空串。
	 * 
	 * @param list
	 *            是Map的集合
	 * @param sep
	 *            length必须等于1
	 * @param key
	 *            是HMap中的id的key
	 * @return String
	 */
	public static String listToString(List list, String sep, String key) {
		StringBuffer sb = new StringBuffer();
		Object obj;
		Class clz;

		if(sep == null || sep.length()!= 1) {
			sep = SEP;
		}
		if(key == null || key.trim().length() < 0) {
			clz = String.class;
		} else {
			clz = Map.class;
		}
		for (Iterator it = list.iterator(); it.hasNext();) {
			if(clz.equals(String.class)){
				obj =  it.next();
			}else{
				obj = ((Map) it.next()).get(key);
			}
			sb.append(NVL(obj)).append(sep);
		}

		if (sb.length() > 0) {
			sb = sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();

	}
	
	/**
	 * 把一个list里面的所有str元素用提供的分隔符参数分开。
	 * <p>
	 * list为空，则返回一个空串。
	 * 
	 * @param list
	 *            list
	 * @param sep
	 *            length必须等于1。
	 * @return String
	 */
	public static String listToString(List list, String sep) {
		return listToString(list,sep,null);

	}

	/**
	 * 把一个list里面的所有str元素用SEP分隔符分开。
	 * <p>
	 * list为空，则返回一个空串。
	 * 
	 * @param list
	 *            list
	 * @return String
	 */
	public static String listToString(List list) {
		return listToString(list,SEP,null);
	}
	
	/**
	 * 在str中去除list中的所有id.
	 * <p>
	 * 
	 * @param str
	 *            是用逗号分隔的ids。比如：1,2,3,4
	 * @param list
	 *            是str的集合。
	 * @return String
	 */
	public static String stringExcepList(String str, List list) {
		str = SEP + str + SEP;
		String s;

		for (Iterator it = list.iterator(); it.hasNext();) {
			s = (String) it.next();
			s = SEP + s + SEP;
			str = str.replace(s, SEP);
		}

		if (str.length() > 2) {
			str = str.substring(1, str.length() - 1);
		} else {
			str = "";
		}

		return str;
	}

	/**
	 * 在str中去除list中的所有id.
	 * <p>
	 * 
	 * @param str
	 *            是用逗号分隔的ids。比如：1,2,3,4
	 * @param list
	 *            是Map的集合。
	 * @param key
	 *            是HMap中的id的key
	 * @return String
	 */
	public static String stringExcepList(String str, List list, String key) {
		str = SEP + str + SEP;
		Map hs;
		String s;

		for (Iterator it = list.iterator(); it.hasNext();) {
			hs = (Map) it.next();
			s = (String) hs.get(key);
			s = SEP + s + SEP;
			str = str.replace(s, SEP);
		}

		if (str.length() > 2) {
			str = str.substring(1, str.length() - 1);
		} else {
			str = "";
		}

		return str;
	}
	
	/**
	 * 將用逗號分割的ids中每個id用引號括起來。
	 * 
	 * @param ids
	 *            ids
	 * @return String
	 */
	public static String makeOracleNumberIdsToString(String ids) {
		String ret = null;
		
		if (ids == null || ids.length() <= 0) {
			return ret;
		}
		
		ids = SEP + ids + SEP;
		ids = ids.replaceAll(",", "','");
		ret = ids.substring(2, ids.length() - 2);

		return ret;
	}
	
	/**
	 * 在已知ids后面增加id
	 * 
	 * @param ids
	 *            ids
	 * @param id
	 *            id
	 * @return String
	 */
	public static String appendToIds(String ids, String id) {
		if (ids == null || ids.length() <= 0) {
			ids = id;
		} else {
			ids = ids + SEP + id;
		}

		return ids;
	}

	/**
	 * fit dont asistant this character"〜" +301C so change 〜 +301c to ～ +ff5e
	 * 
	 * @param str
	 *            str
	 * @return String
	 */
	public static String treatSpecialChar(String str) {
		if (str.indexOf(0x301C) >= 0) {
			str = str.replace((char) 0x301C, (char) 0xff5e);
		}
		return str;
	}

	/**
	 * "A：xxx" ->"xxx"
	 * 
	 * @param area
	 *            area
	 * @return String
	 */
	public static String formatAreaStr(String area) {
		if (area == null || area.equals("")) {
			return area;
		}
		if (area.indexOf("：") < 0) {
			return area;
		}
		area = area.substring(area.indexOf("：") + 1);
		return area;
	}

	/**
	 * NVL
	 * 
	 * @param Object
	 *            str
	 * @return String
	 */
	public static String NVL(Object str) {
		if (str == null) {
			return "";
		}
		return NVL(str.toString());
	}

	/**
	 * NVL
	 * 
	 * @param str
	 *            str
	 * @return String
	 */
	public static String NVL(String str) {
		if (str == null) {
			return "";
		}
		if (str.equals("null")) {
			return "";
		}
		if (str.equals("null|null")) {
			return "";
		}
		return str;
	}
	
	/**
	 * 过滤HTML 及 脚本
	 * @param s
	 * @return
	 */
	public static String filterString(String s){
		s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
		return s;
	}
	
	public static boolean isTrue(String str) {
		try {
			return Boolean.parseBoolean(str);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Long toLong(String str) {
		if (isEmpty(str)) return null;
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Long toLong(String str, Long defaultValue) {
		Long value = toLong(str);
		return value == null ? defaultValue : value;
	}
	
	public static String fetchRandomString() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssms");
		return sdf.format(new Date()) + new Random().nextInt(1000);
	}
	
	
	/**
	 * 处理XML特殊字符
	 * @param text
	 * @return
	 */
	public static String transXMLText(String text){
		if(text!=null){
			return text.replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("&","&amp;").replaceAll("'","&apos;").replaceAll("\"", "&quot;").trim();
		}
		return "";
		
	}
	
	public static String transTextXML(String text){
		if(text!=null){
			return text.replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&amp;","&").replaceAll("&apos;","'").replaceAll("&quot;", "\"");
		}
		return "";
	}
	
}
