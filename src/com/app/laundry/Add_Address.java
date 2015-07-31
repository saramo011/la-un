package com.app.laundry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.lazyloading.ImageLoader;
import com.app.laundry.util.ProgressDialogClass;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Add_Address extends Fragment {
	String AddressName,CityID,CountryID,ContactNo,AddressLine1,AddressLine2,
	AddressLine3,countryName,CityName;
	int position;
	JSONObject json,json1,json2;
	ArrayList<String> city=new ArrayList<String>();
	ArrayList<String> country=new ArrayList<String>();
	
	ArrayList<String> city_id=new ArrayList<String>();
	ArrayList<String> country_id=new ArrayList<String>();
	
	Handler mHandler=new Handler();
	
	Spinner sp1,sp2;
	EditText add_name,cont_no, add_lane1, add_lane2, add_lane3;
	Button bt_save;
	ImageView imageView_banner,imageView_small_banner;
	ImageLoader imageLoader;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.new_edit_address, container,false);
		
		add_name=(EditText)view.findViewById(R.id.AddressName);
		cont_no=(EditText)view.findViewById(R.id.ContactNo);
		add_lane1=(EditText)view.findViewById(R.id.AddressLine1);
		add_lane2=(EditText)view.findViewById(R.id.AddressLine2);
		add_lane3=(EditText)view.findViewById(R.id.AddressLine3);
		
		bt_save=(Button)view.findViewById(R.id.bt_save);
		
		imageView_banner=(ImageView)view.findViewById(R.id.imageView_banner);
		imageView_small_banner=(ImageView)view.findViewById(R.id.imageView_small_banner);
		
		imageView_banner.setVisibility(View.GONE);
		imageView_small_banner.setVisibility(View.GONE);
		
		sp1=(Spinner)view.findViewById(R.id.spinner1);
		sp2=(Spinner)view.findViewById(R.id.spinner2);

		bt_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AddressName=add_name.getText().toString();
				
				if(AddressName.length()<2){
					Toast.makeText(getActivity(), "Enter short name!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				AddressLine1=add_lane1.getText().toString();
				if(AddressLine1.length()<4){
					Toast.makeText(getActivity(), "Enter correct Address!", Toast.LENGTH_SHORT).show();	
					return;
				}
				
				ContactNo=cont_no.getText().toString();
				if(ContactNo.length()<5){
					Toast.makeText(getActivity(), "Enter correct contact!", Toast.LENGTH_SHORT).show();	
					return;
				}
				
				AddressLine2=add_lane2.getText().toString();
				AddressLine3=add_lane3.getText().toString();
				
				CityName=sp1.getSelectedItem().toString();
				int city_index=city.indexOf(CityName);
				if(city_index==-1)
					CityID=(city_index+1)+"";
				else
					CityID=city_id.get(city_index);
				
				countryName=sp2.getSelectedItem().toString();
				int country_index=country.indexOf(countryName);
				if(country_index==-1)
					CountryID=(country_index+1)+"";
				else
					CountryID=country_id.get(country_index);
				
				updateDefault();
			}
		});
		
		update_city_country();
		return view;
	}
	
	protected void updateDefault() {
		// TODO Auto-generated method stub
		ProgressDialogClass.showProgressDialog(getActivity(),"Saving...");
		json=null;
		final Thread update=new Thread(){
			@SuppressWarnings("deprecation")
			@Override
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("AddressName", AddressName));
				params.add(new BasicNameValuePair("ContactNo", ContactNo));
				params.add(new BasicNameValuePair("AddressLine1",AddressLine1));
				params.add(new BasicNameValuePair("AddressLine2", AddressLine2));
				params.add(new BasicNameValuePair("AddressLine3", AddressLine3));
				params.add(new BasicNameValuePair("CityID", CityID));
				params.add(new BasicNameValuePair("CountryID", CountryID));
				
				params.add(new BasicNameValuePair("UserID", Config.userid));
				params.add(new BasicNameValuePair("DefaultAddress", 0+""));
				
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.add_address,"POST", params);
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
										Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
										Fragment fragment=new Manage_Address();
										android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
										fragmentManager.beginTransaction()
												.replace(R.id.frame_container, fragment).commit();
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
	
	private void update_city_country() {
		// TODO Auto-generated method stub
		ProgressDialogClass.showProgressDialog(getActivity(),"Loading...");
		final Thread getdata=new Thread(){
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JGetParsor j=new JGetParsor();
				json1 = j.makeHttpRequest(Config.new_city_url,"POST", params);
				json2 = j.makeHttpRequest(Config.new_country_url,"POST", params);
				if(json1!=null){
					try {
						if(json1.getInt("status")==200){
							JSONArray j_arr=json1.getJSONArray("data");
							for(int i=0; i<j_arr.length();i++){
								JSONObject j_obj=j_arr.getJSONObject(i);
								city.add(j_obj.getString("CityName"));
								city_id.add(j_obj.getString("CityID"));
							}		
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
					
					if(json2!=null){
						try {
							if(json2.getInt("status")==200){
								JSONArray j_arr=json2.getJSONArray("data");
								for(int i=0; i<j_arr.length();i++){
									JSONObject j_obj=j_arr.getJSONObject(i);
									country.add(j_obj.getString("countryName"));
									country_id.add(j_obj.getString("CountryID"));
								}		
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
				}

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
							if(city.size()>0){
								sp1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, city));
							}
							if(country.size()>0){
								sp2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, country));
							}
							getBanner();
							ProgressDialogClass.dismissProgressDialog();
						}
					});
				}
			}
		});
		show.start();
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
