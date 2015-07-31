
package com.app.laundry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainViewerFragment extends Fragment {
	 TextView title;
	 
	 
	 @Override
		public void onSaveInstanceState(Bundle savedInstanceState) {
		    //savedInstanceState.putInt(STATE_USER, mUser);
		    // Always call the superclass so it can save the view hierarchy state
		    super.onSaveInstanceState(savedInstanceState);
		}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState); 
    	View v = inflater.inflate(R.layout.main_viewer_fragment, container, false);

   	 
        return v;
    }
    
    
}
