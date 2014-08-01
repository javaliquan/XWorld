import java.util.HashMap;
import java.util.Map;

import javax.xml.xquery.XQException;

import ognl.OgnlException;

import com.founderdpt.comm.xworld.util.OgnlUtil;
import com.founderdpt.comm.xworld.util.config.XMLConfigUtil;

public class Test {
	String name="111";
	String paw="222";
	Test2 test=new Test2();
	
	public String getName() {
		return name;
	}

 

	public Test2 getTest() {
		return test;
	}



	public void setTest(Test2 test) {
		this.test = test;
	}



	public void setName(String name) {
		this.name = name;
	}


	public static void main(String[] args) throws XQException, OgnlException {
		Map param = new HashMap();
		param.put("dept", "xxx");
		Test test =new Test();
		param.put("test", test);
		 System.out.println("------------>" +
		 XMLConfigUtil.getConfigByXpath("mail", "getPeopleBydept2",
				 test).getXql()); //
		 
		 // System.out.println(OgnlUtil.getValue("1<value&&value<11","2"));
		 
		//DptXMLDBSessionFactory.getXQSession().beginTransaction();
		//DptXMLDBSessionFactory.getXQSession().getXqConnection().prepareExpression("let $x return $x");

	}
}
class Test2{
	public String name="test2";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}