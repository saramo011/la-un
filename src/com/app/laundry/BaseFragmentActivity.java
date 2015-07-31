package com.app.laundry;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.laundry.adapter.NavDrawerListAdapter;

import com.app.laundry.model.NavDrawerItem;
import com.app.laundry.tabs.LaundryFragment;

public class BaseFragmentActivity extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private Fragment fragment = null;
	int city_array_size;
	// nav drawer title
	private CharSequence mDrawerTitle;

	JSONObject json;
	
	ActionBar bar;
	ActionMode actionMode;
	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragmrnt_activity_main);

		/*
		 * HomeFragment fragment = new HomeFragment();
		 * 
		 * // adding fragment to relative layout by using layout id
		 * FragmentTransaction ft =
		 * getSupportFragmentManager().beginTransaction();
		 * //getFragmentManager().beginTransaction().add(android.R.id.content,
		 * details) ft.add(R.id.fragmentLeft, fragment);
		 * ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		 * ft.commit();
		 */
		
		
		bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
		bar.setDisplayShowHomeEnabled(true);
		
		  try {
		        ViewConfiguration config = ViewConfiguration.get(this);
		        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
		        if(menuKeyField != null) {
		            menuKeyField.setAccessible(true);
		            menuKeyField.setBoolean(config, false);
		        }
		    } catch (Exception ex) {
		        // Ignore
		    }
		  
	
		//bar.setIcon(R.drawable.ic_launcher);
		bar.setDisplayHomeAsUpEnabled(true);
	    bar.setHomeButtonEnabled(true);
		
		
		//SpannableString s = new SpannableString("Name On Cake");
		//s.setSpan(new TypefaceSpan1(MainActivity.this, "GrandHotel-Regular.ttf"), 0, s.length(),
		  //      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//s.setSpan(new RelativeSizeSpan(1.4f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		bar.setTitle(getString(R.string.app_name));
		
		
		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		
		mDrawerToggle = new ActionBarDrawerToggle(
	            BaseFragmentActivity.this,                             /* host Activity */
	            mDrawerLayout,                    /* DrawerLayout object */
	            R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
	            R.string.app_name /* "close drawer" description for accessibility */
	    ) {
	        @Override
	        public void onDrawerClosed(View drawerView) {
	            invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
	            
	        }

	        @Override
	        public void onDrawerOpened(View drawerView) {
	            invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
	        }
	    };

	    // Defer code dependent on restoration of previous instance state.
	    // NB: required for the drawer indicator to show up!
	    mDrawerLayout.post(new Runnable() {
	        @Override
	        public void run() {
	        	
	        	
	                     android.content.res.Resources resources = getResources();
	                     float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, resources.getDisplayMetrics());
	                     DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
	                     params.width = (int) (width);
	                     mDrawerList.setLayoutParams(params);
	                     mDrawerToggle.syncState();
	                 }
	             });


	    mDrawerLayout.setDrawerListener(mDrawerToggle);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons
				.getResourceId(8, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons
				.getResourceId(9, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		/*
		 * getActionBar().setDisplayHomeAsUpEnabled(true);
		 * getActionBar().setHomeButtonEnabled(true);
		 * 
		 * mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
		 * R.drawable.ic_drawer, //nav menu toggle icon R.string.app_name, //
		 * nav drawer open - description for accessibility R.string.app_name //
		 * nav drawer close - description for accessibility ) { public void
		 * onDrawerClosed(View view) { getActionBar().setTitle(mTitle); //
		 * calling onPrepareOptionsMenu() to show action bar icons
		 * invalidateOptionsMenu(); }
		 * 
		 * public void onDrawerOpened(View drawerView) {
		 * getActionBar().setTitle(mDrawerTitle); // calling
		 * onPrepareOptionsMenu() to hide action bar icons
		 * invalidateOptionsMenu(); } };
		 */
		 

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

		

		
		fragment = new HomeFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment,"home").commit();
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(final int position) {
		// update the main content by replacing fragments

		switch (position) {
		case 0:
			// mDrawerLayout.closeDrawer(mDrawerList);
			// fragment = new HomeFragment();
//			if(HomeFragment.pager!=null)
//				HomeFragment.pager.setCurrentItem(0);
//			else{
				//mDrawerLayout.closeDrawer(mDrawerList);
				fragment=new HomeFragment();
				FragmentManager fragmentManager3 = getSupportFragmentManager();
				fragmentManager3.beginTransaction()
						.replace(R.id.frame_container, fragment,"home").commit();
				
				// this will clear the back stack and displays no animation on the screen
				 fragmentManager3.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			//}
			
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 7: {
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment=new AboutActivity();
			FragmentManager fragmentManager_about = getSupportFragmentManager();
			fragmentManager_about.beginTransaction()
					.replace(R.id.frame_container, fragment,"About").addToBackStack("home").commit();
			break;
		}
		case 3:
			mDrawerLayout.closeDrawer(mDrawerList);
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id="
							+ getApplicationContext().getPackageName())));
			break;
		case 4: {
			mDrawerLayout.closeDrawer(mDrawerList);
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT,
					getResources().getString(R.string.app_name));
			String sAux = "Check out Spot My Laundry for your smartphone to get laundry service at your doorstep at \n\n";
			sAux = sAux + "https://play.google.com/store/apps/details?id="
					+ getApplicationContext().getPackageName() + " \n";
			i.putExtra(Intent.EXTRA_TEXT, sAux);
			startActivity(Intent.createChooser(i, "choose one"));
			}
			break;
		case 5: {
			mDrawerLayout.closeDrawer(mDrawerList);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri
					.parse("mailto:" + "ahmad.asjad@znsoftech.com"));
			intent.putExtra(Intent.EXTRA_SUBJECT, "Error in Laundry App");
			startActivity(intent);			
			}
			break;
		
		case 2:
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment=new Manage_Address();
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment,"manage_address").addToBackStack("home").commit();
			
			break;
		case 1:
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment=new Order_History();
			FragmentManager fragmentManager9 = getSupportFragmentManager();
			fragmentManager9.beginTransaction()
					.replace(R.id.frame_container, fragment,"order_history").addToBackStack("home").commit();
			break;
		case 6:
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment=new Contact_Us();
			FragmentManager fragmentManager1 = getSupportFragmentManager();
			fragmentManager1.beginTransaction()
					.replace(R.id.frame_container, fragment,"contact_us").addToBackStack("home").commit();
			
			break;
			
		case 8:
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment=new User_Agreement();
			FragmentManager fragmentManager2 = getSupportFragmentManager();
			fragmentManager2.beginTransaction()
					.replace(R.id.frame_container, fragment,"user_agreement").addToBackStack("home").commit();
			
			break;
			
		case 9: {
			mDrawerLayout.closeDrawer(mDrawerList);
			SharedPreferences prefs = getSharedPreferences("Settings",
					Context.MODE_PRIVATE);
			prefs.edit().clear().commit();
			Config.email = "";
			Config.mobile = "";
			Config.password = "";
			Config.name = "";
			Config.userid = "0";
			Config.latitude = "78.0";
			Config.longitude = "28.0";

			Intent intent = new Intent(BaseFragmentActivity.this,
					LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			finish();
		}
			break;

		default:
			break;
		}

		/*if (fragment != null) {
			mDrawerLayout.closeDrawer(mDrawerList);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container, fragment).commit();

					// update selected item and title, then close the drawer
					mDrawerList.setItemChecked(position, true);
					mDrawerList.setSelection(position);
					setTitle(navMenuTitles[position]);
					// mDrawerLayout.closeDrawer(mDrawerList);
				}
			}, 350);

		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}*/
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		// getActionBar().setTitle(mTitle);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}
		
		if(actionMode!=null)
			actionMode.finish();
		
		super.onBackPressed();
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		// mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		// mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		Config.newCityArray.clear();
		city_array_size=Config.cityArray.size();
		for (int i = 0; i <city_array_size ; i++)
		{
			try{
				String citystr=Config.cityArray.get(i);
				JSONObject jobj=new JSONObject(citystr);
				menu.add(0, i, i, jobj.getString("CityName"));
				Config.newCityArray.add(jobj.getString("CityName"));
			}
			catch(JSONException e){
				e.printStackTrace();
			}
			
		}
	    
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_search) {
			bar.startActionMode(mActionModeCallback);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
			return true;
     	}
		else if(id<city_array_size)
		{
			String selected_item=item.getTitle().toString();
			//Toast.makeText(getApplicationContext(), selected_item, Toast.LENGTH_SHORT).show();
		
			//Log.e("CityOutsideOutsideOutsideOutsideOutsideOutside",Config.cityArray.get(i));
			for (int i = 0; i < Config.cityArray.size(); i++)
			{
				if(selected_item.equals(Config.newCityArray.get(i)))
				{
					Config.city=Config.newCityArray.get(i);
					//Toast.makeText(BaseFragmentActivity.this, "equal", Toast.LENGTH_SHORT).show();
					if(HomeFragment.pager!=null)
					{
						int index=HomeFragment.pager.getCurrentItem();
						Fragment new_fragment=new HomeFragment();
						Bundle data = new Bundle();
						String[] str=new String[]{"all","nearby","suggest","favorite"};
						
						if(index<4)
						{
							data.putString("Tab", str[index]);
						
						new_fragment.setArguments(data);
						fragment=new_fragment;
						
						FragmentManager fragmentManager = getSupportFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.frame_container, fragment).commit();
						}
						//Toast.makeText(getApplicationContext(), index+" If condition.", Toast.LENGTH_SHORT).show();
					}
					return true;
				}
			}
		}
		
		else
		{
			 if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
					mDrawerLayout.closeDrawer(mDrawerList);
				} else
					mDrawerLayout.openDrawer(Gravity.LEFT);		
		}
	return super.onOptionsItemSelected(item);
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
	    	 mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
	    		    @Override
	    		    public void onFocusChange(View view, boolean hasFocus) {
	    		     if (!hasFocus){
	    		    	 mode.finish();
	    		      }
	    		      
	    		     
	    		    }
	    		   });
	    	 mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
		            @Override
		            public boolean onQueryTextSubmit(String s) {
		            	
		            	actionMode.finish();
		            	Intent intent = new Intent(BaseFragmentActivity.this,
								SearchActivity.class);
						intent.putExtra("SearchText",s);

						startActivity(intent);
						overridePendingTransition(R.anim.right_in, R.anim.left_out);
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

	    }

	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	          
	            case R.id.action_search:
	                //close the action mode
	                //mode.finish();
	                return true;
	            default:
	            	actionMode.finish();
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
	
}