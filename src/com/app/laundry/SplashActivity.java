package com.app.laundry;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;



public class SplashActivity extends Activity {


	SharedPreferences prefs;
	ArrayList<HashMap<String, String>> countResult = new ArrayList<HashMap<String, String>>();
	Handler mHandler = new Handler();
	protected boolean _active = true;
	protected int _splashTime = 5000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Use our own list adapter
		setContentView(R.layout.activity_splash);

		
		MediaController controller=new MediaController(SplashActivity.this);
		controller.hide();
		 controller.setVisibility(View.GONE);
		//Uri video = Uri.parse("android.resource://" + getPackageName() + "/" 
			//	+ R.raw.your_raw_file);
		VideoView videoView= (VideoView) findViewById(R.id.videoView1);
		videoView.setMediaController(controller);
		videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName()+"/"+R.raw.splash_video));
		videoView.start();

		
		
		//ImageView img = (ImageView)findViewById(R.id.imageView1);
		 //img.setBackgroundResource(R.drawable.splash_anim);

		 // Get the background, which has been compiled to an AnimationDrawable object.
		 //AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

		 // Start the animation (looped playback by default).
		 //frameAnimation.start();
	/*	String encodeName="Atal Buy�r";
		try {
			encodeName = URLEncoder.encode("Atal Buy�r", "UTF-8").replace("+", " ");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try
		{
		    final String s = new String(encodeName.getBytes(), "UTF-8");
		    String sa=s;
		    String sa1=sa;
		}
		catch (UnsupportedEncodingException e)
		{
		    Log.e("utf8", "conversion", e);
		}
		*/
		
		
		//ConversionTest ct = new ConversionTest();
		//String sourcePoint = ct.testLatitudeLongitudeToUMT32XYConversion("59.9627222","10.8780242");
		
		/*String uuid = "6d25168f-8a45-4946-9233-b96258c4b5a5";
		String token = "a9e28828612af1674b46e33953dc534f";
		String secret = "30dd53935f448de8f918d6d07cbe1f08";
		String issuance = "1397076527";
		AppBlade.register(getApplicationContext(), token, secret, uuid, issuance);
		*/
	
		//Config.TF_ApexNew_Bold = Typeface.createFromAsset(getAssets(),
			//	"fonts/apexnew_bold.otf");
		//Config.TF_ApexNew_Medum = Typeface.createFromAsset(getAssets(),
			//	"fonts/apexnew_medium.otf");

		prefs = this.getSharedPreferences("Settings",
				Context.MODE_PRIVATE);
		Config.email = prefs.getString("email", "");
		Config.mobile = prefs.getString("mobile", "");
		Config.password = prefs.getString("password", "");
		Config.name = prefs.getString("name", "");
		Config.userid=prefs.getString("userid", "0");
		Config.latitude = prefs.getString("Latitude", "78");
		Config.longitude = prefs.getString("Longitude","28");
		

		/*DatabaseHelper db = new DatabaseHelper(SplashActivity.this);
		try {
			db.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {

					//if (Config.email.equals("")) {
						startActivity(new Intent(SplashActivity.this,
								LoginActivity.class));
						overridePendingTransition(R.anim.right_in, R.anim.left_out);
					//} else {
						
						//	Intent intent = new Intent(SplashActivity.this,
							//		BaseFragmentActivity.class);
							
						//	startActivity(intent);
						
					//}

					finish();
				}
			}
		};
		splashTread.start();

	}

	

}