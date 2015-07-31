package com.app.laundry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.laundry.Manage_Address.ViewHolder;
import com.app.laundry.json.JGetParsor;
import com.app.laundry.lazyloading.ImageLoader;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RateListActivity extends Fragment {
	
	ImageView imageView_banner,imageView_small_banner;
	ImageLoader imageLoader;
	JSONObject json;
	Handler mHandler=new Handler();
	ListView list;
	private Add_Adapter list_adapter;
	ArrayList<HashMap<String, String>> array_list=new ArrayList<HashMap<String,String>>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.new_order_history, container,false);
		
		imageView_banner=(ImageView)view.findViewById(R.id.imageView_banner);
		imageView_small_banner=(ImageView)view.findViewById(R.id.imageView_small_banner);
		
		imageView_banner.setVisibility(View.GONE);
		imageView_small_banner.setVisibility(View.GONE);
		
		list = (ListView)view.findViewById(R.id.listView1);
		
		if(Network.HaveNetworkConnection(getActivity())){
			getAllOrders();
			//new loginAccess().execute();
			}
			else{
				AlertUtil alert=new AlertUtil();
				alert.messageAlert(getActivity(),
						getResources()
								.getString(R.string.network_title),
						getResources().getString(
								R.string.network_message));
			}
		
		return view;
	}
	
	private void getAllOrders(){
		ProgressDialogClass.showProgressDialog(getActivity(),"Loading...");
		
		final Thread fetch_address=new Thread(){
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("user_id", Config.userid));
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.get_order,"POST", params);
				
				if(json!=null){
					try {
						if(json.getInt("status")==200){
							JSONArray json_array=json.getJSONArray("data");
							
							for(int i=0; i<json_array.length();i++){
								HashMap<String, String> hashmap=new HashMap<String, String>();
								JSONObject j_obj=json_array.getJSONObject(i);
								
								hashmap.put("LaundryOrderDate", j_obj.getString("LaundryOrderDate"));
								hashmap.put("LaundryOrderID", j_obj.getString("LaundryOrderID"));
								hashmap.put("Total", j_obj.getString("Total"));
								
								array_list.add(hashmap);	
							}
							
						}
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
							list_adapter=new Add_Adapter(getActivity());
							list.setAdapter(list_adapter);
							getBanner();
							ProgressDialogClass.dismissProgressDialog();
						}
					});
				}
				
			}
		});
		display.start();
	}
	
	private class Add_Adapter extends BaseAdapter{
		private LayoutInflater mInflater;
		
		public Add_Adapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(array_list.size()==0){
				return 0;
			}
			return array_list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null || convertView.getTag() == null) {
				convertView = mInflater.inflate(R.layout.order_history, null);
				holder = new ViewHolder();
				
				holder.textView1 = (TextView)convertView.findViewById(R.id.textView1);
				holder.textView3 = (TextView)convertView.findViewById(R.id.textView3);
				holder.textView2 = (TextView)convertView.findViewById(R.id.textView2);
				holder.main=(LinearLayout)convertView.findViewById(R.id.main);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String date=array_list.get(position).get("LaundryOrderDate");
			String[] d_date=date.split(" ");
			date=d_date[0];
			holder.textView1.setText(date);
			holder.textView2.setText(array_list.get(position).get("LaundryOrderID"));
			holder.textView3.setText(array_list.get(position).get("Total"));
			
			holder.main.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle data = new Bundle();
					Fragment fragment=new Invoice();
					
					data.putString("LaundryOrderID", array_list.get(position).get("LaundryOrderID"));
					
					fragment.setArguments(data);
					
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();
					
				}
			});
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		TextView textView1,textView2,textView3;
		LinearLayout main;
	}
	
	
	private void getBanner(){
		imageLoader=new ImageLoader(getActivity());
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
}
