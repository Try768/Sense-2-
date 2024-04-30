package nader.tools.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class NaderESPManager {
	
	private static final String PREFS_FILENAME = "naderesp";
	
	private SharedPreferences sharedPreferences;
	
	public NaderESPManager(Context context) throws GeneralSecurityException, IOException {
		// Create a MasterKey for encryption
		MasterKey masterKey = new MasterKey.Builder(context)
		.setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
		.build();
		
		// Create EncryptedSharedPreferences instance
		sharedPreferences = EncryptedSharedPreferences.create(
		context,
		PREFS_FILENAME,
		masterKey,
		EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
		EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
		);
	}
	
	public void setString(String key, String value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.apply();
	}
	
	public String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}
	
	public void setInt(String key, int value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.apply();
	}
	
	public int getInt(String key, int defaultValue) {
		return sharedPreferences.getInt(key, defaultValue);
	}
	
	public void setBoolean(String key, boolean boool) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, boool);
		editor.apply();
	}
	
	public boolean getBoolean(String key, boolean boool) {
		return sharedPreferences.getBoolean(key, boool);
	}
	
	// Add similar methods for other data types as needed (e.g., boolean, float, etc.)
	
}