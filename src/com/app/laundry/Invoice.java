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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.lazyloading.ImageLoader;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;

@SuppressWarnings("deprecation")
public class Invoice extends Fragment {
	
	JSONObject json;
	ImageView imageView_banner;
	ImageLoader imageLoader;
	Handler mHandler = new Handler();
	String LaundryOrderID;
	TextView tv1,tv2;
	ListView list;
	private Add_Adapter list_adapter;
	Button button_rate;
	String laundryId;
	ArrayList<HashMap<String, String>> array_list=new ArrayList<HashMap<String,String>>();

	
	Context mContext=null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(mContext==null)
        	mContext = getActivity();
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	 	if(mContext==null)
	 		mContext = getActivity();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.new_invoice, container,false);
		imageView_banner = (ImageView)view.findViewById(R.id.imageView_banner);
		
		list = (ListView)view.findViewById(R.id.listView1);
		
		Bundle bundle=getArguments();
		LaundryOrderID=bundle.getString("LaundryOrderID");
		
		tv1=(TextView)view.findViewById(R.id.textView1);
		tv2=(TextView)view.findViewById(R.id.textView2);
		
		if(mContext==null)
	 		mContext = getActivity();
		
		button_rate=(Button)view.findViewById(R.id.button1);
		button_rate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getActivity(),New_Rate.class);
				i.putExtra("LaundryID", laundryId);
				startActivity(i);
				
				
			}
		});
		
		if(Network.HaveNetworkConnection(getActivity())){
			getDetails();
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

	private void getDetails(){
		ProgressDialogClass.showProgressDialog(getActivity(),"Loading...");
		array_list.clear();
		
		final Thread fetch_address=new Thread(){
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("user_id", Config.userid));
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.get_order+"/order_id/"+LaundryOrderID,"POST", params);
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

							if(json!=null){
								try {
									if(json.getInt("status")==200){
										JSONObject jo_order_titles=json.getJSONObject("data");
										tv1.setText(jo_order_titles.getString("LaundryName"));
										
										JSONObject pickup_add=jo_order_titles.getJSONObject("pickup_address");
										
										String pickup_address="";
										if(!pickup_add.getString("AddressLine1").equals(""))
											pickup_address+=pickup_add.getString("AddressLine1")+", ";
										if(!pickup_add.getString("AddressLine2").equals(""))
											pickup_address+=pickup_add.getString("AddressLine2")+", ";
										if(!pickup_add.getString("AddressLine3").equals(""))
											pickup_address+=pickup_add.getString("AddressLine3")+", ";
										if(!pickup_add.getString("CityName").equals(""))
											pickup_address+=pickup_add.getString("CityName")+", ";
										if(!pickup_add.getString("countryName").equals(""))
											pickup_address+=pickup_add.getString("countryName");
										
										JSONObject dropoff_address=jo_order_titles.getJSONObject("pickup_address");
										
										String drop_address="";
										if(!dropoff_address.getString("AddressLine1").equals(""))
											drop_address+=dropoff_address.getString("AddressLine1")+", ";
										if(!dropoff_address.getString("AddressLine2").equals(""))
											drop_address+=dropoff_address.getString("AddressLine2")+", ";
										if(!dropoff_address.getString("AddressLine3").equals(""))
											drop_address+=dropoff_address.getString("AddressLine3")+", ";
										if(!dropoff_address.getString("CityName").equals(""))
											drop_address+=dropoff_address.getString("CityName")+", ";
										if(!dropoff_address.getString("countryName").equals(""))
											drop_address+=dropoff_address.getString("countryName");
											
										String[] date=jo_order_titles.getString("LaundryOrderDate").split("\\s");
												
										String titles="<b>Date:</b> "+date[0]+"<br><b>Order Id:</b> "+LaundryOrderID+"<br><b>Pick-up Address:</b> "+pickup_address+"<br><b>Drop-Off Address:</b> "+drop_address+"";
										tv2.setText(Html.fromHtml(titles));
										laundryId=jo_order_titles.getString("LaundryID");

										JSONArray j_array=jo_order_titles.getJSONArray("detail");
										
										for(int i=0; i<j_array.length();i++){
											HashMap<String, String> hashmap=new HashMap<String, String>();
											JSONObject j_obj=j_array.getJSONObject(i);

											hashmap.put("LaundryItemName", j_obj.getString("LaundryItemName"));
											hashmap.put("LaundryServiceName", j_obj.getString("LaundryServiceName"));
											hashmap.put("Price", j_obj.getString("Price"));
											hashmap.put("Quantity", j_obj.getString("Quantity"));
											hashmap.put("Amount", j_obj.getString("Amount"));
											
											array_list.add(hashmap);	
										}
										
										HashMap<String, String> hashmap=new HashMap<String, String>();
										
										hashmap.put("LaundryItemName", " ");
										hashmap.put("LaundryServiceName", " ");
										hashmap.put("Price", " ");
										hashmap.put("Quantity", "Total Amount");
										hashmap.put("Amount", jo_order_titles.getString("Total"));
										
										array_list.add(hashmap);	
										
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
								}
							}
							
							if(mContext==null)
						 		mContext = getActivity();
							
							list_adapter=new Add_Adapter(mContext);
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
	
	private void getBanner(){
		imageLoader=new ImageLoader(mContext);
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
				convertView = mInflater.inflate(R.layout.new_invoice_items, null);
	
				holder = new ViewHolder();
				
				holder.item = (TextView)convertView.findViewById(R.id.item);
				holder.service = (TextView)convertView.findViewById(R.id.service);
				holder.rate = (TextView)convertView.findViewById(R.id.rate);
				holder.quantity=(TextView)convertView.findViewById(R.id.quantity);
				holder.amount=(TextView)convertView.findViewById(R.id.amount);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(position==array_list.size()-1){
				//String item=break_string("-", );			
				holder.item.setText(array_list.get(position).get("LaundryItemName"));
				
				//String service=break_string("+", array_list.get(position).get("LaundryServiceName"));			
				holder.service.setText(array_list.get(position).get("LaundryServiceName"));
				
				//Float rate=Float.parseFloat(array_list.get(position).get("Price"));
				//int r=Math.round(rate);
				holder.rate.setText(array_list.get(position).get("Price"));
		
				holder.quantity.setText(array_list.get(position).get("Quantity"));
				try{
					Float amount=Float.parseFloat(array_list.get(position).get("Amount"));
					int a=Math.round(amount);
					holder.amount.setText(a+"");
				}catch(NumberFormatException e){
					holder.amount.setText(" ");
				}
			}else{
				
			String item=break_string("-", array_list.get(position).get("LaundryItemName"));			
			holder.item.setText(item);
			
			String service=break_string("+", array_list.get(position).get("LaundryServiceName"));			
			holder.service.setText(service);
			try{
				Float rate=Float.parseFloat(array_list.get(position).get("Price"));
				int r=Math.round(rate);
				holder.rate.setText(r+"");
			}catch(NumberFormatException e){
				holder.rate.setText(" ");
			}
	
			holder.quantity.setText(array_list.get(position).get("Quantity"));
			
			try{
				Float amount=Float.parseFloat(array_list.get(position).get("Amount"));
				int a=Math.round(amount);
				holder.amount.setText(a+"");
			}catch(NumberFormatException e){
				holder.amount.setText(" ");
			}
			}
			return convertView;
		}

		private String break_string(String main_str, String str) {
			// TODO Auto-generated method stub
			String[] arr=str.split("\\s");
			main_str="";
			for(int i=0;i<arr.length;i++)
				main_str+=arr[i]+"\n";

			return main_str;
		}
		
	}
	
	static class ViewHolder{
		TextView item,service,rate,quantity,amount;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    int city_array_size=Config.cityArray.size();
	    if(city_array_size>0)
	    	for(int i=0;i<city_array_size; i++)
	    		menu.findItem(i).setVisible(false);
	}
}
