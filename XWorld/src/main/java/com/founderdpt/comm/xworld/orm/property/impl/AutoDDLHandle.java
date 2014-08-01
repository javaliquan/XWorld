package com.founderdpt.comm.xworld.orm.property.impl;

import java.util.Map;

import com.founderdpt.comm.xworld.orm.Configuration;
import com.founderdpt.comm.xworld.orm.property.IPropertyHandle;
import com.founderdpt.comm.xworld.util.java.PackageHelp;

public class AutoDDLHandle implements IPropertyHandle {
	
	
	@Override
	public void handle(String value, Configuration configuration) {
		Map<String, String> propertys = configuration.getPropertys();
		
		String packageStr = propertys.get(configuration.HANDLE_XWPACKAGE_COMMAND);
		
	
		Package pkg =  Package.getPackage(packageStr);
		PackageHelp.getClassesForPackage(pkg);
		
	}


	 
	
}
