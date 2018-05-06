package ui.custom;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Matthias on 09.02.2018.
 */

public class SchafkopfActivity extends AppCompatActivity {

    private static final long INACTIVITY_TIMEOUT = 600000; //10 min

    protected boolean gameAvailable = false;
    protected Timer inactivityTimer = new Timer();
    protected boolean timerRunning = false;
    private static Context applicationContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationContext = this;
    }

    public void resetInactivityTimer() {
        if (timerRunning) {
            inactivityTimer.cancel();
            inactivityTimer.purge();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        inactivityTimer = new Timer();
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                });
            }
        }, INACTIVITY_TIMEOUT);
        timerRunning = true;
    }

    public void vibrate(int ms) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(ms);
    }

    public boolean isGameAvailable() {
        return gameAvailable;
    }

    public static void showToast(String text) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(applicationContext, text, duration);
        toast.show();
    }
}
