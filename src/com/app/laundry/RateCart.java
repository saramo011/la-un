package com.app.laundry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.util.ProgressDialogClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class RateCart extends ActionBarActivity{
	String laundryId="";
	ListView lv;
	ArrayList<HashMap<String, String>> all_list;

	JSONArray searchResult = new JSONArray();
	JSONObject jsonObj=new JSONObject();
	JSONObject json;
	Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		
		setContentView(R.layout.ratecard);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);
		bar.setTitle("Rate Card");
		
		lv=(ListView)findViewById(R.id.listView1);
		Intent intent = getIntent();
		laundryId = intent.getExtras().getString("LaundryID");
		//Toast.makeText(this, "laundryId:"+laundryId, Toast.LENGTH_LONG).show();
		all_list=new ArrayList<HashMap<String,String>>();

		new loginAccess().execute();
	
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
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
			  startActivity(new Intent(RateCart.this,
						BaseFragmentActivity.class));
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
		  } 
		return super.onOptionsItemSelected(item);
	}
	
	
	class loginAccess extends AsyncTask<String, String, String> {

		protected void onPreExecute() {
			ProgressDialogClass.showProgressDialog(RateCart.this,
					"Loading...");
		}
		@Override
		protected String doInBackground(String... arg0) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("laundryid", laundryId));
			JGetParsor j=new JGetParsor();
			JSONObject json = j.makeHttpRequest(Config.Ratecard_Url,"GET", params);
			
			String check="failed";

			try {
				int success = json.getInt("status");
				if (success == 200) 
				 {	
					 check="Success";
					 JSONArray list_array=json.getJSONArray("data");
					 Config.Item_Array.clear();
					 Config.Service_Array.clear();
					 Config.Amount_Array.clear();
					 for(int i=0; i<list_array.length(); i++)
					  {
						  JSONObject jo=list_array.getJSONObject(i);
						  String item=jo.getString("LaundryItemName");
						  String service=jo.getString("LaundryServiceName");
						  String amount=jo.getString("Amount");
						 
						 // String rate_id=jo.getString("LaundryRateID");
						 // int index=Integer.parseInt(rate_id);
						  Config.Item_Array.add(i,item);
						  Config.Service_Array.add(i,service);
						  Config.Amount_Array.add(i,amount);
					  }
				 }
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return check;
		}
		protected void onPostExecute(String file_url) {
			int index=0;
			String item="";
			String service="";
			String rate="";

			
			for(int i=0; i<Config.Item_Array.size();i++){
				item=Config.Item_Array.get(i);
				
				
				if(i != (Config.Item_Array.size()-1) && item.equals(Config.Item_Array.get(i+1))){
					service+="\n"+Config.Service_Array.get(i);
					rate+="\n"+Config.Amount_Array.get(i);
				}else{
					service+="\n"+Config.Service_Array.get(i)+"\n";
					rate+="\n"+Config.Amount_Array.get(i)+"\n";
					
					HashMap<String, String> map=new HashMap<String, String>();
					//item=Config.Item_Array.get(i);
					map.put("textView_item", item);
					map.put("textView_service", service);
					map.put("textView_amnt", rate);
					
					all_list.add(map);
					
					service="";
					rate="";
				}
				
			}
			
			//
			ListAdapter adapter=new SimpleAdapter(RateCart.this, all_list, R.layout.ratecard_items, new String[]{"textView_item", "textView_service", "textView_amnt"}, new int[]{R.id.textView_item, R.id.textView_service, R.id.textView_amnt});
			lv.setAdapter(adapter);
			ProgressDialogClass.dismissProgressDialog();
		}
		
	  }
}

