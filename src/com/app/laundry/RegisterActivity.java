package com.app.laundry;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.laundry.json.JsonReturn;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;
import com.app.laundry.util.ValidationUtil;

public class RegisterActivity extends ActionBarActivity {

	JSONArray searchResult = new JSONArray();
	Handler mHandler = new Handler();
	SharedPreferences prefs;
	 GPSTracker gps;
	 boolean isLoad;
	 
	 Spinner spinner_city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		//#00516C
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);
	    bar.setTitle("Sign Up");
	    
	    
		prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
		
		isLoad = false;
		gps = new GPSTracker(RegisterActivity.this);

		// check if GPS location can get
		if (gps.canGetLocation()) {
			Log.d("Your Location", "latitude:" + gps.getLatitude()
					+ ", longitude: " + gps.getLongitude());

			final Thread splashTread = new Thread() {
				@Override
				public void run() {

					Thread splashTread = new Thread() {
						@Override
						public void run() {
							try {
								int waited = 0;
								while (waited < 200) {
									sleep(100);
									waited += 100;
									gps.getLocation();
									if (gps.getLatitude() == 0.0) {
										waited = 0;
									}

								}
							} catch (InterruptedException e) {
								// do nothing
							} finally {
								runOnUiThread(new Thread(new Runnable() {
									public void run() {
										getLocation();
									}
								}));

							}
						}
					};
					try {
						splashTread.start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			};
			splashTread.start();
		} else {
			// Can't get user's current location
			showGPSDisabledAlertToUser(10);
			// stop executing code by return
			return;
		

	}

		spinner_city = (Spinner) findViewById(R.id.spinner_city);
		final EditText editText_name = (EditText) findViewById(R.id.editText_username);
		final EditText editText_email = (EditText) findViewById(R.id.EditText_email);
		final EditText editText_email2 = (EditText) findViewById(R.id.EditText_email2);
		final EditText editText_mobile = (EditText) findViewById(R.id.EditText_mobile);
		final EditText editText_password = (EditText) findViewById(R.id.EditText_password);
		final EditText editText_address = (EditText) findViewById(R.id.EditText_address);
		final EditText editText_country = (EditText) findViewById(R.id.EditText_country);
		final EditText editText_phone = (EditText) findViewById(R.id.EditText_phone);
		
		final Spinner spinner_operator = (Spinner) findViewById(R.id.spinner_operator);
		
		
		 final String[] operator_items = new String[] { "050", "052","055", "056"};

		
		 
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegisterActivity.this

				,android.R.layout.simple_spinner_dropdown_item,operator_items);
		spinner_operator.setAdapter(arrayAdapter);

		Button but_register = (Button) findViewById(R.id.but_register);
		but_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mobileNumberWithCode="";
				final AlertUtil alert = new AlertUtil();
				if (Network.HaveNetworkConnection(RegisterActivity.this)) {
					if (ValidationUtil.isNull(editText_name.getText()
							.toString())) {
						alert.confirmationAlert(RegisterActivity.this, "",
								"Please enter your name",
								new Button.OnClickListener() {

									@Override
									public void onClick(View v) {
										editText_name.setText("");
										alert.release();
									}
								});
					} else if (!ValidationUtil.isEmail(editText_email.getText()
							.toString())) {
						alert.confirmationAlert(RegisterActivity.this, "",
								"Please enter valid email id",
								new Button.OnClickListener() {

									@Override
									public void onClick(View v) {
										editText_email.setText("");
										alert.release();
									}
								});
					}
					else if (!ValidationUtil.isEmail(editText_email2.getText()
							.toString())) {
						alert.confirmationAlert(RegisterActivity.this, "",
								"Please enter valid email id",
								new Button.OnClickListener() {

									@Override
									public void onClick(View v) {
										editText_email2.setText("");
										alert.release();
									}
								});
					}
					else if (!editText_email.getText().toString().equals(editText_email2.getText().toString())) {
						alert.confirmationAlert(RegisterActivity.this, "",
								"Please enter correct email id",
								new Button.OnClickListener() {

									@Override
									public void onClick(View v) {
										//editText_email.setText("");
										alert.release();
									}
								});
					}
					else if (ValidationUtil.isNull(editText_password
							.getText().toString())) {
						alert.confirmationAlert(RegisterActivity.this, "",
								"Please enter password",
								new Button.OnClickListener() {

									@Override
									public void onClick(View v) {
										alert.release();
									}
								});
					} else if (ValidationUtil.isNull(editText_mobile.getText().toString())) {
						
						if(editText_mobile.getText().toString().length()==7||
								editText_mobile.getText().toString().length()==8){
							mobileNumberWithCode=String.valueOf(spinner_operator.getSelectedItem())+editText_mobile.getText().toString();
						}else{alert.confirmationAlert(RegisterActivity.this, "",
								"Please check mobile no.( must be of 7or 8-digit)",
								new Button.OnClickListener() {

									@Override
									public void onClick(View v) {
										alert.release();
									}
								});}
					}else {alert.confirmationAlert(RegisterActivity.this, "",
							"Please enter mobile no.",
							new Button.OnClickListener() {

								@Override
								public void onClick(View v) {
									alert.release();
								}
							});	
						register(editText_name.getText().toString(),
								editText_email.getText().toString(),
								editText_password.getText().toString(),
								editText_phone.getText().toString(),
								mobileNumberWithCode,
								editText_address.getText().toString(),
								spinner_city.getSelectedItem()+"",
								editText_country.getText().toString(),
								"",
								Config.latitude, Config.longitude);

					}
				}
				else{
				alert.messageAlert(RegisterActivity.this, getResources()
						.getString(R.string.network_title), getResources()
						.getString(R.string.network_message));
				}
			}
		});
		if(Config.newCityArray.size()==0)
			getCityName();
		else{
			spinner_city
			.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
			android.R.layout.simple_spinner_dropdown_item,
			Config.newCityArray));
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		gps.stopUsingGPS();
		Intent intent=new Intent(
				RegisterActivity.this,
				LoginActivity.class);

		startActivity(intent);
		
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
		

	}

	void register(final String name, final String email, final String password,
			final String phone, final String mobile, final String address,
			final String city, final String country, final String usertype, String lat, String log) {

		ProgressDialogClass.showProgressDialog(RegisterActivity.this,
				"Loading...");

		final Thread splashTread = new Thread() {
			@Override
			public void run() {

				/*
				 * String encodeName=name; String encodeEmail=email; String
				 * encodePassword=password; String encodePhone=phone; String
				 * encodeMobile=mobile; String encodeAddress=address; String
				 * encodeCity=city; String encodeState=state; String
				 * encodeCountry=country; String encodeUserType=usertype;
				 * //String encodeEmailId=emailId; try {
				 * 
				 * encodeName = URLEncoder.encode(name, "UTF-8"); encodeEmail =
				 * URLEncoder.encode(email, "UTF-8"); encodePassword =
				 * URLEncoder.encode(password, "UTF-8"); encodePhone =
				 * URLEncoder.encode(phone, "UTF-8"); encodeMobile =
				 * URLEncoder.encode(mobile, "UTF-8"); encodeAddress =
				 * URLEncoder.encode(address, "UTF-8"); encodeCity =
				 * URLEncoder.encode(city, "UTF-8"); encodeState =
				 * URLEncoder.encode(state, "UTF-8"); encodeCountry =
				 * URLEncoder.encode(country, "UTF-8"); encodeUserType =
				 * URLEncoder.encode(usertype, "UTF-8");
				 * 
				 * //encodeEmailId = URLEncoder.encode(emailId, "UTF-8"); }
				 * catch (UnsupportedEncodingException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */
				ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("uname", name));
				nameValuePairs.add(new BasicNameValuePair("uemail", email));
				nameValuePairs
						.add(new BasicNameValuePair("upassword", password));
				nameValuePairs.add(new BasicNameValuePair("uphone", phone));
				nameValuePairs.add(new BasicNameValuePair("umobile", mobile));
				nameValuePairs.add(new BasicNameValuePair("uaddress", address));
				nameValuePairs.add(new BasicNameValuePair("ucity", city));
				nameValuePairs.add(new BasicNameValuePair("ucountry", country));
				nameValuePairs
						.add(new BasicNameValuePair("uusertype", usertype));
				nameValuePairs.add(new BasicNameValuePair("ulat",
						Config.latitude));
				nameValuePairs.add(new BasicNameValuePair("ulong",
						Config.longitude));
				nameValuePairs.add(new BasicNameValuePair("ucreatedby", ""));
				
				String ipAddress=getLocalIpAddress();
				nameValuePairs.add(new BasicNameValuePair("uip", ipAddress));
				
				nameValuePairs.add(new BasicNameValuePair("ustate", ""));
				nameValuePairs.add(new BasicNameValuePair("uzipcode", ""));

				JsonReturn jsonReturn = new JsonReturn();
				searchResult = jsonReturn.postData(Config.Register_Url,
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
							if (searchResult.length() > 0) {
								try {
									if (!searchResult.getJSONObject(0).has("status")) {

										Config.userid = searchResult
												.getJSONObject(0).getString(
														"UserID");
										Config.name = name;
										Config.password = password;
										Config.email = email;
										Config.phone = phone;
										Config.mobile = mobile;
										Config.address = address;
										Config.city = city;
										Config.country = country;
										Config.usertype = usertype;

										prefs.edit()
												.putString("name", Config.name)
												.commit();
										prefs.edit()
												.putString("userid",
														Config.userid).commit();
										prefs.edit()
												.putString("email",
														Config.email).commit();
										prefs.edit()
												.putString("mobile",
														Config.mobile).commit();
										prefs.edit()
												.putString("password",
														Config.password)
												.commit();

										prefs.edit()
												.putString("phone",
														Config.phone).commit();
										prefs.edit()
												.putString("address",
														Config.address)
												.commit();
										prefs.edit()
												.putString("city", Config.city)
												.commit();
										

										prefs.edit()
												.putString("country",
														Config.country)
												.commit();
										
										prefs.edit()
												.putString("usertype",
														Config.usertype)
												.commit();

										Intent intent=new Intent(
												RegisterActivity.this,
												BaseFragmentActivity.class);
								
										startActivity(intent);
										overridePendingTransition(R.anim.right_in, R.anim.left_out);
										finish();
									} else {
										AlertUtil alert = new AlertUtil();
										alert.messageAlert(
												RegisterActivity.this,
												"",
												searchResult
														.getJSONObject(0)
														.getString(
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

	public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                	String ipaddress=inetAddress.getHostAddress().toString();
                        Log.e("ip address",""+ipaddress);
                        return ipaddress;
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e("ip address", ex.toString());
	    }
	    return null;
	}
	
	private void getLocation() {
		// updateSeekProgress();

		//progressBar.setVisibility(ProgressBar.VISIBLE);

		Config.latitude = gps.getLatitude()+"";

		Config.longitude = gps.getLongitude()+"";

		if (!isLoad) {
			//Toast.makeText(RegisterActivity.this, "latitude:" + gps.getLatitude()
				//	+ ", longitude: " + gps.getLongitude(),Toast.LENGTH_LONG).show();
			isLoad = true;
			//progressBar.setVisibility(ProgressBar.GONE);
			gps.stopUsingGPS();
		}

	}

	// lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

	private void showGPSDisabledAlertToUser(final int code) {
		// AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		/*
		 * final Dialog dialog = new Dialog(NearByMetroActivity.this);
		 * dialog.setContentView(R.layout.activity_gps_popup);
		 * dialog.getWindow().setBackgroundDrawable(new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT)); // set the custom
		 * dialog components - text, image and button
		 * 
		 * Button button_turn_on = (Button)
		 * dialog.findViewById(R.id.button_turn_on); TextView textView_cancel =
		 * (TextView) dialog.findViewById(R.id.textView_cancel); // if button is
		 * clicked, close the custom dialog
		 * button_turn_on.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent callGPSSettingIntent =
		 * new Intent(
		 * android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		 * startActivityForResult(callGPSSettingIntent, code); dialog.dismiss();
		 * } }); textView_cancel.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { dialog.dismiss();
		 * onBackPressed(); } });
		 * 
		 * dialog.show();
		 */

		final Dialog dialog = new Dialog(RegisterActivity.this);
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.custom_alert_popup);
		TextView title1 = (TextView) dialog.findViewById(R.id.textView_title);
		title1.setText("TURN GPS ON");
		TextView msg = (TextView) dialog.findViewById(R.id.textView_message);
		msg.setText("To get your nearest metro station distance turn on your GPS.");

		Button ok = (Button) dialog.findViewById(R.id.button_positive);
		ok.setText("ON GPS NOW");
		// if button is clicked, close the custom dialog
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callGPSSettingIntent = new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(callGPSSettingIntent, code);
				dialog.dismiss();
			}
		});

		Button cencle = (Button) dialog.findViewById(R.id.button_negative);
		cencle.setText("Cancel");
		// if button is clicked, close the custom dialog
		cencle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				onBackPressed();
			}
		});
		dialog.show();

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (gps.canGetLocation()) {
			gps.getLocation();
			Log.d("Your Location", "latitude:" + gps.getLatitude()
					+ ", longitude: " + gps.getLongitude());
			/*
			 * while (gps.getLatitude()!=0.0) {
			 * 
			 * gps.getLocation(); }
			 */
			final Thread splashTread = new Thread() {
				@Override
				public void run() {

					Thread splashTread = new Thread() {
						@Override
						public void run() {
							try {
								int waited = 0;
								while (waited < 200) {
									sleep(100);
									waited += 100;
									gps.getLocation();
									if (gps.getLatitude() == 0.0) {
										waited = 0;
									}

								}
							} catch (InterruptedException e) {
								// do nothing
							} finally {
								runOnUiThread(new Thread(new Runnable() {
									public void run() {
										getLocation();
									}
								}));

							}
						}
					};
					try {
						splashTread.start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			};
			splashTread.start();

		} else {
			// Can't get user's current location
			
			showGPSDisabledAlertToUser(10);
			// stop executing code by return
			return;
		}
	}// on
	
	void getCityName() {

			ProgressDialogClass.showProgressDialog(RegisterActivity.this,
					"Loading...");

			final Thread splashTread = new Thread() {
				@Override
				public void run() {

					ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();


					JSONArray jArray=new JSONArray();
					JsonReturn jsonReturn = new JsonReturn();
					jArray = jsonReturn.postLaundryData(
							Config.City_Url, nameValuePairs);
					Config.cityArray.clear();
					for (int i = 0; i < jArray.length(); i++) {
						try {
							Config.cityArray.add(jArray.get(i).toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

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
								if (Config.cityArray != null) {
									if (Config.cityArray.size() > 0) {
										//try {
											
											//JSONObject jsonObj=serviceArray
												//	.getJSONObject(0);
										//	if (!jsonObj.has(
											//		"status")) {
												
												Config.newCityArray.clear();
												int city_array_size=Config.cityArray.size();
												for (int i = 0; i <city_array_size ; i++)
												{
													try{
														String citystr=Config.cityArray.get(i);
														JSONObject jobj=new JSONObject(citystr);
														Config.newCityArray.add(jobj.getString("CityName"));
													}
													catch(JSONException e){
														e.printStackTrace();
													}
													
												}
										
												spinner_city
												.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
												android.R.layout.simple_spinner_dropdown_item,
												Config.newCityArray));
												
														
											//} else {
												//final AlertUtil alert = new AlertUtil();
												
												
											/*	alert.confirmationAlert(LaundryBookingActivity.this, "",
														searchResult.getJSONObject(
																0).getString(
																"stauts_message"),
														new Button.OnClickListener() {

															@Override
															public void onClick(View v) {
																alert.release();
																onBackPressed();
															}
														});

											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}*/

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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		  if (id==android.R.id.home){
	        onBackPressed();
	        return true;
		
	    }
		return super.onOptionsItemSelected(item);
	}
	
	
}
