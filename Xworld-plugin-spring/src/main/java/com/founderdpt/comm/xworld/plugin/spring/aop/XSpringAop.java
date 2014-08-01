package com.founderdpt.comm.xworld.plugin.spring.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xquery.XQException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.founderdpt.comm.xworld.db.DptXMLDBSessionFactory;
import com.founderdpt.comm.xworld.db.IXQSession;
import com.founderdpt.comm.xworld.plugin.spring.annotation.XNTranscation;

/**
 * 
 * @ClassName: XSpringAop
 * @Description: 事务的aop 实现
 * @author qianlong.cheng
 * @date 2013-4-12 下午2:59:36
 * 
 */
public class XSpringAop {

	private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

	// private static final String ISTRANSACTION_STR="isTransaction";

	// 事务的第一个方法
	private static final String FIRST_METHOD_NAME = "FMethodName";

	/**
	 * 是不是第一个方法
	 * 
	 * @return true表示是第一个方法
	 */
	public boolean isFirstMethod() {
		Map<String, Object> map =threadLocal.get();
		return  map==null||map.get(FIRST_METHOD_NAME)==null;
	}

	public void setFirstMethod(Method method) {
		Map<String, Object> map = threadLocal.get();
		if(map==null){
			map=new HashMap<String, Object>();
		}
		map.put(FIRST_METHOD_NAME, method);
		threadLocal.set(map);
	}

	public Method getFirstMethod() {
		Map<String, Object> object=threadLocal.get();
		if(object!=null){
			return (Method) object.get(FIRST_METHOD_NAME);

		}
		return null;
	}

	private void getTranscation() throws XQException {
		DptXMLDBSessionFactory.getXQSession().beginTransaction();
	}

	private void commit() throws XQException {
		IXQSession ixqSession=DptXMLDBSessionFactory.getXQSession();
		if(ixqSession!=null&&!ixqSession.isClosed()){
			ixqSession.commit();
		}
	}

	private void rollback() throws XQException {
		IXQSession ixqSession=DptXMLDBSessionFactory.getXQSession();
		if(ixqSession!=null&&!ixqSession.isClosed()){
			ixqSession.rollBack();
		}
		
	}

	private void close() throws XQException {
		DptXMLDBSessionFactory.closeXQSession();
		
	}
	public Object transcation(ProceedingJoinPoint joinpoint) throws Throwable {
		Object object=null;
		boolean isTranscation = true;
		Class tagetClass = joinpoint.getTarget().getClass();
		MethodSignature signature = (MethodSignature) joinpoint.getSignature();
		Method method = signature.getMethod();

		Annotation[] annotations = method.getAnnotations();
		if (annotations != null) {
			for (Annotation annotation : annotations) {
				if (annotation instanceof XNTranscation) {
					isTranscation = false;
					break;
				}
			}
		}
		if(isTranscation){
			if (tagetClass.getAnnotation(XNTranscation.class) != null) {
				isTranscation = false;
			}
		}

		if (isTranscation) {
			try {
				// 是否是第一个方法,第一个方法才能得到事务
				if (isFirstMethod()) {
					setFirstMethod(method);
					getTranscation();
				}
				object=joinpoint.proceed();
				// 当执行到最后一个方法时,提交
				if (getFirstMethod() == method) {
					commit();
				}
			} catch (Throwable e) {
				rollback();
				throw e;
			} finally {
				// 当执行到最后一个方法时,close
				if (getFirstMethod() == method) {
					this.setFirstMethod(null);
					close();
				}
			}
		} else {
			object= joinpoint.proceed();
		}
		return object;
	}

	@XNTranscation
	public void test() {

	}

	public static void main(String[] args) {

		XSpringAop xSpringAop = new XSpringAop();
		Annotation[] annotations = xSpringAop.getClass().getAnnotations();
		for (Annotation a : annotations) {
			if (a.getClass().equals(XNTranscation.class)) {
				System.out.println("======");
				;
			}
		}
		System.out.println(annotations);
	}
}
