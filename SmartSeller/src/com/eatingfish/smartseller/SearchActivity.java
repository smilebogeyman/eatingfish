package com.eatingfish.smartseller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends Activity {
	Spinner spinCato;
	RadioButton rBSearchById;
	RadioButton rBSearchByCato;
	Button bTSearch;
	EditText eTId;
	ListView lVItem;
	private Toast toast;
	private ArrayAdapter<String> adapter; 
	private static final int REQUEST_TIMEOUT = 5 * 1000;
	private static final int SO_TIMEOUT = 10 * 1000;
	private List<String> catoList = new ArrayList<String>();
	private String selectedCatoName;
	private int storeId;
	private JSONArray ja;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
        Intent intent = getIntent();  
		
         //获得前一个页面的userName和storeId
        storeId = intent.getIntExtra("storeId", 0); 
		
		eTId = (EditText)findViewById(R.id.eTId);
		
		spinCato = (Spinner)findViewById(R.id.spinCato);
		rBSearchById = (RadioButton)findViewById(R.id.rBSearchById);
		rBSearchByCato = (RadioButton)findViewById(R.id.rBSearchByCato);
		lVItem = (ListView)findViewById(R.id.listView1);
		
		Thread thread = new Thread(new GetCato());
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, catoList);		
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);        
        spinCato.setAdapter(adapter);
        
		bTSearch = (Button)findViewById(R.id.btSearch);
		bTSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(rBSearchById.isChecked() == true ){
					if(eTId.getText().toString().equals("")){
					toast = Toast.makeText(getApplicationContext(), "id不能为空",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					}
					else{
						// 向服务器请求
					}
				}
				else if(rBSearchByCato.isChecked() == true ){
					if(spinCato.getSelectedItem() == null){
					toast = Toast.makeText(getApplicationContext(), "id不能为空",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					}
					else{
						 selectedCatoName = spinCato.getSelectedItem().toString();
						Thread getItemThread = new Thread(new GetItemByCato());
						 getItemThread.start();
						 try {
							getItemThread.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							showListView();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 获取数据后显示出来
					}
				}
			}
			
		});
/*		String[] mForm = new String[] { "title1", "title2", "time" };
		int[] mTo = new int[] { R.id.title1, R.id.title2, R.id.time };
		List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
		Map<String, Object> mMap = null;
		for (int i = 0; i < 10; i++) {
			mMap = new HashMap<String, Object>();
			mMap.put("title1", "标题");
			mMap.put("title2", "副标题");
			mMap.put("time", "2011-08-15 09:00");
			mList.add(mMap);
		}
		// 创建适配器
		SimpleAdapter mAdapter = new SimpleAdapter(this, mList, R.layout.items,
				mForm, mTo);
		lVItem.setAdapter(mAdapter);*/
        
		
		
	}
	private void showListView() throws JSONException{
        String[] mForm = new String[] { "itemName", "itemCato", "itemPrice","itemDiscount","itemStock","itemNumber","itemGift","itemGiftNum" };
    		int[] mTo = new int[] { R.id.itemName, R.id.itemCato, R.id.itemPrice, R.id.itemDiscount, R.id.itemStock, R.id.itemNumber, R.id.itemGift, R.id.itemGiftNum };
    		List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
    		Map<String, Object> mMap = null;
    		for (int i = 0; i < ja.length(); i++) {
    			mMap = new HashMap<String, Object>();
    			mMap.put("itemName", ja.getJSONObject(i).get("itemname"));
    			mMap.put("itemCato", selectedCatoName);
    			mMap.put("itemPrice", String.valueOf(ja.getJSONObject(i).get("itemprice")));
    			mMap.put("itemDiscount", ja.getJSONObject(i).get("discount"));
    			mMap.put("itemStock", ja.getJSONObject(i).get("stock"));
    			mMap.put("itemNumber", ja.getJSONObject(i).get("number"));
    			mMap.put("itemGift", ja.getJSONObject(i).get("giftId"));
    			mMap.put("itemGiftNum", ja.getJSONObject(i).get("giftnum"));
    			mList.add(mMap);
    		}
    		// 创建适配器
    		SimpleAdapter mAdapter = new SimpleAdapter(this, mList, R.layout.items,
    				mForm, mTo);
    		lVItem.setAdapter(mAdapter); 
	}
	class GetItemByCato implements Runnable{
		public void run(){
			GetItemByCato();
		}
	}
	private void GetItemByCato(){
		String urlStr = "http://192.168.1.2:8088/ServerManagement/GetItem.action";
		HttpPost request = new HttpPost(urlStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("catoName", selectedCatoName));
		params.add(new BasicNameValuePair("itemId",""));
		params.add(new BasicNameValuePair("storeId", String.valueOf(storeId)));
		try{
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			
			 if(response.getStatusLine().getStatusCode()==200){   //获得响应信息  
				 
                 String responseMsg = EntityUtils.toString(response.getEntity(), "UTF-8"); 
	             ja = new JSONArray(responseMsg);  // 获得返回数据之后，显示在listview中
	             System.out.println(ja.getJSONObject(0).get("stock"));
	             

	             
	             
	             
	             
	             
	             }
		}catch(Exception e){
			System.out.println("catch");
			e.printStackTrace();

		}	
		
	}
	
	
	class GetCato implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			GetCato();	
		}	
	}
	private void GetCato(){
		String urlStr = "http://192.168.1.2:8088/ServerManagement/GetCatoServlet.action";
		HttpPost request = new HttpPost(urlStr);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", "userName"));
		try{
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			 if(response.getStatusLine().getStatusCode()==200)
	            {
	             //获得响应信息       
                 String responseMsg = EntityUtils.toString(response.getEntity(), "UTF-8"); 
	             JSONArray ja0 = new JSONArray(responseMsg);
	             for(int i = 0; i < ja0.length(); i++){
	            	 catoList.add(ja0.getJSONObject(i).getString("catoName"));
	            	 
	             }
	             
	             
	            }
	
		}catch(Exception e){
			e.printStackTrace();

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
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
