package com.founderdpt.comm.xworld.util.config.builder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.NoSuchPropertyException;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlParser;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.founderdpt.comm.xworld.orm.XMLConfigClientTemplate;
import com.founderdpt.comm.xworld.orm.parsing.XNode;

/**
 * 查询的环境定义
 * @author Administrator
 *
 */
public class DynamicContext {
	/**
	 * 参数默认的KEY值
	 */
	public static final String PARAMETER_OBJECT_KEY = "_parameter";

	static {
		//注册OGNL的适配器
		OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
	}

	private ContextMap bindings;
	private StringBuilder xql = new StringBuilder();
	private List<String> xql_list=new ArrayList<String>();
	private List<XNode> nodes= new ArrayList<XNode>();
	
	
	public void setXql(StringBuilder xql) {
		this.xql = xql;
	}

	public List<String> getXql_list() {
		return xql_list;
	}

	public void setXql_list(List<String> xql_list) {
		this.xql_list = xql_list;
	}

	 
	public List<XNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<XNode> nodes) {
		this.nodes = nodes;
	}

	public String getXql() {
		return xql.toString();
	}

	public StringBuilder getXqlBuilder() {
		return xql;
	}

	public void getXqlBuilder(StringBuilder xql) {
		this.xql = xql;
	}
	public void setXql(String xql) {
		this.xql = new StringBuilder(xql);
	}

	public void appendXQ(String xq) {
		this.xql.append(xq);
	}

	public Object getParameter() {
		if (bindings != null) {
			return bindings.get(PARAMETER_OBJECT_KEY);
		}
		return null;
	}

	public void setParameter(Object parameter) {
		if (bindings == null) {
			bindings = new ContextMap();
		}
	     if (parameter != null && !(parameter instanceof Map)) {
	    	 bindings.put(PARAMETER_OBJECT_KEY, parameter);
		}else {
			  bindings.put(PARAMETER_OBJECT_KEY, parameter);
	    }
	}

	public Map getBindings() {
		return bindings;
	}

	public void setBindings(ContextMap bindings) {
		this.bindings = bindings;
	}

	static class ContextMap extends HashMap<String, Object> {
		private static final long serialVersionUID = 2977601501966151582L;

		@Override
		public Object get(Object key) {
			String strKey = (String) key;
			if (super.containsKey(strKey)) {
				return super.get(strKey);
			}
			return null;
		}
	}
	 static class ContextAccessor implements PropertyAccessor {
			private static Logger log = Logger.getLogger(ContextAccessor.class);

		    public Object getProperty(Map context, Object target, Object name)
		        throws OgnlException {
		      Map map = (Map) target;
		      //System.out.println("_----------------<name:"+name);
		      Object result = map.get(name);
		      if (result == null) {
		    	  Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
			      if (parameterObject instanceof Map) {
			    	  result= ((Map)parameterObject).get(name);
			      }else{
			    	//  try {
			    		  //BeanUtils.getProperty(parameterObject, String.valueOf(name));
			    		  result= OgnlCache.getValue(String.valueOf(name),parameterObject);
					/*} catch (Exception e) {
						if(log.isDebugEnabled()){
							log.debug("OGNL 获取数据为空  KEY:"+name+" msg:"+e.getMessage());
						}
						return null;
					}*/
			      }
		      }
		      if(log.isDebugEnabled()){
		    	  if(result==null){
		    		  log.debug("OGNL 获取数据为空  KEY:"+name);
		    	  }
				}
		      return result;
		    }

		    public void setProperty(Map context, Object target, Object name, Object value)
		        throws OgnlException {
		      Map map = (Map) target;
		      map.put(name, value);
		    }

			@Override
			public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub
				return null;
			}
		  }
}
