package com.app.laundry;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class User_Agreement extends Fragment {
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

		View view = inflater.inflate(R.layout.user_agreement, container,false);
		title=(TextView)view.findViewById(R.id.title);
		message=(TextView)view.findViewById(R.id.message);
		title.setText("User Agreement");
		message.setText("Copyright © 2015, Nazish Ahsan, contact me: nazish.ahsan@znsoftech.com\n\n" +
				"This application is free; you can sharte it and" +
				" it under the terms of the GNU General Public License as" +
				" published by the Free Application Foundation; either version 1.0 of the" +
				" License.");
		message.setLinkTextColor(Color.BLUE);
		
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
