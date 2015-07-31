package com.app.laundry;



import java.util.ArrayList;

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
import android.os.IBinder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.laundry.json.JsonReturn;
import com.app.laundry.lazyloading.ImageLoader;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;

public class SearchActivity extends ActionBarActivity {

	JSONArray searchResult = new JSONArray();
	Handler mHandler = new Handler();
	private EfficientAdapter list_ed;
	ListView listView_laundry;
	ProgressBar progressBar1;
	ImageView imageView_banner;
	ImageLoader imageLoader;
	SearchView mSearchView;
	ActionMode actionMode;
	ArrayList<JSONObject> bannerArray = new ArrayList<JSONObject>();
	String searchText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_laundry);
	
		

		Intent intent=getIntent();
		 searchText=intent.getExtras().getString("SearchText");
		 
		ActionBar actionBar=getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		actionBar.startActionMode(mActionModeCallback);
		
		
		LinearLayout linearLayout1=(LinearLayout)findViewById(R.id.linearLayout1);
		linearLayout1.setVisibility(View.GONE);
		
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.GONE);
		listView_laundry = (ListView) findViewById(R.id.listView_laundry);
		 imageView_banner = (ImageView) findViewById(R.id.imageView_banner);
		
		InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		
		imageView_banner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		
			

		listView_laundry.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SearchActivity.this,
						laundryDetailActivity.class);
				try {
					intent.putExtra("LaundryID", searchResult.getJSONObject(position).getString("LaundryID"));
					intent.putExtra("LaundryName", searchResult.getJSONObject(position).getString("LaundryName"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				startActivity(intent);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});

		list_ed = new EfficientAdapter(SearchActivity.this);
		listView_laundry.setAdapter(list_ed);
		
		
			imageLoader = new ImageLoader(SearchActivity.this
					.getApplicationContext());
			 if(Network.HaveNetworkConnection(SearchActivity.this)){
				float ppi = getResources().getDisplayMetrics().density;
				 int height = (int) (90 * ppi);
				 imageView_banner.getLayoutParams().height = height;
			GetBanner();
			if(searchText.length()>0){
			getLaundry(searchText);
			}
			 }
			 else{
				 final AlertUtil alert=new AlertUtil();
				 alert.confirmationAlert(SearchActivity.this, getResources()
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

	void getLaundry(final String searchtext) {

		progressBar1.setVisibility(View.VISIBLE);
		

		final Thread splashTread = new Thread() {
			@Override
			public void run() {

				ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

				// nameValuePairs.add(new BasicNameValuePair("iemail",
				// emailId));
				// nameValuePairs.add(new BasicNameValuePair("ipwd", password));
				String url = String.format(Config.Get_All_Laundry, "search");

					
				 
					 nameValuePairs.add(new BasicNameValuePair("q",searchtext));
				

				 JsonReturn jsonReturn=new JsonReturn();
				searchResult = jsonReturn.postLaundryData(url, nameValuePairs);

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
							if(searchResult!=null){
							if (searchResult.length() > 0) {
								try {

									if (!searchResult.getJSONObject(0).has(
											"status")) {
										list_ed.notifyDataSetChanged();
									} else {
										final AlertUtil alert=new AlertUtil();
										 alert.confirmationAlert(SearchActivity.this, "", searchResult.getJSONObject(0).getString("stauts_message"),
													new Button.OnClickListener() {

														@Override
														public void onClick(View v) {
															alert.release();
															
														}
													});
										searchResult=new JSONArray();
										list_ed.notifyDataSetChanged();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
							}
							progressBar1.setVisibility(View.GONE);

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
			if(searchResult!=null){
				try {
					if (searchResult.getJSONObject(0).has(
							"status")) 
						return 0;
					else
						return searchResult.length();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return 0;
				}
			}
			
			else
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
				convertView = mInflater.inflate(R.layout.laundary_row, null);
				holder = new ViewHolder();
				holder.textView_laundry_name = (TextView) convertView
						.findViewById(R.id.textView_laundry_name);
				holder.textView_description = (TextView) convertView
						.findViewById(R.id.textView_description);
				holder.imageView_num = (Button) convertView
						.findViewById(R.id.imageView_num);
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
				holder.background_imageView=(ImageView) convertView
						.findViewById(R.id.imageView);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if(position%2==0){
				holder.background_imageView.setBackgroundResource(R.drawable.list_row_background1);
			}
			else{
				holder.background_imageView.setBackgroundResource(R.drawable.list_row_background2);
			}
			
			float rate = (float) 0.0;
			try {
				holder.textView_laundry_name.setText(searchResult
						.getJSONObject(position).getString("LaundryName"));
				holder.textView_description.setText(searchResult.getJSONObject(
						position).getString("LaundryAddress"));
				
				try {
					rate = Float.parseFloat(searchResult.getJSONObject(position)
							.getString("avgratings"));
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			holder.imageView_num.setText((position + 1) + "");

			int rateInt = (int) rate;
			switch (rateInt) {
			case 5:
				holder.imageView_star5.setVisibility(View.VISIBLE);
				holder.imageView_star5.setImageResource(R.drawable.stared);
			case 4:
				holder.imageView_star4.setVisibility(View.VISIBLE);
				holder.imageView_star4.setImageResource(R.drawable.stared);
			case 3:
				holder.imageView_star3.setVisibility(View.VISIBLE);
				holder.imageView_star3.setImageResource(R.drawable.stared);
			case 2:
				holder.imageView_star2.setVisibility(View.VISIBLE);
				holder.imageView_star2.setImageResource(R.drawable.stared);
			case 1:
				holder.imageView_star1.setVisibility(View.VISIBLE);
				holder.imageView_star1.setImageResource(R.drawable.stared);
				break;

			default:
				break;
			}

			if (rate - rateInt > 0) {
				switch (rateInt) {
				case 5:
					holder.imageView_star5
							.setImageResource(R.drawable.star_half);
					break;
				case 4:
					holder.imageView_star4
							.setImageResource(R.drawable.star_half);
					break;
				case 3:
					holder.imageView_star3
							.setImageResource(R.drawable.star_half);
					break;
				case 2:
					holder.imageView_star2
							.setImageResource(R.drawable.star_half);
					break;
				case 1:
					holder.imageView_star1
							.setImageResource(R.drawable.star_half);
					break;

				default:
					break;
				}
			}

			return convertView;
		}

	}

	static class ViewHolder {
		TextView textView_laundry_name;
		TextView textView_description;
		Button imageView_num;
		ImageView background_imageView;
		ImageView imageView_star1, imageView_star2, imageView_star3,
				imageView_star4, imageView_star5;

	}
	
	
	private void GetBanner() {

		final Thread splashTread = new Thread() {
			@Override
			public void run() {

			
				bannerArray = JsonReturn.BannerparseJson1("http://laundry.znsoftech.com/banner.php/");
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
							if (bannerArray.size() > 0) {
								JSONObject tempHashMap = bannerArray.get(0);

								try {
									

										
										imageLoader.DisplayImage(tempHashMap
												.getString("BannerURL"),
												imageView_banner,false);

										// Handler handler = new Handler();
										// handler.postDelayed(r, 1000);

									

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

						}
					});
				}
			}
		});
		displayThread.start();

	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback(){

	    @Override 
	    public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
	    	
	    	actionMode=mode;
	    	MenuInflater inflater = mode.getMenuInflater();
	    	inflater.inflate(R.menu.main1, menu);
	    	
	    	
	    	 MenuItem searchItem = menu.findItem(R.id.edt_mySearch);
	    	 RelativeLayout m = (RelativeLayout) MenuItemCompat.getActionView(searchItem);
	    	  SearchView mSearchView = (SearchView) m
                     .findViewById(R.id.edt_search);
	    	 mSearchView.setIconifiedByDefault(false);
	    	 mSearchView.setFocusable(true);
	    	 mSearchView.requestFocusFromTouch();
	    	 mSearchView.setQuery(searchText, true);
	    	 
	    	 mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		            @Override
		            public boolean onQueryTextSubmit(String s) {
		            	
		            	if (Network.HaveNetworkConnection(SearchActivity.this)) {
			        		if(s.length()<2){
			        			AlertUtil alert=new AlertUtil();
								alert.messageAlert(SearchActivity.this,"",
										"Please enter minimum two characters.");
			        		}
			        		else{
			        			InputMethodManager im = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			        			im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
							getLaundry(s);
			        		}
						} 
						else{
							AlertUtil alert=new AlertUtil();
							alert.messageAlert(SearchActivity.this,
									getResources()
											.getString(R.string.network_title),
									getResources().getString(
											R.string.network_message));
						}
		                return true;
		            }

		            @Override
		            public boolean onQueryTextChange(String s) {
		                return false;
		            }
		        });

	    	
	    	
	    	
	    
	          return true;
	        }

	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    	onBackPressed();

	    }

	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	          
	            case R.id.action_search:
	                //close the action mode
	                //mode.finish();
	                return true;
	            default:
	            	onBackPressed();
	                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    			if (inputManager != null) {
	    				View view = getCurrentFocus();
	    				if (view != null) {
	    					IBinder token = view.getWindowToken();
	    					if (token != null)
	    						inputManager.hideSoftInputFromWindow(token,
	    								InputMethodManager.HIDE_NOT_ALWAYS);
	    				}
	    			}
	                return false;
	       }
	    }
	    

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			// TODO Auto-generated method stub
			return false;
		}
	};
	
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
			  startActivity(new Intent(SearchActivity.this,
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