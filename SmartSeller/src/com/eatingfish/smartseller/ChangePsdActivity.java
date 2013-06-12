package com.eatingfish.smartseller;

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
import org.json.JSONObject;

import com.eatingfish.smartseller.MainActivity.LoginThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePsdActivity extends Activity {

	Button changeBt;
	Button backBt;
	EditText originalPsdEt;
	EditText newPsdEt;
	EditText rePsdEt;
	String originalPsd;
	String newPsd;
	String rePsd;
	Toast toast;
	Handler handler;
	String responseMsg="";
	int userId;
	static final int REQUEST_TIMEOUT = 5*1000;
	static final int SO_TIMEOUT = 10*1000;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepsd);
		
		Intent intent = getIntent();  
		userId = intent.getIntExtra("userId", 0); 
		
		originalPsdEt = (EditText) findViewById(R.id.eT_OriginalPsd);
		newPsdEt = (EditText) findViewById(R.id.eT_NewPsd);
		rePsdEt = (EditText) findViewById(R.id.eT_RePsd);
		changeBt = (Button) findViewById(R.id.Bt_Change);
		backBt = (Button) findViewById(R.id.Bt_Back);

		changeBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 修改密码
				originalPsd = originalPsdEt.getText().toString().trim();
				newPsd = newPsdEt.getText().toString().trim();
				rePsd = rePsdEt.getText().toString().trim();
				if(originalPsd == null || newPsd == null || rePsd == null){
					toast = Toast.makeText(getApplicationContext(),"所填信息不能为空", Toast.LENGTH_LONG);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				}
				else if(!newPsd.equals(rePsd)){
					toast = Toast.makeText(getApplicationContext(),"两次新密码不一致", Toast.LENGTH_LONG);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				}
				else if(newPsd.length() < 4 || newPsd.length() > 20){
					toast = Toast.makeText(getApplicationContext(),"新密码长度应介于4-20之间", Toast.LENGTH_LONG);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				}
				else{
					Thread changePsdThread = new Thread(new ChangePsdThread());
					changePsdThread.start();
				}
					
			}

		});
		handler = new Handler(){
			public void handleMessage(Message msg){
				System.out.println("the message what is" + msg.what);
				switch(msg.what){
				case 0:
					toast = Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_LONG);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
					break;
				case 1:
					toast = Toast.makeText(getApplicationContext(),"修改失败，密码错误", Toast.LENGTH_LONG);
				    toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				    break;
				}
			}
		};
		
		
		backBt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 // TODO Auto-generated method stub   返回主界面
				
			}
			
		});

	}
	class ChangePsdThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			boolean changeValidate = changeServer(originalPsd, newPsd, rePsd);
			System.out.println("changeServer gets the result:"+changeValidate);
			if(changeValidate){
				Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
			}
			else{
				Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
			}
				 
			
		}
		
	}
	public boolean changeServer(String o, String n, String r ){
		String urlStr = "http://192.168.1.2:8088/ServerManagement/ChangePsdServlet.action";
		HttpPost request = new HttpPost(urlStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", String.valueOf(userId)));
		params.add(new BasicNameValuePair("originalPsd",o));
		params.add(new BasicNameValuePair("newPsd",n));
		params.add(new BasicNameValuePair("rePsd",r));
		try{
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			 if(response.getStatusLine().getStatusCode()==200)
	            {
	             //获得响应信息       
                 String responseMsg = (EntityUtils.toString(response.getEntity(), "UTF-8")).trim(); 
                 
            	 // JSONObject jsonObject = new JSONObject(out);  
                 //responseMsg = jsonObject.getString("result"); 				 
				 // storeId = jsonObject.getInt("storeId");
	                
	                System.out.println("result is:" + responseMsg);
	                if(responseMsg.equals("success"))
	                	 return true;
	       
	                else
	                	return false;
	            }
			 else
				 return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}
	public HttpClient getHttpClient(){
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
	
	
	
	
	
	
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_psd, menu);
		return true;
	}

}
