package com.app.laundry;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.laundry.json.JGetParsor;
import com.app.laundry.network.Network;
import com.app.laundry.util.AlertUtil;
import com.app.laundry.util.ProgressDialogClass;

public class New_Rate extends FragmentActivity {

	JSONObject jsonObj = new JSONObject();
	Handler mHandler = new Handler();
	JSONObject json;
	String url_to_call;
	
	ImageView imageView_star1;
	ImageView imageView_star2;
	ImageView imageView_star3;
	ImageView imageView_star4;
	ImageView imageView_star5;

	ImageView imageView_star11;
	ImageView imageView_star12;
	ImageView imageView_star13;
	ImageView imageView_star14;
	ImageView imageView_star15;
	
	ImageView imageView_star21;
	ImageView imageView_star22;
	ImageView imageView_star23;
	ImageView imageView_star24;
	ImageView imageView_star25;
	
	ImageView imageView_star31;
	ImageView imageView_star32;
	ImageView imageView_star33;
	ImageView imageView_star34;
	ImageView imageView_star35;
	
	Button ok;
	EditText editText_msg;
	int tag1,tag2,tag3,tag4;
	String review;
	String laundryId;
	String user_id=Config.userid;

	// private GoogleMap mMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_rating);

		editText_msg = (EditText) findViewById(R.id.editText1);
		ok= (Button) findViewById(R.id.button_ok);
		tag1=tag2=tag3=tag4=1;
		Intent intent = getIntent();
		laundryId = intent.getExtras().getString("LaundryID");
		
		//Toast.makeText(New_Rate.this, Config.userid+" ",Toast.LENGTH_LONG).show();

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Network.HaveNetworkConnection(New_Rate.this)) {
					review=editText_msg.getText().toString();
					update_rating();
				} else {
					final AlertUtil alert = new AlertUtil();
					alert.confirmationAlert(New_Rate.this, getResources()
							.getString(R.string.network_title), getResources()
							.getString(R.string.network_message),
							new Button.OnClickListener() {

								@Override
								public void onClick(View v) {
															
									alert.release();
								}
							});

				}

			}
		});
		

		imageView_star1 = (ImageView) findViewById(R.id.imageView_star1);
		imageView_star2 = (ImageView) findViewById(R.id.imageView_star2);
		imageView_star3 = (ImageView) findViewById(R.id.imageView_star3);
		imageView_star4 = (ImageView) findViewById(R.id.imageView_star4);
		imageView_star5 = (ImageView) findViewById(R.id.imageView_star5);

		imageView_star1.setOnClickListener(clickLis);
		imageView_star2.setOnClickListener(clickLis);
		imageView_star3.setOnClickListener(clickLis);
		imageView_star4.setOnClickListener(clickLis);
		imageView_star5.setOnClickListener(clickLis);

		imageView_star1.setTag(1);
		imageView_star2.setTag(2);
		imageView_star3.setTag(3);
		imageView_star4.setTag(4);
		imageView_star5.setTag(5);

		imageView_star11 = (ImageView) findViewById(R.id.imageView_star11);
		imageView_star12 = (ImageView) findViewById(R.id.imageView_star12);
		imageView_star13 = (ImageView) findViewById(R.id.imageView_star13);
		imageView_star14 = (ImageView) findViewById(R.id.imageView_star14);
		imageView_star15 = (ImageView) findViewById(R.id.imageView_star15);

		imageView_star11.setOnClickListener(clickLis1);
		imageView_star12.setOnClickListener(clickLis1);
		imageView_star13.setOnClickListener(clickLis1);
		imageView_star14.setOnClickListener(clickLis1);
		imageView_star15.setOnClickListener(clickLis1);

		imageView_star11.setTag(1);
		imageView_star12.setTag(2);
		imageView_star13.setTag(3);
		imageView_star14.setTag(4);
		imageView_star15.setTag(5);
		
		//
		imageView_star21 = (ImageView) findViewById(R.id.imageView_star21);
		imageView_star22 = (ImageView) findViewById(R.id.imageView_star22);
		imageView_star23 = (ImageView) findViewById(R.id.imageView_star23);
		imageView_star24 = (ImageView) findViewById(R.id.imageView_star24);
		imageView_star25 = (ImageView) findViewById(R.id.imageView_star25);

		imageView_star21.setOnClickListener(clickLis2);
		imageView_star22.setOnClickListener(clickLis2);
		imageView_star23.setOnClickListener(clickLis2);
		imageView_star24.setOnClickListener(clickLis2);
		imageView_star25.setOnClickListener(clickLis2);

		imageView_star21.setTag(1);
		imageView_star22.setTag(2);
		imageView_star23.setTag(3);
		imageView_star24.setTag(4);
		imageView_star25.setTag(5);
		//
		
		imageView_star31 = (ImageView) findViewById(R.id.imageView_star31);
		imageView_star32 = (ImageView) findViewById(R.id.imageView_star32);
		imageView_star33 = (ImageView) findViewById(R.id.imageView_star33);
		imageView_star34 = (ImageView) findViewById(R.id.imageView_star34);
		imageView_star35 = (ImageView) findViewById(R.id.imageView_star35);

		imageView_star31.setOnClickListener(clickLis3);
		imageView_star32.setOnClickListener(clickLis3);
		imageView_star33.setOnClickListener(clickLis3);
		imageView_star34.setOnClickListener(clickLis3);
		imageView_star35.setOnClickListener(clickLis3);

		imageView_star31.setTag(1);
		imageView_star32.setTag(2);
		imageView_star33.setTag(3);
		imageView_star34.setTag(4);
		imageView_star35.setTag(5);
		
		update_rating_onStart();

	}

	OnClickListener clickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			imageView_star5.setImageResource(R.drawable.rating_before);
			imageView_star4.setImageResource(R.drawable.rating_before);
			imageView_star3.setImageResource(R.drawable.rating_before);
			imageView_star2.setImageResource(R.drawable.rating_before);
			imageView_star1.setImageResource(R.drawable.rating_before);
			int tag = Integer.parseInt(v.getTag().toString());
			tag1=tag;
			//Toast.makeText(New_Rate.this, tag1+"", Toast.LENGTH_SHORT).show();
			switch (tag) {
			case 5:
				imageView_star5.setImageResource(R.drawable.rating_after);
			case 4:
				imageView_star4.setImageResource(R.drawable.rating_after);
			case 3:
				imageView_star3.setImageResource(R.drawable.rating_after);
			case 2:
				imageView_star2.setImageResource(R.drawable.rating_after);
			case 1:
				imageView_star1.setImageResource(R.drawable.rating_after);
			default:
				break;
			}

		}
	};

	OnClickListener clickLis1 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			imageView_star15.setImageResource(R.drawable.rating_before);
			imageView_star14.setImageResource(R.drawable.rating_before);
			imageView_star13.setImageResource(R.drawable.rating_before);
			imageView_star12.setImageResource(R.drawable.rating_before);
			imageView_star11.setImageResource(R.drawable.rating_before);
			int tag = Integer.parseInt(v.getTag().toString());
			tag2=tag;
			//Toast.makeText(New_Rate.this, tag2+"", Toast.LENGTH_SHORT).show();
			switch (tag) {
			case 5:
				imageView_star15
						.setImageResource(R.drawable.rating_after);
			case 4:
				imageView_star14
						.setImageResource(R.drawable.rating_after);
			case 3:
				imageView_star13
						.setImageResource(R.drawable.rating_after);
			case 2:
				imageView_star12
						.setImageResource(R.drawable.rating_after);
			case 1:
				imageView_star11
						.setImageResource(R.drawable.rating_after);
			default:
				break;
			}

		}
	};
	//
	OnClickListener clickLis2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			imageView_star25.setImageResource(R.drawable.rating_before);
			imageView_star24.setImageResource(R.drawable.rating_before);
			imageView_star23.setImageResource(R.drawable.rating_before);
			imageView_star22.setImageResource(R.drawable.rating_before);
			imageView_star21.setImageResource(R.drawable.rating_before);
			int tag = Integer.parseInt(v.getTag().toString());
			tag3=tag;
			//Toast.makeText(New_Rate.this, tag3+"", Toast.LENGTH_SHORT).show();
			switch (tag) {
			case 5:
				imageView_star25
						.setImageResource(R.drawable.rating_after);
			case 4:
				imageView_star24
						.setImageResource(R.drawable.rating_after);
			case 3:
				imageView_star23
						.setImageResource(R.drawable.rating_after);
			case 2:
				imageView_star22
						.setImageResource(R.drawable.rating_after);
			case 1:
				imageView_star21
						.setImageResource(R.drawable.rating_after);
			default:
				break;
			}

		}
	};
	//
	OnClickListener clickLis3 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			imageView_star35.setImageResource(R.drawable.rating_before);
			imageView_star34.setImageResource(R.drawable.rating_before);
			imageView_star33.setImageResource(R.drawable.rating_before);
			imageView_star32.setImageResource(R.drawable.rating_before);
			imageView_star31.setImageResource(R.drawable.rating_before);
			int tag = Integer.parseInt(v.getTag().toString());
			tag4=tag;
			//Toast.makeText(New_Rate.this, tag4+"", Toast.LENGTH_SHORT).show();
			switch (tag) {
			case 5:
				imageView_star35
						.setImageResource(R.drawable.rating_after);
			case 4:
				imageView_star34
						.setImageResource(R.drawable.rating_after);
			case 3:
				imageView_star33
						.setImageResource(R.drawable.rating_after);
			case 2:
				imageView_star32
						.setImageResource(R.drawable.rating_after);
			case 1:
				imageView_star31
						.setImageResource(R.drawable.rating_after);
			default:
				break;
			}

		}
	};
	
	
	private void update_rating_onStart() {
		// TODO Auto-generated method stub
		
		json=null;
		ProgressDialogClass.showProgressDialog(New_Rate.this,"Please wait...");
		final Thread getdata=new Thread(){
			@SuppressWarnings("deprecation")
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JGetParsor j=new JGetParsor();
				url_to_call=Config.set_ratings_onstart+"laundryid/"+laundryId+"/userid/"+Config.userid;
				json = j.makeHttpRequest(url_to_call,"POST", params);
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
							String str="";
							ProgressDialogClass.dismissProgressDialog();
							if(json!=null){
								str=" URL="+url_to_call+json.toString();
								try {
									//Toast.makeText(New_Rate.this, json.getInt("status")+"",Toast.LENGTH_LONG).show();
									//editText_msg.setText(json.toString());
									str=" URL="+url_to_call+json.toString();
									if(json.getInt("status")==200){
											//Toast.makeText(New_Rate.this, json.getString("status_message"),Toast.LENGTH_LONG).show();
											JSONArray j_arr=json.getJSONArray("data");
											JSONObject j_obj=j_arr.getJSONObject(0);
											
											Float pq=Float.parseFloat(j_obj.getString("PriceQuality"));
											Float sq=Float.parseFloat(j_obj.getString("ServiceQuality"));
											Float pq1=Float.parseFloat(j_obj.getString("PickupQuality"));
											Float dq=Float.parseFloat(j_obj.getString("DropoffQuality"));
											
											int PriceQuality=Math.round(pq);
											int ServiceQuality=Math.round(sq);
											int PickupQuality=Math.round(pq1);
											int DropoffQuality=Math.round(dq);
											
											String Comments=j_obj.getString("Comments");
											
											//
											
											update_data(PriceQuality,new ImageView[]{imageView_star1,imageView_star2,imageView_star3,imageView_star4,imageView_star5} );
											
											update_data(ServiceQuality,new ImageView[]{imageView_star11,imageView_star12,imageView_star13,imageView_star14,imageView_star15} );

    										update_data(PickupQuality,new ImageView[]{imageView_star21,imageView_star22,imageView_star23,imageView_star24,imageView_star25} );
											
											update_data(DropoffQuality,new ImageView[]{imageView_star31,imageView_star32,imageView_star33,imageView_star34,imageView_star35} );
											
											editText_msg.setText(Comments);
											
											//Toast.makeText(New_Rate.this, PriceQuality+" "+ServiceQuality+" "+PickupQuality+" "+DropoffQuality+" "+Comments, Toast.LENGTH_LONG).show();
											
									}
//									else
//											Toast.makeText(New_Rate.this, "Json status wrong.",Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
							}
//							else
//								Toast.makeText(New_Rate.this, "Json null.",Toast.LENGTH_LONG).show();
							
						//	Toast.makeText(New_Rate.this, Config.userid+" ",Toast.LENGTH_LONG).show();
						//	writeToFile("json_data", str);
							
						}
					});
				}
			}
		});
		show.start();
	}
	
//	public  void writeToFile(String fileName, String body)
//    {
//        FileOutputStream fos = null;
//
//        try {
//            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Error/" );
//
//            if (!dir.exists())
//            {
//                dir.mkdirs(); 
//            }
//
//            final File myFile = new File(dir, fileName + ".txt");
//
//            if (!myFile.exists()) 
//            {    
//                myFile.createNewFile();
//            } 
//
//            fos = new FileOutputStream(myFile);
//
//            fos.write(body.getBytes());
//            fos.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
	
	private void update_data(int value, ImageView[] image){
		
		for(int i=0;i<image.length;i++)
			image[i].setImageResource(R.drawable.rating_before);
		
		switch(value){
		
		case 5:
			image[4].setImageResource(R.drawable.rating_after);
		case 4:
			image[3].setImageResource(R.drawable.rating_after);
		case 3:
			image[2].setImageResource(R.drawable.rating_after);
		case 2:
			image[1].setImageResource(R.drawable.rating_after);
		case 1:
			image[0].setImageResource(R.drawable.rating_after);
		default:
				break;
			
		}
		
	}
	
	private void update_rating() {
		// TODO Auto-generated method stub
		ProgressDialogClass.showProgressDialog(New_Rate.this,"Updating...");
		final Thread getdata=new Thread(){
			@SuppressWarnings("deprecation")
			public void run(){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("LaundryID", laundryId));
				params.add(new BasicNameValuePair("UserID", Config.userid));
				params.add(new BasicNameValuePair("PriceQuality", tag1+""));
				params.add(new BasicNameValuePair("ServiceQuality", tag2+""));
				params.add(new BasicNameValuePair("PickupQuality", tag3+""));
				params.add(new BasicNameValuePair("DropoffQuality", tag4+""));
				params.add(new BasicNameValuePair("Comments", review));
				JGetParsor j=new JGetParsor();
				json = j.makeHttpRequest(Config.set_ratings,"POST", params);
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
							
							if(json!=null){
								try {
									//Toast.makeText(New_Rate.this, json.toString(),Toast.LENGTH_LONG).show();
									if(json.getInt("status")==200){
											Toast.makeText(New_Rate.this, "Updated successfully.",Toast.LENGTH_LONG).show();
											onBackPressed();
										}
									//Toast.makeText(New_Rate.this, json.getInt("status"),Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
							}
							ProgressDialogClass.dismissProgressDialog();
							//Toast.makeText(New_Rate.this, tag1+" "+tag2+" "+tag3+" "+tag4,Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		});
		show.start();
	}
}
