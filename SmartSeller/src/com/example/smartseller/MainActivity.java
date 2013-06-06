package com.example.smartseller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.example.smartseller.R;

import eating.defined.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btnLogin;
	private String responseMsg="";
	private static final int REQUEST_TIMEOUT = 5*1000;
	private static final int SO_TIMEOUT = 10*1000;
	private SharedPreferences sp;
	private EditText userNameText;
	private EditText passwordText;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnLogin = (Button) findViewById(R.id.logBT);
		userNameText = (EditText)findViewById(R.id.cardNumAuto);
		passwordText = (EditText)findViewById(R.id.passwordET);
		btnLogin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Thread loginThread = new Thread(new LoginThread());
				loginThread.start();
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	class LoginThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String userName = userNameText.getText().toString();
			String password = passwordText.getText().toString();
			boolean loginValidate = loginServer(userName, password);
			if(loginValidate){
				 System.out.println("yes success");
			}
			else{
				System.out.println("no failure");
			}
				 
			
		}
		
	}
	private boolean loginServer(String userName,String password){
		boolean loginValidate = false;
		String urlStr = "http://192.168.1.2:8088/Android_Servlet/servlet/LoginServlet";
		HttpPost request = new HttpPost(urlStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName",userName));
		params.add(new BasicNameValuePair("password",password));
		try{
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			 if(response.getStatusLine().getStatusCode()==200)
	            {
	                loginValidate = true;
	                //获得响应信息
	                responseMsg = EntityUtils.toString(response.getEntity());
	                System.out.println(responseMsg);
	            }
		}catch(Exception e){
			e.printStackTrace();
		}	
		return loginValidate;
	}
	public HttpClient getHttpClient(){
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

}
