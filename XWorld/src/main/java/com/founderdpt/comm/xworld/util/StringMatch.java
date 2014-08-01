package com.founderdpt.comm.xworld.util;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class StringMatch {
 

	public static String translateParamtwo(String expression, char[] openChars,
			Map map) {
		int maxLoopCount = 1;
		String result = expression;
		for (char open : openChars) {
			int loopCount = 1;
			int pos = 0;

			// this creates an implicit StringBuffer and shouldn't be used in
			// the inner loop
			final String lookupChars = open + "{";

			while (true) {
				int start = expression.indexOf(lookupChars, pos);
				if (start == -1) {
					pos = 0;
					loopCount++;
					start = expression.indexOf(lookupChars);
				}
				if (loopCount > maxLoopCount) {
					// translateVariables prevent infinite loop / expression
					// recursive evaluation
					break;
				}
				int length = expression.length();
				int x = start + 2;
				int end;
				char c;
				int count = 1;
				while (start != -1 && x < length && count != 0) {
					c = expression.charAt(x++);
					if (c == '{') {
						count++;
					} else if (c == '}') {
						count--;
					}
				}
				end = x - 1;

				if ((start != -1) && (end != -1) && (count == 0)) {
					String var = expression.substring(start + 2, end);
					Object obj = map.get(var);
					String o ="";
					if(obj instanceof String[]){
						o=((String[])obj)[0];
					}
					if(obj instanceof String){
						o=(String) obj;
					}
					String left = expression.substring(0, start);
					String right = expression.substring(end + 1);
					String middle = null;
					if (o != null) {
						middle = o.toString();
						if (StringUtils.isEmpty(left)) {
							result = o;
						} else {
							result = left.concat(middle);
						}

						if (StringUtils.isNotEmpty(right)) {
							result = result.toString().concat(right);
						}

						expression = left.concat(middle).concat(right);
					} else {
						// the variable doesn't exist, so don't display anything
						expression = left.concat(right);
						result = expression;
					}
					pos = (left != null && left.length() > 0 ? left.length() - 1
							: 0)
							+ (middle != null && middle.length() > 0 ? middle
									.length() - 1 : 0) + 1;
					pos = Math.max(pos, 1);
				} else {
					break;
				}
			}
		}
		return result;
	}
	
	
	
	/**
	 * 匹配$[XXX]
	 * @param expression
	 * @param openChars
	 * @param map
	 * @return
	 */
	public static String translateParam(String expression, char[] openChars,
			Map map) {
		int maxLoopCount = 1;
		String result = expression;
		for (char open : openChars) {
			int loopCount = 1;
			int pos = 0;

			// this creates an implicit StringBuffer and shouldn't be used in
			// the inner loop
			final String lookupChars = open + "[";

			while (true) {
				int start = expression.indexOf(lookupChars, pos);
				if (start == -1) {
					pos = 0;
					loopCount++;
					start = expression.indexOf(lookupChars);
				}
				if (loopCount > maxLoopCount) {
					// translateVariables prevent infinite loop / expression
					// recursive evaluation
					break;
				}
				int length = expression.length();
				int x = start + 2;
				int end;
				char c;
				int count = 1;
				while (start != -1 && x < length && count != 0) {
					c = expression.charAt(x++);
					if (c == '[') {
						count++;
					} else if (c == ']') {
						count--;
					}
				}
				end = x - 1;

				if ((start != -1) && (end != -1) && (count == 0)) {
					String var = expression.substring(start + 2, end);
					Object obj = map.get(var);
					String o ="";
					if(obj instanceof String[]){
						o=((String[])obj)[0];
					}
					if(obj instanceof String){
						o=(String) obj;
					}
					String left = expression.substring(0, start);
					String right = expression.substring(end + 1);
					String middle = null;
					if (o != null) {
						middle = o.toString();
						if (StringUtils.isEmpty(left)) {
							result = o;
						} else {
							result = left.concat(middle);
						}

						if (StringUtils.isNotEmpty(right)) {
							result = result.toString().concat(right);
						}

						expression = left.concat(middle).concat(right);
					} else {
						// the variable doesn't exist, so don't display anything
						expression = left.concat(right);
						result = expression;
					}
					pos = (left != null && left.length() > 0 ? left.length() - 1
							: 0)
							+ (middle != null && middle.length() > 0 ? middle
									.length() - 1 : 0) + 1;
					pos = Math.max(pos, 1);
				} else {
					break;
				}
			}
		}
		return result;
	}
	

	
	

	
	/**
	 * 匹配${XXX}
	 * @param expression
	 * @param openChars
	 * @param map
	 * @return
	 */
	public static String translateParam(String expression,Map map) {
		if(map==null){
			return expression;
		}
		return translateParamtwo(expression, new char[] { '$' },map);
	}
	
	
	/**
	 * 全局匹配 匹配$[XXX]
	 * @param expression
	 * @param map
	 * @return
	 */
	public static String translateParamGlobal(String expression,Map map) {
		if(map==null){
			return expression;
		}
		return translateParam(expression, new char[] { '$' },map);
	}
	
	

	
	
	
}
