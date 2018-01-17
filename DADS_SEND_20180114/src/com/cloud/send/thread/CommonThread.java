package com.cloud.send.thread;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloud.send.strategy.MQSendStrategy;
import com.cloud.send.strategy.PostSendStrategy;
import com.cloud.send.strategy.Strategy;
import com.cloud.send.utils.C3P0Util;
import com.cloud.send.utils.PropertiesReader;

import net.sf.json.JSONObject;

public class CommonThread extends Thread {
	private static Logger log = Logger.getLogger(CommonThread.class);
	private String baseQuerySql;
	private String baseUpdateSql;
	private String sendWay;
	private ThreadStateFlag threadStateFlag;
	private String tableName;
	private String sendFlag;
	private String appType;
	private String sendPath;
	//发送策略
	Strategy sendStrategy;

	public CommonThread(String sendWay, String tableName, String sendFlag, String appType, String sendPath,
			ThreadStateFlag threadFlag) {
		super();

		this.sendWay = sendWay;
		this.tableName = tableName;
		this.sendFlag = sendFlag;
		this.appType = appType;
		this.sendPath = sendPath;
		threadStateFlag = threadFlag;

		this.baseQuerySql = PropertiesReader.getProp("baseQuerySql_" + sendWay);
		this.baseUpdateSql = PropertiesReader.getProp("baseUpdateSql_" + sendWay);
	}

	@Override
	public void run() {
		//主流业务

		synchronized (ThreadStateFlag.class) {

			//先查询未发送的数据
			baseQuerySql = String.format(baseQuerySql, tableName, sendFlag, "%" + appType + "%");
			//查询一下未发送的数据
			List<HashMap<String, String>> data = C3P0Util.getData(baseQuerySql);
			if (data != null && data.size() > 0) {
				for (HashMap<String, String> hashMap : data) {
					JSONObject dataJson = new JSONObject();
					//将需要发送给emr的参数封装成json对象
					dataJson.put("appType", appType);
					dataJson.put("dataType", hashMap.get("dataType"));
					dataJson.put("collectDate", hashMap.get("receiveTime"));
					dataJson.put("phone", hashMap.get("phone"));
					dataJson.put("dataValue", hashMap.get("dataValue"));

					//通过策略模式发送数据
					if ("PostStrategy".equals(PropertiesReader.getProp(sendWay))) {
						sendStrategy = new PostSendStrategy();
					} else {
						sendStrategy = new MQSendStrategy();
					}

					if (null != sendStrategy) {
						if (sendStrategy.sendData(appType, sendPath, dataJson.toString())) {
							//更新发送标示
							baseUpdateSql = String.format(baseUpdateSql, tableName, sendFlag, hashMap.get("id"));
							boolean executeSuccess = C3P0Util.executeUpdate(baseUpdateSql);
							if (executeSuccess) {
								//做日志
								log.info(this.getClass().getSimpleName()
										+ " send data successfully,and update flag successed.The sql is: "
										+ baseUpdateSql);
							} else {
								//做日志
								log.error(this.getClass().getSimpleName()
										+ " send data successfully,but update flag failed.The sql is: "
										+ baseUpdateSql);
							}

						} else {
							//等会再发
							try {
								Thread.sleep(20000);
							} catch (Exception e) {
								// TODO: handle exception
							}
							//做日志
							log.error(this.getClass().getSimpleName() + " send data failed");
						}
					}
				}
			}
			//该线程执行完毕后，释放锁
			threadStateFlag.notify();
		}
	}

}
