package com.cloud.send.observer;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.cloud.send.observer.subject.Subject;
import com.cloud.send.thread.CommonThread;
import com.cloud.send.thread.ThreadStateFlag;
import com.cloud.send.utils.DatatypeInfoContext;
import com.cloud.send.utils.PropertiesReader;

public abstract class AbstractObserver implements Observer {

	/**
	 *公共查询
	 * @param subject
	 * @param commonObserver
	 * @param hashMap
	 */
	public void commonUpdate(Subject subject, CommonObserver commonObserver, HashMap<String, CommonThread> threadMap) {
		//根据dataType类型查询其表
		String dataType = subject.getDataType();
		String tableName = DatatypeInfoContext.getProperty(dataType, "tableName");

		//判断配置的发送方式
		String sendWayALl = PropertiesReader.getProp("sendWayList");
		if (StringUtils.isBlank(sendWayALl)) {
			return;
		}
		String[] sendWayListsStrings = sendWayALl.split(";");

		for (String sendWay : sendWayListsStrings) {
			//判断发送方式有无开启
			if (!"on".equals(PropertiesReader.getProp(sendWay + "_toggle"))) {
				//日志记录
				continue;
			}

			//线程key,若key重复，则说明线程重复
			//App类型一样,sendway一样，则不会起重复的线程
			String threadKey = commonObserver.getAppType() + "_" + tableName + "_" + sendWay;
			CommonThread commonThread;
			if (threadMap.containsKey(threadKey)) {
				commonThread = threadMap.get(threadKey);
				//若线程存活,则不需要开启新的线程
				if (commonThread.isAlive()) {
					continue;
				}
			}

			//若不存在该线程key，则开启新线程
			commonThread = new CommonThread(sendWay, tableName, commonObserver.getSendFlag(),
					commonObserver.getAppType(), commonObserver.getSendPath(),
					ThreadStateFlag.getNewInstance(threadKey));
			commonThread.setName(threadKey);
			commonThread.start();
			threadMap.put(threadKey, commonThread);
		}
	}

}
