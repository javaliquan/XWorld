package com.founderdpt.comm.xworld.plugin.struts2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 请求对象
 * 
 */
public class Request {
	private String requestURI;
	private Map<String, String[]> parameterMap;

	Request() {
		this("");
	}

	Request(String requestURI) {
		this.requestURI = requestURI;
		parameterMap = new HashMap<String, String[]>();
	}

	Request(String requestURI, Map<String, String[]> parameterMap) {
		this.requestURI = requestURI;
		this.parameterMap = parameterMap;
	}

	/**
	 * 获得指定名称的参数
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		String[] values = parameterMap.get(name);
		if (values != null && values.length > 0) {
			return values[0];
		}
		return null;
	}

	/**
	 * 获得所有的参数名称
	 * 
	 * @return
	 */
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(parameterMap.keySet());
	}

	/**
	 * 获得指定名称的参数值(多个)
	 * 
	 * @param name
	 * @return
	 */
	public String[] getParameterValues(String name) {
		return parameterMap.get(name);
	}

	/**
	 * 获得请求的url地址
	 * 
	 * @return
	 */
	public String getRequestURI() {
		return requestURI;
	}

	/**
	 * 获得 参数-值Map
	 * 
	 * @return
	 */
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{");
		buf.append("\n  url = ").append(this.requestURI);
		buf.append("\n  paramsMap = {");
		if (this.parameterMap.size() > 0) {
			for (Map.Entry<String, String[]> e : this.parameterMap.entrySet()) {
				buf.append(e.getKey()).append("=").append(
						Arrays.toString(e.getValue())).append(",");
			}
			buf.deleteCharAt(buf.length() - 1);
		}
		buf.append("}\n}");
		return buf.toString();
	}
	// 剩下的函数,自己根据需求实现了,一般返回0或者null即可
	// 这里就不贴了,HttpServletRequest的接口方法也太多了

}