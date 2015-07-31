package com.app.laundry;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.json.JsonReturn;
import com.app.laundry.lazyloading.ImageLoader;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;

public class LaundryBookingActivity extends ActionBarActivity implements OnItemSelectedListener {

	
	ArrayList<String> serviceArray=new ArrayList<String>();
	ArrayList<String> laundryArray=new ArrayList<String>();
	ArrayList<HashMap<String, String>> bookingArray=new ArrayList<HashMap<String,String>>();
	
	ArrayList<HashMap<String, String>> l_items=new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, String>> s_items=new ArrayList<HashMap<String,String>>();
	
	JSONObject jsonObj=new JSONObject();
	Handler mHandler = new Handler();
	TextView textView_laundry_name;
	TextView textView_laundry_review;
	
	String laundryId,strlaundryName;
	TextView textView_detail;
	TextView textView_laundry_vote;
	Button button_add;
	
	JSONObject json;
	
	 Spinner spinner_service;
	 Spinner spinner_laundry;
	
	 EditText spinner_quantity;
	 private EfficientAdapter list_ed;
	 ListView listView_booking;
	 ImageView imageView_bannerdsa,plus,minus;
	 ImageLoader imageLoader;
	
	 ArrayList<JSONObject> bannerArray = new ArrayList<JSONObject>();
	 
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking);

		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);

		 this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//imageView_banner = (ImageView) findViewById(R.id.imageView_banner);
		listView_booking = (ListView) findViewById(R.id.listView1);
		list_ed = new EfficientAdapter(LaundryBookingActivity.this);
		listView_booking.setAdapter(list_ed);
		// final String[] quantity_items = new String[] { "1", "2","3", "4","5", "6","7", "8","9", "10","11", "12","13", "14","15", "16","17", "18","19", "20"};
		spinner_service = (Spinner) findViewById(R.id.spinner_laundry2);
		spinner_laundry = (Spinner) findViewById(R.id.spinner_laundry);
		
		spinner_quantity = (EditText) findViewById(R.id.spinner_laundry3);
		
		spinner_laundry.setOnItemSelectedListener(this);
		
		minus = (ImageView) findViewById(R.id.imageView121);
		minus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String number=spinner_quantity.getText().toString();
					
					if(number.length()>9)
					{
						Toast.makeText(LaundryBookingActivity.this, "Too much Quantity.",Toast.LENGTH_SHORT).show();
						return;
					}
					
					int value=Integer.parseInt(number);
					if(value>=2)
					{
						value--;
						spinner_quantity.setText(""+value);
					}
					else
					{
						//do nothing right now
					}
				}
			});
			
		plus = (ImageView) findViewById(R.id.imageView2);
		plus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String number=spinner_quantity.getText().toString();
					
					if(number.length()>9)
					{
						Toast.makeText(LaundryBookingActivity.this, "Too much Quantity.",Toast.LENGTH_SHORT).show();
						return;
					}
					
					int value=Integer.parseInt(number);
					value++;
					spinner_quantity.setText(""+value);
				}
			});
		
		textView_laundry_name = (TextView) findViewById(R.id.textView_laundry_name);

		button_add = (Button) findViewById(R.id.button_add);
	//    final Button	btn_finish_order = (Button) findViewById(R.id.btn_order_finish);
		Intent intent = getIntent();
		final String laundryName = intent.getExtras().getString("LaundryName");
		bar.setTitle(laundryName);
		laundryId=intent.getExtras().getString("LaundryId");

		textView_laundry_name.setText("Fill Order Details");
		Button button_submit = (Button) findViewById(R.id.button_submit);
		button_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//startActivity(new Intent(laundryDetailActivity.this,
					//	RegisterActivity.class));
				if(bookingArray.size()!=0){
				JSONArray jArray = new JSONArray();
				for (int i = 0; i < bookingArray.size(); i++) {
					//Log.w("Putting detail for another page: "+i, "running till here");
						JSONObject ticketjson = null;
						try {
							ticketjson = new JSONObject();
							ticketjson.put(
									"LaundryItemID",
									bookingArray.get(i).get(
											"LaundryItemID"));
							ticketjson.put(
									"LaundryServiceID",
									bookingArray.get(i).get(
											"LaundryServiceID"));
							ticketjson.put(
									"Amount",
									bookingArray.get(i).get(
											"Amount"));
							ticketjson.put(
									"Quantity",
									bookingArray.get(i).get(
											"Quantity"));
							

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						jArray.put(ticketjson);
						//Toast.makeText(LaundryBookingActivity.this, jArray.toString(),Toast.LENGTH_LONG).show();
				}
				
				
				Intent intent=new Intent(LaundryBookingActivity.this,
						CommentActivity.class);
				Log.w("Putting detail for another page", "running till here");
				intent.putExtra("LaundryId", laundryId);
				intent.putExtra("LaundryArray", jArray.toString());
				intent.putExtra("LaundryName", laundryName);
				
				
				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				}
				else{
					//Toast.makeText(LaundryBookingActivity.this, "Please add atleast one item.",Toast.LENGTH_LONG).show();
				}
			}
		});
		
		button_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				add_button();
			}
		});
		
		
		final AlertUtil alert = new AlertUtil();
		if (Network.HaveNetworkConnection(LaundryBookingActivity.this)) {
			//getLaundryDetail("");
			//new loginAccess().execute();
			update_laundry_items();
			imageLoader = new ImageLoader(LaundryBookingActivity.this
					.getApplicationContext());
				float ppi = getResources().getDisplayMetrics().density;
				 int height = (int) (60 * ppi);
				// imageView_banner.getLayoutParams().height = height;
			
		} else {
			alert.confirmationAlert(LaundryBookingActivity.this, getResources()
					.getString(R.string.network_title), getResources()
					.getString(R.string.network_message),
					new Button.OnClickListener() {

						@Override
						public void onClick(View v) {
							alert.release();
							onBackPressed();
						}
					});
		}
		// Use AsyncTask execute Method To Prevent ANR Problem
		// tryLogin("", "");
		// postData();

	}
	
	public void add_button(){
		
		String qty=spinner_quantity.getText()+"";
		
		if(qty.length()>9)
		{
			Toast.makeText(LaundryBookingActivity.this, "Too much Quantity.",Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(Integer.parseInt(qty)==0)
		{
			Toast.makeText(LaundryBookingActivity.this, "Please add Quantity.",Toast.LENGTH_SHORT).show();
			return;
		}
		
		String temp_item=spinner_laundry.getSelectedItem().toString();
		String temp_service=spinner_service.getSelectedItem().toString();
		
		boolean flag=false;
		
		for (int i = 0; i < bookingArray.size(); i++) {
			HashMap< String, String> tempHashMap=bookingArray.get(i);
			
			if(tempHashMap.get("Item").equals(temp_item) && tempHashMap.get("Service").equals(temp_service)){
				//Toast.makeText(LaundryBookingActivity.this, "Entered in loop.",Toast.LENGTH_SHORT).show();
				int quantity=Integer.parseInt(qty)+Integer.parseInt(tempHashMap.get("Quantity"));
				if(quantity<1)
				{
					//Toast.makeText(LaundryBookingActivity.this, "Too much Quantity.",Toast.LENGTH_SHORT).show();
					return;
				}
				
				tempHashMap.put("Quantity", quantity+"");
				bookingArray.remove(i);
				bookingArray.add(i, tempHashMap);
				flag=true;
				break;
			}
		}

		if(!flag){
			HashMap<String, String> tempHash=new HashMap<String, String>();
			tempHash.put("Item",temp_item );
			tempHash.put("Service", temp_service);
			
			int item_index=spinner_laundry.getSelectedItemPosition();
			int service_index=spinner_service.getSelectedItemPosition();
			
			String amount=s_items.get(service_index).get("Amount");
			float value=Float.parseFloat(amount);
			int dhs=(int)value;
			
			tempHash.put("LaundryItemID",l_items.get(item_index).get("LaundryItemID"));
			tempHash.put("LaundryServiceID", s_items.get(service_index).get("LaundryServiceID"));
			
			tempHash.put("DHS", dhs+"");
			tempHash.put("Quantity", qty);
			tempHash.put("Amount", amount);
			
			
			bookingArray.add(tempHash);
		}
		
		list_ed.notifyDataSetChanged();
	}
	
	//New fetch data
	private void update_laundry_items() {
		// TODO Auto-generated method stub
		ProgressDialogClass.showProgressDialog(LaundryBookingActivity.this,"Loading...");
		final Thread getdata=new Thread(){
			@SuppressWarnings("deprecation")
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.laundry_booking_data+laundryId,"POST", params);
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
							if(json!=null){
								try {
									if(json.getInt("status")==200){
										
										JSONArray arr=json.getJSONArray("data");
										for(int i=0;i<arr.length();i++){
											JSONObject obj=arr.getJSONObject(i);
											HashMap<String, String> map=new HashMap<String, String>();
											map.put("LaundryItemID", obj.getString("LaundryItemID"));
											map.put("LaundryItemName", obj.getString("LaundryItemName"));
											l_items.add(map);
											laundryArray.add(obj.getString("LaundryItemName"));
										}
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
								}
							}
							
							spinner_laundry
							.setAdapter(new ArrayAdapter<String>(LaundryBookingActivity.this,
							android.R.layout.simple_spinner_dropdown_item,
							laundryArray));
							
							ProgressDialogClass.dismissProgressDialog();
							//GetBanner();
						}
					});
				}
			}
		});
		show.start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Config.isBookingService)
		finish();
	}
	
	
	void Booking(final String laundryId) {

		//	ProgressDialogClass.showProgressDialog(LaundryBookingActivity.this,
			//		"Loading...");

			final Thread splashTread = new Thread() {
				@Override
				public void run() {

					ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

					
					JSONArray jArray = new JSONArray();
					//int count = 0;
					for (int i = 0; i < bookingArray.size(); i++) {
						
							JSONObject ticketjson = null;
							try {
								ticketjson = new JSONObject();
								ticketjson.put(
										"item",
										bookingArray.get(i).get(
												"Item"));
								ticketjson.put(
										"service",
										bookingArray.get(i).get(
												"Service"));
								ticketjson.put(
										"quantity",
										bookingArray.get(i).get(
												"Quantity"));
								

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							jArray.put(ticketjson);
						
					}
				
					nameValuePairs.add(new BasicNameValuePair("laundry_id",
							laundryId));
					nameValuePairs.add(new BasicNameValuePair("user_id",
							Config.userid));
					nameValuePairs.add(new BasicNameValuePair("cur_lat",
							Config.latitude));
					nameValuePairs.add(new BasicNameValuePair("cur_long",
							Config.longitude));
					nameValuePairs.add(new BasicNameValuePair("cur_add",
							""));
					nameValuePairs.add(new BasicNameValuePair("comment",
							""));
					nameValuePairs.add(new BasicNameValuePair("orders",
							jArray.toString()));

					JsonReturn jsonReturn = new JsonReturn();
					jsonObj = jsonReturn.setReview(
							Config.Order_Online_Url, nameValuePairs);
					

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
								if (jsonObj != null) {
									if (jsonObj.length() > 0) {
										try {
											
												if (jsonObj.getString("status")
														.equals("200")) {
													
													
													Toast.makeText(LaundryBookingActivity.this, jsonObj.getString(
															"stauts_message"),Toast.LENGTH_LONG).show();
												}
												else{
												AlertUtil alert=new AlertUtil();
												alert.messageAlert(
														LaundryBookingActivity.this,"",jsonObj.getString(
																"stauts_message"));
												}
											
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

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

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			if(bookingArray.size()==0){
				return 0;
			}
			return bookingArray.size()+1;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;

			if (convertView == null || convertView.getTag() == null) {
				convertView = mInflater.inflate(R.layout.booking_row, null);
				holder = new ViewHolder();
				//holder.textView_number = (TextView) convertView
				//		.findViewById(R.id.textView_number);
				holder.textView_item = (TextView) convertView
						.findViewById(R.id.textView_item);
				holder.textView_service = (TextView) convertView
						.findViewById(R.id.textView_service);
				holder.textView_dhs = (TextView) convertView
						.findViewById(R.id.textView_dhs);
				holder.textView_quantity = (TextView) convertView
						.findViewById(R.id.textView_quantity);
				holder.textView_amnt = (TextView) convertView
						.findViewById(R.id.textView_amnt);
				holder.imageView_cross = (ImageView) convertView
						.findViewById(R.id.imageView_cross);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if(bookingArray.size()==position){
				//holder.textView_number.setText("");
				//holder.textView_number.setVisibility(View.INVISIBLE);
				holder.textView_item.setVisibility(View.INVISIBLE);
				holder.textView_dhs.setVisibility(View.INVISIBLE);
				//holder.textView_amnt.setVisibility(View.INVISIBLE);
				holder.imageView_cross.setVisibility(View.INVISIBLE);
				holder.textView_service.setText("TOTAL");
				int total=0;
				int total_amnt=0;
				for (int i = 0; i < bookingArray.size(); i++) {
					total=total+Integer.parseInt(bookingArray.get(i).get("Quantity").toString());
					total_amnt=total_amnt+Integer.parseInt(bookingArray.get(i).get("Quantity").toString())*Integer.parseInt(bookingArray.get(i).get("DHS").toString());
				}
				holder.textView_quantity.setText(total+"");
				holder.textView_amnt.setText(total_amnt+"");
			}
			else{
				//holder.textView_number.setVisibility(View.VISIBLE);
				holder.textView_item.setVisibility(View.VISIBLE);
				holder.textView_dhs.setVisibility(View.VISIBLE);
				holder.textView_amnt.setVisibility(View.VISIBLE);
				holder.imageView_cross.setVisibility(View.VISIBLE);
				//holder.textView_number.setText(position+1+"");
			holder.textView_service.setText(bookingArray.get(position).get("Service"));
			holder.textView_item.setText(bookingArray.get(position).get("Item"));
			holder.textView_dhs.setText(bookingArray.get(position).get("DHS"));
			int amount=Integer.parseInt(bookingArray.get(position).get("DHS"))*Integer.parseInt(bookingArray.get(position).get("Quantity"));
			holder.textView_amnt.setText(amount+"");
			holder.textView_quantity.setText(bookingArray.get(position).get("Quantity"));
			
			holder.imageView_cross.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					bookingArray.remove(position);
					list_ed.notifyDataSetChanged();
					
				}
			});
			}

			return convertView;
		}

	}

	static class ViewHolder {
		//TextView textView_number;
		TextView textView_item;
		TextView textView_service;
		TextView textView_dhs;
		TextView textView_quantity;
		TextView textView_amnt;
		ImageView imageView_cross;

	}

	
	private void GetBanner() {

		final Thread splashTread = new Thread() {
			@Override
			public void run() {

			
				bannerArray = JsonReturn.BannerparseJson1("http://laundry.znsoftech.com/banner.php/");
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
							if (bannerArray.size() > 0) {
								JSONObject tempHashMap = bannerArray.get(0);

//								try {
//									
////									imageLoader.DisplayImage(tempHashMap
////												.getString("BannerURL"),
////												imageView_banner,false);
//
//								} catch (JSONException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}

							}

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
			  startActivity(new Intent(LaundryBookingActivity.this,
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		update_service_items(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	private void update_service_items(final int position) {
		// TODO Auto-generated method stub
		try {
			
			JSONArray arr=json.getJSONArray("data");
			JSONObject obj1 = arr.getJSONObject(position);
			
			JSONArray arr_service=obj1.getJSONArray("Services");
			s_items.clear();
			serviceArray.clear();
			for(int i=0;i<arr_service.length();i++){
				HashMap<String, String> map=new HashMap<String, String>();
				JSONObject obj=arr_service.getJSONObject(i);
				map.put("LaundryServiceID", obj.getString("LaundryServiceID"));
				map.put("LaundryServiceName", obj.getString("LaundryServiceName"));
				map.put("Amount", obj.getString("Amount"));
				s_items.add(map);
				serviceArray.add(obj.getString("LaundryServiceName"));
			 }
			
			spinner_service
			.setAdapter(new ArrayAdapter<String>(LaundryBookingActivity.this,
			android.R.layout.simple_spinner_dropdown_item,
			serviceArray));
			
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
								
	}

	
}
