package com.cloud.send.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 应用系统上下文信息，
 * 保存各个app的appType,sendFlag,sendPath,appToggle等相关信息
 * <br>系统启动时即从数据库读取到内存，以减少数据库访问次数，若信息有更新，则需要及时刷新此上下文
 *
 */
public class AppInfoContext {
	public static HashMap<String, HashMap<String, String>> info = new HashMap<String, HashMap<String, String>>();
	static {
		initAppInfo();
	}

	public static void initAppInfo() {
		info.clear();
		String sql = "SELECT appName as appType, appSendFlag as sendFlag, appUrl as sendPath, appToggle, appQueueName FROM product";
		List<HashMap<String, String>> list = C3P0Util.getData(sql);
		if (list != null && list.size() > 0) {
			for (HashMap<String, String> map : list) {
				info.put(map.get("appType"), map);
			}
		}
	}

	public static String getPropertyByApp(String appType, String prop) {

		HashMap<String, String> appInfo = info.get(appType);
		if (appInfo != null) {
			return appInfo.get(prop);
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		Set<Entry<String, HashMap<String, String>>> entrySet = info.entrySet();
		for (Entry<String, HashMap<String, String>> entry : entrySet) {
			System.out.println(entry.toString());
		}
		System.out.println("aaa:" + info.get("aa"));
	}
}
