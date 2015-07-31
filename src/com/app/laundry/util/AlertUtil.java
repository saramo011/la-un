package com.app.laundry.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.app.laundry.R;



public class AlertUtil {
	 
    private static final int MESSAGE_ALERT = 1;
    private static final int CONFIRM_ALERT = 2;
    private static final int DECISION_ALERT = 3;
    public static Dialog dialog;
    public void release() {
    	dialog.dismiss();
    }
    public void messageAlert(Context ctx, String title, String message) {
    	if(ctx!=null)
        showAlertDialog(MESSAGE_ALERT, ctx, title, message, null,null, "OK");
    }
 
    public  void confirmationAlert(Context ctx, String title, String message, Button.OnClickListener callBack) {
    	if(ctx!=null)
    	showAlertDialog(CONFIRM_ALERT, ctx, title, message, callBack,null, "OK");
    }
 
    public  void decisionAlert(Context ctx, String title, String message, Button.OnClickListener posCallback,Button.OnClickListener posCallback1, String... buttonNames) {
    	if(ctx!=null)
    	showAlertDialog(DECISION_ALERT, ctx, title, message, posCallback,posCallback1, buttonNames);
    }
 
    public  void showAlertDialog(int alertType, Context ctx, String title, String message, Button.OnClickListener posCallback,Button.OnClickListener posCallback1, String... buttonNames) {
        if ( title == null ) title = ctx.getResources().getString(R.string.app_name);
        if ( message == null ) message = "default message";
        
        
        
        dialog = new Dialog(ctx);
		//dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.custom_alert_popup);
		TextView textview_title = (TextView) dialog.findViewById(R.id.textView_title);
		textview_title.setText(title);
		if(title.equals(""))
				textview_title.setVisibility(TextView.GONE);
		TextView textview_msg = (TextView) dialog.findViewById(R.id.textView_message);
		textview_msg.setText(message);


		

		
        
        
       /* 
        
      
 
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title)
                .setMessage(message)
 
                // false = pressing back button won't dismiss this alert
                .setCancelable(false);
 
                // icon on the left of title
               // .setIcon(android.R.drawable.ic_dialog_alert);
 */
        switch (alertType) {
            case MESSAGE_ALERT:
            {
            	Button dialogButton = (Button) dialog.findViewById(R.id.button_positive);
            	dialogButton.setText(buttonNames [buttonNames.length - 1]);
        		// if button is clicked, close the custom dialog
        		dialogButton.setOnClickListener(new OnClickListener() {
        			@Override
        			public void onClick(View v) {
        				dialog.dismiss();
        			}
        		});
            	
            }
            	
                break;
 
            case CONFIRM_ALERT:{
            	Button dialogButton = (Button) dialog.findViewById(R.id.button_positive);
            	dialogButton.setText(buttonNames[0]);
        		// if button is clicked, close the custom dialog
        		dialogButton.setOnClickListener(posCallback);
            }
            	
            	
               // builder.setPositiveButton(buttonNames[0], posCallback);
                break;
 
            case DECISION_ALERT:
            	Button dialogButton = (Button) dialog.findViewById(R.id.button_positive);
            	dialogButton.setText(buttonNames[0]);
        		// if button is clicked, close the custom dialog
        		dialogButton.setOnClickListener(posCallback);
            	
            	
            	Button dialogButtonCancle = (Button) dialog.findViewById(R.id.button_negative);
            	dialogButtonCancle.setVisibility(Button.VISIBLE);
            	dialogButtonCancle.setText(buttonNames[buttonNames.length - 1]);
        		// if button is clicked, close the custom dialog
            	dialogButtonCancle.setOnClickListener(posCallback1);
        		/*dialogButtonCancle.setOnClickListener(new OnClickListener() {
        			@Override
        			public void onClick(View v) {
        				dialog.dismiss();
        			}
        		});*/
                break;
        }
 
        dialog.show();
        
    }
}
