package com.eatingfish.smartseller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpGetThread extends Thread{
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "http://10.0.2.2:8080/ServerManagement/" ;
	private String result ;
	private String _url ;
	
	public HttpGetThread(String url) 
	{
		_url = url ;
	}
	public void run()
	{
		try {
			result = getRequest(_url) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getRequest(String url)
			throws Exception
		{
			// ����HttpGet����
			HttpGet get = new HttpGet(url);
			// ����GET����
			HttpResponse httpResponse = httpClient.execute(get);
			// ����������ɹ��ط�����Ӧ
			if (httpResponse.getStatusLine().getStatusCode() == 200)
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
