package com.android.indigo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Handler handler = new Handler();
		handler.postDelayed(new Splash(), 1000);
	}
	
	private class Splash implements Runnable {

		@Override
		public void run() {
			Intent homeIntent = new Intent(SplashActivity.this, HomeActivity.class);
			startActivity(homeIntent);
			SplashActivity.this.finish();
		}
	}
}
