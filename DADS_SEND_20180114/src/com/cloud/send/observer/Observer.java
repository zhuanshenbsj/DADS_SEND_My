package com.cloud.send.observer;

import com.cloud.send.observer.subject.Subject;

/**
 * 观察者
 * @author user
 *
 */
public interface Observer {
	public abstract void update(Subject subject);
}
