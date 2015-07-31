package com.app.laundry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.lazyloading.ImageLoader;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Manage_Address extends Fragment{
	
	Handler mHandler = new Handler();
	JSONObject json;
	
	ImageView imageView_banner;
	ImageLoader imageLoader;
	ListView listView_address;
	Button bt_add_address;
	

	ArrayList<HashMap<String, String>> array_list=new ArrayList<HashMap<String,String>>();
	private Addadapter list_adapter;
	
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

		View view = inflater.inflate(R.layout.new_add_address, container,false);
		
		imageView_banner = (ImageView)view.findViewById(R.id.imageView_banner);
		
		
		listView_address = (ListView)view.findViewById(R.id.listView_laundry1);
		bt_add_address = (Button)view.findViewById(R.id.button1);
		
		if(mContext==null)
	 		mContext = getActivity();
		
		imageView_banner.setVisibility(View.GONE);
		bt_add_address.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Fragment fragment=new Add_Address();
				android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment,"add_address").addToBackStack("manage_address").commit();

			}
		});
		
		
		if(Network.HaveNetworkConnection(getActivity())){
			getAllAddresses();
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
//	//No need of this class, we can use it instead of getAllAddresses method
//	class loginAccess extends AsyncTask<String, String, String> {
//		
//		protected void onPreExecute() {
//			ProgressDialogClass.showProgressDialog(getActivity(),"Loading...");
//		}
//		
//		@Override
//		protected String doInBackground(String... params) {
//			// TODO Auto-generated method stub
//			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
//			JGetParsor j=new JGetParsor();
//			json = j.makeHttpRequest(Config.manage_addresses+Config.userid,"POST", params2);
//			
//			if(json!=null){
//				try {
//					if(json.getInt("status")==200){
//						JSONArray json_array=json.getJSONArray("data");
//						for(int i=0; i<json_array.length();i++){
//							HashMap<String, String> hashmap=new HashMap<String, String>();
//							JSONObject j_obj=json_array.getJSONObject(i);
//							
//							hashmap.put("AddressID", j_obj.getString("AddressID"));
//							hashmap.put("UserID", j_obj.getString("UserID"));
//							hashmap.put("AddressName", j_obj.getString("AddressName"));
//							hashmap.put("CityID", j_obj.getString("CityID"));
//							hashmap.put("CountryID", j_obj.getString("CountryID"));
//							hashmap.put("ContactNo", j_obj.getString("ContactNo"));
//							
//							hashmap.put("AddressLine1", j_obj.getString("AddressLine1"));
//							hashmap.put("AddressLine2", j_obj.getString("AddressLine2"));
//							hashmap.put("AddressLine3", j_obj.getString("AddressLine3"));
//							hashmap.put("countryName", j_obj.getString("countryName"));
//							hashmap.put("CityName", j_obj.getString("CityName"));
//							hashmap.put("DefaultAddress", j_obj.getString("DefaultAddress"));
//							
//							array_list.add(hashmap);
//							
//						}
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//				}
//			}
//			return null;
//		}
//		
//		@Override
//		protected void onPostExecute(String file_url) {
//			if(mContext==null)
//		 		mContext = getActivity();
//			list_adapter=new Addadapter(mContext);
//			listView_address.setAdapter(list_adapter);
//			ProgressDialogClass.dismissProgressDialog();
//		}
//		
//	}
	
	void getAllAddresses(){
		ProgressDialogClass.showProgressDialog(getActivity(),"Loading...");
		json=null;
		array_list.clear();
		final Thread fetch_address=new Thread(){
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.manage_addresses+Config.userid,"POST", params);
				if(json!=null){
					try {
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
							if(mContext==null)
						 		mContext = getActivity();
							list_adapter=new Addadapter(mContext);
							listView_address.setAdapter(list_adapter);
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
	
	private class Addadapter extends BaseAdapter {
		private LayoutInflater mInflater;
		
		public Addadapter(Context context) {
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
				convertView = mInflater.inflate(R.layout.new_add_address_items, null);
				holder = new ViewHolder();
				
				holder.AddressID = (TextView)convertView.findViewById(R.id.AddressID);
				holder.UserID = (TextView)convertView.findViewById(R.id.UserID);
				holder.AddressName = (TextView)convertView.findViewById(R.id.AddressName);
				holder.CityID = (TextView)convertView.findViewById(R.id.CityID);
				holder.CountryID = (TextView)convertView.findViewById(R.id.CountryID);
				
				holder.ContactNo = (TextView)convertView.findViewById(R.id.ContactNo);
				holder.AddressLine1 = (TextView)convertView.findViewById(R.id.AddressLine1);
				holder.AddressLine2 = (TextView)convertView.findViewById(R.id.AddressLine2);
				holder.AddressLine3 = (TextView)convertView.findViewById(R.id.AddressLine3);
				holder.countryName = (TextView)convertView.findViewById(R.id.countryName);
				holder.CityName = (TextView)convertView.findViewById(R.id.CityName);
				
				holder.DefaultAddress = (TextView)convertView.findViewById(R.id.DefaultAddress);
				
				holder.textView1 = (TextView)convertView.findViewById(R.id.textView1);
			/*	holder.textView4 = (TextView)convertView.findViewById(R.id.textView4);
				holder.textView2 = (TextView)convertView.findViewById(R.id.textView2);
				*/
				holder.imageView1=(ImageView)convertView.findViewById(R.id.imageView1);
				holder.imageViewDiscard=(ImageView)convertView.findViewById(R.id.imageViewDiscard);
				holder.imageViewEdit=(ImageView)convertView.findViewById(R.id.imageViewEdit);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.AddressID.setText(array_list.get(position).get("AddressID"));
			holder.UserID.setText(array_list.get(position).get("UserID"));
			holder.AddressName.setText(array_list.get(position).get("AddressName"));
			holder.CityID.setText(array_list.get(position).get("CityID"));
			holder.CountryID.setText(array_list.get(position).get("CountryID"));
			
			holder.ContactNo.setText(array_list.get(position).get("ContactNo"));
			holder.AddressLine1.setText(array_list.get(position).get("AddressLine1"));
			holder.AddressLine2.setText(array_list.get(position).get("AddressLine2"));
			holder.AddressLine3.setText(array_list.get(position).get("AddressLine3"));
			holder.countryName.setText(array_list.get(position).get("countryName"));
			holder.CityName.setText(array_list.get(position).get("CityName"));
			holder.DefaultAddress.setText(array_list.get(position).get("DefaultAddress"));
			
			if(array_list.get(position).get("DefaultAddress").equals("0")){
				holder.imageView1.setImageResource(R.drawable.off);
			}
			else{
				holder.imageView1.setImageResource(R.drawable.on);
			}
			
			String full_address=array_list.get(position).get("AddressName")+"\n"+
					array_list.get(position).get("AddressLine1")+", "+
					array_list.get(position).get("AddressLine2")+", "+
					array_list.get(position).get("AddressLine3")+",\n"+
					array_list.get(position).get("CityName")+"\n"+
					array_list.get(position).get("countryName")+"\n"+
					array_list.get(position).get("ContactNo");
			
			holder.textView1.setText(full_address);
			//holder.textView4.setText(Html.fromHtml("<u>Edit</u>"));
			
			holder.imageView1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(array_list.get(position).get("DefaultAddress").equals("0")){
						
						updateDefault(position);
					}
				}
			});
			
			//holder.textView4.setOnClickListener(new View.OnClickListener() {
				holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle data = new Bundle();
					Fragment fragment=new Edit_Address();
					
					data.putString("AddressID", array_list.get(position).get("AddressID"));
					data.putString("UserID", array_list.get(position).get("UserID"));
					data.putString("AddressName", array_list.get(position).get("AddressName"));
					data.putString("CityID", array_list.get(position).get("CityID"));
					data.putString("CountryID", array_list.get(position).get("CountryID"));
					data.putString("ContactNo", array_list.get(position).get("ContactNo"));
					
					data.putString("AddressLine1", array_list.get(position).get("AddressLine1"));
					data.putString("AddressLine2", array_list.get(position).get("AddressLine2"));
					data.putString("AddressLine3", array_list.get(position).get("AddressLine3"));
					data.putString("countryName", array_list.get(position).get("countryName"));
					data.putString("CityName", array_list.get(position).get("CityName"));
					data.putString("DefaultAddress", array_list.get(position).get("DefaultAddress"));
					
					data.putInt("position", position);
					
					fragment.setArguments(data);
					
					android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment,"edit_address").addToBackStack("manage_address").commit();
				}
			});
			
//			holder.textView2.setText(Html.fromHtml("<u>Delete</u>"));
//			
//			holder.textView2.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					delete_address(position);
//				}
//			});
//			//holder.textView2.setText(Html.fromHtml("<u>Delete</u>"));
			
			holder.imageViewDiscard.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					   Builder builder=new AlertDialog.Builder(mContext);
			            builder.setMessage("Are you sure you want to delete this address?");
			            builder.setTitle("Warning");
			//button 
			            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			                 
			                  @Override
			                  public void onClick(DialogInterface dialog, int which)
			                  {
			                       // Toast.makeText(mContext, "Nothing happened", Toast.LENGTH_LONG).show();
			                       
			                  }
			            });
			//button
			      builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			                 
			                  @Override
			                  public void onClick(DialogInterface dialog, int which)
			                  {
			                        //finish();
			                	  delete_address(position);
			                       
			                  }
			            }); 
			      AlertDialog dialog=builder.create();
			      dialog.show();
					
				}
				//
		
			});
			return convertView;
		}
		
		protected void delete_address(final int position){
			// TODO Auto-generated method stub
						ProgressDialogClass.showProgressDialog(getActivity(),"Deleting...");
						json=null;
						final Thread update=new Thread(){
							@Override
							public void run(){
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								JGetParsor j=new JGetParsor();
								json = j.makeHttpRequest(Config.delete_address+array_list.get(position).get("AddressID"),"POST", params);
							}
						};
						update.start();
						
						final Thread show=new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									update.join();
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
														array_list.remove(position);
														list_adapter.notifyDataSetChanged();
														//ProgressDialogClass.dismissProgressDialog();
													}
												} catch (JSONException e) {
													// TODO Auto-generated catch block
													//e.printStackTrace();
												}
											}
											
											ProgressDialogClass.dismissProgressDialog();
										}
									});
								}
							}
						});
						show.start();
		}
		
		protected void updateDefault(final int position) {
			// TODO Auto-generated method stub
			ProgressDialogClass.showProgressDialog(getActivity(),"Updating...");
			json=null;
			final Thread update=new Thread(){
				@Override
				public void run(){
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("DefaultAddress", "1"));
					JGetParsor j=new JGetParsor();
					json = j.makeHttpRequest(Config.update_default_address+array_list.get(position).get("AddressID"),"POST", params);
				}
			};
			update.start();
			
			final Thread show=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						update.join();
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
											for(int i=0; i<array_list.size();i++)
												array_list.get(i).put("DefaultAddress", "0");
											array_list.get(position).put("DefaultAddress", "1");
											list_adapter.notifyDataSetChanged();
											
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										//e.printStackTrace();
									}
								}
								ProgressDialogClass.dismissProgressDialog();
							}
						});
					}
				}
			});
			show.start();
		}
		
	}
	
	static class ViewHolder {
		//TextView textView_number;
		TextView AddressID,UserID,AddressName,CityID,CountryID,ContactNo,
		AddressLine1,
		AddressLine2,//Landmark
		AddressLine3,//ZipCode
		countryName,
		CityName,
		textView1,DefaultAddress;
		//textView4,		textView2,

		ImageView imageView1;
		ImageView imageViewDiscard;
		ImageView imageViewEdit;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    int city_array_size=Config.cityArray.size();
	    if(city_array_size>0)
	    	for(int i=0;i<city_array_size; i++)
	    		menu.findItem(i).setVisible(false);
	}
	
//	@Override
//	public void onResume() {
//		list_adapter.notifyDataSetChanged();
//	}
}
