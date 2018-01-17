package com.cloud.send.utils;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
/**
 * 
 *  产品和数据类型对应信息上下文
 *	map 以 apptype_datatype 为key , 开关标志位toggle 为value
 */
public class AppDatatypeInfoContext {
	public static HashMap<String,String> info = new HashMap<String, String>();
	static{
		initAppDatatype();
	}

	public static void initAppDatatype() {
		info.clear();
		String sql="SELECT p.appName AS appType,d.dataTypeName AS dataType,pd.toggle FROM product p JOIN product_datatype pd" +
				" ON p.appID=pd.productID JOIN datatype d ON pd.dataTypeID=d.datatypeID where pd.toggle='on'";
		List<HashMap<String, String>> list = C3P0Util.getData(sql);
		if(list != null && list.size() >0){
			for (HashMap<String, String> map : list) {
				info.put(map.get("appType")+"_"+map.get("dataType"),map.get("toggle"));
			}
		}
	}
	public static boolean isDataTypeOpenForAppType(String appType,String dataType){
		
		return info.containsKey(appType+"_"+dataType);
	}
	@Test
	public void test() {
		Assert.assertTrue(isDataTypeOpenForAppType("WSYD", "stepCount"));
	}
}
