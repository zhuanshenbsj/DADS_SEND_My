package com.cloud.send.observer.subject;

import java.util.List;

import com.cloud.send.observer.Observer;

/**
 * 主题(观察者模式被观察对象)
 * @author user
 *
 */
public abstract class Subject {
	protected String dataType;
	protected String appType;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public List<Observer> getObservers() {
		return observers;
	}

	public void setObservers(List<Observer> observers) {
		this.observers = observers;
	}

	//观察者的集合
	private List<Observer> observers;

	//添加观察者
	public void addObserver(Observer o) {
		observers.add(o);
	}

	//移除被观察者
	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	//通知观察者
	public void notifyALlObservers() {
		if (null != observers) {
			return;
		}
		for (Observer observer : observers) {
			//通知观察者，调用他们的更新方法
			observer.update(this);
		}
	}
}
