package com.eatingfish.smartseller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.eatingfish.smartseller.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserAction extends Activity {
	Button exitButton ;
	Button addButton ;
	Button searchButton ;
	Button ordersButton ;
	Button submitButton ;
	TextView name ;
	EditText itemId ;
	String userName;
	int storeId;
	int userId;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState) ;
		setContentView(R.layout.mainpage) ;
		Intent intent = getIntent();  
		
        userName = intent.getStringExtra("userName");   //获得前一个页面的userName和storeId
        storeId = intent.getIntExtra("storeId", 0); 
        userId = intent.getIntExtra("userId", 0);
		
		
		exitButton = (Button)findViewById(R.id.exitButton) ;
		name = (TextView)findViewById(R.id.userName) ;
		addButton = (Button)findViewById(R.id.addButton) ;
		searchButton = (Button)findViewById(R.id.search) ;
		ordersButton = (Button)findViewById(R.id.orders) ;
		submitButton = (Button)findViewById(R.id.submit) ;
		itemId = (EditText)findViewById(R.id.idNumber) ;
		
		exitButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					//在此添加退出事件
				}
			});
		
		addButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					try {
						getItemId() ;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		
		searchButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent intent = new Intent(UserAction.this, SearchActivity.class);
					//intent.putExtra("userId", userId);
					intent.putExtra("storeId", storeId);
					startActivity(intent);
					finish();         //在此添加搜索;跳转到搜索界面
				}
			});
		
		ordersButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					//在此添加历史订单
				}
			});
		
		submitButton.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					//在此添加添加事件
				}
			});
		
		name.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					Intent intent = new Intent(UserAction.this, ChangePsdActivity.class);
					intent.putExtra("userId", userId);
					startActivity(intent);
					finish();                         //修改密码吧，那我跳转啦~~~我跳-我跳-我跳跳跳~~~）
				}
			});
	}
	
	private boolean getItemId() throws Exception {
		EditText idNumText = (EditText)findViewById(R.id.idNumber) ;
		String currItemId = idNumText.getText().toString() ;
		//storeId需要改哦~  ？？《storeId改什么呢？》
		String currUrl = HttpGetThread.BASE_URL + "getItemInfo.action?itemId=" + currItemId + "&storeId=同济" ;
	
	    HttpGetThread test = new HttpGetThread(currUrl) ;
		//test.start();
		//test.join();
	    JSONObject jsonObject = new JSONObject(test.getResult()) ;  
        String t = jsonObject.getString("itemName");  
	    
        TextView temp = (TextView)findViewById(R.id.textView1) ;
		temp.setText(t) ;  
		
		return true ;
	}

	
	
    
}
