package com.cloud.send.main;

import java.util.HashMap;
import java.util.List;

import com.cloud.send.observer.subjectFactory.SubjectFactory;
import com.cloud.send.utils.C3P0Util;

public class StartSend {
	public static void main(String[] args) {

		String tablename = "sports";
		String sqlStr = "select dataType,appType from " + tablename + "  where sendFlag='0' limit 1";
		//获取还未发送的数据
		//sports为运动数据, sendFlag='0'为尚未发送的数据
		List<HashMap<String, String>> data = C3P0Util.getData(sqlStr);
		if (null != data && data.size() > 0) {
			for (HashMap<String, String> hashMap : data) {
				//获取主题，通知观察者进行更新操作
				SubjectFactory.getSubject(hashMap.get("dataType"), hashMap.get("appType")).notifyALlObservers();
			}
		}
	}
}
