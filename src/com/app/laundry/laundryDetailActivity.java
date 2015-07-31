package com.app.laundry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.json.JsonReturn;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

@SuppressWarnings("deprecation")
public class laundryDetailActivity extends ActionBarActivity {

	JSONArray searchResult = new JSONArray();
	JSONObject jsonObj=new JSONObject();
	Handler mHandler = new Handler();
	TextView textView_laundry_name;
	TextView textView_laundry_review;
	JSONObject json;
	
	String laundryId,laundryName;
	//String for_check="Hello";
	
	TextView textView_detail;
	TextView textView_laundry_vote;
	Button button_rate,bt1;
	ImageButton button_bookmark;
	private GoogleMap mMap;
	
	static String laundaryName,phoneNumber="";
	static double  lat,longt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_laundry_detail);

		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);
		
		Intent intent = getIntent();
		laundryId = intent.getExtras().getString("LaundryID");
		//new loginAccess().execute();
		Config.Item_Array.clear();
		Config.Service_Array.clear();
		Config.Amount_Array.clear();
		laundryName=intent.getExtras().getString("LaundryName");
		
		bar.setTitle(laundryName);
		
		bt1=(Button)findViewById(R.id.button1);
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(laundryDetailActivity.this,RateCart.class);
				
				intent.putExtra("LaundryID", laundryId);
				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				
			}
		});

		textView_laundry_name = (TextView) findViewById(R.id.textView_laundry_name);
		textView_laundry_review = (TextView) findViewById(R.id.textView_laundry_review);
		textView_laundry_review.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String reviews=textView_laundry_review.getText().toString();
				String[] reviews_array=reviews.split("\\s");
				
				int review=0;
				try{
					review=Integer.parseInt(reviews_array[0]);
				}catch(NumberFormatException e){
					review=0;
				}
				
				if(review==0){
					Toast.makeText(getApplicationContext(), "0 reviews.", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent=new Intent(laundryDetailActivity.this,LaundryReview.class);
					intent.putExtra("LaundryId", laundryId);
					intent.putExtra("LaundryName", laundryName);
					startActivity(intent);
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
				}
			}
		});

		textView_detail = (TextView) findViewById(R.id.textView_detail);
		textView_laundry_vote = (TextView) findViewById(R.id.textView_laundry_vote);
		button_rate = (Button) findViewById(R.id.button_rate);

		

		Button button_pick_up_request = (Button) findViewById(R.id.button_pick_up_request);
		button_pick_up_request.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				Intent intent=new Intent(laundryDetailActivity.this, ChooseOnlineOfflineActivity.class);
				intent.putExtra("LaundryId", laundryId);
				intent.putExtra("LaundryName",laundryName);
				
				startActivity(intent);
				
				
			}
		});
		
		ImageButton button_call = (ImageButton) findViewById(R.id.imageButton_call);
		button_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:"+phoneNumber));
					startActivity(intent); 
				
				
			}
		});
		
		button_bookmark = (ImageButton) findViewById(R.id.imageButton_bookmark);
		button_bookmark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Network.HaveNetworkConnection(laundryDetailActivity.this)){
				setBookmark(laundryId);
				}
				else{
					AlertUtil alert=new AlertUtil();
					alert.messageAlert(
							laundryDetailActivity.this,
							getResources()
									.getString(R.string.network_title),
							getResources().getString(
									R.string.network_message));
					
				}
			}
		});
		ImageButton button_rate = (ImageButton) findViewById(R.id.imageButton_rate);
//		button_rate.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(laundryDetailActivity.this,
//						New_Rate.class);
//				intent.putExtra("LaundryID", laundryId);
//
//				startActivity(intent);
//				
//			}
//		});

		
		 android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
         mMap = ((SupportMapFragment)fragmentManager.findFragmentById(R.id.map)).getMap();
         final LatLng LAUNDARY= new LatLng(lat, longt);
         Marker laundary = mMap.addMarker(new MarkerOptions()
                                   .position(LAUNDARY)
                                   .title(laundaryName));
         laundary.showInfoWindow();
        
      

		final AlertUtil alert = new AlertUtil();
		if (Network.HaveNetworkConnection(laundryDetailActivity.this)) {
			getLaundryDetail(laundryId);
			
		} else {
			alert.confirmationAlert(laundryDetailActivity.this, getResources()
					.getString(R.string.network_title), getResources()
					.getString(R.string.network_message),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							alert.release();
							onBackPressed();
						}
					});
		}

	}

	
	void getLaundryDetail(final String laundryId) {

		ProgressDialogClass.showProgressDialog(laundryDetailActivity.this,
				"Loading...");
		json=null;
		//new loginAccess().execute();
		
		final Thread splashTread = new Thread() {
			@Override
			public void run() {

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("ilaundryid",
						laundryId));
			
				nameValuePairs.add(new BasicNameValuePair("iuserid",
						Config.userid));
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.Get_Laundry_Detail_Url,"POST", nameValuePairs);	
				

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
							if(json!=null){
								Log.e("json_data",json.toString());
								try {
									Log.e("status",json.getString("status"));
									JSONArray array=json.getJSONArray("data");
									JSONObject j_obj=array.getJSONObject(0);
									
//									

									if(j_obj.getString("bookmark").equalsIgnoreCase("1"))
									{
										button_bookmark.setBackgroundResource(R.drawable.bookmark_selected);
										//="Bookmark True";
									}
									else
										button_bookmark.setBackgroundResource(R.drawable.second);
									
									textView_laundry_name
											.setText(j_obj
													.getString(
															"LaundryName"));
									laundaryName=j_obj
											.getString(
													"LaundryName");
									
									String reviewData="";
									
									int review=0;
									try{
										review=Integer.parseInt(j_obj.getString("total_reviews"));
									}catch(NumberFormatException e){
										review=0;
									}
											
//									if(j_obj.getString("total_reviews").equals(null) || j_obj.getString("total_reviews").equals(""))
//										reviewData="0 Reviews";
//									else
//									reviewData=j_obj.getString("total_reviews")+ " Reviews";
									
									if(review<2)
										reviewData=review+" Review";
									else
										reviewData=review+" Reviews";
									
									SpannableString content = new SpannableString(reviewData);
									content.setSpan(new UnderlineSpan(), 0, reviewData.length(), 0);
									textView_laundry_review.setText(content);									
									
									int total_rating=0;
									
									try{
										total_rating=Integer.parseInt(j_obj.getString("total_ratings"));
									}catch(NumberFormatException e){
										total_rating=0;
									}
									
									String ratingData="";
									if(total_rating<2){
										ratingData=total_rating+" Vote";
									}else{
										ratingData=total_rating+" Votes";
									}
									
									textView_laundry_vote.setText(ratingData);
									
//									if(j_obj.getString("total_ratings").equals(null) || j_obj.getString("total_ratings").equals(""))
//										textView_laundry_vote.setText("0 Votes");
//									else
//										textView_laundry_vote.setText(j_obj.getString("total_ratings")+ " Votes");
									if(total_rating==0)
										button_rate.setVisibility(View.INVISIBLE);
										
										
									try {
										Double avgDouble=Double.parseDouble(j_obj
												.getString("avgratings"));
										String str=String.format("%.1f", avgDouble);
										button_rate.setText("Rating: "+str);
									} catch (NumberFormatException e) {
										// TODO: handle exception
									}
									
									
									Log.e("LaundryAddress",j_obj.getString("LaundryAddress"));
									Log.e("LaundryCity",j_obj.getString("LaundryCity"));
									Log.e("LaundryZipCode",j_obj.getString("LaundryZipCode"));
									Log.e("LaundryCallPrefernce",j_obj.getString("LaundryCallPrefernce"));
									Log.e("LaundryWebsite",j_obj.getString("LaundryWebsite"));
									Log.e("LaundryLat",j_obj.getString("LaundryLat"));
									Log.e("LaundryLong",j_obj.getString("LaundryLong"));
									
									lat  = Double.parseDouble(j_obj.getString("LaundryLat"));
									longt  = Double.parseDouble(j_obj.getString("LaundryLong"));
									
									String address="Address "+"\n";
									if(!j_obj.getString("LaundryAddress").equals(""))
									address=address+j_obj.getString("LaundryAddress");
									if(!address.equals("")){
										address=address+"\n";
									}
									if(!j_obj.getString("LaundryCity").equals(""))
										address=address+j_obj.getString("LaundryCity")+", ";
									
									if(!address.equals("")&&!j_obj.getString("LaundryZipCode").equals("")){
										address=address+"Zip code: ";
									}
									if(!j_obj.getString("LaundryZipCode").equals(""))
										address=address+j_obj.getString("LaundryZipCode")+"\n";
									
									if(!address.equals("")){
										//address=address+"\n";
									}
									
									if(!j_obj.getString("LaundryCallPrefernce").equals(""))
										phoneNumber=j_obj.getString("LaundryCallPrefernce");
									
									if(!j_obj.getString("LaundryCallPrefernce").equals("")&&!j_obj.getString("LaundryWebsite").equals("")){
										address=address+"\n";
									}
									
									if(!j_obj.getString("LaundryWebsite").equals(""))
										address=address+j_obj.getString("LaundryWebsite");
									
										
									
									textView_detail.setText(address);
									//
									/* double lat=Double.parseDouble(searchResult.getJSONObject(0).getString("LaundryLat"));
									 double lon=Double.parseDouble(searchResult.getJSONObject(0).getString("LaundryLong"));
									 LatLng  latlong=new LatLng(lat, lon);
									 mMap.addMarker(new MarkerOptions()
							            .position(latlong));
									4
									 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 14));
										*/
									 android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
							         mMap = ((SupportMapFragment)fragmentManager.findFragmentById(R.id.map)).getMap();
							         final LatLng LAUNDARY= new LatLng(lat, longt);
							         Marker laundary = mMap.addMarker(new MarkerOptions()
							                                   .position(LAUNDARY)
							                                   .title(laundaryName));
							         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LAUNDARY, 14));
							        // laundary.showInfoWindow();
									
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							// ProgressDialogClass.dismissProgressDialog();
							ProgressDialogClass.dismissProgressDialog();
							//Toast.makeText(getApplicationContext(), for_check, Toast.LENGTH_LONG).show();
						}

					});
				}
			}
		});
		displayThread.start();

	}

	void setBookmark(final String laundryId) {

		ProgressDialogClass.showProgressDialog(laundryDetailActivity.this,
				"Loading...");

		final Thread splashTread = new Thread() {
			@Override
			public void run() {

				ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("laundry_id",
						laundryId));
				nameValuePairs.add(new BasicNameValuePair("user_id",
						Config.userid));

				JsonReturn jsonReturn = new JsonReturn();
				jsonObj = jsonReturn.setBookmark(Config.Bookmark_Url,
						nameValuePairs);

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
										if (!jsonObj.has(
												"status")) {
											if(jsonObj.getString("IsBookmark").equalsIgnoreCase("1"))
											{
											button_bookmark.setBackgroundResource(R.drawable.bookmark_selected);
											//for_check="Bookmark True onClick";
											}
											else
												button_bookmark.setBackgroundResource(R.drawable.second);
											
										} else {

											AlertUtil alert = new AlertUtil();
											
											alert.messageAlert(
													laundryDetailActivity.this,
													"",
													jsonObj.getString(
															"stauts_message"));

										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}
							ProgressDialogClass.dismissProgressDialog();
							//Toast.makeText(getApplicationContext(), for_check, Toast.LENGTH_LONG).show();
						}

					});
				}
			}
		});
		displayThread.start();

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
			  startActivity(new Intent(laundryDetailActivity.this,
						BaseFragmentActivity.class));
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
		  } 
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_menu, menu);	
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

}
