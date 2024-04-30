package nader.tools.util;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MaterialLover {
	public static void showCustomMaterialAlertDialog(Context context, String title, String message, String positiveButtonText, Runnable positiveAction) {
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, (dialog, which) -> {
                // Check if positiveAction is not null and execute it
                if (positiveAction != null) {
                    positiveAction.run();
                }
            })
            .setBackgroundInsetStart(48) // Adjust inset if needed
            .setBackgroundInsetEnd(48); // Adjust inset if needed

    AlertDialog dialog = builder.create();
    dialog.show();
}
}