package com.cloud.send.strategy;

public interface Strategy {
	public boolean sendData(String appType, String url, String params);
}
