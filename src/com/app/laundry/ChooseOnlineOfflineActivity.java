package com.app.laundry;

import org.json.JSONArray;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;

public class ChooseOnlineOfflineActivity extends FragmentActivity {

	JSONArray searchResult = new JSONArray();
	Handler mHandler = new Handler();
	ImageView imageView_on_online;
	ImageView imageView_on_offline;
	
	Button but_ok;
	boolean isOnline=true;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_online_offline_popup);

		but_ok = (Button) findViewById(R.id.button_ok);

		but_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Network.HaveNetworkConnection(ChooseOnlineOfflineActivity.this)) {

				if (isOnline) {
					Config.isBookingService=false;
					Intent intent=new Intent(ChooseOnlineOfflineActivity.this,
							LaundryBookingActivity.class);
					intent.putExtra("LaundryId", getIntent().getExtras().getString("LaundryId"));
					intent.putExtra("LaundryName", getIntent().getExtras().getString("LaundryName"));
					
					startActivity(intent);
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
					
					
				
				}
				else{
					Intent intent=new Intent(ChooseOnlineOfflineActivity.this,
							CommentActivity.class);
					intent.putExtra("LaundryId", getIntent().getExtras().getString("LaundryId", ""));
					intent.putExtra("LaundryName", getIntent().getExtras().getString("LaundryName", ""));
					intent.putExtra("LaundryOffline", true);
					
					startActivity(intent);
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
				}
				finish();
				} else {
					final AlertUtil alert = new AlertUtil();
					alert.confirmationAlert(ChooseOnlineOfflineActivity.this, getResources()
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

			}
		});
		

		imageView_on_online = (ImageView) findViewById(R.id.imageView_on_online);
		imageView_on_offline = (ImageView) findViewById(R.id.imageView_on_offline);
		
		
		LinearLayout linearLayout_online = (LinearLayout) findViewById(R.id.linearLayout_online);
		LinearLayout linearLayout_offline = (LinearLayout) findViewById(R.id.linearLayout_offline);
		
		if(isOnline){
			imageView_on_online.setImageResource(R.drawable.on);
			imageView_on_offline.setImageResource(R.drawable.off);
		}
		
		linearLayout_online.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isOnline=true;
					imageView_on_online.setImageResource(R.drawable.on);
					imageView_on_offline.setImageResource(R.drawable.off);
				
				
			}
		});
		
		linearLayout_offline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isOnline=false;
				imageView_on_offline.setImageResource(R.drawable.on);
				imageView_on_online.setImageResource(R.drawable.off);
			}
		});

	}

	void showDialog(String msgStr, final EditText editText) {
		final Dialog dialog = new Dialog(ChooseOnlineOfflineActivity.this);
		dialog.setContentView(R.layout.custom_alert_popup1);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		TextView title = (TextView) dialog.findViewById(R.id.textView1);
		title.setVisibility(TextView.GONE);
		TextView msg = (TextView) dialog.findViewById(R.id.textView2);
		msg.setText(msgStr);

		TextView ok = (TextView) dialog.findViewById(R.id.textView_ok);
		ok.setText("Ok");
		LinearLayout lay_abort = (LinearLayout) dialog
				.findViewById(R.id.lay_abort);
		// if button is clicked, close the custom dialog
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (editText != null)
					editText.setText("");
			}
		});
		lay_abort.setVisibility(TextView.GONE);

		dialog.show();
	}
}
