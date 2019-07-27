package ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.schenk.matthias.schafkopfcalculator.R;

public class AppRater {
   private final static int DAYS_UNTIL_PROMPT = 7;//Min number of days
   private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches
   private static AppRater instance;
   private static Context context;

   public AppRater(Context context) {
      this.context = context;
   }

   public static AppRater getInstance(Context mContext) {
      if (instance == null) {
         instance = new AppRater(mContext);
      }
      context = mContext;
      return instance;
   }

   public void app_launched() {
      SharedPreferences prefs = context.getSharedPreferences("apprater", 0);
      if (prefs.getBoolean("dontshowagain", false)) {
         return;
      }

      SharedPreferences.Editor editor = prefs.edit();

      // Increment launch counter
      long launch_count = prefs.getLong("launch_count", 0) + 1;
      editor.putLong("launch_count", launch_count);

      // Get date of first launch
      Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
      if (date_firstLaunch == 0) {
         date_firstLaunch = System.currentTimeMillis();
         editor.putLong("date_firstlaunch", date_firstLaunch);
      }

      // Wait at least n days before opening
      if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
         if (System.currentTimeMillis() >=
               date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
            showRateDialog(editor);
         }
      }

      editor.commit();
   }

   public void showRateDialog(final SharedPreferences.Editor editor) {
      final String APP_PNAME = context.getPackageName();
      AlertDialog alertDialog = new AlertDialog.Builder(context).create();
      alertDialog.setTitle(context.getString(R.string.rateme_dialog_title));
      alertDialog.setMessage(context.getString(R.string.rate_me_text));
      alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
            context.getString(R.string.text_button_rate), new DialogInterface.OnClickListener() {

               public void onClick(DialogInterface dialog, int id) {
                  context.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + APP_PNAME)));
                  dialog.dismiss();
               }
            });

      alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
            context.getString(R.string.text_button_rate_remind_me),
            new DialogInterface.OnClickListener() {

               public void onClick(DialogInterface dialog, int id) {
                  dialog.dismiss();
               }
            });

      alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
            context.getString(R.string.text_button_rate_never),
            new DialogInterface.OnClickListener() {

               public void onClick(DialogInterface dialog, int id) {
                  if (editor != null) {
                     editor.putBoolean("dontshowagain", true);
                     editor.commit();
                  }
                  dialog.dismiss();
               }
            });
      alertDialog.show();
   }
}
