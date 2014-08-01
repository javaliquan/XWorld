package com.founderdpt.comm.xworld.plugin.struts2.util;

import java.util.HashMap;
import java.util.Map;

import ognl.OgnlException;

import com.founderdpt.comm.xworld.orm.exception.BuilderException;
import com.founderdpt.comm.xworld.util.OgnlUtil;
import com.founderdpt.comm.xworld.util.config.builder.OgnlCache;
import com.opensymphony.xwork2.ActionInvocation;
 public class BindingTokenParser implements TokenHandler {

    private ActionInvocation invocation;
    private Map map;

    public BindingTokenParser(ActionInvocation invocation,Map map) {
      this.invocation = invocation;
      this.map=map;
    }

    public String handleToken(String content) {
      try {
    	  Map param_map = new HashMap();
    	  param_map.putAll(invocation.getStack().getContext());
    	  param_map.putAll(map);
        Object value = OgnlUtil.getValue(content, param_map);
        return (value == null ? "" : String.valueOf(value)); // issue #274 return "" instead of "null"
      } catch (OgnlException e) {
        throw new BuilderException("Error evaluating expression '" + content + "'. Cause: " + e, e);
      }
    }
  }