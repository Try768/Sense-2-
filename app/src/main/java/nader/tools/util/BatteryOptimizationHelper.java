package nader.tools.util;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

public class BatteryOptimizationHelper {
	
	public static boolean isBatteryOptimizationDisabled(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			if (powerManager != null) {
				return !powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
			}
		}
		// For API level 26 and below, assume battery optimization is not a concern
		return false;
	}
}