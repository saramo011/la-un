package com.app.laundry;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class PolicyActivity extends ActionBarActivity {

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy);
		
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);
	    bar.setTitle("Privacy Policy");
		
		
		WebView webView1=(WebView)findViewById(R.id.webView1);
		webView1.loadUrl("file:///android_asset/privacypolicy.html");
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_menu, menu);	
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		  if (id==android.R.id.home){
	        onBackPressed();
	        return true;
		
	    }
		 else if(id==R.id.direct_to_home)
		  {
			  startActivity(new Intent(PolicyActivity.this,
						BaseFragmentActivity.class));
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
		  } 
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

}
