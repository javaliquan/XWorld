package com.founderdpt.comm.xworld.util;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

public class POJOUtil {
	 /**
	  * 根据对象生成map
	  * @param obj  需要转换的javabean
	  * @param root 根
	  * @return
	  */
	public static Map<String,String> object2MapUtil(Object obj,String root) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		java.beans.BeanInfo info = java.beans.Introspector.getBeanInfo(obj
				.getClass());
		if (root == null || "".equals(root.trim())) {
			root = "";
		} else {
			root += ".";
			root = root.trim();
		}
		java.beans.PropertyDescriptor pd[] = info.getPropertyDescriptors();
		for (PropertyDescriptor p : pd) {
			String fieldName = p.getName();
			if (fieldName != null && !fieldName.equals("class")) {
				java.lang.reflect.Method method = p.getReadMethod();
				Object object = method.invoke(obj);
				if (object != null) {
					if (object instanceof String) {
						String objects = ((String) object).trim();
						if (!objects.equals("")) {
							map.put(root + fieldName, objects.toString());
						}
					}
					if (object instanceof Number) {
						 map.put(root + fieldName, object.toString());
					}
					// 处理嵌套类
					if (object.getClass().getPackage() == obj.getClass()
							.getPackage()) {
						map.putAll(object2MapUtil(object, root + fieldName));
					}

				}
			}
		}
		return map;
	}
	public static Map<String,String> object2MapUtil(Object obj) throws Exception{
		return object2MapUtil(obj,"");
	}
}
