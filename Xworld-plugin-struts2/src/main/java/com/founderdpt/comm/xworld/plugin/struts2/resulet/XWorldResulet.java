package com.founderdpt.comm.xworld.plugin.struts2.resulet;

import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.founderdpt.comm.xworld.db.DptXMLDBSessionFactory;
import com.founderdpt.comm.xworld.db.IXQSession;
import com.founderdpt.comm.xworld.orm.XMLConfigClientTemplate;
import com.founderdpt.comm.xworld.plugin.struts2.exception.XResuletException;
import com.founderdpt.comm.xworld.util.string.StringUtil;
import com.opensymphony.xwork2.ActionInvocation;

public class XWorldResulet extends StrutsResultSupport {
	private static XMLConfigClientTemplate template = new XMLConfigClientTemplate();
	/**
	 * 
	 */
	private static final long serialVersionUID = -4848613840429172031L;
	private String namespace;
	private String path;
	private Object root;
	
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {

		if (path == null) {
			if (StringUtil.isEmpty(finalLocation)) {
				throw new XResuletException("配置文件不能为空");
			}
			String[] namespace_local = finalLocation.split("!");
			if (namespace_local.length <= 1) {
				this.path = namespace_local[0];
			} else {
				this.namespace = namespace_local[0];
				this.path = namespace_local[1];
			}
		}
		if(root==null){
			root= invocation.getStack().getContext();
		}
		
		IXQSession session = DptXMLDBSessionFactory.getXQSession();
		try {
			session.beginTransaction();
			template.queryForSring(this.namespace, this.path, root);
			session.commit();
		} catch (Exception e) {
			if (session != null) {
				session.rollBack();
			}
		} finally {
			DptXMLDBSessionFactory.closeXQSession();
		}
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Object getRoot() {
		return root;
	}

	public void setRoot(Object root) {
		this.root = root;
	}

}
