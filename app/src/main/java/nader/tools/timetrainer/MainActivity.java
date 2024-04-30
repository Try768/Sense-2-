package nader.tools.timetrainer;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;

import android.media.MediaPlayer;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.os.Build;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.CountDownTimer;
import android.os.Bundle;
import java.io.IOException;
import java.security.GeneralSecurityException;
import nader.tools.util.AudioPlayService;
import nader.tools.util.BatteryOptimizationHelper;
import nader.tools.util.NaderESPManager;
import androidx.security.crypto.EncryptedSharedPreferences;
import nader.tools.util.MaterialLover;

import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
	private MediaPlayer beep;
	private boolean isBeeping = false;

	private CountDownTimer countDownTimer;
	private NaderESPManager esp;
	private Context context;
	private boolean isPlaying = false;
	private Intent audioServiceIntent;
	private TextView countDown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
loadBeepSound();
		//beep = MediaPlayer.create(this, R.);
//beep = MediaPlayer.create(this, getResources().getIdentifier("single_beep", "raw", getPackageName()));

		context = MainActivity.this;
		countDown = findViewById(R.id.countView);
		try {
			esp = new NaderESPManager(this);
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}

		// Initialize intent for AudioPlayService
		audioServiceIntent = new Intent(this, AudioPlayService.class);

		// Show battery optimization dialog and request exemption
		Runnable disableBatteryOptimization = () -> {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
				intent.setData(Uri.parse("package:" + context.getPackageName()));
				context.startActivity(intent);
			}
		};

		if (BatteryOptimizationHelper.isBatteryOptimizationDisabled(context)) {
			MaterialLover.showCustomMaterialAlertDialog(MainActivity.this, "Battery Optimization", "Correct that",
					"Doing That", disableBatteryOptimization);
		} else {
			MaterialLover.showCustomMaterialAlertDialog(MainActivity.this, "Battery Optimization", "Enabled", "Okay",
					null);

			defaultSettings(esp);
		}

		// Setup play button functionality
		FloatingActionButton fab1 = findViewById(R.id.fab1);
		fab1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!isPlaying) {
					startCountdown(getInterval(esp)); // 10 seconds countdown
					startService(audioServiceIntent);
					isPlaying = true;
					isBeeping = true;

				} else {
					stopCountdown();

					stopService(audioServiceIntent);
					isPlaying = false;
					isBeeping = false;
				}
			}
		});
		
		FloatingActionButton about = findViewById(R.id.cnader);
		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,NaderAIActivity.class);
				startActivity(i);

			}
		});

	}

	public static void defaultSettings(NaderESPManager e) {
		setInterval(e, 10); //2seconds
		getVoice(e);
		setInALoop(e, true);

	}

	public static int getInterval(NaderESPManager e) {
		return e.getInt("interval", 2);
	}

	public static void setInterval(NaderESPManager e, int interval) {
		e.setInt("interval", interval);
	}

	public static String getVoice(NaderESPManager e) {
		return e.getString("voice", "nader_ai_english_1.mp3");
	}

	public static boolean ifInALoop(NaderESPManager e) {
		return e.getBoolean("loop", false);
	}

	public static void setInALoop(NaderESPManager e, boolean x) {
		e.setBoolean("loop", x);
	}

	private void startCountdown(int seconds) {
		countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				// Update countdown display (e.g., update TextView)
				countDown.setText("" + millisUntilFinished / 1000);
				
				// Play beep sound every second if not already playing
				if (beep != null && !isBeeping) {
					if (!beep.isPlaying()) {
						beep.start();
					}
					isBeeping = true;
				}
			}
			
			@Override
			public void onFinish() {
				// Countdown finished, perform any required actions
				countDown.setText("00");
				
				// Stop playing beep sound
				if (beep != null && beep.isPlaying()) {
					beep.pause(); // Pause beep sound
					beep.seekTo(0); // Reset playback position to start for next play
				}
				isBeeping = false;
			}
		};
		
		countDownTimer.start();
	}

	private void stopCountdown() {
		if (countDownTimer != null) {
			countDownTimer.cancel();
			countDownTimer = null;
			// Reset countdown display (e.g., countDown.setText(""))
		}
	}
	
	private void loadBeepSound() {
    try {
        AssetManager assetManager = getAssets();
        AssetFileDescriptor descriptor = assetManager.openFd("sounds/single_beep.mp3");
        
        if (beep == null) {
            beep = new MediaPlayer();
        } else {
            beep.reset();
        }
        
        beep.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
        descriptor.close();
        beep.prepare(); // Prepare the MediaPlayer
    } catch (IOException e) {
        e.printStackTrace();
    }
}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Release MediaPlayer resources when the activity is destroyed
		if (beep != null) {
			beep.release();
			beep = null;
		}
	}

}