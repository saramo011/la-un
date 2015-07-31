package com.app.laundry;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.json.JsonReturn;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;

public class LaundryReview extends ActionBarActivity {

	JSONArray searchResult = new JSONArray();
	Handler mHandler = new Handler();
	private EfficientAdapter list_ed;
	ListView listView_laundry_review;
	JSONObject json=null;
	ArrayList<HashMap<String, String>> array_list=new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review_laundry);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);
	    
	    
		Intent intent=getIntent();
		
		listView_laundry_review = (ListView) findViewById(R.id.listView_laundry_review);
		
		//TextView textView_laundryname=(TextView)findViewById(R.id.textView_laundryname);
		//textView_laundryname.setText(intent.getExtras().getString("LaundryName"));
		
		 bar.setTitle( intent.getExtras().getString("LaundryName")+" Reviews");
		
		if(Network.HaveNetworkConnection(LaundryReview.this)){
			getReview(intent.getExtras().getString("LaundryId"));
			}
			else{
				AlertUtil alert=new AlertUtil();
				alert.messageAlert(LaundryReview.this,
						getResources()
								.getString(R.string.network_title),
						getResources().getString(
								R.string.network_message));
			}
		
	}

	void getReview(final String laundryId) {

		json=null;
		array_list.clear();
		ProgressDialogClass.showProgressDialog(LaundryReview.this,
				"Loading...");
		final Thread splashTread = new Thread() {
			@Override
			public void run() {

				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.Review_Url+laundryId,"POST", params);

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
								try {
									if(json.getInt("status")==200){
										JSONArray json_array=json.getJSONArray("data");
										
										for(int i=0; i<json_array.length();i++){
											HashMap<String, String> hashmap=new HashMap<String, String>();
											JSONObject j_obj=json_array.getJSONObject(i);
											
											hashmap.put("avgratings", j_obj.getString("avgratings"));
											hashmap.put("Comments", j_obj.getString("Comments"));
											hashmap.put("CreatedOn", j_obj.getString("CreatedOn"));
											hashmap.put("Name", j_obj.getString("Name"));
											array_list.add(hashmap);	
										}
										
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
								}
							}
							
							list_ed = new EfficientAdapter(LaundryReview.this);
							listView_laundry_review.setAdapter(list_ed);
							
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
			if(array_list.size()>0)
				return array_list.size();
			return 0;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;

			if (convertView == null || convertView.getTag() == null) {
				convertView = mInflater.inflate(R.layout.review_row, null);
				holder = new ViewHolder();
				
				holder.textView_review = (TextView) convertView
						.findViewById(R.id.textView_review);
				
				holder.textView12 = (TextView) convertView
						.findViewById(R.id.textView12);
				
				holder.textView_date = (TextView) convertView
						.findViewById(R.id.textView_date);
				
				holder.imageView_star1 = (ImageView) convertView
						.findViewById(R.id.imageView_star1);
				holder.imageView_star2 = (ImageView) convertView
						.findViewById(R.id.imageView_star2);
				holder.imageView_star3 = (ImageView) convertView
						.findViewById(R.id.imageView_star3);
				holder.imageView_star4 = (ImageView) convertView
						.findViewById(R.id.imageView_star4);
				holder.imageView_star5 = (ImageView) convertView
						.findViewById(R.id.imageView_star5);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.textView_review.setText(array_list.get(position).get("Comments"));
			holder.textView_date.setText(array_list.get(position).get("CreatedOn"));
			holder.textView12.setText(array_list.get(position).get("Name"));
			
			float avgratings = (float) 0.0;
			try {			
				
				avgratings = Float.parseFloat(array_list.get(position).get("avgratings"));
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}


			
			int rateInt = (int) avgratings;
			
			switch (rateInt) {
			case 5:
				holder.imageView_star5.setImageResource(R.drawable.rating_after);
			case 4:
				holder.imageView_star4.setImageResource(R.drawable.rating_after);
			case 3:
				holder.imageView_star3.setImageResource(R.drawable.rating_after);
			case 2:
				holder.imageView_star2.setImageResource(R.drawable.rating_after);
			case 1:
				holder.imageView_star1.setImageResource(R.drawable.rating_after);
			
				break;

			default:
				break;
			}
			
			if(avgratings-rateInt>0){
				rateInt++;
			
				switch (rateInt) {
				case 5:
					holder.imageView_star5.setImageResource(R.drawable.star_half);
					break;
				case 4:
					holder.imageView_star4.setImageResource(R.drawable.star_half);
					break;
				case 3:
					holder.imageView_star3.setImageResource(R.drawable.star_half);
					break;
				case 2:
					holder.imageView_star2.setImageResource(R.drawable.star_half);
					break;
				case 1:
					holder.imageView_star1.setImageResource(R.drawable.star_half);
				
					break;
	
				default:
					break;
				}
			}
			
			return convertView;
		}

	}

	static class ViewHolder {
		TextView textView_review, textView12;
		TextView textView_date;
		ImageView imageView_star1,imageView_star2, imageView_star3,
				imageView_star4, imageView_star5;
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
			  startActivity(new Intent(LaundryReview.this,
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