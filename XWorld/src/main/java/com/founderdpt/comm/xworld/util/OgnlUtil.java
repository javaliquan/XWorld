package com.founderdpt.comm.xworld.util;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ognl.ExpressionSyntaxException;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.OgnlParser;
import ognl.ParseException;
import ognl.TokenMgrError;

public class OgnlUtil {
	public static Object getValue(String expression, Object root)
			throws OgnlException {
		if (root == null||SimpleTypeRegistry.isSimpleType(root.getClass())) {
			Map root_map = new HashMap();
			root_map.put("value", root);
			return Ognl.getValue(parseExpression(expression), root_map);
		}
		return Ognl.getValue(parseExpression(expression), root);
		
	}

	public static boolean evaluateBoolean(String expression, Object root) {
		try {
			Object value = OgnlUtil.getValue(expression, root);
			if (value instanceof Boolean)
				return (Boolean) value;
			if (value instanceof Number)
				return !new BigDecimal(String.valueOf(value))
						.equals(BigDecimal.ZERO);
			return value != null;
		} catch (OgnlException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static Object parseExpression(String expression)
			throws OgnlException {
		try {
			Node node = new OgnlParser(new StringReader(expression))
					.topLevelExpression();
			return node;
		} catch (ParseException e) {
			throw new ExpressionSyntaxException(expression, e);
		} catch (TokenMgrError e) {
			throw new ExpressionSyntaxException(expression, e);
		}
	}

}
