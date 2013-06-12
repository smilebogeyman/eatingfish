package com.eatingfish.smartseller;

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

import org.apache.http.HttpEntity;
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
import org.json.JSONObject;

import com.eatingfish.smartseller.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btnLogin;
	private String responseMsg = "";
	private int storeId;
	private int userId;
	private static final int REQUEST_TIMEOUT = 5 * 1000;
	private static final int SO_TIMEOUT = 10 * 1000;
	private EditText userNameText;
	private EditText passwordText;
	private Toast toast;
	private String userName;
	private String password;

	private Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnLogin = (Button) findViewById(R.id.logBT);
		userNameText = (EditText) findViewById(R.id.userName);
		passwordText = (EditText) findViewById(R.id.password);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Thread loginThread = new Thread(new LoginThread());
				loginThread.start();

			}
		});
		handler = new Handler() {
			public void handleMessage(Message msg) {
				System.out.println("the message what is" + msg.what);
				switch (msg.what) {
				case 0:
					toast = Toast.makeText(getApplicationContext(), "登陆成功",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					Intent intent = new Intent(MainActivity.this,
							UserAction.class);
					intent.putExtra("userName", userName);
					intent.putExtra("storeId", storeId);
					intent.putExtra("userId", userId);
					startActivity(intent);
					finish();
					break;
				case 1:
					toast = Toast.makeText(getApplicationContext(), "用户名密码错误，",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					break;
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class LoginThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			userName = userNameText.getText().toString();
			password = passwordText.getText().toString();
			boolean loginValidate = loginServer(userName, password);
			System.out.println("loginserver gets the result:" + loginValidate);
			if (loginValidate) {
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}

		}

	}

	private boolean loginServer(String userName, String password) {
		String urlStr = "http://192.168.1.2:8088/ServerManagement/LoginServlet.action";
		HttpPost request = new HttpPost(urlStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("password", password));
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				// 获得响应信息
				String out = EntityUtils
						.toString(response.getEntity(), "UTF-8");
				JSONObject jsonObject = new JSONObject(out);
				responseMsg = jsonObject.getString("result");
				storeId = jsonObject.getInt("storeId");
				userId = jsonObject.getInt("userId");

				System.out.println("result is:" + responseMsg + "&& store is:"
						+ storeId);
				if (responseMsg.equals("success"))
					return true;

				else
					return false;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
}
