package com.founderdpt.comm.xworld.orm.parsing;

import ognl.OgnlException;

import com.founderdpt.comm.xworld.orm.exception.BuilderException;
import com.founderdpt.comm.xworld.util.SimpleTypeRegistry;
import com.founderdpt.comm.xworld.util.config.builder.DynamicContext;
import com.founderdpt.comm.xworld.util.config.builder.OgnlCache;
 public class BindingTokenParser implements TokenHandler {

    private DynamicContext context;

    public BindingTokenParser(DynamicContext context) {
      this.context = context;
    }

    public String handleToken(String content) {
      try {
        Object parameter = context.getParameter();
        if (parameter == null) {
          context.getBindings().put("value", null);
        } else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
          context.getBindings().put("value", parameter);
        }
        Object value = OgnlCache.getValue(content, context.getBindings());
        return (value == null ? "" : String.valueOf(value)); // issue #274 return "" instead of "null"
      } catch (OgnlException e) {
        throw new BuilderException("Error evaluating expression '" + content + "'. Cause: " + e, e);
      }
    }
  }