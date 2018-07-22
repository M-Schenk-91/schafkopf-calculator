package ui;

import android.content.res.Resources;
import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import game.GameRound;

/**
 * Created by Matze on 04.02.2018.
 */

public class UiUtils {
    public static double formatScoreValue(int score) {
        return ((double) score) / 100;
    }

    public static String getScoreString(double score) {
        if (score > 0) return "+ " + String.format("%.2f", score);
        if (score < 0) return "- " + String.format("%.2f", score * -1);

        return "0";
    }

    public static int multiplicatorToDoubleUps(int multiplicator) {
        return (int) (Math.log(multiplicator) / Math.log(2));
    }

    public static String getDateString() {
        Calendar calendar = Calendar.getInstance();
        String formatedDate = new SimpleDateFormat("dd.MM.yyyy, HH.mm.ss").format(calendar.getTime());
        SimpleDateFormat formatedDay = new SimpleDateFormat("EEEE", Locale.GERMANY);
        return formatedDay.format(calendar.getTime()).substring(0, 2) + ", " + formatedDate + " Uhr";
    }

    public static boolean isInteger(String str) {
        try {
            int i = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
