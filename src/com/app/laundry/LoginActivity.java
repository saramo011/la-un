package com.app.laundry;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.laundry.json.JsonReturn;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;
import com.app.laundry.util.ValidationUtil;

public class LoginActivity extends ActionBarActivity {

	JSONArray searchResult = new JSONArray();
	Handler mHandler = new Handler();
	 EditText editText_email;
	 EditText editText_password;
	 SharedPreferences prefs;
	 Boolean isRemember=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayHomeAsUpEnabled(false);
	    bar.setHomeButtonEnabled(false);
	    bar.setTitle("Sign In");
	    
		
		 prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
		
	
		 isRemember=prefs.getBoolean("isRemember", false);
		 editText_email = (EditText) findViewById(R.id.editText_username);
		//editText_email.setTypeface(Config.TF_ApexNew_Medum);
		 editText_password = (EditText) findViewById(R.id.EditText_password);
	//	editText_password.setTypeface(Config.TF_ApexNew_Medum);

		 
		final ImageView imageView_remember=(ImageView)findViewById(R.id.imageView_remember);
		 
		imageView_remember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isRemember){
				imageView_remember.setImageResource(R.drawable.check_box_right);
				}
				else{
				imageView_remember.setImageResource(R.drawable.check_box_login);
				}
				isRemember=!isRemember;
				
			}
		});
		
		TextView txt_register = (TextView) findViewById(R.id.textView_new_user);
		String str="NEW USER";
		SpannableString content = new SpannableString(str);
		content.setSpan(new UnderlineSpan(), 0, str.length(), 0);
		txt_register.setText(content);
		txt_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				finish();
				
			}
		});
		
		txt_register.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){            
	            case MotionEvent.ACTION_DOWN:
	            ((TextView)view).setTextColor(0xFF2489CE); //white
	                break;          
	            case MotionEvent.ACTION_CANCEL:             
	            case MotionEvent.ACTION_UP:
	            ((TextView)view).setTextColor(0xFFFFFFFF); //black
	                break;
	    } 
				return false;
			}
		});

		/*TextView txt_forgot = (TextView) findViewById(R.id.textView_forgot);
		txt_forgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,
						ForgotActivity.class));
			}
		});
		*/
		
		
		/*txt_forgot.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){            
	            case MotionEvent.ACTION_DOWN:
	            ((TextView)view).setTextColor(0xFF2489CE); //white
	                break;          
	            case MotionEvent.ACTION_CANCEL:             
	            case MotionEvent.ACTION_UP:
	            ((TextView)view).setTextColor(0xFFFFFFFF); //black
	                break;
	    } 
				return false;
			}
		});
		*/
		Button but_submit = (Button) findViewById(R.id.but_submit);
		//but_submit.setTypeface(Config.TF_ApexNew_Bold);
		but_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertUtil alert=new AlertUtil();
				if(Network.HaveNetworkConnection(LoginActivity.this)){
				
				if (!ValidationUtil
						.isEmail(editText_email.getText().toString())) {
					
					alert.confirmationAlert(
							LoginActivity.this,
							"",
							"Please enter valid email id",
							new Button.OnClickListener() {

								@Override
								public void onClick(
										View v) {
									editText_email.setText("");
									alert.release();
								}
							});
					
					
				}

				else if (ValidationUtil.isNull(editText_password.getText()
						.toString())) {
					alert.confirmationAlert(
							LoginActivity.this,
							"",
							"Please enter password",
							new Button.OnClickListener() {

								@Override
								public void onClick(
										View v) {
									alert.release();
								}
							});
				}
				else 
					login(editText_email.getText().toString(),
							editText_password.getText().toString());
				
				
				

			}
				else{
					
					
					alert.messageAlert(
							LoginActivity.this,
							getResources()
									.getString(R.string.network_title),
							getResources().getString(
									R.string.network_message));
					
				}
			}
			
		});
		
		if(isRemember){
			editText_email.setText(Config.email);
			editText_password.setText(Config.password);
			imageView_remember.setImageResource(R.drawable.check_box_right);
				
			login(editText_email.getText().toString(),
					editText_password.getText().toString());
		}

	}

	
	void login(final String emailId, final String password) {

		ProgressDialogClass.showProgressDialog(LoginActivity.this,"Loading...");

		final Thread splashTread = new Thread() {
			@Override
			public void run() {

				
				/*String encodePwd=password;
				String encodeEmailId=emailId;
				try {
					encodePwd = URLEncoder.encode(password, "UTF-8");
					encodeEmailId = URLEncoder.encode(emailId, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				 ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			        
					nameValuePairs.add(new BasicNameValuePair("iemail", emailId));
			        nameValuePairs.add(new BasicNameValuePair("ipwd", password));
			        
				
			        JsonReturn jsonReturn=new JsonReturn();
				searchResult = jsonReturn.postData(Config.Login_Url,nameValuePairs);

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
							if (searchResult.length() > 0) {
								try {
									if (!searchResult.getJSONObject(0).has("status")) {
										Config.name=searchResult.getJSONObject(0).getString("Name");
										Config.userid=searchResult.getJSONObject(0).getString("UserID");
										Config.email=emailId;
										Config.password=searchResult.getJSONObject(0).getString("Password");
										Config.phone=searchResult.getJSONObject(0).getString("Phone");
										Config.mobile=searchResult.getJSONObject(0).getString("Mobile");
										Config.address=searchResult.getJSONObject(0).getString("Address");
										Config.city=searchResult.getJSONObject(0).getString("City");
										Config.country=searchResult.getJSONObject(0).getString("Country");
										Config.usertype=searchResult.getJSONObject(0).getString("UserType");
										Config.latitude=searchResult.getJSONObject(0).getString("Latitude");
										Config.longitude=searchResult.getJSONObject(0).getString("Longitude");
										
										prefs.edit().putString("name",Config.name ).commit();
										prefs.edit().putString("userid",Config.userid ).commit();
										prefs.edit().putString("email", Config.email).commit();
										prefs.edit().putString("mobile",Config.mobile ).commit();
										prefs.edit().putString("password", Config.password).commit();
										prefs.edit().putString("address", Config.address).commit();
										prefs.edit().putString("city", Config.city).commit();
										prefs.edit().putString("country", Config.country).commit();
										prefs.edit().putString("usertype", Config.usertype).commit();
										prefs.edit().putString("Latitude", Config.latitude).commit();
										prefs.edit().putString("Longitude", Config.longitude).commit();
										
										
										
										prefs.edit().putBoolean("isRemember", isRemember).commit();
										getCityName();
									
									} else {
										ProgressDialogClass.dismissProgressDialog();
										AlertUtil alert=new AlertUtil();
										alert.messageAlert(
												LoginActivity.this,"",searchResult.getJSONObject(0).getString("stauts_message"));
										
										
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							//ProgressDialogClass.dismissProgressDialog();

						}

					});
				}
			}
		});
		displayThread.start();

	}
	
	void getCityName() {

		

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
									//if(Config.cityArray.get(0).equals("200")){
									//	getCityName();
									//}
									//else{
										
										startActivity(new Intent(LoginActivity.this,BaseFragmentActivity.class));
										overridePendingTransition(R.anim.right_in, R.anim.left_out);
										finish();
										ProgressDialogClass.dismissProgressDialog();
										
									//}

								}
								
							}
							

						}

					});
				}
			}
		});
		displayThread.start();

	}
	
	
}
