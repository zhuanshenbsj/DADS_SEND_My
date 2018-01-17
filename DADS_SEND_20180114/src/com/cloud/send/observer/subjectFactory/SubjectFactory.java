package com.cloud.send.observer.subjectFactory;

import org.apache.commons.lang.StringUtils;

import com.cloud.send.observer.CommonObserver;
import com.cloud.send.observer.subject.DataSaveSubject;
import com.cloud.send.observer.subject.Subject;
import com.cloud.send.utils.AppDatatypeInfoContext;
import com.cloud.send.utils.AppInfoContext;

/**
 * 主题工厂，用于获取主题
 * @author user
 *
 */
public class SubjectFactory {
	public static Subject getSubject(String datatype, String apptype) {
		DataSaveSubject dataSaveSubject = new DataSaveSubject();
		//StringUtils.isNotBlank apache工具包，校验非null且非""
		if (StringUtils.isNotBlank(datatype) && StringUtils.isNotBlank(apptype)) {
			dataSaveSubject.setDataType(datatype);
			String[] appTypes = apptype.split(";");
			if (appTypes.length > 0) {
				for (String app : appTypes) {
					//判断app有没有开启
					if (!"on".equals(AppInfoContext.getPropertyByApp(app, "appToggle"))) {
						continue;
					}
					//判断该app下的指定类型是否能接受该数据类型
					if (AppDatatypeInfoContext.isDataTypeOpenForAppType(app, datatype)) {
						continue;
					}
					//若app和数据类型都能接受的话，则构造观察者
					String sendFlag = AppInfoContext.getPropertyByApp(app, "sendFlag");
					String sendPath = AppInfoContext.getPropertyByApp(app, "sendPath");
					CommonObserver commonObserver = new CommonObserver(sendFlag, app, sendPath);
					dataSaveSubject.addObserver(commonObserver);
				}
			}
		}
		return dataSaveSubject;
	}
}
