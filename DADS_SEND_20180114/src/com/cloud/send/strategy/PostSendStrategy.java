package com.cloud.send.strategy;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * post请求发送策略
 * @author user
 *
 */
public class PostSendStrategy implements Strategy {

	@Override
	public boolean sendData(String appType, String url, String params) {
		boolean sendSucess = false;
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);

		//将参数放入post请求体中
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		post.addParameter("data", params);

		//设置连接超时时间
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		//设置响应超时时间
		client.getHttpConnectionManager().getParams().setSoTimeout(15000);

		int responCode;
		try {
			responCode = client.executeMethod(post);
			if (responCode == 200) {
				sendSucess = true;
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sendSucess;
	}

}
