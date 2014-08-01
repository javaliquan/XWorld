package com.founderdpt.comm.xworld.orm.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;

import org.apache.commons.lang.StringUtils;

import com.founderdpt.comm.xworld.orm.parsing.BindingTokenParser;
import com.founderdpt.comm.xworld.util.config.builder.DynamicContext;
import com.founderdpt.comm.xworld.util.config.builder.TextXQLNode;

public class ExpressionBindUtil {
	public static List bindParamMap(String expression, DynamicContext context){
		List list = new  ArrayList();
		
		
		Map result_map = new HashMap();
		
		
		char[] openChars = new char[] { '#' };

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
					String o = new BindingTokenParser(context).handleToken(var);
					String left = expression.substring(0, start);
					String right = expression.substring(end + 1);
					String middle = null;
					if (o != null) {
						result_map.put(var.toString(), o);
						o="$"+var;
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
		list.add(result);
		list.add(result_map);
		return list;
	}

}
