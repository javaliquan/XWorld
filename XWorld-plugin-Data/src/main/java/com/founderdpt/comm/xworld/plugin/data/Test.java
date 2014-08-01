package com.founderdpt.comm.xworld.plugin.data;

import com.founderdpt.comm.xworld.util.config.ProperConfigUtil;

public class Test {
	public static void main(String[] args) throws Exception {
		String path="D:/2";
        XDBDataUtil.exportData(path);
		ProperConfigUtil.loadConfigFile(Test.class.getClassLoader().getResourceAsStream(
				"ImpCommXConfig.properties"));
	    XDBDataUtil.importData(path,true);
	}
}
