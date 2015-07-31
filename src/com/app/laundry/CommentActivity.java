package com.app.laundry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.lazyloading.ImageLoader;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;


public class CommentActivity extends ActionBarActivity {
	Handler mHandler = new Handler();
	
	JSONObject json;
	
	String AddressID,UserID,AddressName,CityID,CountryID,ContactNo,AddressLine1,AddressLine2,
	AddressLine3,countryName,CityName,DefaultAddress;
	String address_position1, address_position2;

	Button but_submit;
	String  laundryId,laundryArray,laundryName;
	boolean online_offline;
	
	//ImageView imageView_banner,imageView_small_banner;
	ImageLoader imageLoader;
	ImageView image_edit1, image_edit2;
	
	Spinner sp1,sp2;
	
	EditText et1, et2, et3;
	
	ArrayList<HashMap<String, String>> array_list=new ArrayList<HashMap<String,String>>();
	ArrayList<String> pick_up=new ArrayList<String>();

	// private GoogleMap mMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_pick_up_address);

	//	Log.w("On Comment Activity Page", "running till here");
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);
	    
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    
	  //  imageView_banner=(ImageView)findViewById(R.id.imageView_banner);
		//imageView_small_banner=(ImageView)findViewById(R.id.imageView_small_banner);
		
	    Intent intent = getIntent();

		laundryId = intent.getExtras().getString("LaundryId");
		laundryArray = intent.getExtras().getString("LaundryArray");
		laundryName=intent.getExtras().getString("LaundryName");

		address_position1=intent.getExtras().getString("address_position1");
		address_position2=intent.getExtras().getString("address_position2");
		online_offline=intent.getExtras().getBoolean("LaundryOffline");
		
		bar.setTitle("Confirm Address");
		
		sp1=(Spinner)findViewById(R.id.spinner1);
		sp2=(Spinner)findViewById(R.id.spinner2);
		//Log.w("On Comment Activity Page 2", "running till here");
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				String sp_name=sp1.getSelectedItem().toString();
				
				if(sp_name.equals("new")){
					

					Intent i=new Intent(CommentActivity.this, Add_CommentAddress.class );
					
					i.putExtra("address_position1", 0+"");
					i.putExtra("address_position2", 0+"");
					
					i.putExtra("laundryId",laundryId );
					i.putExtra("laundryArray",laundryArray );
					i.putExtra("laundryName",laundryName );
					i.putExtra("online_offline",online_offline );

					startActivity(i);
					finish();
					return;
					
				}
				
				String full_address="";
				
				if(array_list.get(position).get("AddressLine1").length()>2)
					full_address=array_list.get(position).get("AddressLine1")+", ";
				
				if(array_list.get(position).get("AddressLine2").length()>2)
					full_address+=array_list.get(position).get("AddressLine2")+", ";
				
				if(array_list.get(position).get("AddressLine3").length()>2)
					full_address+=array_list.get(position).get("AddressLine3")+", ";
				
				if(array_list.get(position).get("CityName").length()>2)
					full_address+=array_list.get(position).get("CityName")+", ";
				
				if(array_list.get(position).get("countryName").length()>2)
					full_address+=array_list.get(position).get("countryName");
				
				et1.setText(full_address);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		sp2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				String sp_name=sp2.getSelectedItem().toString();
				
				if(sp_name.equals("new")){
					

					Intent i=new Intent(CommentActivity.this, Add_CommentAddress.class );
					
					i.putExtra("address_position1", 0+"");
					i.putExtra("address_position2", 0+"");
					
					i.putExtra("laundryId",laundryId );
					i.putExtra("laundryArray",laundryArray );
					i.putExtra("laundryName",laundryName );
					i.putExtra("online_offline",online_offline );

					startActivity(i);
					finish();
					return;
					
				}
				
				String full_address2="";
				
				if(array_list.get(position).get("AddressLine1").length()>2)
					full_address2=array_list.get(position).get("AddressLine1")+", ";
				
				if(array_list.get(position).get("AddressLine2").length()>2)
					full_address2+=array_list.get(position).get("AddressLine2")+", ";
				
				if(array_list.get(position).get("AddressLine3").length()>2)
					full_address2+=array_list.get(position).get("AddressLine3")+", ";
				
				if(array_list.get(position).get("CityName").length()>2)
					full_address2+=array_list.get(position).get("CityName")+", ";
				
				if(array_list.get(position).get("countryName").length()>2)
					full_address2+=array_list.get(position).get("countryName");
				
				et2.setText(full_address2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		et1=(EditText)findViewById(R.id.editText1);
		et2=(EditText)findViewById(R.id.editText2);
		et3=(EditText)findViewById(R.id.editText3);
		
		et1.setEnabled(false);
		et2.setEnabled(false);
		et1.setTextColor(Color.parseColor("#A4A1A1"));
		et2.setTextColor(Color.parseColor("#A4A1A1"));
		
		//Log.w("On Comment Activity Page 3", "running till here");
		
		SharedPreferences prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
		Config.name=prefs.getString("name", "");
		Config.isBookingService=false;

		Button button_submit = (Button) findViewById(R.id.button1);
		
		image_edit1=(ImageView)findViewById(R.id.imageViewEdit1);
		image_edit1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int address_position1=sp1.getSelectedItemPosition();
				int address_position2=sp2.getSelectedItemPosition();
				
				AddressID=array_list.get(address_position1).get("AddressID");
				UserID=array_list.get(address_position1).get("UserID");
				AddressName=array_list.get(address_position1).get("AddressName");
				CityID=array_list.get(address_position1).get("CityID");
				CountryID=array_list.get(address_position1).get("CountryID");
				ContactNo=array_list.get(address_position1).get("ContactNo");
				AddressLine1=array_list.get(address_position1).get("AddressLine1");
				AddressLine2=array_list.get(address_position1).get("AddressLine2");
				AddressLine3=array_list.get(address_position1).get("AddressLine3");
				countryName=array_list.get(address_position1).get("countryName");
				CityName=array_list.get(address_position1).get("CityName");
				
				Intent i=new Intent(CommentActivity.this, EditAddressComment.class );
				
				i.putExtra("address_position1", address_position1+"");
				i.putExtra("address_position2", address_position2+"");
				
				i.putExtra("AddressID", AddressID);
				i.putExtra("UserID", UserID);
				i.putExtra("AddressName", AddressName);
				i.putExtra("CityID", CityID);
				i.putExtra("CountryID", CountryID);
				i.putExtra("ContactNo", ContactNo);
				i.putExtra("AddressLine1", AddressLine1);
				i.putExtra("AddressLine2", AddressLine2);
				i.putExtra("AddressLine3",AddressLine3 );
				i.putExtra("countryName", countryName);
				i.putExtra("CityName",CityName );
				
				i.putExtra("laundryId",laundryId );
				i.putExtra("laundryArray",laundryArray );
				i.putExtra("laundryName",laundryName );
				i.putExtra("online_offline",online_offline );

				startActivity(i);
				finish();
				
			}
		});
		
		image_edit2=(ImageView)findViewById(R.id.imageViewEdit2);
		image_edit2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int address_position1=sp1.getSelectedItemPosition();
				int address_position2=sp2.getSelectedItemPosition();
				
				AddressID=array_list.get(address_position2).get("AddressID");
				UserID=array_list.get(address_position2).get("UserID");
				AddressName=array_list.get(address_position2).get("AddressName");
				CityID=array_list.get(address_position2).get("CityID");
				CountryID=array_list.get(address_position2).get("CountryID");
				ContactNo=array_list.get(address_position2).get("ContactNo");
				AddressLine1=array_list.get(address_position2).get("AddressLine1");
				AddressLine2=array_list.get(address_position2).get("AddressLine2");
				AddressLine3=array_list.get(address_position2).get("AddressLine3");
				countryName=array_list.get(address_position2).get("countryName");
				CityName=array_list.get(address_position2).get("CityName");
				
				
				Intent i=new Intent(CommentActivity.this, EditAddressComment.class );
				
				i.putExtra("address_position2", address_position2+"");
				i.putExtra("address_position1", address_position1+"");
				i.putExtra("AddressID", AddressID);
				i.putExtra("UserID", UserID);
				i.putExtra("AddressName", AddressName);
				i.putExtra("CityID", CityID);
				i.putExtra("CountryID", CountryID);
				i.putExtra("ContactNo", ContactNo);
				i.putExtra("AddressLine1", AddressLine1);
				i.putExtra("AddressLine2", AddressLine2);
				i.putExtra("AddressLine3",AddressLine3 );
				i.putExtra("countryName", countryName);
				i.putExtra("CityName",CityName );
				i.putExtra("laundryId",laundryId );
				i.putExtra("laundryArray",laundryArray );
				i.putExtra("laundryName",laundryName );
				i.putExtra("online_offline",online_offline );
				
				startActivity(i);
				finish();
				
			}
		});
		

		button_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Booking();
				
				
			}
		});
		
		if (Network.HaveNetworkConnection(CommentActivity.this)) {
			getAllAddresses();
		} else {
			final AlertUtil alert = new AlertUtil();
			alert.confirmationAlert(CommentActivity.this,
					getResources().getString(R.string.network_title),
					getResources().getString(R.string.network_message),
					new Button.OnClickListener() {

						@Override
						public void onClick(View v) {
							alert.release();
							onBackPressed();
						}
					});

		}

	}
	
	void getAllAddresses(){
		ProgressDialogClass.showProgressDialog(CommentActivity.this,"Loading...");
		
		final Thread fetch_address=new Thread(){
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.manage_addresses+Config.userid,"POST", params);
				if(json!=null){
					try {
						Log.e("json",json.toString());
						if(json.getInt("status")==200){
							JSONArray json_array=json.getJSONArray("data");
							for(int i=0; i<json_array.length();i++){
								HashMap<String, String> hashmap=new HashMap<String, String>();
								JSONObject j_obj=json_array.getJSONObject(i);
								
								hashmap.put("AddressID", j_obj.getString("AddressID"));
								hashmap.put("UserID", j_obj.getString("UserID"));
								hashmap.put("AddressName", j_obj.getString("AddressName"));
								hashmap.put("CityID", j_obj.getString("CityID"));
								hashmap.put("CountryID", j_obj.getString("CountryID"));
								hashmap.put("ContactNo", j_obj.getString("ContactNo"));
								
								hashmap.put("AddressLine1", j_obj.getString("AddressLine1"));
								hashmap.put("AddressLine2", j_obj.getString("AddressLine2"));
								hashmap.put("AddressLine3", j_obj.getString("AddressLine3"));
								hashmap.put("countryName", j_obj.getString("countryName"));
								hashmap.put("CityName", j_obj.getString("CityName"));
								hashmap.put("DefaultAddress", j_obj.getString("DefaultAddress"));
								
								pick_up.add(j_obj.getString("AddressName"));
								array_list.add(hashmap);
								
							}
							
						}
						pick_up.add("new");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
					}
				}
			}
		};
		fetch_address.start();
		
		final Thread display=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					fetch_address.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
				if(mHandler!=null){
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							sp1.setAdapter(new ArrayAdapter<String>(CommentActivity.this, android.R.layout.simple_spinner_dropdown_item, pick_up));
							sp2.setAdapter(new ArrayAdapter<String>(CommentActivity.this, android.R.layout.simple_spinner_dropdown_item, pick_up));

							if(pick_up.size()>1){
								
							
							String full_address="";
							
							if(array_list.get(0).get("AddressLine1").length()>1)
								full_address=array_list.get(0).get("AddressLine1")+", ";
							
							if(array_list.get(0).get("AddressLine2").length()>1)
								full_address+=array_list.get(0).get("AddressLine2")+", ";
							
							if(array_list.get(0).get("AddressLine3").length()>1)
								full_address+=array_list.get(0).get("AddressLine3")+", ";
							
							if(array_list.get(0).get("CityName").length()>1)
								full_address+=array_list.get(0).get("CityName")+", ";
							
							if(array_list.get(0).get("countryName").length()>1)
								full_address+=array_list.get(0).get("countryName");
									
							et1.setText(full_address);
							et2.setText(full_address);
							
							if(address_position1!=null){
								int position=Integer.parseInt(address_position1);
								sp1.setSelection(position);
								if(array_list.get(position).get("AddressLine1").length()>1)
									full_address=array_list.get(position).get("AddressLine1")+", ";
								
								if(array_list.get(position).get("AddressLine2").length()>1)
									full_address+=array_list.get(position).get("AddressLine2")+", ";
								
								if(array_list.get(position).get("AddressLine3").length()>1)
									full_address+=array_list.get(position).get("AddressLine3")+", ";
								
								if(array_list.get(position).get("CityName").length()>1)
									full_address+=array_list.get(position).get("CityName")+", ";
								
								if(array_list.get(position).get("countryName").length()>1)
									full_address+=array_list.get(position).get("countryName");
										
								et1.setText(full_address);
							 }
							
							if(address_position2!=null){
								int position=Integer.parseInt(address_position2);
								sp2.setSelection(position);
								if(array_list.get(position).get("AddressLine1").length()>1)
									full_address=array_list.get(position).get("AddressLine1")+", ";
								
								if(array_list.get(position).get("AddressLine2").length()>1)
									full_address+=array_list.get(position).get("AddressLine2")+", ";
								
								if(array_list.get(position).get("AddressLine3").length()>1)
									full_address+=array_list.get(position).get("AddressLine3")+", ";
								
								if(array_list.get(position).get("CityName").length()>1)
									full_address+=array_list.get(position).get("CityName")+", ";
								
								if(array_list.get(position).get("countryName").length()>1)
									full_address+=array_list.get(position).get("countryName");
										
								et2.setText(full_address);
							 }
							}
							
						//	getBanner();
							ProgressDialogClass.dismissProgressDialog();
						}
					});
				}
				
			}
		});
		display.start();
	}
	
	
	public void Booking() {
		json=null;
		 ProgressDialogClass.showProgressDialog(CommentActivity.this, "Loading...");

		final Thread splashTread = new Thread() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("laundry_id",laundryId));
				
				nameValuePairs.add(new BasicNameValuePair("user_id",Config.userid));
				nameValuePairs.add(new BasicNameValuePair("cur_lat",Config.latitude));
				nameValuePairs.add(new BasicNameValuePair("cur_long",Config.longitude));
				
				String comment=et3.getText().toString();
				nameValuePairs.add(new BasicNameValuePair("comment", comment));
				
				int p_index=sp1.getSelectedItemPosition();
				String p_id=array_list.get(p_index).get("AddressID");
				
				int d_index=sp2.getSelectedItemPosition();
				String d_id=array_list.get(d_index).get("AddressID");
				
				
				nameValuePairs.add(new BasicNameValuePair("pick_up_id", p_id));
				nameValuePairs.add(new BasicNameValuePair("drop_off_id", d_id));

				
				String url = Config.Order_Offline_Url;
				if(!online_offline) {
					nameValuePairs.add(new BasicNameValuePair("orders", laundryArray));
					url = Config.Order_Online_Url;
				}
				
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(url,"POST", nameValuePairs);
			
			}
		};
		splashTread.start();

		final Thread displayThread = new Thread(new Runnable() {
			public void run() {

				try {
					splashTread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (mHandler != null) {
					mHandler.post(new Runnable() {
						public void run() {
							// ProgressDialogClass.dismissProgressDialog();
							if (json != null) {
									try {
										
											if (json.getString("status")
													.equals("200")) {
												
												onBackPressed();
												Config.isBookingService=true;
												Toast.makeText(CommentActivity.this, json.getString(
														"stauts_message"),Toast.LENGTH_LONG).show();
											}
											else{
											AlertUtil alert=new AlertUtil();
											alert.messageAlert(
													CommentActivity.this,"",json.getString(
															"stauts_message"));
											}
										
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
							ProgressDialogClass.dismissProgressDialog();

						}

					});
				}
			}
		});
		displayThread.start();

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
			  startActivity(new Intent(CommentActivity.this,
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
