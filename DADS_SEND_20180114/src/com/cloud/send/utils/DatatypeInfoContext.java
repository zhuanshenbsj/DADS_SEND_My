package com.cloud.send.utils;

import java.util.HashMap;
import java.util.List;

public class DatatypeInfoContext {
	public static HashMap<String, HashMap<String, String>> data;

	static {
		data = new HashMap<String, HashMap<String, String>>();
		String sqlString = "select * from datatype";
		List<HashMap<String, String>> list = C3P0Util.getData(sqlString);
		if (null != list && list.size() > 0) {
			for (HashMap<String, String> hashMap : list) {
				data.put(hashMap.get("datatypeName"), hashMap);
			}
		}
	}

	public static String getProperty(String dataType, String param) {
		HashMap<String, String> hashMap = data.get(dataType);
		if (null != hashMap) {
			return hashMap.get(param);
		}
		return null;
	}
}
