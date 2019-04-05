package fi.tamk.rentogames;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {
	private DungeonEscape game;
	MyService mService;
	boolean mBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		game = new DungeonEscape();

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(game, config);

		Intent intent = new Intent(this, MyService.class);

		if(Build.VERSION.SDK_INT>=26) {
			startForegroundService(intent);
		}else{
			startService(intent);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to LocalService
		Intent intent = new Intent(this, MyService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}
	@Override
	protected void onStop() {
		super.onStop();
		unbindService(connection);
		mBound = false;
	}
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
									   IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			MyService.LocalBinder binder = (MyService.LocalBinder) service;
			mService = binder.getService();
			mBound = true;
			game.setGetSteps(mService);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};


}
