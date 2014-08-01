package com.founderdpt.comm.xworld.plugin.struts2.resulet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.http.HttpServletResponse;
import javax.xml.xquery.XQResultSequence;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.founderdpt.comm.xworld.db.DptXMLDBSessionFactory;
import com.founderdpt.comm.xworld.db.IXQSession;
import com.founderdpt.comm.xworld.plugin.struts2.util.BindingTokenParser;
import com.founderdpt.comm.xworld.plugin.struts2.util.GenericTokenParser;
import com.founderdpt.comm.xworld.plugin.struts2.util.HttpRequestParser;
import com.founderdpt.comm.xworld.plugin.struts2.util.Request;
import com.founderdpt.comm.xworld.plugin.struts2.util.StringFile;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

public class XQYResulet extends StrutsResultSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 504201301628653528L;
	public static final int BUFFER_SIZE = 1024;
	private static final Logger LOG = LoggerFactory.getLogger(XQYResulet.class);

	private String charSet;

	@Override
	protected void doExecute(String location, ActionInvocation invocation)
			throws Exception {
		String xqy_str = getXQYText(location, invocation);
		// System.out.println(xqy_str);
		if (xqy_str != null && !"".equals(xqy_str.trim())) {
			IXQSession session = DptXMLDBSessionFactory.getXQSession();
			try {
				session.beginTransaction();
				XQResultSequence sequence = session.getXqConnection()
						.createExpression().executeQuery(xqy_str);
				String result = sequence.getSequenceAsString(null);
				session.commit();
				HttpServletResponse response = ServletActionContext
						.getResponse();
				if (charSet != null && !"".equals(charSet.trim())) {
					response.setContentType("text/html; charset=" + charSet);
				} else {
					response.setContentType("text/html");
				}
				response.setCharacterEncoding(charSet);
				PrintWriter writer = response.getWriter();
				writer.write("<!DOCTYPE html>");
				StringReader reader = new StringReader(result);
				try {
					char[] buffer = new char[BUFFER_SIZE];
					int charRead = 0;
					while ((charRead = reader.read(buffer)) != -1) {
						writer.write(buffer, 0, charRead);
					}
				} finally {
					if (reader != null)
						reader.close();
					if (writer != null) {
						writer.flush();
						writer.close();
					}
				}
			} catch (Exception e) {
				if(session!=null){
					session.rollBack();
				}
			} finally {
				DptXMLDBSessionFactory.closeXQSession();
			}
		}
	}

	private String getXQYText(String location, ActionInvocation invocation)
			throws IOException {
		Request reqparse = HttpRequestParser.parse(location);
		String urlString = reqparse.getRequestURI();
		String basePath = ServletActionContext.getServletContext().getRealPath(
				"/");
		String xqy = basePath + "/" + urlString;
		File file = new File(xqy);
		if (!file.exists()) {
			ServletActionContext.getResponse().sendError(404, urlString);
			return null;
		}
		String xqy_str = StringFile.file2String(file, getCharSet());
		GenericTokenParser parser = new GenericTokenParser("${", "}",
				new BindingTokenParser(invocation,reqparse.getParameterMap()));
		return parser.parse(xqy_str);
	}

	/**
	 * Set the character set
	 * 
	 * @return The character set
	 */
	public String getCharSet() {
		return charSet;
	}

	@Inject(StrutsConstants.STRUTS_I18N_ENCODING)
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

}
