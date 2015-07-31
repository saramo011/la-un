package com.app.laundry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.lazyloading.ImageLoader;
import com.app.laundry.util.ProgressDialogClass;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Add_CommentAddress extends ActionBarActivity {
	String AddressID,UserID,AddressName,CityID,CountryID,ContactNo,AddressLine1,AddressLine2,
	AddressLine3,countryName,CityName,DefaultAddress;
	
	JSONObject json,json1,json2;
	ArrayList<String> city=new ArrayList<String>();
	ArrayList<String> country=new ArrayList<String>();
	
	ArrayList<String> city_id=new ArrayList<String>();
	ArrayList<String> country_id=new ArrayList<String>();
	
	Handler mHandler=new Handler();
	
	Spinner sp1,sp2;
	EditText add_name,cont_no, add_lane1, add_lane2, add_lane3;
	Button bt_save;
	ImageView imageView_banner,imageView_small_banner;
	ImageLoader imageLoader;
	String address_position2, address_position1, laundryId, laundryName, laundryArray;
	boolean online_offline;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_edit_address);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);
	    bar.setTitle("Add Address");
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    
		
		add_name=(EditText)findViewById(R.id.AddressName);
		cont_no=(EditText)findViewById(R.id.ContactNo);
		add_lane1=(EditText)findViewById(R.id.AddressLine1);
		add_lane2=(EditText)findViewById(R.id.AddressLine2);
		add_lane3=(EditText)findViewById(R.id.AddressLine3);
		
		TextView tv=(TextView)findViewById(R.id.textView1);
		tv.setText("Add Address");
		
		bt_save=(Button)findViewById(R.id.bt_save);
		
		imageView_banner=(ImageView)findViewById(R.id.imageView_banner);
		imageView_small_banner=(ImageView)findViewById(R.id.imageView_small_banner);
		
		imageView_banner.setVisibility(View.GONE);
		imageView_small_banner.setVisibility(View.GONE);
		
		sp1=(Spinner)findViewById(R.id.spinner1);
		sp2=(Spinner)findViewById(R.id.spinner2);
		
		Intent bundle=getIntent();
		
		AddressID=bundle.getStringExtra("AddressID");
		UserID=bundle.getStringExtra("UserID");
		AddressName=bundle.getStringExtra("AddressName");
		CityID=bundle.getStringExtra("CityID");
		CountryID=bundle.getStringExtra("CountryID");
		ContactNo=bundle.getStringExtra("ContactNo");
		
		AddressLine1=bundle.getStringExtra("AddressLine1");
		AddressLine2=bundle.getStringExtra("AddressLine2");
		AddressLine3=bundle.getStringExtra("AddressLine3");
		countryName=bundle.getStringExtra("countryName");
		CityName=bundle.getStringExtra("CityName");
		
		address_position2=bundle.getStringExtra("address_position2");
		address_position1=bundle.getStringExtra("address_position1");
		laundryId=bundle.getStringExtra("laundryId");
		laundryArray=bundle.getStringExtra("laundryArray");
		laundryName=bundle.getStringExtra("laundryName");
		online_offline=bundle.getBooleanExtra("online_offline", false);
		
		add_name.setText(AddressName);
		cont_no.setText(ContactNo);
		add_lane1.setText(AddressLine1);
		add_lane2.setText(AddressLine2);
		add_lane3.setText(AddressLine3);
		
		bt_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AddressName=add_name.getText().toString();
				
				if(AddressName.length()<2){
					Toast.makeText(Add_CommentAddress.this, "Enter short name!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				AddressLine1=add_lane1.getText().toString();
				if(AddressLine1.length()<4){
					Toast.makeText(Add_CommentAddress.this, "Enter correct Address!", Toast.LENGTH_SHORT).show();	
					return;
				}
				
				ContactNo=cont_no.getText().toString();
				if(ContactNo.length()<5){
					Toast.makeText(Add_CommentAddress.this, "Enter correct contact!", Toast.LENGTH_SHORT).show();	
					return;
				}
				
				AddressLine2=add_lane2.getText().toString();
				AddressLine3=add_lane3.getText().toString();
				
				CityName=sp1.getSelectedItem().toString();
				int city_index=city.indexOf(CityName);
				if(city_index==-1)
					CityID=(city_index+1)+"";
				else
					CityID=city_id.get(city_index);
				
				countryName=sp2.getSelectedItem().toString();
				int country_index=country.indexOf(countryName);
				if(country_index==-1)
					CountryID=(country_index+1)+"";
				else
					CountryID=country_id.get(country_index);
				
				updateDefault();
			}
		});
		
		update_city_country();
	}
	
	protected void updateDefault() {
		// TODO Auto-generated method stub
		ProgressDialogClass.showProgressDialog(Add_CommentAddress.this,"Saving...");
		json=null;
		final Thread update=new Thread(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("AddressName", AddressName));
				params.add(new BasicNameValuePair("ContactNo", ContactNo));
				params.add(new BasicNameValuePair("AddressLine1",AddressLine1));
				params.add(new BasicNameValuePair("AddressLine2", AddressLine2));
				params.add(new BasicNameValuePair("AddressLine3", AddressLine3));
				params.add(new BasicNameValuePair("CityID", CityID));
				params.add(new BasicNameValuePair("CountryID", CountryID));
				
				params.add(new BasicNameValuePair("UserID", Config.userid));
				params.add(new BasicNameValuePair("DefaultAddress", 0+""));
				
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.add_address,"POST", params);
			}
		};
		update.start();
		
		final Thread show=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					update.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				if(mHandler!=null){
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(json!=null){
								try {
									if(json.getInt("status")==200){
										Toast.makeText(Add_CommentAddress.this, "Saved!", Toast.LENGTH_SHORT).show();
										Intent i=new Intent(Add_CommentAddress.this, CommentActivity.class);
										i.putExtra("laundryArray", laundryArray);
										i.putExtra("laundryId", laundryId);
										i.putExtra("laundryName", laundryName);
										i.putExtra("address_position1", address_position1);
										i.putExtra("address_position2", address_position2);
										i.putExtra("online_offline",online_offline );
										
										startActivity(i);
										finish();
										
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
							}
							ProgressDialogClass.dismissProgressDialog();
						}
					});
				}
			}
		});
		show.start();
	}
	
	private void getBanner(){
		imageLoader=new ImageLoader(Add_CommentAddress.this);
		json=null;
		final Thread image_url=new Thread(){
			@Override
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.banner_url,"POST", params);
			}
		};
		image_url.start();
		
		final Thread show=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					image_url.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				if(mHandler!=null){
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								if(json.getInt("status")==200){
									JSONArray j_arr=json.getJSONArray("data");
									
									JSONObject j_obj=j_arr.getJSONObject(0);
									imageLoader.DisplayImage(j_obj.getString("BannerURL"), imageView_banner, false);
									
									JSONObject j_obj1=j_arr.getJSONObject(1);
									imageLoader.DisplayImage(j_obj1.getString("BannerURL"), imageView_small_banner, false);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
						}
					});
				}
			}
		});
		show.start();
	}
	
	private void update_city_country() {
		// TODO Auto-generated method stub
		ProgressDialogClass.showProgressDialog(Add_CommentAddress.this,"Loading...");
		final Thread getdata=new Thread(){
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JGetParsor j=new JGetParsor();
				json1 = j.makeHttpRequest(Config.new_city_url,"POST", params);
				json2 = j.makeHttpRequest(Config.new_country_url,"POST", params);
				if(json1!=null){
					try {
						if(json1.getInt("status")==200){
							JSONArray j_arr=json1.getJSONArray("data");
							for(int i=0; i<j_arr.length();i++){
								JSONObject j_obj=j_arr.getJSONObject(i);
								city.add(j_obj.getString("CityName"));
								city_id.add(j_obj.getString("CityID"));
							}		
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
					
					if(json2!=null){
						try {
							if(json2.getInt("status")==200){
								JSONArray j_arr=json2.getJSONArray("data");
								for(int i=0; i<j_arr.length();i++){
									JSONObject j_obj=j_arr.getJSONObject(i);
									country.add(j_obj.getString("countryName"));
									country_id.add(j_obj.getString("CountryID"));
								}		
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
				}

			}
		};
		getdata.start();
		
		final Thread show=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					getdata.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
				if(mHandler!=null){
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(city.size()>0){
								sp1.setAdapter(new ArrayAdapter<String>(Add_CommentAddress.this, android.R.layout.simple_spinner_dropdown_item, city));
								int index=city.indexOf(CityName);
								if(index>=0)
									sp1.setSelection(index);
							}
							if(country.size()>0){
								sp2.setAdapter(new ArrayAdapter<String>(Add_CommentAddress.this, android.R.layout.simple_spinner_dropdown_item, country));
								int index=country.indexOf(countryName);
								if(index>=0)
									sp2.setSelection(index);
							}
							getBanner();
							ProgressDialogClass.dismissProgressDialog();
						}
					});
				}
			}
		});
		show.start();
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
			  startActivity(new Intent(Add_CommentAddress.this,BaseFragmentActivity.class));
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
		  }  
		  
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i=new Intent(Add_CommentAddress.this, CommentActivity.class);
		i.putExtra("laundryArray", laundryArray);
		i.putExtra("laundryId", laundryId);
		i.putExtra("laundryName", laundryName);
		i.putExtra("address_position1", address_position1);
		i.putExtra("address_position2", address_position2);
		i.putExtra("online_offline",online_offline );
		startActivity(i);
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
		finish();
	}
}
