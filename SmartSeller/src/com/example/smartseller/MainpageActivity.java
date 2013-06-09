package com.example.smartseller;

import com.example.smartseller.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MainpageActivity extends Activity {
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);
		Intent intent = getIntent();  
        String userName = intent.getStringExtra("userName");  
        int storeId = intent.getIntExtra("storeId",0); 
        System.out.println("yes I get the values:" + userName + storeId);
      //  textView = (TextView)findViewById(R.id.show);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainpage, menu);
		return true;
	}

}
