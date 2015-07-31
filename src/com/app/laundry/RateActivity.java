package com.app.laundry;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.laundry.json.JsonReturn;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;

public class RateActivity extends FragmentActivity {

	JSONObject jsonObj = new JSONObject();
	Handler mHandler = new Handler();
	ImageView imageView_star1;
	ImageView imageView_star2;
	ImageView imageView_star3;
	ImageView imageView_star4;
	ImageView imageView_star5;
	Button but_rate;

	ImageView imageView_clening_star1;
	ImageView imageView_clening_star2;
	ImageView imageView_clening_star3;
	ImageView imageView_clening_star4;
	ImageView imageView_clening_star5;
	Button but_cleaning;

	// private GoogleMap mMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate_popup);

		but_rate = (Button) findViewById(R.id.textView_rate);
		but_cleaning = (Button) findViewById(R.id.but_cleaning);
		final EditText editText_msg = (EditText) findViewById(R.id.editText_msg);
		Button button_clear = (Button) findViewById(R.id.button_clear);

		Button button_submit = (Button) findViewById(R.id.button_submit);
		Intent intent = getIntent();
		final String laundryId = intent.getExtras().getString("LaundryID");

		button_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Network.HaveNetworkConnection(RateActivity.this)) {
					rateLaundry(laundryId, but_rate.getText().toString(),but_cleaning.getText().toString(),
							editText_msg.getText().toString());
				} else {
					final AlertUtil alert = new AlertUtil();
					alert.confirmationAlert(RateActivity.this, getResources()
							.getString(R.string.network_title), getResources()
							.getString(R.string.network_message),
							new Button.OnClickListener() {

								@Override
								public void onClick(View v) {
									
									
									alert.release();
								}
							});

				}

			}
		});
		button_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editText_msg.setText("");
				but_rate.setText("1");
				but_cleaning.setText("1");
				imageView_star5.setImageResource(R.drawable.rating_before);
				imageView_star4.setImageResource(R.drawable.rating_before);
				imageView_star3.setImageResource(R.drawable.rating_before);
				imageView_star2.setImageResource(R.drawable.rating_before);
				imageView_star1.setImageResource(R.drawable.rating_after);
				
				imageView_clening_star5.setImageResource(R.drawable.rating_before);
				imageView_clening_star4.setImageResource(R.drawable.rating_before);
				imageView_clening_star3.setImageResource(R.drawable.rating_before);
				imageView_clening_star2.setImageResource(R.drawable.rating_before);
				imageView_clening_star1.setImageResource(R.drawable.rating_after);
			}
		});

		imageView_star1 = (ImageView) findViewById(R.id.imageView_star1);
		imageView_star2 = (ImageView) findViewById(R.id.imageView_star2);
		imageView_star3 = (ImageView) findViewById(R.id.imageView_star3);
		imageView_star4 = (ImageView) findViewById(R.id.imageView_star4);
		imageView_star5 = (ImageView) findViewById(R.id.imageView_star5);

		imageView_star1.setOnClickListener(clickLis);
		imageView_star2.setOnClickListener(clickLis);
		imageView_star3.setOnClickListener(clickLis);
		imageView_star4.setOnClickListener(clickLis);
		imageView_star5.setOnClickListener(clickLis);

		imageView_star1.setTag(1);
		imageView_star2.setTag(2);
		imageView_star3.setTag(3);
		imageView_star4.setTag(4);
		imageView_star5.setTag(5);

		imageView_clening_star1 = (ImageView) findViewById(R.id.imageView_clening_star1);
		imageView_clening_star2 = (ImageView) findViewById(R.id.imageView_clening_star2);
		imageView_clening_star3 = (ImageView) findViewById(R.id.imageView_clening_star3);
		imageView_clening_star4 = (ImageView) findViewById(R.id.imageView_clening_star4);
		imageView_clening_star5 = (ImageView) findViewById(R.id.imageView_clening_star5);

		imageView_clening_star1.setOnClickListener(clickLis_clining);
		imageView_clening_star2.setOnClickListener(clickLis_clining);
		imageView_clening_star3.setOnClickListener(clickLis_clining);
		imageView_clening_star4.setOnClickListener(clickLis_clining);
		imageView_clening_star5.setOnClickListener(clickLis_clining);

		imageView_clening_star1.setTag(1);
		imageView_clening_star2.setTag(2);
		imageView_clening_star3.setTag(3);
		imageView_clening_star4.setTag(4);
		imageView_clening_star5.setTag(5);

		// Use AsyncTask execute Method To Prevent ANR Problem
		// tryLogin("", "");
		// postData();

	}

	OnClickListener clickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			imageView_star5.setImageResource(R.drawable.rating_before);
			imageView_star4.setImageResource(R.drawable.rating_before);
			imageView_star3.setImageResource(R.drawable.rating_before);
			imageView_star2.setImageResource(R.drawable.rating_before);
			imageView_star1.setImageResource(R.drawable.rating_before);
			int tag = Integer.parseInt(v.getTag().toString());
			but_rate.setText(tag + "");
			switch (tag) {
			case 5:
				imageView_star5.setImageResource(R.drawable.rating_after);
			case 4:
				imageView_star4.setImageResource(R.drawable.rating_after);
			case 3:
				imageView_star3.setImageResource(R.drawable.rating_after);
			case 2:
				imageView_star2.setImageResource(R.drawable.rating_after);
			case 1:
				imageView_star1.setImageResource(R.drawable.rating_after);
			default:
				break;
			}

		}
	};

	OnClickListener clickLis_clining = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			imageView_clening_star5.setImageResource(R.drawable.rating_before);
			imageView_clening_star4.setImageResource(R.drawable.rating_before);
			imageView_clening_star3.setImageResource(R.drawable.rating_before);
			imageView_clening_star2.setImageResource(R.drawable.rating_before);
			imageView_clening_star1.setImageResource(R.drawable.rating_before);
			int tag = Integer.parseInt(v.getTag().toString());
			but_cleaning.setText(tag + "");
			switch (tag) {
			case 5:
				imageView_clening_star5
						.setImageResource(R.drawable.rating_after);
			case 4:
				imageView_clening_star4
						.setImageResource(R.drawable.rating_after);
			case 3:
				imageView_clening_star3
						.setImageResource(R.drawable.rating_after);
			case 2:
				imageView_clening_star2
						.setImageResource(R.drawable.rating_after);
			case 1:
				imageView_clening_star1
						.setImageResource(R.drawable.rating_after);
			default:
				break;
			}

		}
	};

	void rateLaundry(final String laundryId, final String rate,final String cleaning_rate, final String msg) {

		ProgressDialogClass.showProgressDialog(RateActivity.this, "Loading...");

		final Thread splashTread = new Thread() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {

				/*
				 * String encodeMsg=msg; try { encodeMsg =
				 * URLEncoder.encode(msg, "UTF-8"); } catch
				 * (UnsupportedEncodingException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); }
				 */
				ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("laundry_id",
						laundryId));
				nameValuePairs.add(new BasicNameValuePair("user_id",
						Config.userid));
				nameValuePairs.add(new BasicNameValuePair("service_quality", rate));
				nameValuePairs.add(new BasicNameValuePair("cleaning_quality", cleaning_rate));
				nameValuePairs.add(new BasicNameValuePair("review", msg));
				
				JsonReturn jsonReturn = new JsonReturn();
				jsonObj = jsonReturn.setReview(Config.Feedback_Url,
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
										
											if (jsonObj.getString("status")
													.equals("200")) {
												
												onBackPressed();
												Toast.makeText(RateActivity.this, jsonObj.getString(
														"stauts_message"),Toast.LENGTH_LONG).show();
											}
											else{
											AlertUtil alert=new AlertUtil();
											alert.messageAlert(
													RateActivity.this,"",jsonObj.getString(
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

	
}
