package com.cloud.send.observer;

import java.util.HashMap;

import com.cloud.send.observer.subject.Subject;
import com.cloud.send.thread.CommonThread;

public class CommonObserver extends AbstractObserver {

	private String sendFlag = null;
	private String appType = null;
	private String sendPath = null;
	//内存的方法区(共享的东西),存储已启动的线程，防止重复的线程重复启动
	private static HashMap<String, CommonThread> threadMap = new HashMap<String, CommonThread>();

	@Override
	public void update(Subject subject) {
		super.commonUpdate(subject, this, threadMap);
	}

	public CommonObserver(String sendFlag, String appType, String sendPath) {
		super();
		this.sendFlag = sendFlag;
		this.appType = appType;
		this.sendPath = sendPath;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getSendPath() {
		return sendPath;
	}

	public void setSendPath(String sendPath) {
		this.sendPath = sendPath;
	}

}
