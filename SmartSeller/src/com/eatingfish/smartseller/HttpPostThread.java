package com.eatingfish.smartseller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpPostThread extends Thread {
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "http://10.0.2.2:8080/ServerManagement/" ;
	private String result ;
	private String _url ;
	Map<String ,String> _map ;
	
	public HttpPostThread(String url,  Map<String ,String> map) 
	{
		_url = url ;
		_map = map ;
	}
	public void run()
	{
		try {
			result = postRequest(_url, _map) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String postRequest(String url, Map<String ,String> rawParams) throws Exception
	{
		// 创建HttpPost对象。
		HttpPost post = new HttpPost(url);
		// 如果传递参数个数比较多的话可以对传递的参数进行封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(String key : rawParams.keySet())
		{
			//封装请求参数
			params.add(new BasicNameValuePair(key , rawParams.get(key)));
		}
		// 设置请求参数
		post.setEntity(new UrlEncodedFormEntity(
			params, "gbk"));
		// 发送POST请求
		HttpResponse httpResponse = httpClient.execute(post);
		// 如果服务器成功地返回响应
		if (httpResponse.getStatusLine()
			.getStatusCode() == 200)
		{
			// 获取服务器响应字符串
			String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			return result;
		}
		return null;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
