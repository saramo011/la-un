package com.app.laundry;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutActivity extends Fragment {
	
	TextView title, message;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_about, container,false);
		
		TextView textView_about=(TextView)view.findViewById(R.id.textView_about);
		//TextViewJustify.justifyText(textView_about, 340f);
		
		TextView textView_terms=(TextView)view.findViewById(R.id.textView_terms);
		TextView textView_privacy=(TextView)view.findViewById(R.id.textView_privacy);
		
		textView_privacy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),PolicyActivity.class));
				getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});
		
		textView_terms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),TermsActivity.class));
				getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		});
			
	return view;	
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
