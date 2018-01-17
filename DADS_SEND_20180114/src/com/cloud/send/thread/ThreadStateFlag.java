package com.cloud.send.thread;

import java.util.HashMap;

public class ThreadStateFlag {
	private ThreadStateFlag() {

	}

	private static HashMap<String, ThreadStateFlag> map = new HashMap<String, ThreadStateFlag>();

	public static ThreadStateFlag getNewInstance(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			map.put(key, new ThreadStateFlag());
			return map.get(key);
		}
	}

}
