package nader.tools.util;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;

import java.io.IOException;

public class AudioPlayService extends Service {
	
	private MediaPlayer mediaPlayer;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mediaPlayer = new MediaPlayer();
		try {
			// Load the audio file from assets
			mediaPlayer.setDataSource(getApplicationContext().getAssets().openFd("a.mp3"));
			mediaPlayer.prepare();
			} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Start audio playback after 10 seconds
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!mediaPlayer.isPlaying()) {
					mediaPlayer.start();
				}
			}
		}, 10000); // 10 seconds delay
		
		return START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}