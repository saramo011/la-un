package com.app.laundry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.laundry.tabs.LaundryFragment;
import com.app.laundry.tabs.DealsFragment;
import com.app.laundry.tabs.New_Hometab;
import com.app.laundry.tabs.lib.TabPageIndicator2;

public class HomeFragment extends Fragment {
	
		
		View rootView;
		
		FragmentPagerAdapter adapter ;
		static ViewPager pager;
		TabPageIndicator2 indicator ;
		private static final String[] CONTENT = new String[] { "Home", "Nearby","Leading", "Favourite", "Deals"};


		public static HomeFragment newInstance(){
			HomeFragment fragment = new HomeFragment();
			return fragment;
		}
	    
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);


		}
	   @Override
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
	      /**
	       * Inflate the layout for this fragment
	       */
		   		rootView =  inflater.inflate( R.layout.mymusic_fragment, container, false);
		   		adapter = new GoogleMusicAdapter(getChildFragmentManager());

				pager = (ViewPager)rootView.findViewById(R.id.mymusic_pager);
				indicator = (TabPageIndicator2)rootView.findViewById(R.id.mymusic_indicator);
				pager.setAdapter(adapter);
				pager.setOffscreenPageLimit(0);
				indicator.setViewPager(pager);	
	            
	       return rootView;
	   }
	   
	  
	 
	   class GoogleMusicAdapter extends FragmentPagerAdapter {
			public GoogleMusicAdapter(FragmentManager fm) {
				super(fm);
			}

			@Override
			public Fragment getItem(int position) {
				Fragment fragment = null;
				Bundle data = new Bundle();
				if(CONTENT[position].trim().equalsIgnoreCase("Home")){
					fragment = New_Hometab.newInstance();
					data.putString("Tab", "all");
				}
				else if(CONTENT[position].trim().equalsIgnoreCase("Nearby")){
					fragment = LaundryFragment.newInstance();
					data.putString("Tab", "nearby");
				}
				else if(CONTENT[position].trim().equalsIgnoreCase("Leading")){
					fragment = LaundryFragment.newInstance();
					data.putString("Tab", "suggest");
				}
				else if(CONTENT[position].trim().equalsIgnoreCase("Favourite")){
					fragment = LaundryFragment.newInstance();
					data.putString("Tab", "favorite");
				}
				else if(CONTENT[position].trim().equalsIgnoreCase("Deals")){
					fragment = DealsFragment.newInstance();					
				}				
				else {
					fragment = LaundryFragment.newInstance();
				}
				

				fragment.setArguments(data);
				return fragment;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return CONTENT[position % CONTENT.length].toUpperCase();
			}

			@Override
			public int getCount() {
				return CONTENT.length;
			}


		}
	
}
