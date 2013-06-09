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
		// ����HttpPost����
		HttpPost post = new HttpPost(url);
		// ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(String key : rawParams.keySet())
		{
			//��װ�������
			params.add(new BasicNameValuePair(key , rawParams.get(key)));
		}
		// �����������
		post.setEntity(new UrlEncodedFormEntity(
			params, "gbk"));
		// ����POST����
		HttpResponse httpResponse = httpClient.execute(post);
		// ����������ɹ��ط�����Ӧ
		if (httpResponse.getStatusLine()
			.getStatusCode() == 200)
		{
			// ��ȡ��������Ӧ�ַ���
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
